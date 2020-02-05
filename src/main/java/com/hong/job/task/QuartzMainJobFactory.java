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
        AbstractTaskService taskService = (AbstractTaskService) ApplicationContextHolder.getBean(beanName);
        taskService.run(taskNo);
    }
}
