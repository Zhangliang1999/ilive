package com.jwzt.filemonitor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

import com.jwzt.common.Logger;
import com.jwzt.common.SomsConfigInfo;
import com.jwzt.livems.listener.FilePathPo;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyListener;

/**
 * 文件目录监听
 */
public class FileDirMonitor extends Thread {
	public static Queue<FilePathPo> queue = new LinkedList<FilePathPo>();

	/**
	 * 一个JNotify测试实例
	 * 
	 * @throws Exception
	 */
	public void tnotify(List<String> pathList) throws Exception {
		List<Integer> watchIdList = new ArrayList<Integer>();
		try {

			// 被监听事件
			int mask = JNotify.FILE_CREATED // 文件创建
					| JNotify.FILE_DELETED // 文件删除
//					| JNotify.FILE_MODIFIED // 文件修改  因获取判断文件是否能读取利用的是 重命名的方式，重命名方式又会触发文件修改 因此去此方法
					| JNotify.FILE_RENAMED; // 文件改名

			// 是否监听子目录
			boolean watchSubtree = true;

			// 添加监听
			for (String path : pathList) {
				System.out.println("开始监听文件目录：：" + path);
				Logger.log(Logger.getCurTime2()+" 开始监听文件目录：：" + path, 3);
				Listener listen = new Listener(path);
				int watchID = JNotify
						.addWatch(path, mask, watchSubtree, listen);
				watchIdList.add(watchID);
			}

		} catch (Exception e) {
			System.out.println("error:" + e.getMessage());
			Logger.log(Logger.getCurTime2()+"error:" + e.getMessage(), 3);
		}
	}
}

/**
 * 文件监听器
 */
class Listener extends Thread implements JNotifyListener {

	public Listener(String path) {

	}

	/**
	 * 该方法监听文件改名事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param oldName
	 *            被改名前文件名
	 * @param newName
	 *            被改名后文件名
	 */
	public void fileRenamed(int wd, String rootPath, String oldName,
			String newName) {
	}

	/**
	 * 该方法监听文件修改事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被修改文件名
	 */

	/**
	 * 注意：JNotify存在一次文件修改，触发多次fileModified方法的BUG，
	 * 该方法可以用来修复一次文件修改可能会触发多个fileModified方法， 从而减少没有必要的资源重新加载。但是由于t变量是类内共享变量，所
	 * 以注意线程安全，尽量避免共用Listener导致错误
	 */
	long t = 0;

	public void fileModified(int wd, String rootPath, String name) {
	/*	File file = new File(rootPath, name);
		if (t != file.lastModified()) {
			try {
				String allow_vod_format = SomsConfigInfo
						.get("allow_vod_format");
				String fileTotalPath = rootPath + name;
				String fileName = fileTotalPath.substring(
						fileTotalPath.lastIndexOf("\\") + 1,
						fileTotalPath.length());
				String fileExt = fileName.substring(
						fileName.lastIndexOf(".") + 1, fileName.length());
				// 发现新文件 执行 文件操作流程
				// 加入处理
				if (new File(fileTotalPath).isFile()) {
					if (allow_vod_format.indexOf(fileExt + "|") != -1) {
						if (fileName.indexOf("_seq_") == -1) {
							String filePathAndName = fileTotalPath + "JWZTBR"
									+ name;
							FilePathPo newPo = new FilePathPo();
							newPo.setPath(filePathAndName);
							if (!FileDirMonitor.queue.contains(newPo)) {
								FilePathPo po = new FilePathPo();
								UUID uuid = UUID.randomUUID();
								po.setUuid(uuid.toString());
								po.setPath(fileTotalPath + "JWZTBR" + name);
								po.setRepeatNum(0);
								FileDirMonitor.queue.offer(po);
								File filequeueTxt = new File(
										SomsConfigInfo.getHomePath()
												+ "/filequeueTask/"
												+ uuid.toString() + ".txt");
								filequeueTxt.getParentFile().mkdir();

								filequeueTxt.createNewFile();
								FileWriter fileWriter = new FileWriter(
										filequeueTxt);
								fileWriter.write(po.getPath());
								fileWriter.close();

								print(" fileModified  fileTotalPath== "
										+ fileTotalPath);
							}
						}
					}

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			t = file.lastModified();
		}*/
	}

	/**
	 * 该方法监听文件删除事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被删除文件名
	 */
	public void fileDeleted(int wd, String rootPath, String name) {
	}

	/**
	 * 改方法监听文件创建事件
	 * 
	 * @param wd
	 *            被监听目录ID
	 * @param rootPath
	 *            被监听目录
	 * @param name
	 *            被创建文件名
	 */
	public void fileCreated(int wd, String rootPath, String name) {
		String allow_vod_format = SomsConfigInfo.get("allow_vod_format");
		String fileTotalPath = rootPath + name;
		String fileName = fileTotalPath.substring(
				fileTotalPath.lastIndexOf("\\") + 1, fileTotalPath.length());
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1,
				fileName.length());
		// 发现新文件 执行 文件操作流程
		// 加入处理
		if (new File(fileTotalPath).isFile()) {
			if (allow_vod_format.indexOf(fileExt + "|") != -1) {
				if (fileName.indexOf("_seq_") == -1) {
					FilePathPo po = new FilePathPo();
					UUID uuid = UUID.randomUUID();
					po.setUuid(uuid.toString());
					po.setPath(fileTotalPath + "JWZTBR" + name);
					po.setRepeatNum(0);
					FileDirMonitor.queue.offer(po);
					File filequeueTxt = new File(SomsConfigInfo.getHomePath()
							+ "/filequeueTask/" + uuid.toString() + ".txt");
					filequeueTxt.getParentFile().mkdir();
					try {
						filequeueTxt.createNewFile();
						FileWriter fileWriter = new FileWriter(filequeueTxt);
						fileWriter.write(po.getPath());
						fileWriter.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					print(" fileCreated  fileTotalPath== " + fileTotalPath);
					Logger.log(Logger.getCurTime2()+ " fileCreated  fileTotalPath== " + fileTotalPath, 3);
				}
			}
		}
	}

	/**
	 * 错误打印
	 * 
	 * @param msg
	 *            错误信息
	 */
	void print(String msg) {
		System.err.println(" " + msg);
	}
}
