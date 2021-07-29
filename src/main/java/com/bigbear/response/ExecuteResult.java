package com.bigbear.response;

/**
 * @author li
 * @description
 */
public class ExecuteResult {

  private static final Integer CODE_SUCCESS = 0;

  private static final String COMMON_EXCEPTION="通用异常信息";

  /**
   * 0-成功
   **/
  private final Integer code;

  private final Boolean success;

  private final String result;

  private final String exception;

  public Integer getCode() {
    return code;
  }

  public Boolean getSuccess() {
    return success;
  }

  public String getResult() {
    return result;
  }

  public String getException() {
    return exception;
  }

  @Override
  public String toString() {
    return "ExecuteResult{" +
        "code=" + code +
        ", success=" + success +
        ", result='" + result + '\'' +
        ", exception='" + exception + '\'' +
        '}';
  }

  public ExecuteResult(Builder builder){
    this.code = builder.code;
    this.success = CODE_SUCCESS.equals(this.code)?Boolean.TRUE:Boolean.FALSE;
    this.result = builder.result;
    this.exception = builder.exception;
  }



  public static ExecuteResult defaultResult() {
    return new Builder().code(1).exception(COMMON_EXCEPTION).build();

  }

  public static class Builder{
    private int code;

    private String result;

    private String exception;
    public Builder(){}

    public Builder code(int code){
      this.code = code;
      return this;
    }
    public Builder result(String result){
      this.result = result;
      return this;
    }
    public Builder exception(String exception){
      this.exception = exception;
      return this;
    }
    public ExecuteResult build(){
      return new ExecuteResult(this);
    }
  }

}
