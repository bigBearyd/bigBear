package com.bigbear.util;

import com.bigbear.handler.MyHandler;
import com.bigbear.handler.MyStreamHandler;
import com.bigbear.response.ExecuteResult;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.StringUtils;

/**
 * @author li
 * @description docker 执行工具
 */
@Slf4j
public class DockerUtils {

  private static final long WATCH_TIME_OUT = 1000 * 60 * 60 * 24L;

  private DockerUtils() {
  }

  public static ExecuteResult doExecute(String command, boolean isCreate, String uuid) {
    CommandLine cmdLine = CommandLine.parse(command);
    //这边可以做个线程池来异步跑
    DefaultExecutor executor = new DefaultExecutor();
    executor.setExitValues(null);
    //超时时间为一天
    ExecuteWatchdog watchdog = new ExecuteWatchdog(WATCH_TIME_OUT);
    executor.setWatchdog(watchdog);

    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream()) {
      int execute = 0;
      if (isCreate) {
        MyStreamHandler.map.put(uuid, new LinkedList<>());
        executor.setStreamHandler(new MyStreamHandler(outputStream, errorStream, uuid));
        executor.execute(cmdLine, new MyHandler(uuid));
      } else {
        executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream));
        execute = executor.execute(cmdLine);
        log.info("execute = {}", execute);
      }
      //获取程序外部程序执行结果
      String out = outputStream.toString("gbk");
      log.info("out = {}", out);
      String error = errorStream.toString("gbk");
      log.info("error = {}", error);
      return new ExecuteResult.Builder().code(execute).result(out).exception(error).build();
    } catch (Exception e) {
      log.error("command exec exception", e);
    }
    return ExecuteResult.defaultResult();

  }

  public static ExecuteResult doCreate(String command, String uuid) {
    return doExecute(command, true, uuid);
  }

  public static ExecuteResult doOther(String command) {
    return doExecute(command, false, null);
  }

  public static void removeContainerByUuid(String uuid) {
//    String command ="docker rm $(docker ps --all --filter 'name="+uuid+"' --format '{{.ID}}') ";
    String getContainerIdCommend =
        "docker ps -a --filter 'name=" + uuid + "' --format '{{.ID}}'";
    ExecuteResult executeResult = DockerUtils.doOther(getContainerIdCommend);
    if (Boolean.TRUE.equals(executeResult.getSuccess()) && StringUtils
        .isNotBlank(executeResult.getResult())
    ) {
      String rmContainerCommend = "docker rm " + executeResult.getResult();
      DockerUtils.doOther(rmContainerCommend);
    } else {
      log.error("{}容器清除失败：{}", uuid, executeResult.getException());
    }
  }
}
