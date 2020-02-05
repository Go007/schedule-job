package com.hong.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hong.job.domain.QuartzTaskLock;

import java.util.Map;

/**
 * Created by wanghong
 * Date 2020-02-04 14:24
 * Description:
 */
public interface QuartzTaskLockMapper extends BaseMapper<QuartzTaskLock> {

    int lock(Map<String, Object> map);

    int unlock(Map<String, Object> map);

    int batchResetStatus();

    int insertTaskLock(QuartzTaskLock quartzTaskLock);
}
