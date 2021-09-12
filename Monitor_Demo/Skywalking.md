# Skywalking

## Tomcat
- Linux
  - CATALINA_OPTS="$CATALINA_OPTS -javaagent:/path/to/skywalking-agent/skywalking-agent.jar"; export CATALINA_OPTS
- Windows
  - set "CATALINA_OPTS=-javaagent:/path/to/skywalking-agent/skywalking-agent.jar"
## Spring Boot / Jar
  - java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar -jar yourApp.jar

## 获取追踪ID
  - apm-toolkit-trace
    - TraceContext
      - traceId
    - ActiveSpan
      - error/info/debug
  
## 过滤指定端点
  - apm-trace-ignore-plugin
    - Dskywalking.trace.ignore_path=/xx?*

## 告警功能
  - config中alarm-seeting.yml
    - rules
      - metrics_name 度量名称
      - threshold 阈值
      - op 操作符
      - period 多就触发一次
      - count 超过多少次告警
      - silence-period 多久内忽略相同告警信息
      - message 告警信息内容
      - include-names 本规则告警生效的服务列表
      - webhooks 产生告警后的回调地址

## Agent实现
  - 实现premain方法(会优先调用双参数的)
  - MANIFEST.MF指定premain-class项

## 统计方法调用时间
  - 实现 Byte Buddy 
    - premain中
      - AgentBuilder.transformer()
      - DynamicType.Builder transform(){builder.method.intercept(拦截方法)}
      - new AgentBuilder.Default().type(拦截包名).transform().installOn()