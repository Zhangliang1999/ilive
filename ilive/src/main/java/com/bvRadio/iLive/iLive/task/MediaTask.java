package com.bvRadio.iLive.iLive.task;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.bvRadio.iLive.iLive.action.front.vo.ILiveAppEnterprise;
import com.bvRadio.iLive.iLive.action.util.EnterpriseCache;
import com.bvRadio.iLive.iLive.action.util.EnterpriseUtil;
import com.bvRadio.iLive.iLive.entity.DocumentManager;
import com.bvRadio.iLive.iLive.entity.ILiveEnterprise;
import com.bvRadio.iLive.iLive.entity.ILiveMediaFile;
import com.bvRadio.iLive.iLive.entity.PictureInfo;
import com.bvRadio.iLive.iLive.manager.DocumentManagerMng;
import com.bvRadio.iLive.iLive.manager.ILiveEnterpriseMng;
import com.bvRadio.iLive.iLive.manager.ILiveMediaFileMng;
import com.bvRadio.iLive.iLive.manager.PictureInfoMng;
import com.bvRadio.iLive.iLive.util.ApplicationContextUtil;
import com.bvRadio.iLive.iLive.web.ConfigUtils;

/**
 * 文件存储定时器
 * 
 * @author Administrator
 *
 */
public class MediaTask extends TimerTask {
	ExecutorService newFixedThreadPool = null;
	public MediaTask() {
		newFixedThreadPool = Executors.newFixedThreadPool(10);
	}
	@Override
	public void run() {
		try {
			Hashtable<Integer, List<Integer>> enterpriseMap = EnterpriseCache.getEnterpriseMap();
			//2019-03-05 修改此处的逻辑 改为从数据库拿取所有企业id并对其重新计算套餐使用情况
			ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
			ILiveEnterpriseMng iLiveEnterpriseMng = (ILiveEnterpriseMng) applicationContext.getBean("iLiveEnterpriseMng");
			List<ILiveAppEnterprise> list=iLiveEnterpriseMng.getBatchEnterpriseForStatics(null, 30000);
			if(!list.isEmpty()){
				for(ILiveAppEnterprise app:list){
					Integer enterpriseId=app.getEnterpriseId();
					log.info("开始更新企业id为："+enterpriseId+"的套餐使用情况");
					runnableMedia(enterpriseId);
				}
			}
//			if (!enterpriseMap.isEmpty()) {
//				Iterator<Integer> enterpriseIds = enterpriseMap.keySet().iterator();
//				while (enterpriseIds.hasNext()) {
//					Integer enterpriseId = enterpriseIds.next();
//					List<Integer> list = enterpriseMap.get(enterpriseId);
//					if(list==null){
//						list = new ArrayList<Integer>();
//					}
//					for (Integer code : list) {
//						if(code.equals(EnterpriseCache.ENTERPRISE_FUNCTION_Store)){
//							System.out.println("开始执行runnableMedia！！！！！！！！！！！！！！1");
//							runnableMedia(enterpriseId);
//							
//						}
//					}
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final Logger log = LoggerFactory.getLogger(MediaTask.class);
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public void runnableMedia(final Integer enterpriseId) {
		newFixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
					Hashtable<Integer, Hashtable<String, String>> enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
					Hashtable<String, String> strMap = enterpriseStrMap.get(enterpriseId);
					if(strMap==null){
						ILiveEnterpriseMng iLiveEnterpriseMng = (ILiveEnterpriseMng) applicationContext.getBean("iLiveEnterpriseMng");
						ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
						try {
							EnterpriseUtil.selectEnterpriseCache(enterpriseId, iLiveEnterPrise.getCertStatus());
							enterpriseStrMap = EnterpriseCache.getEnterpriseStrMap();
							strMap = enterpriseStrMap.get(enterpriseId);
						} catch (Exception e) {
							log.info("更新企业功能失败");
						}
					}
					if(strMap!=null){
						String store = strMap.get(EnterpriseCache.ENTERPRISE_Store);
						if(store!=null&&store!=""){
							log.info("存储空间容量："+store);
							Long storeAll = Long.valueOf(store);
							if(storeAll<0){
								log.info("存储空间错误");
							}else{
								System.out.println("视频存储计算总量!!");
								//视频存储计算总量
								ILiveMediaFileMng iLiveMediaFileMng = (ILiveMediaFileMng) applicationContext.getBean("iLiveMediaFileMng");
								List<ILiveMediaFile> files = iLiveMediaFileMng.selectILiveMediaFileListByEnterprieId(enterpriseId,1);//文件类型 1视频 2文档 3图片
								Long fileSizeAll = 0l;
								for (ILiveMediaFile iLiveMediaFile : files) {
									Integer isMediaType=iLiveMediaFile.getIsMediaType();
									Integer delFlag=iLiveMediaFile.getDelFlag();
									if(isMediaType==null) {
										isMediaType=0;
									}
									if(isMediaType!=1&&delFlag!=1) {
										Long fileSize = iLiveMediaFile.getFileSize();
										fileSize = fileSize==null?0l:fileSize;
										fileSizeAll = fileSizeAll+fileSize;
									}
									
								}
								System.out.println("视频存储计算总量为："+fileSizeAll);
								System.out.println("文档存储计算总量!!");
								//文档存储计算总量
								DocumentManagerMng documentManagerMng = (DocumentManagerMng) applicationContext.getBean("documentManagerMng");
								List<DocumentManager> docs=documentManagerMng.getListByEnterpriseId(enterpriseId);
								Long docSizeAll = 0l;
								for(DocumentManager doc:docs) {
									Long docSize=doc.getSize().longValue();
									docSize = docSize==null?0l:docSize;
									docSizeAll=docSizeAll+docSize;
									docSizeAll=docSizeAll/1024L;
								}
								System.out.println("文档存储计算总量为："+docSizeAll);
								System.out.println("图片存储计算总量!!");
								//图片存储计算总量
								PictureInfoMng pictureInfoMng = (PictureInfoMng) applicationContext.getBean("pictureInfoMng");
								List<PictureInfo> pics=pictureInfoMng.getListByEnterpriseId(enterpriseId);
								Long picSizeAll = 0l;
								for(PictureInfo pic:pics) {
									Long picSize=pic.getSize().longValue();
									picSize = picSize==null?0l:picSize;
									picSizeAll=picSizeAll+picSize;
									picSizeAll=picSizeAll/1024L;
								}
								System.out.println("图片存储计算总量为："+picSizeAll);
								if((storeAll-docSizeAll-picSizeAll)<fileSizeAll){
									Long size = (fileSizeAll+picSizeAll+docSizeAll)-storeAll;
									for (int i = 0; i < files.size(); i++) {
										ILiveMediaFile iLiveMediaFile = files.get(i);
										iLiveMediaFile.setIsMediaType(ILiveMediaFile.MEDIA_TYPE_Temporary);
										Integer day = 0;
										try {
											String isMediaFileStore = ConfigUtils.get("isMediaFileStore");
											day = Integer.valueOf(isMediaFileStore);
										} catch (Exception e) {
											day=7;
										}
										Calendar calendar = Calendar.getInstance();
								        calendar.setTime(new Date());
								        calendar.add(Calendar.DAY_OF_MONTH, day);//+1今天的时间加一天
								        Date date = calendar.getTime();
								        iLiveMediaFile.setTemporaryTime(Timestamp.valueOf(format.format(date)));
										Long fileSize = iLiveMediaFile.getFileSize();
										if(fileSize==null){
											fileSize=0L;
										}
										if(size>0){
											iLiveMediaFileMng.updateEventFileOnlineFlag(iLiveMediaFile);
											size = size - fileSize;
										}else{
											Long usedValue=storeAll+size;
											ILiveEnterpriseMng iLiveEnterpriseMng = (ILiveEnterpriseMng) applicationContext.getBean("iLiveEnterpriseMng");
											ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
											EnterpriseUtil.totalUpdate(enterpriseId, EnterpriseUtil.ENTERPRISE_USE_TYPE_Store.toString(), iLiveEnterPrise.getCertStatus(), usedValue.toString());
											break;
										}
									}
								}else {
									Long usedValue=fileSizeAll+picSizeAll+docSizeAll;
									System.out.println("总占用存储为："+usedValue);
									ILiveEnterpriseMng iLiveEnterpriseMng = (ILiveEnterpriseMng) applicationContext.getBean("iLiveEnterpriseMng");
									ILiveEnterprise iLiveEnterPrise = iLiveEnterpriseMng.getILiveEnterPrise(enterpriseId);
									EnterpriseUtil.totalUpdate(enterpriseId, EnterpriseUtil.ENTERPRISE_USE_TYPE_Store.toString(), iLiveEnterPrise.getCertStatus(), usedValue.toString());
								}
							}
						}else{
							log.info("企业购买总内存为 ："+store);
						}
					}else{
						log.info("企业存储获取失败   为空");
					}
				} catch (Exception e) {
					e.printStackTrace();
					log.error("文件处理异常 ："+e.toString());
				}
			}
		});
	}
}
