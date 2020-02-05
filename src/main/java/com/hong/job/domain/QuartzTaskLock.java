package com.hong.job.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanghong
 * @date 2020/02/03 18:48
 **/
@NoArgsConstructor
@Data
@TableName("quartz_task_lock")
public class QuartzTaskLock {

    private String taskNo;

    private String acquireTime;

    private String execIp;

    private Integer status;
}
