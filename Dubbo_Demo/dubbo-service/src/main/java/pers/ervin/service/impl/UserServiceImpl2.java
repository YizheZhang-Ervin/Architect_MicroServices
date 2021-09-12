package pers.ervin.service.impl;

import pers.ervin.pojo.User;
import pers.ervin.service.UserService;
import org.apache.dubbo.config.annotation.Service;


//@Service//将该类的对象创建出来，放到Spring的IOC容器中  bean定义
//将这个类提供的方法（服务）对外发布。将访问的地址 ip，端口，路径注册到注册中心中
// 重试及超时
@Service(timeout=3000,retries = 0,version = "v2.0",weight = 100)
public class UserServiceImpl2 implements UserService {

    public String sayHello() {
        return "hello dubbo hello!~";
    }


    public User findUserById(int id) {
        System.out.println("3...");
        //查询User对象
        User user = new User(1,"zhangsan","123");
/*
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return user;
    }
}
