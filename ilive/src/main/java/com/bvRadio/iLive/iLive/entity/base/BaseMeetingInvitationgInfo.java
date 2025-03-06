package com.bvRadio.iLive.iLive.entity.base;

/**
 * 会议邀请函
 */
public abstract class BaseMeetingInvitationgInfo {

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 会议ID
     */
    private Integer roomId;

    /**
     * 会议邀请函LOGO
     */
    private String logo;

    /**
     * 会议邀请函背景图
     */
    private String backImage;

    /**
     * 议程图片（多个之间用英文逗号分割）
     */
    private String processImage;

    /**
     * 议程标识
     * -1：关闭
     * 1：开启
     */
    private Integer processFlag;

    /**
     * 主持人二维码
     */
    private String hostQR;

    /**
     * 参会者二维码
     */
    private String participantQR;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public String getProcessImage() {
        return processImage;
    }

    public void setProcessImage(String processImage) {
        this.processImage = processImage;
    }

    public Integer getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(Integer processFlag) {
        this.processFlag = processFlag;
    }

    public String getHostQR() {
        return hostQR;
    }

    public void setHostQR(String hostQR) {
        this.hostQR = hostQR;
    }

    public String getParticipantQR() {
        return participantQR;
    }

    public void setParticipantQR(String participantQR) {
        this.participantQR = participantQR;
    }
}
