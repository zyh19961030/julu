/**
  * Copyright 2021 json.cn 
  */
package com.qu.modules.web.pojo;
import java.util.Date;

/**
 * Auto-generated: 2021-04-20 17:58:49
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class JsonRootBean {

    private int code;
    private Data data;
    private String msg;
    private Date time;
    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setTime(Date time) {
         this.time = time;
     }
     public Date getTime() {
         return time;
     }

}