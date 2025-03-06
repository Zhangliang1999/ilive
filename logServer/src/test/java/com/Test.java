package com;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class Test {
	public static void main(String[] args) throws Exception {
		list = new ArrayList<String>();
		FTPClient ftp = new FTPClient();
		ftp.setDefaultPort(21);
		ftp.connect("192.168.222.156");
		ftp.setControlEncoding("GBK");//注意编码格式  
		boolean isLogin = ftp.login("administrator","yanxueli19930301");
		if(isLogin){
			ftp.enterLocalPassiveMode();
			FTPFile[] fs = ftp.listFiles("img/2018_05/05/");
			System.out.println(fs.length);
			for (FTPFile ftpFile : fs) {
				downloadFile(ftp,ftpFile, "D:\\log\\1\\", "img/2018_05/05/");
			}
			
		}else{
			System.out.println("==========ftp连接失败========");
		}
	}
	private static List<String> list;
	/** 
     * 递归遍历出目录下面所有文件 
     * @param pathName 需要遍历的目录，必须以"/"开始和结束 
     * @throws IOException 
     */  
    public static void selectlist(FTPClient ftp,String pathName) throws IOException{
        if(pathName.startsWith("/")&&pathName.endsWith("/")){  
            String directory = pathName;  
            //更换目录到当前目录  
            FTPFile[] files = ftp.listFiles(directory);  
            for(FTPFile file:files){  
                if(file.isFile()){  
                	list.add(directory+file.getName());  
                }else if(file.isDirectory()){  
                	selectlist(ftp,directory+file.getName()+"/");  
                }  
            }  
        }
    }
	/**  
     *  
     * 下载FTP文件  
     * 当你需要下载FTP文件的时候，调用此方法  
     * 根据<b>获取的文件名，本地地址，远程地址</b>进行下载  
     * @param ftpFile  
     * @param localPath 本地地址 
     * @param remotePath  远程地址
     */   
    private  static void downloadFile(FTPClient ftp,FTPFile ftpFile, String relativeLocalPath,String relativeRemotePath) {   
        if (ftpFile.isFile()) {  
            if (ftpFile.getName().indexOf("?") == -1) {   
                OutputStream outputStream = null;   
                try {   
                	String fileName = ftpFile.getName();
                	System.out.println("=================="+fileName);
                    File entryDir = new File(relativeLocalPath);  
                    //如果文件夹路径不存在，则创建文件夹  
                    if (!entryDir.exists() || !entryDir.isDirectory())  
                    {  
                        entryDir.mkdirs();  
                    }  
                    File locaFile= new File(relativeLocalPath+ fileName);   
                    //判断文件是否存在，存在则返回   
                    if(locaFile.exists()){   
                    	//ftp.deleteFile(relativeRemotePath+"/"+fileName);
                        return;   
                    }else{   
                        outputStream = new FileOutputStream(locaFile);   
                        ftp.changeWorkingDirectory(relativeRemotePath); 
                        boolean b = ftp.retrieveFile(new String(fileName.getBytes("GBK"), "iso-8859-1"), outputStream);
                        System.out.println(b);
                    	outputStream.flush();   
                        outputStream.close();   
                    } 
                } catch (Exception e) {   
                } finally {   
                    try {   
                        if (outputStream != null){   
                            outputStream.close();   
                        }  
                    } catch (IOException e) {   
                    }   
                }   
            }   
        } 
    }   
}
