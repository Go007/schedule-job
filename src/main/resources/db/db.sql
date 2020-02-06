-- 定时任务信息
DROP TABLE IF EXISTS `quartz_task_info`;
CREATE TABLE `quartz_task_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) NOT NULL COMMENT '版本号：需要乐观锁控制',
  `task_no` varchar(64) NOT NULL COMMENT '任务编号',
  `task_name` varchar(64) NOT NULL COMMENT '任务名称',
   bean_name varchar(64) NOT NULL COMMENT '执行bean名称',
   bean_class varchar(64) NOT NULL COMMENT '执行bean类名',
  `cron` varchar(64) NOT NULL COMMENT '定时配置cron',
  `cron_desc` varchar(64) NOT NULL COMMENT '定时规则描述',
  `status` int(11) default null COMMENT '状态 0-已暂停 1-已启动',
   alarm_to varchar(128) default null comment '告警邮接收人,多个以英文分号隔开',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  CONSTRAINT bean_uq UNIQUE (bean_name)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='定时任务信息表';

-- 定时任务执行数据库锁
DROP TABLE IF EXISTS `quartz_task_lock`;
create table quartz_task_lock(
    task_no varchar(64) primary key NOT NULL COMMENT '任务编号',
    exec_ip varchar(32) default null comment '获取锁的机器ip',
    acquire_time datetime DEFAULT NULL comment '获取锁的时间',
    status int(11) default null comment '当前任务对应锁状态: 0-空闲 1-占用'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='定时任务执行数据库锁';