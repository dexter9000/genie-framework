# Genie Framework

Genie Framework是Genie微服务的框架包，针对各种服务特性提供独立的组件包。

## Genie Core

Genie Core是微服务工程的公共方法库，为具体的微服务工程提供通用的方法和抽象类。
[详细说明](/genie-projects/genie-framework/tree/master/genie-core)

## Genie Sharding

提供分库分表的功能支持
[详细说明](/genie-cloud/genie-framework/tree/master/genie-sharding-jpa)

为微信专门封装的分库分表组件，以account作为分库分表条件
分库策略：从主库的DB_INSTANCE表查询所有的Account
分表策略：WECHAT_ACCOUNT

## Genie Fastdfs Client

提供Fastdfs的Client组件
[详细说明](/genie-cloud/genie-framework/tree/master/genie-fastdfs)
