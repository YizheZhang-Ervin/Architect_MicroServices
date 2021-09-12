# Prometheus

## 主要构成
  - Server
  - Exporter
  - AlertManager
  - PushGateway
## 部署
  - cat /usr/lib/systemd/system/prometheus.service
  - localhost:9090

## 配置
  - global： 此片段指定的是 prometheus 的全局配置， 比如采集间隔，抓取超时时间等。 
  - rule_files： 此片段指定报警规则文件， prometheus 根据这些规则信息，会推送报警信息到 alertmanager 中。 
  - scrape_configs: 此片段指定抓取配置，prometheus 的数据采集通过此片段配置。 
  - alerting: 此片段指定报警配置， 这里主要是指定 prometheus 将报警规则推送到指定的 alertmanager 实例地址。 
  - remote_write: 指定后端的存储的写入 api 地址。 
  - remote_read: 指定后端的存储的读取 api 地址。

## PromQL
  - 运算乘：* 除：/ 加：+ 减：- 
  - 常用函数 
    - sum() 函数：求出找到所有 value 的值 
    - irate() 函数：统计平均速率 
    - by (标签名) 
    - 范围匹配  [5m] # 5 分钟之内
    - 查询指定 mertic_name node_cpu_seconds_total
    - 带标签的查询 node_cpu_seconds_total{instance="172.16.0.8:9100"}
    - 多标签查询 node_cpu_seconds_total{instance="172.16.0.8:9100",mode="system"}
    - 计算 CPU 使用率 100 - (avg(irate(node_cpu_seconds_total{mode="idle"}[5m])) by (instance) * 100)
    - 计算内存使用率 100 -(memFree+cached+buffers)/memTotal*100
    - 计算磁盘使用率 100 - (((node_filesystem_size_bytes{fstype=~"xfs|ext4"} - node_filesystem_free_bytes{fstype=~"xfs|ext4"}) / node_filesystem_size_bytes{fstype=~"xfs|ext4"}) * 100)
  
## Grafana
  - localhost:3000
  - 加入prometheus数据源

## 告警模块部署
  - cat /usr/lib/systemd/system/alertmanager.service
  - 检查语法 
    - ./amtool check-config alertmanager.yml

## 邮件告警
  - alertmanager.yml
  - 告警配置vim /usr/local/prometheus/prometheus.yml
    - 修改 prometheus.yml 的 alerting 部分
    - 定义告警文件prometheus.yml的rule_files部分
    - 告警规则修改host_monitor.yml
      - alert：告警规则的名称。 
      - expr：基于 PromQL 表达式告警触发条件，用于计算是否有时间序列满足该条件。 
      - for：评估等待时间，可选参数。用于表示只有当触发条件持续一段时间后才发送告警。在 等待期间新产生告警的状态为 pending。 
      - labels：自定义标签，允许用户指定要附加到告警上的一组附加标签。 
      - annotations：用于指定一组附加信息，比如用于描述告警详细信息的文字等，annotations 的内容在告警产生时会一同作为参数发送到 Alertmanager。 
      - summary 描述告警的概要信息，description 用于描述告警的详细信息。
    - Prometheus Alert 告警状态有三种状态：Inactive、Pending、Firing。 
      - Inactive：非活动状态，表示正在监控，但是还未有任何警报触发。 
      - Pending：表示这个警报必须被触发。由于警报可以被分组、压抑/抑制或静默/静音，所以等待验证，一旦所有的验证都通过，则将转到 Firing 状态。 
      - Firing：将警报发送到 AlertManager，它将按照配置将警报的发送给所有接收者。一旦警 报解除，则将状态转到 Inactive，如此循环
    - 告警模板
      - 模板email.tmpl
      - 改配置alertmanager.yml
    
## 企业微信告警
  - wechat.tmpl
  - alertmanager.yml

## 告警的标签、路由、分组
  - mysql.yml
  - alertmanager.yml

## 监控实战
  - 主机
  - Mysql
    - GRANT SELECT, PROCESS, SUPER, REPLICATION CLIENT, RELOAD ON *.* TO 'exporter'@'localhost' IDENTIFIED BY '123456';
    - 主库grant replication slave on *.* to repl@'172.16.0.%' identified by '123456'; 
    - 从库CHANGE MASTER TO MASTER_HOST='172.16.0.8', MASTER_USER='repl', MASTER_PASSWORD='123456', MASTER_PORT=3306, MASTER_AUTO_POSITION=1;
  - Redis
  - Docker
## 查询
- 容器 CPU 使用率: 
  -sum(irate(container_cpu_usage_seconds_total{image!=""}[1m])) without (cpu) 
- 查询容器内存使用量（单位：字节）: 
  - container_memory_usage_bytes{image!=""} 
- 查询容器网络接收量速率（单位：字节/秒）：
  - sum(rate(container_network_receive_bytes_total{image!=""}[1m])) without (interface) 
- 查询容器网络传输量速率（单位：字节/秒）： 
  - sum(rate(container_network_transmit_bytes_total{image!=""}[1m])) without (interface) 
- 查询容器文件系统读取速率（单位：字节/秒）： 
  - sum(rate(container_fs_reads_bytes_total{image!=""}[1m])) without (device) 
- 查询容器文件系统写入速率（单位：字节/秒）： 
  - sum(rate(container_fs_writes_bytes_total{image!=""}[1m])) without (device)

## pushgateway
