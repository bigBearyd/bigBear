package com.bigbear.controller;

import com.bigbear.handler.MyStreamHandler;
import com.bigbear.response.ExecuteResult;
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

  /**
   * 创建容器
   **/
  @GetMapping("/create")
  public String create() {
    return this.testCreate();
  }

  /**
   * 实时日志
   **/
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
        "docker run --name " + uuid + " excep";
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
  @GetMapping("/stop/{uuid}")
  public void testStop(@PathVariable String uuid) {
    DockerUtils.stopByUuid(uuid);
  }



  /**
   * 查询任务状态
   **/
  @GetMapping("/query_status/{uuid}")
  public void testQueryStatus(@PathVariable String uuid){
    ExecuteResult executeResult = DockerUtils.queryStatusByUuid(uuid);
    if(Boolean.TRUE.equals(executeResult.getSuccess())){
      log.info("status:{}", executeResult.getResult());
    }
  }

  /**
   * 全量日志
   **/
  @GetMapping("/log_all/{uuid}")
  public void testAllLog(@PathVariable String uuid){
    DockerUtils.queryAllLogsByUuid(uuid);
  }

}
