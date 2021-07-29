package com.bigbear.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author li
 * @description
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueryLogResponse implements Serializable {

  private static final long serialVersionUID = 3357029520891803571L;

  /**
   * 本次读取到的内容
   */
  private List<String> content;
  /**
   * 本次读取到的最后索引（包含），下次查询从下一索引开始
   */
  private Integer end;
  /**
   * 是否已读完：0-未读完，1-已读完
   */
  private Integer readStatus;
}
