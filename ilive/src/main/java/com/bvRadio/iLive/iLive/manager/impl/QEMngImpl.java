package com.bvRadio.iLive.iLive.manager.impl;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bvRadio.iLive.iLive.action.front.vo.AppMediaFile;
import com.bvRadio.iLive.iLive.dao.ProgressStatusPoJoDao;
import com.bvRadio.iLive.iLive.entity.ProgressStatusPoJo;
import com.bvRadio.iLive.iLive.manager.ILiveFieldIdManagerMng;
import com.bvRadio.iLive.iLive.manager.QEMng;


@Service
@Transactional
public class QEMngImpl implements QEMng {

	@Autowired
	private ILiveFieldIdManagerMng fieldIdMng;	//获取主键

	@Autowired
	private ProgressStatusPoJoDao progressStatusPoJoDao;	//获取主键
	
	@Override
	public String makeFailedXml(String type,String errordesc) {
		//得到document对象
		Document document =DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		//添加根节点
		Element root=document.addElement("root");
		//添加节点class,属性名为name与table 分别赋值
		Element responseType = root.addElement("responseType");
		
		responseType.setText(type+"_ret");
		
		Element param=root.addElement("param");
		
		Element issuc=param.addElement("issuc");
		issuc.setText("no");
		Element errdesc=param.addElement("errdesc");
		errdesc.setText(errordesc);
		
		String xml=document.asXML();
		
		return xml;
	}

	
	@Override
	public String makeServerXml(String susername, String spassword, String sipaddr, String sport, String sserverurl) {
		//得到document对象
				Document document =DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				//添加根节点
				Element root=document.addElement("root");
				//添加节点class,属性名为name与table 分别赋值
				Element username = root.addElement("username");
				username.setText(susername);
				
				Element pwdmd5=root.addElement("pwdmd5");
				pwdmd5.setText(spassword);
				
				Element ipadddr=root.addElement("ipadddr");
				ipadddr.setText(sipaddr);
				
				Element port=root.addElement("port");
				port.setText(sport);
				
				Element serverurl=root.addElement("serverurl");
				serverurl.setText(sserverurl);
				
				String xml=document.asXML();
				
				return xml;
	}
	
	@Override
	public String makeLoginXml(String uploadUrl, String uploadPath, String catalogueUrl, String newsEditURL,
			String vodEditURL, String progressQueryUrl, String vodPlayUrl) {
		//得到document对象
				Document document =DocumentHelper.createDocument();
				document.setXMLEncoding("GBK");
				//添加根节点
				Element root=document.addElement("root");
				//添加节点class,属性名为name与table 分别赋值
				Element responseType = root.addElement("responseType");
				
				responseType.setText("login_ret");
				
				Element param=root.addElement("param");
				
				Element issuc=param.addElement("issuc");
				issuc.setText("yes");
				
				Element newsEditEle = param.addElement("newsEditURL");
				newsEditEle.addCDATA(newsEditURL);
				
				Element vodEditEle = param.addElement("vodEditURL");
				vodEditEle.addCDATA(vodEditURL);
				
				Element catalogURL=param.addElement("catalogURL");
				catalogURL.setText(catalogueUrl);
				
				Element httpUploadURL=param.addElement("httpUploadURL");
				httpUploadURL.setText(uploadUrl);
				
				Element httpUploadPath=param.addElement("httpUploadPath");
				httpUploadPath.setText(uploadPath);
				
				Element progressQueryURL = param.addElement("progressQueryURL");
				progressQueryURL.setText(progressQueryUrl);
				
				Element vodPlayUrlEle = param.addElement("vodPlayURL");
				vodPlayUrlEle.setText(vodPlayUrl);
				
				
				String xml=document.asXML();
				
				return xml;
	}

