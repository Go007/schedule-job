package com.hong.job.task;

import com.hong.job.mapper.QuartzTaskLockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


/**
 * @author wanghong
 * @Description
 * @date 2020/02/03 19:21
 **/
@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private QuartzTaskLockMapper quartzTaskLockMapper;

    @Override
    public void run(String... strings) {
        batchResetTaskStatus();
    }

    /**
     * 项目启动时重置所有定时任务的状态为初始状态，
     *  防止因为服务器因为异常宕机等情况致使状态没有释放，
     *  从而导致重启时定时任务得不到执行
     */
    private void batchResetTaskStatus(){
        quartzTaskLockMapper.batchResetStatus();
    }
}
