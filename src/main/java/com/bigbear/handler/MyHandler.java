package com.bigbear.handler;

import com.bigbear.util.DockerUtils;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li
 * @description
 */
public class MyHandler extends DefaultExecuteResultHandler {

  private static final Logger log = LoggerFactory.getLogger(MyHandler.class);

  // TODO: 2021/7/28 实际场景中这边应该还有一个任务id，来方便存储任务id和容器id的映射，以便于后面的接口通过任务id来查询相关信息
//  private Long taskId;

  private final String uuid;

  public MyHandler(String uuid) {
    super();
    this.uuid = uuid;
  }

  @Override
  public void onProcessComplete(int exitValue) {
    // TODO: 2021/7/29 1。日志持久化2。删除容器
    //docker ps -a --filter 'name=afea8187d8fa476a9006dd8e2675e818' --format '{{.ID}}'
    //docker rm $(docker ps -a --filter 'name=afea8187d8fa476a9006dd8e2675e818' --format '{{.ID}}')
    //docker ps -a --filter 'name=26b6538ace0a44cc9deb6b3ddf6decaf' --format '{{.ID}}' | xargs docker rm
    log.info("handler invoke on complete:{}", exitValue);
    MyStreamHandler.UUID_LOGS_MAP.remove(uuid);
    DockerUtils.rmContainerByUuid(this.uuid);
    super.onProcessComplete(exitValue);
  }

  @Override
  public void onProcessFailed(ExecuteException e) {
    log.info("handler invoke on failed", e);
    super.onProcessFailed(e);
  }



}