	@Override
	public String makeUploadFileSuccXml(long fileSize) {

			// 得到document对象
			Document document = DocumentHelper.createDocument();
			document.setXMLEncoding("GBK");
			// 添加根节点
			Element root = document.addElement("root");
			// 添加节点class,属性名为name与table 分别赋值
			Element responseType = root.addElement("responseType");

			responseType.setText("UploadFile_ret");

			Element param = root.addElement("param");

			Element issuc = param.addElement("issuc");
			issuc.setText("yes");
			Element CurFileSize = param.addElement("CurFileSize");
			CurFileSize.setText(Long.toString(fileSize));

			String xml = document.asXML();
			System.out.println(xml);
			return xml;

	}

	@Override
	public String makeFinishNotifySuccXml(long fileSize) {

		// 得到document对象
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		// 添加根节点
		Element root = document.addElement("root");
		// 添加节点class,属性名为name与table 分别赋值
		Element responseType = root.addElement("responseType");

		responseType.setText("FinishNotify_ret");

		Element param = root.addElement("param");

		Element issuc = param.addElement("issuc");
		issuc.setText("yes");
		Element CurFileSize = param.addElement("CurFileSize");
		CurFileSize.setText(Long.toString(fileSize));

		String xml = document.asXML();
		System.out.println(xml);
		return xml;
	}

	@Override
	public String makeFileSizeSuccXml(long fileSize) {

		// 得到document对象
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		// 添加根节点
		Element root = document.addElement("root");
		// 添加节点class,属性名为name与table 分别赋值
		Element responseType = root.addElement("responseType");

		responseType.setText("GetFileSize_ret");

		Element param = root.addElement("param");

		Element issuc = param.addElement("issuc");
		issuc.setText("yes");
		Element CurFileSize = param.addElement("CurFileSize");
		CurFileSize.setText(Long.toString(fileSize));

		String xml = document.asXML();
		return xml;
	}
	
	@Override
	public String makeUploadNotifySuccXml() {

		// 得到document对象
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		// 添加根节点
		Element root = document.addElement("root");
		// 添加节点class,属性名为name与table 分别赋值
		Element responseType = root.addElement("responseType");

		responseType.setText("uploadNotify_ret");

		Element param = root.addElement("param");

		Element issuc = param.addElement("issuc");
		issuc.setText("yes");

		String xml = document.asXML();
		System.out.println(xml);
		return xml;
	}
	
	@Override
	public String makeMergeSuccXml(long fileSize) {

		// 得到document对象
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		// 添加根节点
		Element root = document.addElement("root");
		// 添加节点class,属性名为name与table 分别赋值
		Element responseType = root.addElement("responseType");

		responseType.setText("UploadFile_Merge_ret");

		Element param = root.addElement("param");

		Element issuc = param.addElement("issuc");
		issuc.setText("yes");
		Element CurFileSize = param.addElement("CurFileSize");
		CurFileSize.setText(Long.toString(fileSize));

		String xml = document.asXML();
		return xml;
	}
	
	@Override
	public String makeDownloadXml(List<AppMediaFile> appMediaFileList ){
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("GBK");
		Element root = document.addElement("filelist");
		
		for(AppMediaFile appMediaFile : appMediaFileList){
			Element file = root.addElement("file");
			Element fileid = file.addElement("fileid");
			fileid.addText(appMediaFile.getFileId().toString());
			Element filename = file.addElement("filename");
			filename.addText(appMediaFile.getFileName());
			Element fileurl = file.addElement("fileurl");
			fileurl.addText(appMediaFile.getPlayAddr());
			
		}
		String xml = document.asXML();
		return xml;
		
	}
	
	
	@Override
	public ProgressStatusPoJo getUpdateTask(String taskUUID) {
		// TODO Auto-generated method stub
		return progressStatusPoJoDao.getUpdateTask(taskUUID);
	}

	@Override
	public Long addUpdateTask(ProgressStatusPoJo progressStatusPoJo) {
		// TODO Auto-generated method stub
		Long nextId = fieldIdMng.getNextId("soms4_quickedit_status", "task_id", 1);
		progressStatusPoJo.setTaskId(nextId);
		progressStatusPoJoDao.save(progressStatusPoJo);
		return nextId;
	}
	
	
	
	
	
}
