package com.hong.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.job.domain.QuartzTaskInfo;

import java.util.List;
import java.util.Map;

public interface QuartzTaskInfoMapper extends BaseMapper<QuartzTaskInfo> {
    int insertSelective(QuartzTaskInfo record);

    int updateByPrimaryKeySelective(QuartzTaskInfo record);

    int updateByPrimaryKey(QuartzTaskInfo record);

    List<QuartzTaskInfo> selectList(Map<String, Object> map);

    Integer selectByTaskNo(String taskNo);

    QuartzTaskInfo getTaskByTaskNo(String taskNo);

    QuartzTaskInfo getTaskById(Long id);

    List<QuartzTaskInfo> getTaskByStatus(Integer status);
}
