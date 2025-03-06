package com.bvRadio.iLive.iLive.entity;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bvRadio.iLive.iLive.entity.base.BaseILiveUploadServer;

@SuppressWarnings("serial")
public class ILiveUploadServer extends BaseILiveUploadServer implements java.io.Serializable {
	private static final Logger log = LoggerFactory.getLogger(ILiveUploadServer.class);

	public String getImageRootUrl() {
		String fileRootUrl = "";
		String downloadUrl = getDownloadUrl();
		if (!StringUtils.isBlank(downloadUrl)) {
			fileRootUrl += downloadUrl;
		}
		String httpMount = getHttpMount();
		if (!StringUtils.isBlank(httpMount)) {
			fileRootUrl += httpMount;
		}
		return fileRootUrl;
	}

	public String getFileRootUrl() {
		String fileRootUrl = "";
		String httpUrl = getHttpUrl();
		if (!StringUtils.isBlank(httpUrl)) {
			fileRootUrl += httpUrl;
		}
		String httpMount = getHttpMount();
		if (!StringUtils.isBlank(httpMount)) {
			fileRootUrl += httpMount;
		}
		return fileRootUrl;
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
			log.error("ftp upload error", e);
			return false;
		} catch (IOException e) {
			log.error("ftp upload error", e);
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
		return ftp;
	}

	public ILiveUploadServer() {
	}

	public ILiveUploadServer(Integer id) {
		super(id);
	}

}
