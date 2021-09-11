# DevOps_MicroServices

Micro Services Architecture with SpringCloud related components

## MicroServices Demos

- MicroServices
    - Eureka/Nacos + Feign/Ribbon + Sentinel + SpringCloud Gateway + Mybatis
- MicroServicesV2
    - Consul + Feign + Hystrix + Zuul+ JPA + Sleuth + Zipkin + RabbitMQ
- ElasticSearch_Demo
    - ElasticSearch
- MessageQueue_Demo & MessageQueue_Advanced_Demo
    - RabbitMQ + SpringAMQP
- MultiCache_Demo
    - Canal + Redis
- Redis_Demo
    - Redis
- Seata_Demo
    - Seata + Nacos
- SCStream_Demo
    - RabbitMQ + SpringCloud Stream
- SCConfig_Demo
    - SpringCloud Config + Eureka + RabbitMQ
- Apollo_Demo
    - Apollo

## MicroServices URLs

1. 注册中心&配置中心
    - Eureka:
        - localhost:10086/eureka
        - 可视化前端
    - Nacos:
        - localhost:8848/nacos
        - 可视化前端
        - nacos/bin/startup.cmd -m standalone(登录: nacos-nacos)
    - Consul:
        - localhost:8500
        - 可视化前端
    - SCConfig:
        - localhost:10000
        - 可视化前端
    - Apollo:
        - localhost:8080
        - 可视化前端
2. 网关
    - SCGateway:
        - localhost:10010
    - Zuul:
        - localhost:8080
3. 微服务保护
    - Hystrix:
        - None
    - Sentinel Dashboard:
        - localhost:8080
        - 可视化前端
4. 服务调用
    - Feign:
        - None
    - Ribbon:
        - None
5. 链路追踪
    - Sleuth+Zipkin:
        - localhost:9411
        - 可视化前端
6. 消息队列
    - rabbitMQ:
        - localhost:5672
        - 可视化前端
7. 分布式缓存&分布式数据库&分布式搜索&分布式事务
    - Redis:
        - None
    - Mysql:
        - localhost:3306
    - Canal:
        - localhost:11111
    - ElasticSearch:
        - None
    - Seata:
        - None

  
