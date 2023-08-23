/**
  * Copyright 2021 json.cn 
  */
package com.qu.modules.web.pojo;
import java.util.List;

/**
 * Auto-generated: 2021-04-20 17:58:49
 *
 * @author json.cn (i@json.cn)
 * @website http://www.json.cn/java2pojo/
 */
public class Data {

    private TbUser tbUser;
    private Role role;
    private List<Funcs> funcs;
    private Organ organ;
    private List<Deps> deps;
    private List<Positions> positions;
    public void setTbUser(TbUser tbUser) {
         this.tbUser = tbUser;
     }
     public TbUser getTbUser() {
         return tbUser;
     }

    public void setRole(Role role) {
         this.role = role;
     }
     public Role getRole() {
         return role;
     }

    public void setFuncs(List<Funcs> funcs) {
         this.funcs = funcs;
     }
     public List<Funcs> getFuncs() {
         return funcs;
     }

    public void setOrgan(Organ organ) {
         this.organ = organ;
     }
     public Organ getOrgan() {
         return organ;
     }

    public void setDeps(List<Deps> deps) {
         this.deps = deps;
     }
     public List<Deps> getDeps() {
         return deps;
     }

    public void setPositions(List<Positions> positions) {
         this.positions = positions;
     }
     public List<Positions> getPositions() {
         return positions;
     }

}