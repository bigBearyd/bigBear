//package com;
//
//import java.util.concurrent.atomic.AtomicInteger;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cache.annotation.EnableCaching;
//
///**
// * @author li
// * @description
// */
//@EnableCaching
//@MapperScan("com.**/mapper/**")
//@SpringBootApplication
//public class BigBearApplication {
//
//  private static final AtomicInteger COUNT = new AtomicInteger(0);
//
//  public static void main(String[] args)throws Exception {
////    int countAndIncrement = COUNT.getAndIncrement();
////    while (countAndIncrement<100){
////    System.out.println(countAndIncrement);
////    Thread.sleep(1000L);
////    countAndIncrement= COUNT.getAndIncrement();
////    }
////    throw new RuntimeException("我故意的");
//
////    new SpringApplication(BigBearApplication.class).run();
//    SpringApplication.run(BigBearApplication.class, args);
//  }
//}
