package pers.ervin.feign.clients.fallback;

import pers.ervin.feign.clients.UserClient;
import pers.ervin.feign.pojo.User;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// 隔离和降级
public class UserClientFallbackFactory implements FallbackFactory<UserClient> {
  @Override
  public UserClient create(Throwable throwable) {
    return new UserClient() {
      @Override
      public User findById(Long id) {
        log.error("查询用户异常", throwable);
        return new User();
      }
    };
  }
}
