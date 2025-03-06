package com.jwzt.livems.computer;

/**
 * 对查询出来文件进行操作
 * 
 * @author 赵飞洋
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.jwzt.DB.cdn.mount.MountInfo;
import com.jwzt.DB.cdn.mount.MountMgr;
import com.jwzt.DB.soms.vod.file.FileInfo;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.livems.vod.FileAVInfo;
import com.jwzt.livems.vod.RecordVodMgr;

public class ComputerFind {
	public ComputerFind() {
	}

	/**
	 * 查找数据库中的所有根路径
	 * 
	 * @return 路径集合
	 */
	public static List<String> findpDiskBeans() {
		List<String> findChildFile = FindChildFile();
		return findChildFile;
	}

	/**
	 * 对视频进行切片
	 * 
	 * @param path
	 */
	public static boolean qiepian(String path) {
		RecordVodMgr recordVodMgr = new RecordVodMgr();
		RecordVodMgr r = new RecordVodMgr();
		FileInfo fileInfo = null;
		FileAVInfo fileAVInfo = r.getAVFileInfo(path);
		System.out.println("更新数fileAVInfo==" + fileAVInfo);
		String M3U8VideoCodec = "h264";
		String VideoCodec = fileAVInfo.getVideoCodec();
		String FileFormat = fileAVInfo.getFileFormat();
		String fileType = fileAVInfo.getFileType();
		String AudioCodec = fileAVInfo.getAudioCodec();
		VideoCodec = VideoCodec.toLowerCase();
		AudioCodec = AudioCodec.toLowerCase();
		boolean canM3U8 = false;
		if (FileFormat.toLowerCase().equals("mp4")) {
			if (fileType.equals("only_audio")
					&& (AudioCodec.toLowerCase().equals("mp3") || AudioCodec
							.toLowerCase().equals("aac"))) {
				canM3U8 = true;
			} else if (fileType.indexOf("video") != -1
					&& M3U8VideoCodec.indexOf(VideoCodec) != -1
					&& (AudioCodec.toLowerCase().equals("mp3") || AudioCodec
							.toLowerCase().equals("aac"))) {
				canM3U8 = true;
			}
		}
		if (canM3U8) {
			// 不需要转码 可以做切片
			// 做切片
			boolean segM3U8 = recordVodMgr.SegM3U8(path);
			return segM3U8;
		} else {
			return canM3U8;
		}
	}

	/**
	 * 根据路径删除文件或文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deletefile(String path) {
		boolean flag = false;
		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(path);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(path);
			}
		}

	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据数据库查询路径,获取包含的文件和文件夹
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static DiskBean ParentsToFile(String path) throws Exception {
		Set<String> s = new HashSet<String>();
		DiskBean disk = new DiskBean();
		disk.setDisk_name("测试");
		disk.setDisk_path(path);
		// 获取所有文件和文件夹
		List<FileAttributeBean> findDiskFile = FindDiskFile(path);
		for (FileAttributeBean f : findDiskFile) {
			File file = new File(f.getFile_path());
			if (file.isDirectory()) {
				s.add(file.getName());
			}
		}
		for (FileAttributeBean f : findDiskFile) {
			File file = new File(f.getFile_path());
			if (file.isFile()) {
				String fileName = f.getFile_name();
				String replace = fileName.replace(".", "_");
				boolean contains = s.contains(replace);
				if (contains) {
					f.setFile_qiepian(1);
				}
			}
		}
		disk.setFileList(findDiskFile);
		return disk;
	}

	/**
	 * 根据传过来的根路径，获取路径下的所有文件
	 * 
	 * @param path
	 * @throws Exception
	 */
	public static List<FileAttributeBean> FindDiskFile(String path)
			throws Exception {
		long a = System.currentTimeMillis();
		List<FileAttributeBean> fileList = new ArrayList<FileAttributeBean>();
		File parents = new File(path);
		File[] listFiles = parents.listFiles();
		for (File f : listFiles) {
			FileAttributeBean file = new FileAttributeBean();
			file.setFile_name(f.getName());
			// 获取最后修改时间
			long lastModified = f.lastModified();
			file.setFile_updateTime(LongtoDate(lastModified));
			// 文件类型
			file.setFile_type(getFileType(f));
			// 判断是否为MP4文件
			String mp4 = f.getPath();
			if (mp4.lastIndexOf(".") != -1) {
				String substring = mp4.substring(mp4.lastIndexOf("."));
				if (substring.equals(".mp4")) {
					file.setFile_mp4(1);
				}
			}
			// 文件路径
			String path2 = f.getPath();
			String replace = path2.replace("\\", "/");
			file.setFile_path(replace);
			if (!getFileType(f).equals("文件夹")) {
				// 文件大小
				String fileSizes = getFileSizes(f);
				file.setFile_size(fileSizes);
				file.setFile_img("imges/tubiao.png");

			} else {
				file.setFile_img("imges/wenjian.png");
			}
			fileList.add(file);
		}
		System.out.println("\r<br>FindDiskFile执行耗时 : "
				+ (System.currentTimeMillis() - a) / 1000f + " 秒 ");
		return fileList;
	}

	/**
	 * 获得数据库里根目录文件路径
	 * 
	 * @return 路径字符串集合
	 */
	@SuppressWarnings("unchecked")
	public static List<String> FindChildFile() {
		MountMgr mountMgr = new MountMgr();
		List<String> pathList = new ArrayList<String>();
		List<MountInfo> mountList = mountMgr.getMountList(Integer
				.parseInt(SomsConfigInfo.get("vodServerGroupId")));
		for (MountInfo mountInfo : mountList) {
			String path = mountInfo.getBase_path();
			pathList.add(path);
		}
		return pathList;
	}

	/**
	 * 转换格式，把long类型转换成字符串
	 * 
	 * @param size
	 *            传过来的long值
	 * @return
	 */
	public static String DoubletoString(long size) {
		double unit = 1024 * 1024 * 1024;
		Double ok = size / unit;
		String[] split = ok.toString().split("\\.");
		return split[0];
	}

	/**
	 * 将long型转换成时间
	 * 
	 * @param time
	 * @return 格式化后的时间字符串
	 */
	public static String LongtoDate(long time) {
		Date d = new Date();
		d.setTime(time);
		SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd");
		String format = sfm.format(d);
		return format;
	}

	/**
	 * 
	 * 获取文件的类型
	 * 
	 * @param file
	 * @return 类型字符串
	 */
	public static String getFileType(File file) {
		long a = System.currentTimeMillis();
		String fileTypeName = "";
		if (file.isFile()) {
			String path = file.getPath();
			if (path.lastIndexOf(".") != -1) {
				fileTypeName = path.substring(path.lastIndexOf("."));
			}

		}
		System.out.println("\r<br>getFileType执行耗时 : "
				+ (System.currentTimeMillis() - a) / 1000f + " 秒 ");
		return fileTypeName;
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static String getFileSizes(File f) {// 取得文件大小
		String s = "";
		if (f.exists() && f.isFile()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(f);
				int available = fis.available();
				Integer okint = available / (1024);
				s = okint.toString();
				return s;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {

				f.createNewFile();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		boolean deletefile = deletefile("F:\\imges\\2015-01-19");
		System.out.println(deletefile);
	}
}
