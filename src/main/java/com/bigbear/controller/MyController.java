package com.bigbear.controller;

import com.bigbear.handler.MyStreamHandler;
import com.bigbear.response.ExecuteResult;
import com.bigbear.response.QueryLogResponse;
import com.bigbear.util.DockerUtils;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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


  @GetMapping("/download")
  public void receiveFilename() throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http GET请求
    HttpGet httpGet = new HttpGet("https://cdp-dev-admin.zampdmp.com/open/wecom/chatData/getMedia?id=27&fileId=CtsDKjEqSHRKUjBGaDR1ZWpMT3NGZTJDTUlnOTdJSVZWUzh3K1U0bUtBV2JRRENMV0NCakw1TjNwZk5RMllOc2lWditSTlgxUUJ6VXhwaXJnMVhxeG8yYm1jaFZySzFzSjZENnRrUWwza0hLeGUrNG5TUUJkZ1d2VmFSTGJDVWxUZ3Y3eWRsdlljMkFLU1JjMmJiQnlHV1cxa0JHeko5VUpTZVEzNWNIYkl3dzFVdkRsNGk4VjlmdkFFT2tjd2hGMmFnNFV2RVk5MG1iY3I2MmFHUEM4bUpLcUR6anZndmhJVDdHUDczcmJJWHR4OFU1SU91aVB0eXlMYklMRnhEQ21ocm92eTB4QXpjVUljcWlLQ1hjVitmZHlxMWkzZFpHK2lSeFZ3U00vdXkyNXZ4WVA1Y3Z2YVNWVXZVdE5FblBFeCtPSHJuYW4yTjZKNGcremRoN3A3Kytpbjd2WkhEYWtzUGZpUGhZWDdXSXhYOWJ1ckFTMjBVTVZkSzdTSVRWM1B2V2pmVUVqT0JMSEdVaURMSUJnWXlkMTJXOHNzVURhU0NQTTN4MElSZDM5aDRPNVdONVhibG41TlRGL2ZIZVl1SkZlTzJxYlE4YnIxVkErakJickxoR05ibWc9PRI0TkRkZk1UWTRPRGcxTnpVeU1qSTBPRFkxTVY4NU9URTNOREUzT1Y4eE5qTTBOVFF3TURNMhogNmY3OTY4NzA2MTZlNjU3Mjc0N2E2MTY5NzQ2MTY2NjY=");
    CloseableHttpResponse response = null;
    try {
      // 执行请求
      response = httpclient.execute(httpGet);
      // 判断返回状态是否为200
      if (response.getStatusLine().getStatusCode() == 200) {
        //请求体内容
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        //内容写入文件
        File file = new File("/usr/my.mp3");
        file.createNewFile();
        FileUtils.writeStringToFile(file, content, "UTF-8");
        System.out.println("内容长度："+content.length());
      }
    } finally {
      if (response != null) {
        response.close();
      }
      //相当于关闭浏览器
      httpclient.close();
    }
  }
}
