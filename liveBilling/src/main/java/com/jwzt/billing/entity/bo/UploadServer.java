package com.jwzt.billing.entity.bo;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ysf
 */
public class UploadServer {
	private static final Logger log = LogManager.getLogger();
	private String ftpIp;
	private Integer ftpPort;
	private String ftpUsername;
	private String ftpPassword;
	private String ftpPath;
	private String ftpEncoding;

	public UploadServer(String ftpIp, Integer ftpPort, String ftpUsername, String ftpPassword, String ftpPath,
			String ftpEncoding) {
		super();
		this.ftpIp = ftpIp;
		this.ftpPort = ftpPort;
		this.ftpUsername = ftpUsername;
		this.ftpPassword = ftpPassword;
		this.ftpPath = ftpPath;
		this.ftpEncoding = ftpEncoding;
	}

	public boolean upload(String filePath, InputStream in) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				filePath = getFtpPath() + filePath;
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
			log.error("ftp upload error, ip:{}, port:{}, username:{}, password:{}", getFtpIp(), getFtpPort(),
					getFtpUsername(), getFtpPassword(), e);
			return false;
		} catch (IOException e) {
			log.error("ftp upload error, ip:{}, port:{}, username:{}, password:{}", getFtpIp(), getFtpPort(),
					getFtpUsername(), getFtpPassword(), e);
			return false;
		}
	}

	public boolean deleteFile(String filePath) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				filePath = getFtpPath() + filePath;
				String name = FilenameUtils.getName(filePath);
				String path = FilenameUtils.getFullPath(filePath);
				if (ftp.changeWorkingDirectory(path)) {
					log.info("ftp delete，filePath={}", filePath);
					if (!ftp.deleteFile(name)) {
						log.error("ftp delete faild，filePath={}", filePath);
						return false;
					}
				}
				ftp.logout();
				ftp.disconnect();
			}
			return true;
		} catch (SocketException e) {
			log.error("ftp delete error，filePath={}", filePath, e);
			return false;
		} catch (IOException e) {
			log.error("ftp delete error，filePath={}", filePath, e);
			return false;
		}
	}

	public boolean deletePath(String filePath) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				if (ftp.changeWorkingDirectory(filePath)) {
					FTPFile[] ftpFiles = ftp.listFiles();
					for (FTPFile ftpFile : ftpFiles) {
						String ftpFilename = ftpFile.getName();
						String filePathForDelete;
						if (filePath.endsWith("/")) {
							filePathForDelete = filePath + ftpFilename;
						} else {
							filePathForDelete = filePath + "/" + ftpFilename;
						}
						if (ftpFile.isFile()) {
							if (ftp.deleteFile(filePathForDelete)) {
								log.info("ftp delete，filePath={}", ftpFile.getName());
							} else {
								return false;
							}
						} else {
							deletePath(filePathForDelete);
						}
					}
					if (!ftp.removeDirectory(filePath)) {
						log.error("ftp enter path fail，filePath={}", filePath);
						return false;
					}
				} else {
					log.error("ftp enter path fail，filePath={}", filePath);
					return false;
				}
				ftp.logout();
				ftp.disconnect();
			}
			return true;
		} catch (SocketException e) {
			log.error("ftp delete error，filePath={}", filePath, e);
			return false;
		} catch (IOException e) {
			log.error("ftp delete error，filePath={}", filePath, e);
			return false;
		}
	}

	private FTPClient getClient() throws SocketException, IOException {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
		ftp.setDefaultPort(getFtpPort());
		ftp.connect(getFtpIp());
		int reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			log.warn("FTP server refused connection: {}", getFtpIp());
			ftp.disconnect();
			return null;
		}
		if (!ftp.login(getFtpUsername(), getFtpPassword())) {
			log.warn("FTP server refused login: {}, user: {}", getFtpIp(), getFtpUsername());
			ftp.logout();
			ftp.disconnect();
			return null;
		}
		ftp.setControlEncoding(getFtpEncoding());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		ftp.setBufferSize(1024 * 1024);
		return ftp;
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}

	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}

	public String getFtpUsername() {
		return ftpUsername;
	}

	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}

	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public String getFtpEncoding() {
		return ftpEncoding;
	}

	public void setFtpEncoding(String ftpEncoding) {
		this.ftpEncoding = ftpEncoding;
	}

}
