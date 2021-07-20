package com.bigbear.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li
 */
public class CountDownLatchDemo {

  private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(10);
  private static final Logger logger = LoggerFactory.getLogger(CountDownLatchDemo.class);
  private static final ThreadPoolExecutor POOL_EXECUTOR = new ThreadPoolExecutor(5, 100, 0,
      TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(128),
      Executors.defaultThreadFactory());

  public static void main(String[] args) throws Exception {

    for (int i = 0; i < 10; i++) {
      POOL_EXECUTOR.execute(new MyThread(COUNT_DOWN_LATCH, i));
    }

    COUNT_DOWN_LATCH.await();
    logger.info("log执行完毕");
    POOL_EXECUTOR.shutdown();
  }

  static class MyThread implements Runnable {

    private final CountDownLatch count;
    private final Integer id;

    public MyThread(CountDownLatch count, Integer id) {
      this.count = count;
      this.id = id;
    }

    @Override
    public void run() {
      logger.info("第{}个任务开始"
          + "执行", id);
      count.countDown();
    }
  }
}
