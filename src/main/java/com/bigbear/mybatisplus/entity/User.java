package com.bigbear.mybatisplus.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.ToString;

/**
 * @author li
 * @description
 */
//@Setter
//@Data
@ToString
//@Getter
public class User {

//  @JsonIgnore
  private Long id;

  private String name;
  private Integer age;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JSONField(name = "t_name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  private String email;



  public static void main(String[] args) throws Exception{
    String s = "{\n"
        + "  \"id\": 97,\n"
        + "  \"name\": \"fake_data\",\n"
        + "  \"age\": 53,\n"
        + "  \"email\": \"fake_data\"\n"
        + "}";
//    ObjectMapper objectMapper = new ObjectMapper();
//    ObjectReader objectReader = objectMapper.readerFor(User.class);
//    User user = (User)objectReader.readValue(s);
    Object parse = JSON.parseObject(s, User.class);
    System.out.println("user = " + parse);
//    System.out.println(objectMapper.writeValueAsString(user));
//    User user = new User();
//    user.setName("jjj");
    System.out.println(JSON.toJSONString(parse));
  }
}
