package com.bigbear;

import com.bigbear.mybatisplus.entity.User;
import com.bigbear.mybatisplus.mapper.UserMapper;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author li
 * @description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void testSelect(){
    List<User> users =
        this.userMapper.selectList(null);
    Assert.assertEquals(5, users.size());
    users.forEach(System.out::println);
  }

}
