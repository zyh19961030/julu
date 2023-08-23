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
public class Role {

    private String roleId;
    private String roleName;
    private Date createTime;
    private Date updateTime;
    private String status;
    private String isDelete;
    private String remark;
    private String system;
    private String type;
    private String organ;
    private String funcIds;
    private String funcs;
    public void setRoleId(String roleId) {
         this.roleId = roleId;
     }
     public String getRoleId() {
         return roleId;
     }

    public void setRoleName(String roleName) {
         this.roleName = roleName;
     }
     public String getRoleName() {
         return roleName;
     }

    public void setCreateTime(Date createTime) {
         this.createTime = createTime;
     }
     public Date getCreateTime() {
         return createTime;
     }

    public void setUpdateTime(Date updateTime) {
         this.updateTime = updateTime;
     }
     public Date getUpdateTime() {
         return updateTime;
     }

    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setIsDelete(String isDelete) {
         this.isDelete = isDelete;
     }
     public String getIsDelete() {
         return isDelete;
     }

    public void setRemark(String remark) {
         this.remark = remark;
     }
     public String getRemark() {
         return remark;
     }

    public void setSystem(String system) {
         this.system = system;
     }
     public String getSystem() {
         return system;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setOrgan(String organ) {
         this.organ = organ;
     }
     public String getOrgan() {
         return organ;
     }

    public void setFuncIds(String funcIds) {
         this.funcIds = funcIds;
     }
     public String getFuncIds() {
         return funcIds;
     }

    public void setFuncs(String funcs) {
         this.funcs = funcs;
     }
     public String getFuncs() {
         return funcs;
     }

}