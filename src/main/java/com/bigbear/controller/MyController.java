package com.bigbear.controller;

import com.bigbear.handler.MyStreamHandler;
import com.bigbear.response.QueryLogResponse;
import com.bigbear.util.DockerUtils;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author li
 * @description
 */
@CrossOrigin(origins = "*")
@Slf4j
@RestController
public class MyController {

  @GetMapping("/create")
  public String create() {
    return this.testCreate();
  }

  @GetMapping("/log/{key}/{start}")
  public QueryLogResponse logs(@PathVariable("key") String key,
      @PathVariable("start") Integer start) {
    List<String> logs = MyStreamHandler.UUID_LOGS_MAP.get(key);
    if (logs == null) {
      return QueryLogResponse.builder().content(Collections.emptyList()).end(-1).readStatus(1)
          .build();
    }
    int size = logs.size();
    return QueryLogResponse.builder().content(logs.subList(start + 1, size)).end(size - 1)
        .readStatus(0).build();
  }

  /**
   * 执行任务
   **/
  public String testCreate() {
    //1。第一次从镜像启动
    String uuid = UUID.randomUUID().toString().replaceAll("-", "");
    //选用后台执行
    String command =
        "docker run --name " + uuid + " ppp";
//        "docker run --name " + uuid + " -d ppp";
//    String command = "docker run ppp";
//    2。已有容器，直接启动不新建（每次的任务都不一样）
//    String command = "docker start " + CONTAINER_ID;
//3。如果容器已启动，那么只能新建一个容器启动
//    this.doExecute(command,true);
    DockerUtils.doCreate(command, uuid);
//    this.asyncDoExecute(command, true,uuid);
    return uuid;
  }

  /**
   * 终止任务
   */
//  public void testStop() {
////    String command = "docker container stop " + CONTAINER_ID;
////    this.execute(command,false);
//  }
//
//
//  /**
//   * 查询任务状态
//   **/
//  public void testQueryStatus() throws Exception {
////    String command = "docker ps -a --filter 'id="+CONTAINER_ID+"'";

////    String command = "docker ps -a --filter 'id=" + CONTAINER_ID + "' --format '{{.Status}}'";
//
////    this.execute(command,false);
//  }
//
//  public void testLog() throws Exception {
////    String command = "docker logs -f " + CONTAINER_ID;
////    String command = "docker container logs "+CONTAINER_ID;
//    // TODO: 2021/7/23 怎么实时打印日志
////    ExecuteResult execute = this.execute(command);
////    ExecuteResult executeResult = this.doExecute(command, false);
////    System.out.println("execute.getException() = " + execute.getException());
//  }


//  private ExecuteResult execute(String command, boolean isCreate) {
//    ExecuteResult executeResult = this.doExecute(command, isCreate);
//    if (executeResult.getSuccess()) {
//      String result = executeResult.getResult();
//      log.info("执行成功：{}", result);
//    } else {
//      log.error("执行异常：{}", executeResult.getException());
//    }
//    return executeResult;
//  }

//  private ExecuteResult doExecute(String command, boolean isCreate) {
//    CommandLine cmdLine = CommandLine.parse(command);
//    DefaultExecutor executor = new DefaultExecutor();
//    executor.setExitValues(null);
//    //超时时间为一天
//    ExecuteWatchdog watchdog = new ExecuteWatchdog(1000 * 60 * 60 * 24);
//    executor.setWatchdog(watchdog);
//
//    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();) {
////      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
//      executor.setStreamHandler(new MyStreamHandler(outputStream, errorStream));
//      //这边日志查询的时候需要特殊处理
//      int execute = executor.execute(cmdLine);
//      System.out.println("execute = " + execute);
//      String out = outputStream.toString("gbk");//获取程序外部程序执行结果
//      System.out.println("out = " + out);
//      String error = errorStream.toString("gbk");
//      System.out.println("error = " + error);
//      return new ExecuteResult.Builder().code(execute).result(out).exception(error).build();
//    } catch (Exception e) {
//      log.error("command exec exception", e);
//    }
//    return ExecuteResult.defaultResult();
//
//  }

//  private ExecuteResult asyncDoExecute(String command, boolean isCreate, String uuid) {
//    CommandLine cmdLine = CommandLine.parse(command);
//    DefaultExecutor executor = new DefaultExecutor();
//    executor.setExitValues(null);
//    ExecuteWatchdog watchdog = new ExecuteWatchdog(60000);
//    executor.setWatchdog(watchdog);
//
//    try  {
//      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//      ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
////      PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream, errorStream);
//
////      executor.setStreamHandler(streamHandler);
//      executor.setStreamHandler(new MyStreamHandler(outputStream, errorStream,uuid));
//      MyStreamHandler.map.put(uuid,new LinkedList<>());
//      executor.execute(cmdLine, new MyHandler(uuid));
//
//      String out = outputStream.toString("gbk");//获取程序外部程序执行结果
//      System.out.println("out = " + out);
//      String error = errorStream.toString("gbk");
//      System.out.println("error = " + error);
//      System.out.println("async execute success");
//    } catch (Exception e) {
//      log.error("command exec exception", e);
//    }
//    return ExecuteResult.defaultResult();
//
//  }
}
