# schedule-job
# 1. 基于Spring Boot的简单分布式定时任务
https://blog.csdn.net/fanrenxiang/article/details/85539918

# 2. SpringBoot中使用Thymeleaf模板
https://www.jianshu.com/p/381e02c283f3

# 3. 如何解决spring中抽象类无法注入属性

# 4. 一个关于Quartz中执行时机的细节问题
经测试发现，当cron定时间隔时间小于任务执行时间时，
quartz会动态将cron定时间隔时间变为任务执行时间。
即如果 定时间隔为每 2s 执行一次，本次任务执行时间为 3s，
则下一次 执行时间会被调整为 3s 后。

# 5. 特别说明：项目代码只在单机环境下适用，不支持分布式集群环境。

# 6. cron表达式怎么能在某个时间只执行一次，然后停止？
https://www.cnblogs.com/hill1126/p/12218518.html

