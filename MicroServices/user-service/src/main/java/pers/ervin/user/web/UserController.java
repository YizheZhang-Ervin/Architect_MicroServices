package pers.ervin.user.web;

import pers.ervin.user.config.PatternProperties;
import pers.ervin.user.pojo.User;
import pers.ervin.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/user")
// 配置热更新:方式1
// @RefreshScope
public class UserController {

  @Autowired private UserService userService;

  // @Value("${pattern.dateformat}")
  // private String dateformat;

  @Autowired private PatternProperties properties;

  @GetMapping("prop")
  public PatternProperties properties() {
    return properties;
  }

  @GetMapping("now")
  public String now() {
    return LocalDateTime.now().format(DateTimeFormatter.ofPattern(properties.getDateformat()));
  }

  /**
   * 路径： /user/110
   *
   * @param id 用户id
   * @return 用户
   */
  @GetMapping("/{id}")
  public User queryById(
      @PathVariable("id") Long id, @RequestHeader(value = "Truth", required = false) String truth)
      throws InterruptedException {
    System.out.println("truth: " + truth);
    // 熔断策略-异常比例
    if (id == 1) {
      // id为1 出发慢调用
      Thread.sleep(60);
      // 休眠，触发熔断
    } else if (id == 2) {
      throw new RuntimeException("故意出错，触发熔断");
    }
    return userService.queryById(id);
  }
}
