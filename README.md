# sharding-sphere-demo

## 简介

此项目为sharding-sphere demo项目，项目包含了读写分离和数据库分表。
项目创建了两个库--master和slave，用于实现读写分离。
`user_info`表的数据根据`user_id`进行分片，每张表最多存100条数据，具体分片策略参看`tech.hongjian.sharding_sphere_demo.config.DataSourceConfig`类。  
** 数据库的主从配置这里不做介绍，因此测试插入时，数据并不会同步到从表。**

## 运行步骤

1. 执行schema.sql脚本，创建数据库与表。
2. 根据实际情况修改`application.yml`中datasource相关配置。
3. 运行`App.java`中的`main`方法。
4. 访问`IndexController`中的接口。