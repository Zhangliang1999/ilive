package com.jwzt.DB.cdn.accessMethods;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class ILiveServerAccessMethod extends BaseILiveServerAccessMethod {

	public boolean uploadFile(String ftpPath, String filePath, InputStream in) {
		try {
			FTPClient ftp = getClient();
			if (ftp != null) {
				filePath = ftpPath + filePath;
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
			ftp.disconnect();
			return null;
		}
		if (!ftp.login(getFtp_user(), getFtp_pwd())) {
			ftp.logout();
			ftp.disconnect();
			return null;
		}
		ftp.setControlEncoding(getFtpEncode());
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		ftp.enterLocalPassiveMode();
		return ftp;
	}

}
