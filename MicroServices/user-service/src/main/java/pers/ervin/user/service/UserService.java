package pers.ervin.user.service;

import pers.ervin.user.mapper.UserMapper;
import pers.ervin.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired private UserMapper userMapper;

  public User queryById(Long id) {
    return userMapper.findById(id);
  }
}
