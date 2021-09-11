package pers.ervin.order;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import pers.ervin.feign.clients.UserClient;
import pers.ervin.feign.config.DefaultFeignConfiguration;

@MapperScan("pers.ervin.order.mapper")
@SpringBootApplication
// 修改日志级别
@EnableFeignClients(
    clients = UserClient.class,
    defaultConfiguration = DefaultFeignConfiguration.class)
// 指定扫描包方式2
// @EnableFeignClients(basePackages = "pers.ervin.feign.clients")
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  //  自定义负载均衡
  @Bean
  public IRule randomRule() {
    return new RandomRule();
  }
}
