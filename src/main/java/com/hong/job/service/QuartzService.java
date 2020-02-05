package com.hong.job.service;

import com.hong.job.domain.QuartzTaskInfo;

import java.util.List;

public interface QuartzService {
    String addTask(QuartzTaskInfo quartzTaskInfo);
    List<QuartzTaskInfo> getTaskList(String taskNo, String currentPage);
    QuartzTaskInfo getTaskById(String id);
    String updateTask(QuartzTaskInfo quartzTaskInfo);
    String startJob(String taskNo);
    void initLoadOnlineTasks();
    String runTaskRightNow(String taskNo);
}
