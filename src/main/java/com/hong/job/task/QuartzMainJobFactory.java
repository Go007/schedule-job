package com.hong.job.task;

import com.hong.job.util.ApplicationContextHolder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author wanghong
 * @Description 定时任务的主要执行逻辑，实现Job接口
 * @date 2020/02/03 15:58
 **/
@DisallowConcurrentExecution
public class QuartzMainJobFactory implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String taskNo = jobDataMap.getString("taskNo");
        String beanName = jobDataMap.getString("beanName");
        /**
         * 另外一种思路：
         *  如果将 定时任务项目单独部署一台机器，则任务执行可以通过 HTTP 调用接口的 方式。
         *  扩展原来的 任务信息表 ，加入执行方任务接口 URL ，参数等。
         *  这种方式的弊端就是 当 执行方接口改变，参数变化，需要通知定时任务跟着改动，不便于维护。
         */
        AbstractTaskService taskService = (AbstractTaskService) ApplicationContextHolder.getBean(beanName);
        taskService.run(taskNo);
    }
}
