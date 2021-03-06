package com.bigbear.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li
 * @description
 */
public class MyStreamHandler extends PumpStreamHandler {

  private static final Logger log = LoggerFactory.getLogger(MyStreamHandler.class);

  public static final Map<String, List<String>> UUID_LOGS_MAP = new HashMap<>();

  private final String uuid;

  public MyStreamHandler(final OutputStream out, final OutputStream err, String uuid) {
    super(out, err, null);
    this.uuid = uuid;
  }

  @Override
  protected void createProcessOutputPump(final InputStream is, final OutputStream os) {
    super.createProcessOutputPump(is, os);
  }

  @Override
  protected void createProcessErrorPump(final InputStream is, final OutputStream os) {
    super.createProcessErrorPump(is, os);
  }

  @Override
  protected Thread createPump(final InputStream is, final OutputStream os,
      final boolean closeWhenExhausted) {
    try {
      log.info("进入myHandler");
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String info;
      while ((info = br.readLine()) != null) {
        UUID_LOGS_MAP.get(uuid).add(info);
      }
    } catch (Exception e) {
      log.error("缓存实时日志异常", e);
    }
    return super.createPump(is, os, closeWhenExhausted);
  }

}
