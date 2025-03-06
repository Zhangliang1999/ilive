package com.bvRadio.iLive.iLive.entity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveServerAccessMethod;

public class ILiveServerAccessMethod extends BaseILiveServerAccessMethod {

	private MountInfo mountInfo;
	
	/**
	 * 源站IP
	 */
	private String orgLiveHttpUrl;

	Logger log = LoggerFactory.getLogger(ILiveServerAccessMethod.class);

	public boolean uploadFile(String filePath, FileInputStream in) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				filePath = mountInfo.getFtp_path() + filePath;
				String name = FilenameUtils.getName(filePath);
				String path = FilenameUtils.getFullPath(filePath);
				if (!ftp.changeWorkingDirectory(path)) {
					String[] ps = StringUtils.split(path, '/');
					String p = "/";
					ftp.changeWorkingDirectory(p);
					for (String s : ps) {
						p += s + "/";
						if (!ftp.changeWorkingDirectory(p)) {
							ftp.makeDirectory(s);
							ftp.changeWorkingDirectory(p);
						}
					}
				}
				ftp.storeFile(name, in);
				ftp.logout();
				ftp.disconnect();
			}
			in.close();
			return true;
		} catch (SocketException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	private FTPClient getClient() throws SocketException, IOException {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftp.setDefaultPort(getFtpPort());
		ftp.connect(getFtp_address_inner());
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", getFtp_address_inner());
			ftp.disconnect();
			return null;
		}
		if (!ftp.login(getFtp_user(), getFtp_pwd())) {
			log.warn("FTP server refused login: {}, user: {}", getFtp_address_inner(), getFtp_user());
			ftp.logout();
			ftp.disconnect();
			return null;
		}
		ftp.setControlEncoding(getFtpEncode());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		return ftp;
	}

	public MountInfo getMountInfo() {
		return mountInfo;
	}

	public void setMountInfo(MountInfo mountInfo) {
		this.mountInfo = mountInfo;
	}

	public String getOrgLiveHttpUrl() {
		return orgLiveHttpUrl;
	}

	public void setOrgLiveHttpUrl(String orgLiveHttpUrl) {
		this.orgLiveHttpUrl = orgLiveHttpUrl;
	}
	
	
	

}
