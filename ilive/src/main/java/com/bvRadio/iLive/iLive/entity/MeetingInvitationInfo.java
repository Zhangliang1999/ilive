package com.bvRadio.iLive.iLive.entity;

import com.bvRadio.iLive.iLive.entity.base.BaseMeetingInvitationgInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议邀请函
 */
public class MeetingInvitationInfo extends BaseMeetingInvitationgInfo {
    private List<String> otherImages = new ArrayList<>();
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(List<String> otherImages) {
        this.otherImages = otherImages;
    }
}
