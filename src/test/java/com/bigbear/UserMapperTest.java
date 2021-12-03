//package com.bigbear;
//
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONObject;
//import com.baomidou.ant.user.entity.User;
//import com.baomidou.ant.user.mapper.UserMapper;
//import com.baomidou.ant.user.service.IUserService;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * @author li
// * @description
// */
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class UserMapperTest {
//
//  @Autowired
//  private UserMapper userMapper;
//
//  @Autowired
//  private IUserService userService;
//
//  @Autowired
//  private StringRedisTemplate stringRedisTemplate;
//
//  @Test
//  public void testSelect(){
//    List<User> users =
//        this.userMapper.selectList(null);
//    Assert.assertEquals(5, users.size());
//    users.forEach(System.out::println);
//  }
//
//  @Test
//  public void testInsert(){
//    User user = new User();
//    user.setId(6L);
//    user.setName("lyd");
//    user.setAge(27);
//    user.setEmail("lyd@baidu.com");
//    this.userMapper.insert(user);
//  }
//
//  @Test
//  public void testService(){
////    String s = HttpUtil.get("localhost:8087/common/send_msg?field=" + "18506130134");
//    Map map = new HashMap<String,Object>(2);
//    map.put("mobile", "222222");
//    map.put("msg","test");
//    String post = HttpUtil.post("localhost:8087/common/send_msg", new JSONObject(map).toJSONString(0));
//    System.out.println("s = " + post);
//  }
//
//  @Test
//  public void testZSet(){
//    this.stringRedisTemplate.opsForZSet().add("testZSet","0.0.0.1",0.1);
//    this.stringRedisTemplate.opsForZSet().add("testZSet","0.0.0.2",3);
//    this.stringRedisTemplate.opsForZSet().add("testZSet","0.0.0.3",2.1);
//  }
//
//}
