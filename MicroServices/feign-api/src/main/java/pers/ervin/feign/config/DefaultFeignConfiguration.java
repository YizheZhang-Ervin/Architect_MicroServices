package pers.ervin.feign.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import pers.ervin.feign.clients.fallback.UserClientFallbackFactory;

public class DefaultFeignConfiguration {
  @Bean
  public Logger.Level logLevel() {
    return Logger.Level.BASIC;
  }

  @Bean
  public UserClientFallbackFactory userClientFallbackFactory() {
    return new UserClientFallbackFactory();
  }
}
