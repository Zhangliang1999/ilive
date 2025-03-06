package com.jwzt.DB.soms.live.liveProgram;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.jwzt.DB.common.HibernateUtil;
import com.jwzt.DB.common.pk.PKMgr;
import com.jwzt.DB.soms.live.liveChannel.ChannelInfo;
import com.jwzt.DB.soms.live.liveChannel.ChannelMgr;
import com.jwzt.DB.soms.live.proFile.ProFileMgr;
import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.DB.soms.vod.file.FileMgr;
import com.jwzt.common.AppTools;
import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.common.StringUtil;

public class ProgramMgr {
	public static int COUNTER = 0;
	public static String sCurrentDate = "";
	private String message;

	public static void counter() {
		COUNTER++;
		COUNTER = COUNTER % 10000;
	}

	public int addProgramInfo(ProgramInfo programInfo) {
		boolean bFlag = false;
		int program_id = PKMgr.getNextId("soms4_live_program");
		programInfo.setProgram_id(program_id);
		programInfo.setProgram_order(program_id);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			session.save(programInfo);
			ts.commit();
			bFlag = true;
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("添加节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		if (bFlag == false) {
			program_id = -1;
		}
		System.out.println("bFlag:::"+bFlag);
		return program_id;
	}

	public int addBachFileInfo(ArrayList proInfoList) {
		int result = 0;
		int count = proInfoList.size();
		int program_id = PKMgr.getNextId("soms4_live_program", 1, count);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			for (int i = 0; i < proInfoList.size(); i++) {
				ProgramInfo programInfo = (ProgramInfo) proInfoList.get(i);
				programInfo.setProgram_id(program_id);
				programInfo.setProgram_order(program_id);
				session.save(programInfo);
				program_id++;
			}
			ts.commit();
			result = 60;
		} catch (Exception e) {
			ts.rollback();
			Logger.log("添加节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return result;
	}

	/**
	 * 2009-03-12 添加默认的节目列表
	 * 
	 */
	public void insert2ProgramDefaultInfo(String channelId) {
		ProgramInfo programInfo = new ProgramInfo();
		programInfo.setCycle(1);
		ChannelInfo chaninfo = new ChannelMgr().getChannelInfo(Integer.parseInt(channelId));
		programInfo.setChannelInfo(new ChannelMgr().getChannelInfo(Integer.parseInt(channelId)));
		programInfo.setProFileInfo(new ProFileMgr().getProFileInfo(chaninfo.getProfile_id()));
		programInfo.setCycle_ids("1*2*3*4*5*5*7");
		programInfo.setIf_drm("0");
		programInfo.setPlay_type(1);
		programInfo.setProg_desc("");
		programInfo.setSave_plan_id(0);
		programInfo.setProgram_name("全日节目");
		programInfo.setProgram_tvid("-1");
		Date day = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.HOUR_OF_DAY, 0);
		lastDate.set(Calendar.MINUTE, 0);
		lastDate.set(Calendar.SECOND, 0);
		int YY = lastDate.get(Calendar.YEAR);
		lastDate.set(YY, day.getMonth(), day.getDate());
		String str = sdf.format(lastDate.getTime());
		programInfo.setStart_time(Timestamp.valueOf(str));
		programInfo.setTime_long(86399);
		addProgramInfo(programInfo);
	}

