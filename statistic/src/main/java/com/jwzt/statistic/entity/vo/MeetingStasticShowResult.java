package com.jwzt.statistic.entity.vo;

public class MeetingStasticShowResult {
  private Long TotalNum;
  private Long MeetingSumTime;
  private Double MeetingAvgTime;
public Long getTotalNum() {
	return TotalNum;
}
public void setTotalNum(Long totalNum) {
	TotalNum = totalNum;
}
public Long getMeetingSumTime() {
	return MeetingSumTime;
}
public void setMeetingSumTime(Long meetingSumTime) {
	MeetingSumTime = meetingSumTime;
}
public Double getMeetingAvgTime() {
	return MeetingAvgTime;
}
public void setMeetingAvgTime(Double meetingAvgTime) {
	MeetingAvgTime = meetingAvgTime;
}
  
}
