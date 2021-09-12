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
- Zookeeper_Demo
  - Zookeeper/CuratorAPI  
- Dubbo_Demo
  - Dubbo + Zookeeper  
- Monitor_Demo
  - Skywalking + Prometheus/Grafana
- ElasticStack_LogDemo
  - ElasticSearch + Logstash + Kibana + Beats
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
    - SCConfig+SCBus:
        - localhost:10000
        - 可视化前端
    - Apollo:
        - localhost:8080
        - 可视化前端
    - Zookeeper:
      - None
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
    - Skywalking:
      - localhost: 9010
      - 可视化前端
6. 消息队列
    - rabbitMQ+SpringAMQP+SCStream:
        - localhost:5672
        - 可视化前端
    - Kafka
7. 分布式缓存&分布式数据库&分布式搜索&分布式事务
    - Redis:
        - None
    - Mysql:
        - localhost:3306
    - Canal:
        - localhost:11111
    - ElasticSearch:
        - locahost:9200
    - Seata:
        - None
8. 度量监控
    - Prometheus+Grafana
        - Prometheus: localhost:9090
        - Grafana: localhost:3000
        - 可视化前端
    - Zabbix
      - 可视化前端
9. 分布式日志
    - ElasticSearch + Logstash + Kibana + Beats
        - localhost:5601
        - 可视化前端
10.代理
    - Haproxy
    - F5
    - nginx+LUA
11.交易路由
    - Cassandra
    - TiKV
10. 报文转换
    - xml
    - hession
    - gsan
13.开发框架
    - Dubbo
    - Spring Cloud + Spring Boot + Spring Security
14.运维
    - 自动化编译Jenkins
    - 打包Docker
    - 自动化部署Kubernates
    - 代码检查Arthas
