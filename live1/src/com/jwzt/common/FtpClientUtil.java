package com.jwzt.common;

import java.io.File;
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


/**
 * 
 * @author administrator FTP连接工具
 */
public class FtpClientUtil {
	private String server;
	private int port;
	private String userName;
	private String userPassword;
	private String ftpEncode;

	public FtpClientUtil(String server, int port, String userName, String userPassword) {
		this.server = server;
		this.port = port;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	private FTPClient getClient() throws SocketException, IOException {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftp.setDefaultPort(getPort());
		ftp.connect(getServer());
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			// log.warn("FTP server refused connection: {}",
			// getFtp_address_inner());
			ftp.disconnect();
			return null;
		}
		if (!ftp.login(getUserName(), getUserPassword())) {
			// log.warn("FTP server refused login: {}, user: {}",
			// getFtp_address_inner(), getFtp_user());
			ftp.logout();
			ftp.disconnect();
			return null;
		}
		ftp.setControlEncoding(getFtpEncode());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		return ftp;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getFtpEncode() {
		return ftpEncode;
	}

	public void setFtpEncode(String ftpEncode) {
		this.ftpEncode = ftpEncode;
	}

	/**
	 * 上传文件
	 * 
	 * @param cacheDir
	 * @param ftpFileName
	 * @param ftpDir
	 */
	public boolean upload(String localFileAndName, String ftpFileName, String ftpDir) {
		FileInputStream in = null;
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				String filePath = ftpDir + ftpFileName;
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
				in = new FileInputStream(new File(localFileAndName));
				ftp.storeFile(name, in);
				ftp.logout();
				ftp.disconnect();
			}
			return true;
		} catch (SocketException e) {
			Logger.log(Logger.getCurTime2()+"IOException:"+e.toString(), 3);
			return false;
		} catch (IOException e) {
			Logger.log(Logger.getCurTime2()+"IOException:"+e.toString(), 3);
			return false;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 下载
	 * 
	 * @param ftpDirectoryAndFileName
	 * @param localDirectoryAndFileName
	 * @return
	 */
	public long download(String ftpDirectoryAndFileName, String localDirectoryAndFileName) {

		return 0;
	}

}