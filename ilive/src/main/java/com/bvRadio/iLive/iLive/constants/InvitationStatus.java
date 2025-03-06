package com.bvRadio.iLive.iLive.constants;

/**
 * 邀请函信息
 */
public enum InvitationStatus {
    HOST("host",1),//主持人
    PARTICIPANT("participant",2),//参会者
    AUDIENCE("audience",3);//观众

    InvitationStatus(String roleDesc , Integer roleType){
        this.roleType = roleType;
        this.roleDesc = roleDesc;
    }
    private Integer roleType;
    private String roleDesc;

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}
