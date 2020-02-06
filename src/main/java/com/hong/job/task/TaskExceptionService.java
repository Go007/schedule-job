package com.hong.job.task;

import com.hong.job.domain.QuartzTaskInfo;
import com.hong.job.mapper.QuartzTaskInfoMapper;
import com.hong.job.service.IMailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author wanghong
 * @date 2020/02/06 15:22
 **/
@Service
public class TaskExceptionService {

    @Autowired
    private IMailService mailService;

    @Autowired
    private QuartzTaskInfoMapper quartzTaskInfoMapper;

    @Async
    public void handler(String taskNo,Exception e) {
        // TODO  任务执行发生异常，可以发送告警邮件给相关负责人,也可以在数据库中记录执行情况
        QuartzTaskInfo taskInfo = quartzTaskInfoMapper.getTaskByTaskNo(taskNo);
        // String alarmTo = taskInfo.getAlarmTo();
        String alarmTo = "1536054473@qq.com";
        if (StringUtils.isNotEmpty(alarmTo)) {
            String[] mailTos = alarmTo.split(";");
            String subject = "Schedule-Alarm:" + taskInfo.getTaskName();
            System.out.println(Thread.currentThread().getName());
            String content = "定时任务[" + taskInfo.getTaskName() + "]执行异常，异常信息：" + e;
            mailService.sendSimpleMail(mailTos, subject, content);
        }
    }
}
