package com.bigbear;

import java.io.ByteArrayOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li
 * @description
 */
public class DockerTest {

  private static final Logger log = LoggerFactory.getLogger(DockerTest.class);

  private static final String CONTAINER_ID = "c87868465be3de0a983b2e53b439ed63991241c0edfa07d23df3dfe1efc12cbf";

  /**
   * 执行任务
   **/
  @Test
  public void testCreate() {
    //1。第一次从镜像启动
//    String command = "docker run -d -P training/webapp python app.py";
//    2。已有容器，直接启动不新建
    String command = "docker start " + CONTAINER_ID;
//3。如果容器已启动，那么只能新建一个容器启动
    this.execute(command);
  }

  /**
   * 终止任务
   */
  @Test
  public void testStop() {
    String command = "docker container stop " + CONTAINER_ID;
    this.execute(command);
  }


  /**
   * 查询任务状态
   **/
  @Test
  public void testQueryStatus() throws Exception {
//    String command = "docker ps -a --filter 'id="+CONTAINER_ID+"'";
    String command = "docker ps -a --filter 'id=" + CONTAINER_ID + "' --format '{{.Status}}'";

    this.execute(command);
  }

  @Test
  public void testLog() {
    String command = "docker logs -f " + CONTAINER_ID;
//    String command = "docker container logs "+CONTAINER_ID;
    // TODO: 2021/7/23 怎么实时打印日志
    ExecuteResult execute = this.execute(command);
    System.out.println("execute.getException() = " + execute.getException());
  }


  private ExecuteResult execute(String command) {
    ExecuteResult executeResult = this.doExecute(command);
    if (executeResult.getSuccess()) {
      String result = executeResult.getResult();
      log.info("执行成功：{}", result);
    } else {
      log.error("执行异常：{}", executeResult.getException());
    }
    return executeResult;
  }

  private ExecuteResult doExecute(String command) {
    CommandLine cmdLine = CommandLine.parse(command);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValues(null);
    ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
    executor.setWatchdog(watchdog);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();) {
      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

      executor.setStreamHandler(streamHandler);
      int execute = executor.execute(cmdLine);
      System.out.println("execute = " + execute);
      String out = outputStream.toString("gbk");//获取程序外部程序执行结果
      System.out.println("out = " + out);
      String error = errorStream.toString("gbk");
      System.out.println("error = " + error);
      return new ExecuteResult.Builder().code(execute).result(out).exception(error).build();
    } catch (Exception e) {
      log.error("command exec exception", e);
    }
    return ExecuteResult.defaultResult();

  }
}
