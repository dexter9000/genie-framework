# Genie Core

Genie Core是微服务工程的公共方法库，为具体的微服务工程提供通用的方法和抽象类。


## Logger

```yaml
genie:
    logger-aspect:
        class-names:
        -   "com.genie.crm.repository..*"
        -   "com.genie.crm.service..*"
        -   "com.genie.common.web.rest..*"
        -   "com.genie.crm.web.rest..*"
```

## Sharding Batch
当需要汇聚数据批量处理时，可以继承`ShardingBatchHandler`， 内部已经继承了多线程处理，
可以支持按批量大小和按超时时间两种方式处理数据。


## Async
最简单实现异步方法就是通过增加`@Async`注解来实现，默认的配置如下：
```yaml
genie:
    async:
        corePoolSize: 2         # 线程池大小
        maxPoolSize: 50         # 最大
        queueCapacity: 10000    # 队列大小
```
这里的配置是针对所有异步方法的，也就是说有多个方法被定义为异步方法，公用一个线程池。当然也可以通过定义多个`Executor`的方式对异步方法进行分组。
