package com.hong.job.service;

import com.hong.job.task.AbstractTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wanghong
 * @date 2020/02/04 16:47
 **/
@Slf4j
@Service
public class SendMessageService extends AbstractTaskService {

    @Override
    public void doWork() {
        try {
            Thread.sleep(2000);
            log.info("==============SendMessageService=========");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
