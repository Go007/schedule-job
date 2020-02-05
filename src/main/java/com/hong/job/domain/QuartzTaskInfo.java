package com.hong.job.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wanghong
 * @date 2020/02/04 12:16
 **/
@NoArgsConstructor
@Data
@TableName("quartz_task_info")
public class QuartzTaskInfo {
    private Long id;
    private Integer version;
    /**
     * 任务编号，可以将某一类的任务的编号前缀几位固定，方便标识
     * 如 100开头的任务表示 推送代办 相关任务
     */
    private String taskNo;
    private String taskName;
    private String beanName;
    private String beanClass;
    private String cron;
    private Integer status;
    private String createTime;
    private String updateTime;
}
