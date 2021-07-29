package com.bigbear;

import com.bigbear.handler.MyHandler;
import com.bigbear.handler.MyStreamHandler;
import com.bigbear.response.ExecuteResult;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
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

  private static final String CONTAINER_ID = "6da9ec6e6ce4b7eda11b55f4c23bdee80747fb8e638e916e28b2e74fcdac198c";

  /**
   * 执行任务
   **/
  @Test
  public void testCreate() {
    //1。第一次从镜像启动
    //选用后台执行
    String command =
        "docker run --name " + UUID.randomUUID().toString().replaceAll("-", "") + " ppp";
//        "docker run --name " + UUID.randomUUID().toString().replaceAll("-", "") + " -d ppp";
//    String command = "docker run ppp";
//    2。已有容器，直接启动不新建（每次的任务都不一样）
//    String command = "docker start " + CONTAINER_ID;
//3。如果容器已启动，那么只能新建一个容器启动
//    this.doExecute(command,true);
    //这边就可以保存任务id和uuid的映射关系了
    this.asyncDoExecute(command, true);
  }

  /**
   * 终止任务
   */
  @Test
  public void testStop() {
    String command = "docker container stop " + CONTAINER_ID;
    this.execute(command,false);
  }


  /**
   * 查询任务状态
   **/
  @Test
  public void testQueryStatus() throws Exception {
//    String command = "docker ps -a --filter 'id="+CONTAINER_ID+"'";
    String command = "docker ps -a --filter 'id=" + CONTAINER_ID + "' --format '{{.Status}}'";

    this.execute(command,false);
  }

  @Test
  public void testLog() throws Exception {
    String command = "docker logs -f " + CONTAINER_ID;
//    String command = "docker container logs "+CONTAINER_ID;
    // TODO: 2021/7/23 怎么实时打印日志
//    ExecuteResult execute = this.execute(command);
    ExecuteResult executeResult = this.doExecute(command, false);
//    System.out.println("execute.getException() = " + execute.getException());
  }


  private ExecuteResult execute(String command, boolean isCreate) {
    ExecuteResult executeResult = this.doExecute(command, isCreate);
    if (executeResult.getSuccess()) {
      String result = executeResult.getResult();
      log.info("执行成功：{}", result);
    } else {
      log.error("执行异常：{}", executeResult.getException());
    }
    return executeResult;
  }

  public static ExecuteResult doExecute(String command, boolean isCreate) {
    CommandLine cmdLine = CommandLine.parse(command);
    //这边可以做个线程池来异步跑
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValues(null);
    //超时时间为一天
    ExecuteWatchdog watchdog = new ExecuteWatchdog(1000 * 60 * 60 * 24);
    executor.setWatchdog(watchdog);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();) {
      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
//      executor.setStreamHandler(new MyStreamHandler(outputStream, errorStream,null));
      //这边日志查询的时候需要特殊处理
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

  private ExecuteResult asyncDoExecute(String command, boolean isCreate) {
    CommandLine cmdLine = CommandLine.parse(command);
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValues(null);
    ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
    executor.setWatchdog(watchdog);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();) {
//      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);

//      executor.setStreamHandler(streamHandler);
      executor.setStreamHandler(new MyStreamHandler(outputStream, errorStream,null));
      executor.execute(cmdLine, new MyHandler(null));

      String out = outputStream.toString("gbk");//获取程序外部程序执行结果
      System.out.println("out = " + out);
      String error = errorStream.toString("gbk");
      System.out.println("error = " + error);
      System.out.println("async execute success");
    } catch (Exception e) {
      log.error("command exec exception", e);
    }
    return ExecuteResult.defaultResult();

  }

}