	public int addInfo(ProgramInfo programInfo) {
		int program_id = PKMgr.getNextId("soms4_live_program");
		programInfo.setProgram_id(program_id);
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			session.save(programInfo);
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("添加节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return program_id;
	}

	public int editProgramInfo(ProgramInfo programInfo) {
		Session session = HibernateUtil.currentSession();
		int nRet = -1;
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			session.update(programInfo);
			nRet = programInfo.getProgram_id().intValue();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			nRet = -1;
			Logger.log("编辑节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return nRet;
	}

	public boolean deleteProgramInfo(String[] programIds) {
		boolean flag = false;

		String sDelHql = " from ProgramInfo p where p.program_id in (" + StringUtil.stringArray2SqlIn(programIds) + ")";
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery(sDelHql);
			List list = query.list();
			Iterator I = list.iterator();
			while (I.hasNext()) {
				ProgramInfo programInfo = (ProgramInfo) I.next();
				session.delete(programInfo);
			}
			ts.commit();
			flag = true;
		} catch (Exception e) {
			ts.rollback();
			Logger.log("删除节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return flag;
	}

	public boolean deleteProgramInfo(String sDelHql) {
		boolean flag = false;

		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery(sDelHql);
			List list = query.list();
			Iterator I = list.iterator();
			while (I.hasNext()) {
				ProgramInfo programInfo = (ProgramInfo) I.next();
				session.delete(programInfo);
			}
			ts.commit();
			flag = true;
		} catch (Exception e) {
			ts.rollback();
			Logger.log("删除节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return flag;
	}

	public void delProgramInfo(ArrayList sparelist) {

		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();

			for (int i = 0; i < sparelist.size(); i++) {
				ProgramInfo info = (ProgramInfo) sparelist.get(i);
				session.delete(info);
			}
			ts.commit();
		} catch (Exception e) {
			ts.rollback();
			Logger.log("删除节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	public ProgramInfo getProgramInfo(int program_id) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		ProgramInfo programInfo = null;
		try {
			ts = session.beginTransaction();
			programInfo = (ProgramInfo) session.get(ProgramInfo.class, program_id);
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取节目信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return programInfo;
	}

	public List getProgramInfos(String sHqlConds) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try {
			sHqlConds = ("".equals(sHqlConds)) ? sHqlConds : (" and " + sHqlConds);
			ts = session.beginTransaction();
			Query query = session.createQuery("from ProgramInfo p where 1=1 " + sHqlConds);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}
	public List getProgramInfoList(String sql) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List list = new ArrayList();
		try {
			conn = AppTools.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProgramInfo info = new ProgramInfo();
				info.setProgram_id(rs.getInt("program_id"));
				info.setProgram_name(rs.getString("program_name"));
				info.setStart_time(rs.getTimestamp("start_time"));
				info.setEnd_time(rs.getTimestamp("end_time"));
				info.setProg_desc(rs.getString("prog_desc"));
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("getProgramInfoList-->获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return list;
	}
	public List getProgramInfos1(String sHqlConds) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try {
			// sHqlConds = ("".equals(sHqlConds))?sHqlConds:(" and " +
			// sHqlConds);
			ts = session.beginTransaction();
			Query query = session.createQuery("from ProgramInfo p where 1=1 " + sHqlConds);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	// 为了保证oricle的时间比较问题所以直接传时间对象近来
	public List getProgramInfos(String sHqlConds, Timestamp date) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List list = null;
		try {
			sHqlConds = ("".equals(sHqlConds)) ? sHqlConds : (" and " + sHqlConds);
			ts = session.beginTransaction();
			Query query = session.createQuery("from ProgramInfo p where 1=1 " + sHqlConds);
			// query.setDate("newdate",StringUtil.string2SqlDate(date));
			query.setTimestamp("newdate", date);

			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 调整某天某栏目下(以某节目为基准开始的)节目顺序
	 * 
	 * @param sNowDate
	 *            格式为: yy-mm-dd 的日期字符串.
	 * @param nChannelId
	 *            频道ID
	 * @param nProgramId
	 *            节目ID: -1: 表示全部节目.
	 * @return
	 */
	public String setAllProOrder(String sNowDate, int nChannelId, int nProgramId) {
		String sRet = "failure";

		ProgramInfo thisProgramInfo = null;
		ProgramInfo nextProgramInfo = null;
		int nTotalTime = 0; // 总时间
		int nTotalStartTime = 0; // 总开始时间
		int nTotalEndTime = 0; // 总结束时间
		int n24HourTime = StringUtil.getTime2Count("24:00:00", 2);

		ArrayList fitProList = getProListInDay(sNowDate, nChannelId, nProgramId, true);
		if (fitProList != null && fitProList.size() > 1) {
			boolean ifAllowEdit = true;
			/** * 列表中至少有两个节目时,才需要调整. * */
			for (int i = 0; i < fitProList.size(); i++) {
				thisProgramInfo = (ProgramInfo) fitProList.get(i);
				nextProgramInfo = (i < (fitProList.size() - 1)) ? (ProgramInfo) fitProList.get(i + 1) : null;

				if (nextProgramInfo != null) {
					// 本节目的开始和结束时间
					String sThisProStartTime = thisProgramInfo.getStart_time().getHours() + ":" + thisProgramInfo.getStart_time().getMinutes() + ":" + thisProgramInfo.getStart_time().getSeconds();

					int nThisProStartTime = StringUtil.getTime2Count(sThisProStartTime, 2);
					int nThisProEndTime = nThisProStartTime + thisProgramInfo.getTime_long();

					// 下一节目的开始时间
					String sNextProStartTime = nextProgramInfo.getStart_time().getHours() + ":" + nextProgramInfo.getStart_time().getMinutes() + ":" + nextProgramInfo.getStart_time().getSeconds();
					int nNextProStartTime = StringUtil.getTime2Count(sNextProStartTime, 2);

					if (nThisProEndTime > nNextProStartTime) {
						/* 本节目的结束时间大于下一节目的开始时间 */

						if (thisProgramInfo.getCycle() == 1 && nextProgramInfo.getCycle() == 0) {
							// 本节目为周期的节目
							Timestamp tNextProNewStartDateTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nThisProEndTime, 2));
							nextProgramInfo.setStart_time(tNextProNewStartDateTime);
						} else if (thisProgramInfo.getCycle() == 0 && nextProgramInfo.getCycle() == 1) {
							// 下一节目为周期节目
							int nTimeLong = nThisProEndTime - nNextProStartTime;
							int nThisProNewTimeLong = thisProgramInfo.getTime_long() - nTimeLong;
							thisProgramInfo.setTime_long((nThisProNewTimeLong > 0) ? nThisProNewTimeLong : 0);
						} else if (thisProgramInfo.getCycle() == 1 && nextProgramInfo.getCycle() == 1) {
							sRet = "周期节目时间重叠";
							ifAllowEdit = false;
						} else {
							// 本节目和下一节目都为无周期节目
							Timestamp tNextProNewStartDateTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nThisProEndTime, 2));
							nextProgramInfo.setStart_time(tNextProNewStartDateTime);
						}
					}

					if (nThisProEndTime < nNextProStartTime) {
						/* 本节目的结束时间小于下一节目的开始时间 */

						if (thisProgramInfo.getCycle() == 1 && nextProgramInfo.getCycle() == 0) {
							// 本节目为周期的节目
							Timestamp tNextProNewStartDateTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nThisProEndTime, 2));
							nextProgramInfo.setStart_time(tNextProNewStartDateTime);
						} else if (thisProgramInfo.getCycle() == 0 && nextProgramInfo.getCycle() == 1) {
							// 下一节目为周期节目
							// 不处理.
						} else if (thisProgramInfo.getCycle() == 1 && nextProgramInfo.getCycle() == 1) {
							// 本节目和下一节目都为周期节目
							// 不处理.
						} else {
							// 本节目和下一节目都为无周期节目
							Timestamp tNextProNewStartDateTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nThisProEndTime, 2));
							nextProgramInfo.setStart_time(tNextProNewStartDateTime);
						}
					}

					nTotalStartTime = (i == 0) ? nThisProStartTime : nTotalStartTime;
					nTotalEndTime = (i == (fitProList.size() - 2)) ? (nNextProStartTime + nextProgramInfo.getTime_long()) : nTotalEndTime;
					nTotalTime = (i == (fitProList.size() - 2)) ? (nTotalEndTime - nTotalStartTime) : nTotalTime;
					if ((nProgramId < 0) && (nTotalTime > StringUtil.getTime2Count("24:00:00", 2))) {
						sRet = "节目时长大于24小时";
						break;
					}

					if (ifAllowEdit) {
						int nProId1 = editProgramInfo(thisProgramInfo);
						int nProId2 = editProgramInfo(nextProgramInfo);
						sRet = (nProId1 > 0 && nProId2 > 0) ? "success" : sRet;
					}
				}
			}
		}
		return sRet;
	}

	/**
	 * 设置某天某栏目下(以某节目为基准开始的)节目开始时间
	 * 
	 * @param sNowDate
	 *            格式为: yy-mm-dd 的日期字符串.
	 * @param nChannelId
	 *            频道ID
	 * @param nProgramId
	 *            节目ID: -1: 表示全部节目.
	 * @param nStartTime
	 *            开始时间(以秒为单位的整数型)
	 * @return
	 */
	public boolean setStartTimeForNoCycle(String sNowDate, int nChannelId, int nProgramId, int nStartTime) {
		boolean bRet = false;

		int n24HourTime = StringUtil.getTime2Count("23:59:59", 2);

		if (nStartTime < n24HourTime) {
			ArrayList fitProList = getProListInDay(sNowDate, nChannelId, nProgramId, false);
			if (fitProList != null && fitProList.size() > 0) {
				Timestamp tThisStartTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nStartTime, 2));
				ProgramInfo programInfo = new ProgramInfo();

				for (int i = 0; i < fitProList.size(); i++) {
					programInfo = (ProgramInfo) fitProList.get(i);
					programInfo.setStart_time(tThisStartTime);

					long start = tThisStartTime.getTime();// 得到开始时间毫秒数
					long milliendtime = start + programInfo.getTime_long() * 1000;
					Timestamp endtime = new Timestamp(milliendtime);
					programInfo.setEnd_time(endtime);// 修改节目结束时间nzx

					nStartTime = (nStartTime > n24HourTime) ? n24HourTime : (nStartTime + programInfo.getTime_long());
					tThisStartTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nStartTime, 2));
					if (nStartTime < n24HourTime) {
						editProgramInfo(programInfo);
					}
				}
				bRet = true;
			}
		}
		return bRet;
	}

	/**
	 * 按开始和结束时间设置(以某节目为基准开始的)节目编排时间 (适用于虚拟直播)
	 * 
	 * @param startDate
	 *            节目开始日期yyyy-mm-dd格式
	 * @param endDate
	 *            节目结束日期yyyy-mm-dd格式
	 * @param nChannelId
	 *            频道ID
	 * @param nProgramId
	 *            节目ID
	 * @param startSeconds
	 *            所选当天开始时间秒数(不跨天,相对于当天)
	 * @param endSeconds
	 *            所选当天结束时间秒数(不跨天,相对于当天)
	 * @param allSeconds
	 *            所选开始时间到结束时间的总秒数(跨天)
	 * @param startDateTime
	 *            节目开始时间yyyy-mm-dd hh:mm:ss格式
	 * @return
	 */
	public boolean setTimeForNoCycle(String startDate, String endDate, int nChannelId, int nProgramId, int startSeconds, int endSeconds, long allSeconds, String startDateTime) {
		boolean bRet = false;

		int n24HourTime = StringUtil.getTime2Count("23:59:59", 2);// 结束时间秒数(相对于当天)

		if (allSeconds > 0) {
			ArrayList fitProList = getProListInDay(startDate, nChannelId, nProgramId, false);// 所选范围之内的节目列表
			long seconds = 0;
			for (int i = 0; i < fitProList.size(); i++) {
				ProgramInfo info = (ProgramInfo) fitProList.get(i);
				seconds = seconds + info.getTime_long(); // 获取所有的节目的总时长(秒)
			}

			Timestamp tThisStartTime = StringUtil.string2SqlTimestamp(startDateTime);

			long times = 0; // 所有节目根据开始和结束时间要循环播放的次数
			if (allSeconds == seconds) {
				times = 1;
			} else {
				times = (allSeconds / seconds) + 1;
			}

			for (int k = 0; k < times; k++) {
				if (fitProList != null && fitProList.size() > 0) {

					ProgramInfo programInfo = new ProgramInfo();

					if (k == 0) // 第一次循环(第一次循环只是更改节目的开始时间和时长)
					{
						for (int i = 0; i < fitProList.size(); i++) {
							programInfo = (ProgramInfo) fitProList.get(i);

							int overTime = startSeconds + programInfo.getTime_long();// 节目的结束时间(秒)

							if (startSeconds < n24HourTime && overTime < n24HourTime)// (不跨天)开始时间和结束时间都小于当天最晚时间
							{
								if (startDate.equals(endDate))// 执行到最后一次循环时要检查设置的结束时间(节目编排到最后一天)
								{
									if (startSeconds < endSeconds && overTime < endSeconds)// 开始和结束时间都小于设置的结束时间
									{
										programInfo.setStart_time(tThisStartTime);
										editProgramInfo(programInfo);
										startSeconds = overTime;
										if (startSeconds < n24HourTime) {
											tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
										}
									}
									if (startSeconds < endSeconds && overTime > endSeconds)// 节目编排到最后一天时节目的结束时间大于设置的结束时间
									{
										programInfo.setStart_time(tThisStartTime);
										programInfo.setTime_long(endSeconds - startSeconds);// 截取时长(只播出到这里为止)
										editProgramInfo(programInfo);

										ArrayList sparelist = new ArrayList();
										int spareCount = i + 1;
										for (int n = spareCount; n < fitProList.size(); n++) {
											programInfo = (ProgramInfo) fitProList.get(n);
											sparelist.add(programInfo);// 删除所有之后的节目
										}

										this.delProgramInfo(sparelist);
										break;
									}
								} else // 节目没有编排到最后一天
								{
									programInfo.setStart_time(tThisStartTime);
									editProgramInfo(programInfo);
									startSeconds = overTime;
									if (startSeconds < n24HourTime) {
										tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
									}
								}
							}
							if (startSeconds < n24HourTime && overTime > n24HourTime)// 结束时间大于当天最晚时间(需要跨天)这时不但要修改一条记录,还要添加一条第二天的记录
							{
								int timeLong = programInfo.getTime_long();

								programInfo.setStart_time(tThisStartTime);
								programInfo.setTime_long(n24HourTime - startSeconds);// 截取时长
								editProgramInfo(programInfo);

								programInfo.setTime_long(timeLong);// 把节目的总时长再次赋给该节目

								if (startDate.equals(endDate))// 编排到最后一天时只修改当天的记录,不再添加第二天的记录
								{
									break;
								}

								int spareSeconds = overTime - n24HourTime;
								ProgramInfo sInfo = new ProgramInfo();

								sInfo.setTime_long(spareSeconds);

								Calendar tempDateTime = StringUtil.getStringAsCalendar(startDateTime, 0);
								tempDateTime.add(Calendar.DAY_OF_MONTH, 1);
								startDate = StringUtil.getCalendarAsString(tempDateTime, 0);
								sInfo.setStart_time(StringUtil.string2SqlTimestamp(startDate + " 00:00:00"));

								this.setProgramInfo(sInfo, programInfo);
								this.addInfo(sInfo);

								startDateTime = startDate + " 00:00:00";

								startSeconds = 0 + spareSeconds;
								tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
							}
						}
					} else // 之后的循环要添加相同的节目,添加时修改开始时间和时长
					{
						for (int i = 0; i < fitProList.size(); i++) {
							programInfo = (ProgramInfo) fitProList.get(i);

							int overTime = startSeconds + programInfo.getTime_long();// 获取节目的结束时间
							if (startSeconds < n24HourTime && overTime < n24HourTime)// 不跨天,开始和结束时间都小于当天最晚时间
							{
								if (startDate.equals(endDate))// 执行到最后一次循环时要检查设置的结束时间(节目编排到最后一天)
								{
									if (startSeconds < endSeconds && overTime < endSeconds)// 开始和结束时间都小于设置的结束时间
									{
										ProgramInfo sInfo = new ProgramInfo();

										sInfo.setTime_long(programInfo.getTime_long());
										sInfo.setStart_time(tThisStartTime);

										this.setProgramInfo(sInfo, programInfo);
										this.addInfo(sInfo);

										startSeconds = overTime;
										if (startSeconds < n24HourTime) {
											tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
										}
									}
									if (startSeconds < endSeconds && overTime > endSeconds)// 节目编排到最后一天时节目的结束时间大于设置的结束时间
									{
										ProgramInfo sInfo = new ProgramInfo();

										sInfo.setTime_long(endSeconds - startSeconds);// 截取时长
										sInfo.setStart_time(tThisStartTime);

										this.setProgramInfo(sInfo, programInfo);
										this.addInfo(sInfo);
									}
								} else // 节目没有编排到最后一天
								{
									ProgramInfo sInfo = new ProgramInfo();

									sInfo.setTime_long(programInfo.getTime_long());
									sInfo.setStart_time(tThisStartTime);

									this.setProgramInfo(sInfo, programInfo);
									this.addInfo(sInfo);

									startSeconds = overTime;
									if (startSeconds < n24HourTime) {
										tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
									}
								}
							}
							if (startSeconds < n24HourTime && overTime > n24HourTime)// 需要跨天,结束时间大于当天最晚时间,这时需要添加两条记录
							{
								ProgramInfo sInfo = new ProgramInfo();

								sInfo.setTime_long(n24HourTime - startSeconds);
								sInfo.setStart_time(tThisStartTime);

								this.setProgramInfo(sInfo, programInfo);
								this.addInfo(sInfo);

								if (startDate.equals(endDate))// 编排到最后一天时只添加当天的记录,不再添加第二天的记录
								{
									break;
								}

								int spareSeconds = overTime - n24HourTime;
								sInfo = new ProgramInfo();

								sInfo.setTime_long(spareSeconds);
								Calendar tempDateTime = StringUtil.getStringAsCalendar(startDateTime, 0);
								tempDateTime.add(Calendar.DAY_OF_MONTH, 1);
								startDate = StringUtil.getCalendarAsString(tempDateTime, 0);
								sInfo.setStart_time(StringUtil.string2SqlTimestamp(startDate + " 00:00:00"));

								this.setProgramInfo(sInfo, programInfo);
								this.addInfo(sInfo);

								startDateTime = startDate + " 00:00:00";

								startSeconds = 0 + spareSeconds;
								tThisStartTime = StringUtil.string2SqlTimestamp(startDate + " " + StringUtil.getCount2Time2(startSeconds, 2));
							}
						}
					}

					bRet = true;
				}
			}
		}
		return bRet;
	}

	/**
	 * 设置某周期节目的开始时间.
	 * 
	 * @param sNowDate
	 *            格式为: yy-mm-dd 的日期字符串
	 * @param nProgramId
	 * @param nStartTime
	 * @return
	 */
	public boolean setStartTimeForCycle(int nProgramId, int nStartTime) {
		ProgramInfo programInfo = getProgramInfo(nProgramId);

		String sNowDate = StringUtil.getCurentDateAsString();
		Timestamp tThisStartTime = StringUtil.string2SqlTimestamp(sNowDate + " " + StringUtil.getCount2Time2(nStartTime, 2));
		programInfo.setStart_time(tThisStartTime);

		long start = tThisStartTime.getTime();// 得到开始时间毫秒数
		long milliendtime = start + programInfo.getTime_long() * 1000;
		Timestamp endtime = new Timestamp(milliendtime);
		programInfo.setEnd_time(endtime);// 修改节目结束时间nzx
		int nRet = editProgramInfo(programInfo);

		return (nRet > 0) ? true : false;
	}

	/**
	 * 获取某天某栏目下(以某节目为基准开始的)节目列表
	 * 
	 * @param sNowDate
	 *            格式为: yy-mm-dd 的日期字符串(如果为"", 则取当天日期).
	 * @param nChannelId
	 *            频道ID
	 * @param nProgramId
	 *            节目ID: -1: 表示全部节目.
	 * @param bCycleType
	 *            周期类型: true: 有周期和无周期节目; false: 无周期节目;
	 * @return
	 */
	public ArrayList getProListInDay(String sNowDate, int nChannelId, int nProgramId, boolean bCycleType) {
		// 获取星期
		Calendar calendar = Calendar.getInstance();
		String nowDateStr = "";
		if (!("".equals(sNowDate))) {
			String[] sNowDate2 = StringUtil.splitString2Array(sNowDate, "-");
			if (sNowDate2[1].indexOf("0") != -1 && Integer.parseInt(sNowDate2[1]) < 10) {
				sNowDate2[1] = sNowDate2[1].replace("0", "");
			}
			if (sNowDate2[2].length() > 2 && Integer.parseInt(sNowDate2[2]) < 10) {
				sNowDate2[2] = sNowDate2[2].replace("0", "");
			}
			calendar.set(Integer.parseInt(sNowDate2[0]), Integer.parseInt(sNowDate2[1]) - 1, Integer.parseInt(sNowDate2[2]));
			nowDateStr = sNowDate2[0] + "-" + (Integer.parseInt(sNowDate2[1]) > 10 ? sNowDate2[1] : "0" + sNowDate2[1]) + "-"
					+ (Integer.parseInt(sNowDate2[2]) > 10 ? sNowDate2[2] : "0" + sNowDate2[2]);
		} else {
			// sNowDate =
			// calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH);
			sNowDate = StringUtil.getCalendarAsString(calendar, 0);
			nowDateStr = sNowDate;
		}
		String sDayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK) == 1) ? 7 + "" : (calendar.get(Calendar.DAY_OF_WEEK) - 1) + ""; // 当前星期.

		String sAddHql = "";
		String sStrat_time = "";
		Timestamp tStrat_time = null;
		if (nProgramId > 0) {
			ProgramInfo tempInfo = getProgramInfo(nProgramId);
			sStrat_time = StringUtil.sqlTimestamp2String(tempInfo.getStart_time());
			if (SomsConfigInfo.get("database").toUpperCase().trim().equals("ORACLE")) {
				sAddHql = " and p.start_time>=:newdate";
			} else {
				sAddHql = " and p.start_time>='" + tempInfo.getStart_time() + "'";
			}

			tStrat_time = tempInfo.getStart_time();
		}

		String sHql = "";
		if ("SQL SERVER".equals((SomsConfigInfo.get("database")).toUpperCase().trim())) {
			sHql = " p.channelInfo.channel_id=" + nChannelId + sAddHql + " order by convert(nvarchar, p.start_time, 8)";
		} else {
			sHql = " p.channelInfo.channel_id=" + nChannelId + sAddHql + " order by to_char(p.start_time,'hh24:mi:ss')";
		}

		List list = null;
		if (nProgramId > 0 && SomsConfigInfo.get("database").toUpperCase().trim().equals("ORACLE")) {
			list = getProgramInfos(sHql, tStrat_time);
		} else {
			list = getProgramInfos(sHql);
		}
		ArrayList fitProList = new ArrayList();
		if(null != list && list.size()>0){
			Iterator I = list.iterator();
			while (I.hasNext()) {
				ProgramInfo info = (ProgramInfo) I.next();
				if (info.getPlay_type().intValue() == 3)
			      {
			        info = getNodeProgramInfo(info);
			      }
				String sProStartDate = info.getStart_time().toString().substring(0, 10);
				String[] dbTime = null;
				String dbTimeStr = "";
				if (sProStartDate != null && !"".equals(sProStartDate)) {
					dbTime = sProStartDate.trim().split("-");
					String tempMonth = "";
					String tmpDay = "";
					if (dbTime[1].indexOf("0") != -1) {
						tempMonth = dbTime[1];
					} else {
						tempMonth = (Integer.parseInt(dbTime[1]) > 10 ? dbTime[1] : "0" + dbTime[1]);
					}
					if (dbTime[2].indexOf("0") != -1) {
						tmpDay = dbTime[2];
					} else {
						tmpDay = (Integer.parseInt(dbTime[2]) > 10 ? dbTime[2] : "0" + dbTime[2]);
					}
					dbTimeStr = dbTime[0] + "-" + tempMonth + "-" + tmpDay;
				}
				if (info.getCycle() == 0 && nowDateStr.equals(dbTimeStr)) {
					// 无周期并且当天的节目列表
					fitProList.add(info);
				} else if (bCycleType && (info.getCycle() == 1) && (info.getCycle_ids().indexOf(sDayOfWeek) > -1)) {
					// 有周期并且在当天的节目列表.
					fitProList.add(info);
				}
			}
		}
		
		return fitProList;
	}

	public boolean ifProgramSave(String[] sSavePlanIds) {
		String sHql = " p.save_plan_id in (" + StringUtil.stringArray2SqlIn(sSavePlanIds) + ")";

		List list = getProgramInfos(sHql);
		Iterator I = list.iterator();
		int nRet = -1;
		while (I.hasNext()) {
			ProgramInfo info = (ProgramInfo) I.next();
			info.setSave_plan_id(0);
			nRet = editProgramInfo(info);
		}

		return (nRet > 0) ? true : false;
	}

	/**
	 * 移动(无周期)节目的位置
	 * 
	 * @param nProId
	 *            节目ID
	 * @param nChannelId
	 *            栏目ID
	 * @param sNowDate
	 *            节目日期
	 * @param nMoveType
	 *            移动类型: 0: 上移; 1: 下移
	 * @return
	 */
	public boolean movePosition(int nProId, int nChannelId, String sNowDate, int nMoveType) {
		boolean flag = false;

		List pros = getProListInDay(sNowDate, nChannelId, -1, false);

		Timestamp tempTimeStamp = null;
		ProgramInfo programInfo = new ProgramInfo();
		ProgramInfo programInfo2 = new ProgramInfo();
		if (pros != null && pros.size() > 0) {
			for (int i = 0; i < pros.size(); i++) {
				programInfo = (ProgramInfo) pros.get(i);
				programInfo2 = (ProgramInfo) pros.get(i);
				if (programInfo.getProgram_id() == nProId) {
					tempTimeStamp = programInfo.getStart_time();

					if (nMoveType == 0 && i > 0) {
						// 上移
						programInfo2 = (ProgramInfo) pros.get(i - 1);
					} else if (nMoveType == 1 && (i < (pros.size() - 1))) {
						// 下移
						programInfo2 = (ProgramInfo) pros.get(i + 1);
					}

					programInfo.setStart_time(programInfo2.getStart_time());
					programInfo2.setStart_time(tempTimeStamp);
					editProgramInfo(programInfo);
					editProgramInfo(programInfo2);
					flag = true;
				}
			}
		}

		return true;
	}

	public void setProgramInfo(ProgramInfo sInfo, ProgramInfo programInfo) {
		sInfo.setProgram_name(programInfo.getProgram_name());
		sInfo.setChannelInfo(programInfo.getChannelInfo());
		sInfo.setProgram_order(programInfo.getProgram_order());
		sInfo.setIf_drm(programInfo.getIf_drm());
		sInfo.setProFileInfo(programInfo.getProFileInfo());
		sInfo.setPlay_type(programInfo.getPlay_type());
		sInfo.setFileInfo(programInfo.getFileInfo());
		sInfo.setSave_plan_id(programInfo.getSave_plan_id());
		sInfo.setCycle(programInfo.getCycle());
		sInfo.setCycle_ids(programInfo.getCycle_ids());
		sInfo.setProg_desc(programInfo.getProg_desc());
	}

	public void writeProgramToTxt(String proSql, String startDate, String channel_name) {
		String path = SomsConfigInfo.get("if_program_file");

		// path = path + channel_name + ".txt";
		PrintWriter writer = null;
		File file = null;
		File dir = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			StringBuffer proContent = new StringBuffer();

			Session session = HibernateUtil.currentSession();
			conn = session.connection();
			stmt = conn.prepareStatement(proSql);
			rs = stmt.executeQuery();
			int count = 0;
			while (rs.next()) {
				String startdate = rs.getString(2).substring(0, rs.getString(2).indexOf(" "));
				String starttimes = rs.getString(2).substring(0, 19);
				Calendar calendar = StringUtil.getStringAsCalendar(starttimes, 2);

				int n = calendar.get(Calendar.DAY_OF_WEEK) - 1;

				if (n == 0) {
					n = 7;
				}

				calendar.add(Calendar.SECOND, rs.getInt(3));
				String endtimes = StringUtil.getCalendarAsString(calendar, 2);

				if (startdate.equals(startDate)) {
					if (count == 0) {
						proContent.append(startDate + "   星期" + n).append("\r\n");
						count++;
					}
				} else {
					Calendar cdate = StringUtil.getStringAsCalendar(startDate + " 00:00:00", 0);
					cdate.add(Calendar.DAY_OF_MONTH, 1);
					startDate = StringUtil.getCalendarAsString(cdate, 0);
					proContent.append("\r\n").append(startDate + "   星期" + n).append("\r\n");
					count = 1;
				}

				proContent.append(rs.getString(1)).append("\t\t\t").append(starttimes).append("\t\t\t").append(endtimes).append("\r\n");
			}
			// System.out.println(path);

			dir = new File(path);
			dir.mkdirs();

			file = new File(path + channel_name + ".txt");
			file.createNewFile();

			// writer = new PrintWriter(new OutputStreamWriter(new
			// FileOutputStream(path),"GBK"));
			writer = new PrintWriter(new FileWriter(file), true);
			// writer.println(proContent.toString());
			writer.write(proContent.toString());
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
			}

			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
	}

	public ArrayList getProgramListWithSql(String sSql) {
		ArrayList list = null;

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			Session session = HibernateUtil.currentSession();
			conn = session.connection();
			stmt = conn.prepareStatement(sSql);
			rs = stmt.executeQuery();
			if (rs != null) {
				list = new ArrayList();
				while (rs.next()) {
					String program_name = rs.getString("program_name");
					String start_time = rs.getString("start_time");
					String time_long = rs.getString("time_long");
					String all_info = program_name + "#" + start_time + "#" + time_long;

					list.add(all_info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}

		return list;
	}

	public void writeProgramToTxt2(String proSql, String startDate, String endDate, String channel_name) {
		String path = SomsConfigInfo.get("if_program_file");

		path = path + channel_name + "\\";
		PrintWriter writer = null;
		File file = null;
		File dir = null;

		try {
			dir = new File(path);
			dir.mkdirs();

			List list = getProgramListWithSql(proSql);

			Calendar startCalendar = StringUtil.getStringAsCalendar(startDate, 0);
			Calendar endCalendar = StringUtil.getStringAsCalendar(endDate, 0);

			while (!startCalendar.after(endCalendar)) {
				StringBuffer proContent = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					String all_info = (String) list.get(i);
					String[] arr_all_info = StringUtil.splitString2Array(all_info, "#");
					String startdate = arr_all_info[1].substring(0, arr_all_info[1].indexOf(" "));
					String starttimes = arr_all_info[1].substring(0, 19);

					Calendar calendar = StringUtil.getStringAsCalendar(starttimes, 2);
					calendar.add(Calendar.SECOND, Integer.parseInt(arr_all_info[2]));
					String endtimes = StringUtil.getCalendarAsString(calendar, 2);

					String startCompare = StringUtil.getCalendarAsString(startCalendar, 0);

					if (startCompare.equals(startdate)) {
						proContent.append(arr_all_info[0]).append("  ").append(starttimes).append("\r\n");
						// .append("\t\t\t").append(endtimes).append("\r\n");
					}
				}

				int day_of_week = startCalendar.get(Calendar.DAY_OF_WEEK) - 1;
				day_of_week = (day_of_week == 0) ? 7 : day_of_week;

				file = new File(path + day_of_week + ".txt");
				file.createNewFile();
				writer = new PrintWriter(new FileWriter(file), true);
				writer.write(proContent.toString());
				writer.flush();

				startCalendar.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
			}
		}
	}


	
	
	protected boolean checkYzq2Yzq(ProgramInfo info1, ProgramInfo info2, Integer day1, Integer day2) {
		Timestamp ss = info1.getStart_time();
		Timestamp se = info1.getEnd_time();
		Timestamp ts = info2.getStart_time();
		Timestamp te = info2.getEnd_time();
		// 计算开始时间和结束时间相差多少天
		int sInterval = Math.abs((int) ((se.getTime() - ss.getTime()) / (24 * 60 * 60 * 1000)));
		if (sInterval == 0 && ss.getDate() != se.getDate()) {
			sInterval = 1;
		}
		int tInterval = Math.abs((int) ((te.getTime() - ts.getTime()) / (24 * 60 * 60 * 1000)));
		if (tInterval == 0 && ts.getDate() != te.getDate()) {
			tInterval = 1;
		}
		ss = initTimestamp(ss, 0);
		se = initTimestamp(se, sInterval);
		ts = initTimestamp(ts, 0);
		te = initTimestamp(te, tInterval);
		if (checkTimeWrap(ss, se, ts, te)) {
			return true;
		} else {
			this.message = info1.getProgram_name() + "(" + info1.getProgram_id() + ")与" + info2.getProgram_name() + "(" + info2.getProgram_id() + ")发生时间重叠";
			return false;
		}
	}

	protected Timestamp initTimestamp(Timestamp ts, int interval) {
		Timestamp afterinit = ts;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date tempDate;
		try {
			tempDate = sf.parse("1900-01-01 00:00:00");
		} catch (Exception ex) {
			ex.printStackTrace();
			tempDate = new Date(0);
		}
		tempDate.setTime(tempDate.getTime() + interval * 24 * 60 * 60 * 1000);
		afterinit.setYear(tempDate.getYear());
		afterinit.setMonth(tempDate.getMonth());
		afterinit.setDate(tempDate.getDate());
		return afterinit;
	}

	protected boolean checkInfo(ProgramInfo info1, ProgramInfo info2, Integer day1, Integer day2) {
		Timestamp info1s = info1.getStart_time();
		Timestamp info2s = info1.getStart_time();
		if (info1.getCycle() == 1 && info2.getCycle() == 1) {

			// 如果时间不存在重叠
			if (this.checkYzq2Yzq(info1, info2, day1, day2)) {
				return true;
			}
			// 如果时间存在重叠，开始判断周期是否重叠
			int a = 0; // 是否重叠标志，如果重叠则设为100；
			String weeks = "";// 星期标志
			String[] cycleIds = info1.getCycle_ids().split("\\*");// *号比较特殊

			String tCycleids = info2.getCycle_ids();
			for (int i = 0; i < cycleIds.length; i++) {

				if ((!cycleIds[i].equals("")) && tCycleids.indexOf(cycleIds[i]) != -1) {
					// 具有相同周期，确认存在重叠,形如：*5*4*3字符串，用split("\\*")之后取得的第一个为空
					a = 100;
					weeks += (new String[] { "周一", "周二", "周三", "周四", "周五", "周六", "周日" })[Integer.parseInt(cycleIds[i]) - 1] + "、";
				}

			}
			if (a == 100) {
				this.message = info1.getProgram_name() + "(" + info1.getProgram_id() + ")与" + info2.getProgram_name() + "(" + info2.getProgram_id() + ")已在" + weeks + "发生时间重叠";
				return false;
			}
			return true;
		} else if (info1.getCycle() == 0 && info2.getCycle() == 0) {

			if (!checkTimeWrap(info1.getStart_time(), info1.getEnd_time(), info2.getStart_time(), info2.getEnd_time())) {
				this.message = info1.getProgram_name() + "(" + info1.getProgram_id() + ")与" + info2.getProgram_name() + "(" + info2.getProgram_id() + ")发生时间重叠";
				return false;
			} else {
				return true;
			}
		} else {
			if (info1.getCycle() == 0 && info2.getCycle() == 1) {// 没错

				if (checkYzq2Yzq(info1, info2, day1, day2)) {
					return true; // 时间不重叠
				} else {
					// 时间重叠再验证周期是否重叠
					String sDay;
					if (day1 == 0)
						sDay = "7";
					else {
						sDay = day1.toString();
					}
					// System.out.println("01转换前：day="+day1+"转换后day="+sDay);
					if (info2.getCycle_ids().indexOf(sDay) != -1) {
						this.message = info1.getProgram_name() + "(" + info1.getProgram_id() + ")与" + info2.getProgram_name() + "(" + info2.getProgram_id() + ")发生时间重叠";
						return false;
					} else {
						return true;
					}

				}

			} else if (info1.getCycle() == 1 && info2.getCycle() == 0) {

				if (checkYzq2Yzq(info1, info2, day1, day2)) {
					return true;// 时间不重叠
				} else {
					// 时间不重叠再验证周期是否重叠

					String sDay;
					if (day2 == 0)
						sDay = "7";
					else {
						sDay = day2.toString();
					}
					// System.out.println("10转换前：day="+day2+"转换后day="+sDay);
					if (info1.getCycle_ids().indexOf(sDay) != -1) {
						this.message = info1.getProgram_name() + "(" + info1.getProgram_id() + ")与" + info2.getProgram_name() + "(" + info2.getProgram_id() + ")发生时间重叠";
						return false;
					} else {
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean checkTimeWrap(Timestamp ss, Timestamp se, Timestamp ts, Timestamp te) {

		if (!ss.before(ts) && ss.before(te))
			return false;
		if (!ts.before(ss) && ts.before(se))
			return false;
		if (se.after(ts) && !se.after(te))
			return false;
		if (te.after(ss) && !te.after(se))
			return false;
		return true;
	}

	/**
	 * 获取日期
	 * 
	 * @param temDate
	 *            当前时间
	 * @return 日期
	 */
	public Date change2Date(String temDate) {
		Date d = null;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d = sf.parse(temDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	protected List<ProgramInfo> commitQuery(Map timeMap, String sHqlConds) {
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		List<ProgramInfo> list = new ArrayList<ProgramInfo>();
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery("from ProgramInfo s where " + sHqlConds);
			Iterator it = timeMap.keySet().iterator();
			while (it.hasNext()) {
				String sTime = it.next().toString();
				query.setDate(sTime, (Date) timeMap.get(sTime));
			}
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	/**
	 * 
	 * @param channelId
	 * @param pageCount
	 * @param pageNum
	 * @param curdate
	 * @return 有周期的节目
	 */
	public List<ProgramInfo> getProgramListByChanelId3(int channelId, String curdate) {
		List<ProgramInfo> list = new ArrayList<ProgramInfo>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String database = SomsConfigInfo.get("database");
		System.out.println("database=="+database);
		try {
			String sql="";
			if(database.equals("SQL Server")){
				 sql = "SET DATEFIRST 1 select * from soms4_live_program where channel_id=" + channelId + "  and ((datediff(dd, start_time, '" + curdate
						+ "')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '" + curdate + "') as nvarchar), cycle_ids)>0)) order by datepart(hh,start_time) ,datepart(mi,start_time)";
			}else{
				//Oracle兼容
				sql=" select * from soms4_live_program where channel_id="+channelId+"  and ((to_date(START_TIME, 'yyyy-mm-dd') - to_date('"+curdate+"', 'yyyy-mm-dd')=0 and cycle=0)"+
							" or (cycle=1 and instr(cycle_ids,to_char(to_date('"+curdate+"', 'yyyy-mm-dd'),'D'),1,1)>0))"+
							" order by to_char(start_time,'hh24') ,to_char(start_time,'mi')";
			}
			
			conn = AppTools.getConnection();
			System.out.println("#################getProgramListByChanelId3################"+sql);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProgramInfo info = new ProgramInfo();
				info.setProgram_id(rs.getInt("program_id"));
				info.setProgram_name(rs.getString("program_name"));
				Timestamp start_time = rs.getTimestamp("start_time");
				info.setStart_time(start_time);
				long startTimeLong = start_time.getTime();
				long endTimeLong = startTimeLong+rs.getInt("time_long")*1000;
				info.setEnd_time(new Timestamp(endTimeLong));
				info.setTime_long(rs.getInt("time_long"));
				info.setProg_desc(rs.getString("prog_desc"));
				info.setCycle(rs.getInt("cycle"));
				info.setCycle_ids(rs.getString("cycle_ids"));
				list.add(info);
			}
		} catch (Exception e) {
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		List<Integer> list2 = getProgramIdListInvalidByChannelIdAndDay(channelId+"",curdate);
		list = setProgramListInvalidOrder(list, list2);
		list = removeInvalidProgram(list);
		return list;
	}

	private List<ProgramInfo> removeInvalidProgram(List<ProgramInfo> list) {
		List<ProgramInfo> newList = new ArrayList<ProgramInfo>();
		for(ProgramInfo info: list){
			if(info.getInvalid()==0){
				newList.add(info);
			}
		}
		return newList;
	}

	

	/**
	 * 通过program_id找到fileid
	 * 
	 * @param program_id
	 * @return fileid
	 */
	public int findFileIdByProgramId(String programId) {
		int fileId = -1;
		List<FileInfo> list = null;
		FileInfo ff = null;
		String hql = "from FileInfo where guid_9=? and parent_file_id = file_id ";
		Session session = HibernateUtil.currentSession();
		try {
			Query query = session.createQuery(hql);
			query.setString(0, programId);
			//list = query.list();
			ff = (FileInfo) query.uniqueResult();
			fileId = ff.getFile_id();
		} catch (Exception e) {
			fileId = -1;
			e.printStackTrace();
		} finally {
			HibernateUtil.closeSession();
		}
		return fileId;
	}

	/**
	 * 
	 * @param channelId
	 * @param pageCount
	 * @param pageNum
	 * @param curdate
	 * @return 有周期的节目 ——播放器需要的一个播放列表比对时间进行更改正在播放的名称
	 */
	public List getProgramListByChanelIdyn2(String channelId) {
		List list = new ArrayList();
		int ret = 0;
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String curdate = StringUtil.getCurentDateTimeAsString();
		String hourStr = curdate.substring(11, curdate.length());
		try {
			String sql = "select p.program_id,  p.program_name,  p.start_time,  p.end_time  " + " from soms4_live_program p where channel_id=" + channelId + "  and ((datediff(dd, start_time, '"
					+ curdate + "')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '" + curdate + "') as nvarchar), cycle_ids)>0)) "
					+ "and (CONVERT(varchar(12) , start_time, 114 ) <='" + hourStr + "' and CONVERT(varchar(12) , end_time, 114 ) >='" + hourStr + "' or (CONVERT(varchar(12) , start_time, 114 ) >='"
					+ hourStr + "'))" + " order by datepart(hh,start_time),datepart(mi,start_time)";

			conn = AppTools.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProgramInfo info = new ProgramInfo();
				info.setProgram_id(rs.getInt("program_id"));
				info.setProgram_name(rs.getString("program_name"));
				info.setStart_time(rs.getTimestamp("start_time"));
				info.setEnd_time(rs.getTimestamp("end_time"));
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("获取直播频道信息列表失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return list;
	}
	public ProgramInfo getNodeProgramInfo(ProgramInfo info)
	  {
		try
		{
		    String nodeId = info.getRemote_address().trim();
		    FileMgr mgr = new FileMgr();
		    String sql = "from FileInfo f where f.catalogInfo.catalog_id=" + nodeId + " and f.if_segment<>1 order by f.file_id DESC";
		    List list = mgr.getFileList(sql);
		    FileInfo fileInfo = ((FileInfo)(FileInfo)list.get(0));
		   
		    info.setFileInfo(fileInfo);
		    info.setPlay_type(Integer.valueOf(0));
		}catch(Exception ex)
		{
			   info.setPlay_type(Integer.valueOf(0));
		}
	    return info;
	  }
	
	/**
	 * 
	 * @param parent_file_id
	 * @return
	 */
	public List getFileInfoList(Integer parent_file_id) {
		List list = null;
		Session session = HibernateUtil.currentSession();
		Transaction ts = null;
		try {
			ts = session.beginTransaction();
			Query query = session.createQuery("from FileInfo f  where f.parent_file_id <> f.file_id " +
					"and f.parent_file_id=:parent_file_id and f.file_state=0 order by f.file_size desc ");
			query.setInteger("parent_file_id", parent_file_id);
			list = query.list();
			ts.commit();
		} catch (HibernateException e) {
			ts.rollback();
			Logger.log("获取文件信息失败: " + e.getMessage(), 2);
		} finally {
			HibernateUtil.closeSession();
		}
		return list;
	}

	
	//获取当前日期 及其前6天的数据
	public List<Date> getWeekDate(){
		 List<Date> list = new ArrayList<Date>();
		 for(int i=0;i>-7;i--){
			 Calendar cal=Calendar.getInstance();
				cal.add(Calendar.DATE,i);
			    Date d=cal.getTime();
			    list.add(d);
		 }
		 return list;
	} 
	

	
	public List<ProgramInfo> getProgramInfoListByChannelIdAndDay(String channelId,String day) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ProgramInfo> list = new ArrayList<ProgramInfo>();
		try {
			String database = SomsConfigInfo.get("database");
			String sql="";
			if(database.equals("SQL Server")){
				 sql = "SET DATEFIRST 1 select * from soms4_live_program where channel_id=" + channelId + "  and ((datediff(dd, start_time, '" + day
						+ "')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '" + day + "') as nvarchar), cycle_ids)>0)) order by datepart(hh,start_time) ,datepart(mi,start_time)";
			}else{
				//Oracle兼容
				sql=" select * from soms4_live_program where channel_id="+channelId+"  and ((TO_CHAR (START_TIME, 'yyyymmdd')-TO_CHAR(TO_DATE ('"+day+"', 'yyyy-mm-dd'),'yyyymmdd')=0)"+
							" or (cycle=1 and instr(cycle_ids,to_char(to_date('"+day+"', 'yyyy-mm-dd'),'D'),1,1)>0))"+
							" order by to_char(start_time,'hh24') ,to_char(start_time,'mi')";
			}
			/*String sql= "select program_id, program_name,start_time,time_long,if_drm,play_type,cycle,save_plan_id,tvName"
							+"  from soms4_live_program where channel_id="+channelId+" and ((datediff(dd, start_time, '"+day+"')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '"+day+"') as nvarchar), cycle_ids)>0)) order by convert(nvarchar, start_time, 8)";
*/			
			conn = AppTools.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProgramInfo info = new ProgramInfo();
				info.setProgram_id(rs.getInt("program_id"));
				info.setProgram_name(rs.getString("program_name"));
				info.setStart_time(rs.getTimestamp("start_time"));
				info.setTime_long(rs.getInt("time_long"));
				info.setIf_drm(rs.getString("if_drm"));
				info.setPlay_type(rs.getInt("play_type"));
				info.setCycle(rs.getInt("cycle"));
				info.setSave_plan_id(rs.getInt("save_plan_id"));
				info.setTvName(rs.getString("tvName"));
				info.setInvalid(0);
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("getProgramInfoList-->获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		List<Integer> list2 =  getProgramIdListInvalidByChannelIdAndDay( channelId, day);
		list = setProgramListInvalidOrder(list,list2);
		return list;
	}
	public List<Integer> getProgramIdListInvalidByChannelIdAndDay(String channelId,String day) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Integer> list = new ArrayList<Integer>();
		try {
			String sql= "select program_id  from soms4_live_program_invalid where channel_id="+channelId+" and invalidTime='"+day+"' order by program_id desc";
			conn = AppTools.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getInt("program_id"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("getProgramInfoList-->获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		return list;
	}
	public List<ProgramInfo> setProgramListInvalidOrder(List<ProgramInfo> list1,List<Integer> list2){
		for(Integer programId : list2){
			for(ProgramInfo info : list1){
				if(info.getProgram_id().equals(programId)){
					info.setInvalid(1);
				}
			}
		}
		Collections.sort(list1, comparator);
		return list1;
	}
	Comparator<ProgramInfo>  comparator = new Comparator<ProgramInfo>() {
		
		public int compare(ProgramInfo o1, ProgramInfo o2) {
			if(o1.getInvalid()<=o2.getInvalid()){
				return -1;
			}else{
				return 1;
			}
		}
	};

	public boolean invalidProgram(String[] sProgramIds, String sChannelId,
			String sNowDate) {
		Connection conn = null;
		Statement ps = null;
		boolean bet = false;
		try {
			conn = AppTools.getConnection();
			ps = conn.createStatement();
			for(int i=0;i<sProgramIds.length;i++){
				String sql1 = "delete from soms4_live_program_invalid where program_id="+sProgramIds[i]+" and invalidTime='"+sNowDate+"'";
				String sql2 = "insert into soms4_live_program_invalid (program_id,channel_id,invalidTime) values ("+sProgramIds[i]+","+sChannelId+",'"+sNowDate+"')";
				ps.addBatch(sql1);
				ps.addBatch(sql2);
			}
			ps.executeBatch();
			bet = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return bet;
	}
	public boolean validProgram(String[] sProgramIds, String sChannelId,
			String sNowDate) {
		Connection conn = null;
		Statement ps = null;
		boolean bet = false;
		try {
			conn = AppTools.getConnection();
			ps = conn.createStatement();
			for(int i=0;i<sProgramIds.length;i++){
				String sql = "delete from soms4_live_program_invalid where program_id="+sProgramIds[i]+" and invalidTime='"+sNowDate+"'";
				ps.addBatch(sql);
			}
			ps.executeBatch();
			bet = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			AppTools.closeStatement(ps);
			AppTools.closeConnection(conn);
		}
		return bet;
	}
	

	
	private String SQLTIMESTAMP2STRING(String format , Timestamp time){
		 String ret = "";
		 SimpleDateFormat formatter = new SimpleDateFormat(format);
		 ret = formatter.format(time);
		 return ret;
		 
	}

	
	/**
	 * 找出ben
	 * @param channelId
	 * @param day
	 * @return
	 */
	public ProgramInfo getLastestProgramInfo(String channelId,String day) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ProgramInfo> list = new ArrayList<ProgramInfo>();
		try {
			String database = SomsConfigInfo.get("database");
			String sql="";
			if(database.equals("SQL Server")){
				 sql = "SET DATEFIRST 1 select * from soms4_live_program where channel_id=" + channelId + "  and ((datediff(dd, start_time, '" + day
						+ "')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '" + day + "') as nvarchar), cycle_ids)>0)) order by datepart(hh,start_time) ,datepart(mi,start_time)";
			}else{
				//Oracle兼容
				sql=" select * from soms4_live_program where channel_id="+channelId+"  and ((TO_CHAR (START_TIME, 'yyyymmdd')-TO_CHAR(TO_DATE ('"+day+"', 'yyyy-mm-dd'),'yyyymmdd')=0)"+
							" or (cycle=1 and instr(cycle_ids,to_char(to_date('"+day+"', 'yyyy-mm-dd'),'D'),1,1)>0))"+
							" order by to_char(start_time,'hh24') ,to_char(start_time,'mi')";
			}
			/*String sql= "select program_id, program_name,start_time,time_long,if_drm,play_type,cycle,save_plan_id,tvName"
							+"  from soms4_live_program where channel_id="+channelId+" and ((datediff(dd, start_time, '"+day+"')=0 and cycle=0) or (cycle=1 and charindex(cast(datepart(weekday, '"+day+"') as nvarchar), cycle_ids)>0)) order by convert(nvarchar, start_time, 8)";
*/			
			conn = AppTools.getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				ProgramInfo info = new ProgramInfo();
				info.setProgram_id(rs.getInt("program_id"));
				info.setProgram_name(rs.getString("program_name"));
				info.setStart_time(rs.getTimestamp("start_time"));
				info.setTime_long(rs.getInt("time_long"));
				info.setIf_drm(rs.getString("if_drm"));
				info.setPlay_type(rs.getInt("play_type"));
				info.setCycle(rs.getInt("cycle"));
				info.setSave_plan_id(rs.getInt("save_plan_id"));
				info.setTvName(rs.getString("tvName"));
				info.setInvalid(0);
				list.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log("getProgramInfoList-->获取节目列表信息失败: " + e.getMessage(), 2);
		} finally {
			AppTools.closeResultSet(rs);
			AppTools.closeStatement(stmt);
			AppTools.closeConnection(conn);
		}
		if(list.size()>0){
			return list.get(list.size()-1);
		}else{
			return null;
		}
	}
}
