package com.hong.job.task;

import com.hong.job.util.IPV4Util;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wanghong
 * @Description 抽象任务骨架定义
 * @date 2020/02/03 18:37
 **/
@Slf4j
public abstract class AbstractTaskService {

    //  @Autowired
    // private QuartzTaskLockMapper quartzTaskLockMapper;

  /*  public void run(String taskNo) {
        String ip = IPV4Util.getLocalIpv4Address();
        Map<String,Object> map = new HashMap<>();
        map.put("taskNo",taskNo);
        map.put("execIp",ip);
        int lock = quartzTaskLockMapper.lock(map);
        log.info("task-lock: taskNo=[{}],ip=[{}],lock=[{}]", taskNo, ip, lock);
        if (lock == 1) {
            log.info("taskNo:[{}],任务执行开始", taskNo);
            doWork();
            log.info("taskNo:[{}],任务执行结束", taskNo);
            int unlock = quartzTaskLockMapper.unlock(map);
            log.info("task-unlock: taskNo=[{}],ip=[{}],unlock=[{}]", taskNo, ip, unlock);
        }
    }*/

    public void run(String taskNo) {
        String ip = IPV4Util.getLocalIpv4Address();
        log.info("taskNo=[{}] begin,executorIp=[{}]", taskNo, ip);
        try {
            doWork();
        } catch (Exception e) {
            // TODO  任务执行发生异常，可以发送告警邮件给相关负责人,也可以在数据库中记录执行情况
            log.error("taskNo=[{}] error,executorIp=[{}],exception=[{}]", taskNo, ip, e);
        }
        log.info("taskNo=[{}] end,executorIp=[{}]", taskNo, ip);
    }

    protected abstract void doWork();
}
