package com.bvRadio.iLive.iLive.action.front.vo.cloud;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias(value="CodecInfo")
public class CodecInfo {

	String VideoWidth;
	String VideoHeight;
	String VideoFPS;
	String VideoBitrateKB;
	String VideoCodec;
	String Profile;
	String videoquality;
	String IsCBR;
	String GOPLen;
	String BFrameNum;
	String QMax;
	String Qmin;
	String AudioSamprate;
	String AudioChannel;
	String AudioBitrateKB;
	String AudioCodec;
	public String getVideoWidth() {
		return VideoWidth;
	}
	public void setVideoWidth(String videoWidth) {
		VideoWidth = videoWidth;
	}
	public String getVideoHeight() {
		return VideoHeight;
	}
	public void setVideoHeight(String videoHeight) {
		VideoHeight = videoHeight;
	}
	public String getVideoFPS() {
		return VideoFPS;
	}
	public void setVideoFPS(String videoFPS) {
		VideoFPS = videoFPS;
	}
	public String getVideoBitrateKB() {
		return VideoBitrateKB;
	}
	public void setVideoBitrateKB(String videoBitrateKB) {
		VideoBitrateKB = videoBitrateKB;
	}
	public String getVideoCodec() {
		return VideoCodec;
	}
	public void setVideoCodec(String videoCodec) {
		VideoCodec = videoCodec;
	}
	public String getProfile() {
		return Profile;
	}
	public void setProfile(String profile) {
		Profile = profile;
	}
	public String getVideoquality() {
		return videoquality;
	}
	public void setVideoquality(String videoquality) {
		this.videoquality = videoquality;
	}
	public String getIsCBR() {
		return IsCBR;
	}
	public void setIsCBR(String isCBR) {
		IsCBR = isCBR;
	}
	public String getGOPLen() {
		return GOPLen;
	}
	public void setGOPLen(String gOPLen) {
		GOPLen = gOPLen;
	}
	public String getBFrameNum() {
		return BFrameNum;
	}
	public void setBFrameNum(String bFrameNum) {
		BFrameNum = bFrameNum;
	}
	public String getQMax() {
		return QMax;
	}
	public void setQMax(String qMax) {
		QMax = qMax;
	}
	public String getQmin() {
		return Qmin;
	}
	public void setQmin(String qmin) {
		Qmin = qmin;
	}
	public String getAudioSamprate() {
		return AudioSamprate;
	}
	public void setAudioSamprate(String audioSamprate) {
		AudioSamprate = audioSamprate;
	}
	public String getAudioChannel() {
		return AudioChannel;
	}
	public void setAudioChannel(String audioChannel) {
		AudioChannel = audioChannel;
	}
	public String getAudioBitrateKB() {
		return AudioBitrateKB;
	}
	public void setAudioBitrateKB(String audioBitrateKB) {
		AudioBitrateKB = audioBitrateKB;
	}
	public String getAudioCodec() {
		return AudioCodec;
	}
	public void setAudioCodec(String audioCodec) {
		AudioCodec = audioCodec;
	}
	
	
	

}
