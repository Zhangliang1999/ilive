package com.bvRadio.iLive.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
 
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
 
public class Ftp {
    //ftp对象
    private FTPClient ftp;
    //需要连接到的ftp端的ip
    private String ip;
    //连接端口，默认21
    private int port;
    //要连接到的ftp端的名字
    private String name;
    //要连接到的ftp端的对应得密码
    private String pwd;
 
    //调用此方法，输入对应得ip，端口，要连接到的ftp端的名字，要连接到的ftp端的对应得密码。连接到ftp对象，并验证登录进入fto
    public Ftp(String ip, int port, String name, String pwd) {
        ftp = new FTPClient();
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.pwd = pwd;
 
        //验证登录
        try {
            ftp.connect(ip, port);
            System.out.println(ftp.login(name, pwd));
            ftp.setControlEncoding("UTF-8");
 
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
 
    }
    
   //验证登录
    public void login() {
      try {
          ftp.connect(ip, port);
          System.out.println(ftp.login(name, pwd));
          ftp.setControlEncoding("UTF-8");
 
      } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
    }
 
    //上传文件
    public boolean putFile(String remoteFileName,String localFileUrl) {
        try {
            //将本地的"D:/Tshit.rar"文件上传到ftp的根目录文件夹下面，并重命名为"Tshit.rar"
//            System.out.println(ftp.storeFile("Tshit.rar", new FileInputStream(new File("D:/Tshit.rar"))));
        	ftp.setFileType(FTP.BINARY_FILE_TYPE);
        	return ftp.storeFile(remoteFileName, new FileInputStream(new File(localFileUrl)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
 
    //下载文件
    public boolean getFile(String remoteFileName,String localFileUrl) {
        try {
            //将ftp根目录下的"testavi.avi"文件下载到本地目录文件夹下面，并重命名为"2.avi"
//            ftp.retrieveFile("testavi.avi", new FileOutputStream(new File("D:/2.avi")));
        	return ftp.retrieveFile(remoteFileName, new FileOutputStream(new File(localFileUrl)));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
 
        //删除ftp文件
        public boolean delete(String remoteFileName) {
            try {
            	return ftp.deleteFile(remoteFileName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
        }
 
    public static void main(String args[]) {
        Ftp m = new Ftp("125.124.82.207",21,"test","test123");
         m.login();
//         m.putFile(remoteFileName,localFileUrl);//localFileUrl是本地文件的路径，remoteFileName是上传到ftp服务器后的文件名
//         m.getFile(remoteFileName,localFileUrl);
//         m.delete(remoteFileName);
        
    }
}

