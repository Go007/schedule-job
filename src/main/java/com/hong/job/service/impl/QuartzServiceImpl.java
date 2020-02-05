package com.hong.job.service.impl;

import com.hong.job.domain.QuartzTaskInfo;
import com.hong.job.mapper.QuartzTaskInfoMapper;
import com.hong.job.service.QuartzService;
import com.hong.job.task.AbstractTaskService;
import com.hong.job.task.QuartzMainJobFactory;
import com.hong.job.util.ApplicationContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wanghong
 * @date 2020/02/04 14:22
 **/
@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService, InitializingBean {

    @Autowired
    private QuartzTaskInfoMapper quartzTaskInfoMapper;

    @Autowired
    private SchedulerFactoryBean schedulerBean;

  //  @Autowired
  //  private QuartzTaskLockMapper quartzTaskLockMapper;

    @Override
    public String addTask(QuartzTaskInfo quartzTaskInfo) {
        String taskNo = quartzTaskInfo.getTaskNo();
        quartzTaskInfo.setVersion(0);
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        quartzTaskInfo.setCreateTime(date);
        quartzTaskInfo.setUpdateTime(date);
        Integer count = quartzTaskInfoMapper.selectByTaskNo(taskNo);
        //判断是否重复任务编号
        if (count > 0) {
            return "该任务编号已经存在";
        }
        int insert = quartzTaskInfoMapper.insertSelective(quartzTaskInfo);
        if (insert < 1) {
            return "fail";
        }

       /* QuartzTaskLock quartzTaskLock = new QuartzTaskLock();
        quartzTaskLock.setTaskNo(taskNo);
        quartzTaskLock.setStatus(0);
        quartzTaskLockMapper.insertTaskLock(quartzTaskLock);*/

        return "success";
    }

    @Override
    public List<QuartzTaskInfo> getTaskList(String taskNo, String currentPage) {
        Map<String, Object> map = new HashMap<>();
        Integer start = Integer.parseInt(currentPage);
        map.put("taskNo", taskNo);
        map.put("startIndex", 10 * (start - 1));
        return quartzTaskInfoMapper.selectList(map);
    }

    @Override
    public QuartzTaskInfo getTaskById(String id) {
        return quartzTaskInfoMapper.getTaskById(Long.parseLong(id));
    }

    @Override
    public String updateTask(QuartzTaskInfo quartzTaskInfo) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        quartzTaskInfo.setUpdateTime(date);
        int updateCount = quartzTaskInfoMapper.updateByPrimaryKeySelective(quartzTaskInfo);
        //乐观锁控制并发修改
        if (updateCount < 1) {
            return "更新失败";
        }
        return "更新成功";
    }

    /**
     * 启动 or 暂停 定时任务
     * @param taskNo
     * @return
     */
    @Transactional
    @Override
    public String startJob(String taskNo) {
        QuartzTaskInfo quartzTaskInfo = quartzTaskInfoMapper.getTaskByTaskNo(taskNo);
        try {
            if (quartzTaskInfo == null) {
                return "找不到记录";
            }
            Integer status = quartzTaskInfo.getStatus();
            Scheduler scheduler = schedulerBean.getScheduler();
            QuartzTaskInfo task = new QuartzTaskInfo();
            task.setId(quartzTaskInfo.getId());
            task.setVersion(quartzTaskInfo.getVersion());
            task.setStatus(1 == status ? 0:1);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            task.setUpdateTime(date);
            quartzTaskInfoMapper.updateByPrimaryKeySelective(task);
            scheduler.deleteJob(new JobKey(taskNo));
            log.info("taskNo={},taskName={},cron={},任务{}成功", quartzTaskInfo.getTaskNo(), quartzTaskInfo.getTaskName(), quartzTaskInfo.getCron(), 1 == status ?  "暂停":"启动");

            if (0 == status) {
                this.schedule(quartzTaskInfo, scheduler);
            }

            return "success";
        } catch (SchedulerException e) {
            log.error("taskNo={},taskName={},cron={},任务{}失败,exception:{}", quartzTaskInfo.getTaskNo(), quartzTaskInfo.getTaskName(), quartzTaskInfo.getCron(), 1 == quartzTaskInfo.getStatus() ? "暂停" : "启动",e);
        }
        return "fail";
    }

    /**
     * 初始化加载定时任务
     */
    @Override
    public void initLoadOnlineTasks() {
        List<QuartzTaskInfo> activeTaskList = quartzTaskInfoMapper.getTaskByStatus(1);
        if (CollectionUtils.isEmpty(activeTaskList)) {
            log.info("没有需要初始化加载的定时任务");
            return;
        }
        Scheduler scheduler = schedulerBean.getScheduler();
        for (QuartzTaskInfo activeTask : activeTaskList) {
            try {
                this.schedule(activeTask, scheduler);
            } catch (Exception e) {
                log.error("系统初始化加载定时任务:taskNo={},taskName={}失败,exception={}", activeTask.getTaskNo(), activeTask.getTaskName(), e);
            }
        }
    }

    /**
     * 立即运行一次定时任务
     * @param taskNo
     * @return
     */
    @Override
    public String runTaskRightNow(String taskNo) {
        QuartzTaskInfo quartzTaskInfo = quartzTaskInfoMapper.getTaskByTaskNo(taskNo);
        if (quartzTaskInfo == null) {
            return "找不到记录";
        }
        String beanName = quartzTaskInfo.getBeanName();
        AbstractTaskService taskService = (AbstractTaskService) ApplicationContextHolder.getBean(beanName);
        log.info("立即运行一次定时任务:taskNo=[{}]", taskNo);
        taskService.run(taskNo);
        return "success";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initLoadOnlineTasks();
    }

    @Async
    public void schedule(QuartzTaskInfo quartzTaskInfo, Scheduler scheduler) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzTaskInfo.getTaskNo(), Scheduler.DEFAULT_GROUP);
        JobDetail jobDetail = JobBuilder.newJob(QuartzMainJobFactory.class).withDescription(quartzTaskInfo.getTaskName()).withIdentity(quartzTaskInfo.getTaskNo(), Scheduler.DEFAULT_GROUP).build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("id", quartzTaskInfo.getId().toString());
        jobDataMap.put("taskNo", quartzTaskInfo.getTaskNo());
        jobDataMap.put("beanName", quartzTaskInfo.getBeanName());
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(quartzTaskInfo.getCron());
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withDescription(quartzTaskInfo.getTaskName()).withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("taskNo={},taskName={},beanName={},cron={} load to quartz success!", quartzTaskInfo.getTaskNo(), quartzTaskInfo.getTaskName(), quartzTaskInfo.getBeanName(),quartzTaskInfo.getCron());
    }
}
