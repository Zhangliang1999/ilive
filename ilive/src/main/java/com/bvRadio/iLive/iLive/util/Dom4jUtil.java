package com.bvRadio.iLive.iLive.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



public class Dom4jUtil {
	static List<String> listNode = new ArrayList<String>();
	static List<String> listNodeStartcmd = new ArrayList<String>();
	static List<String> listNodeBackprogramscmd = new ArrayList<String>();
	public static Map<String,String> fileNameMap= new HashMap<String,String>();
	public static Map<String,Integer> downedfileNameMap= new HashMap<String,Integer>();

	public static boolean checkXml(String arr[], String strPath) {
		// System.out.println("strPath==" + strPath);
		Document dom = getDocument(strPath);
		Element root = dom.getRootElement();

		boolean falg = true;
		for (String str : arr) {
			// System.out.println(str + "标签");
			Element element = root.element(str);
			if (element == null) {
				falg = false;
				// System.out.println("缺少" + str + "标签");
			}
		}
		return falg;
	}
	
	public static String getSystemConf(String name) {
		Dom4jUtil dom4jUtil = new Dom4jUtil();
		String strPath;
//		strPath = dom4jUtil.getClass().getResource("/").getPath().substring(1);
		Document dom = dom4jUtil.getDocument(dom4jUtil.getClass().getResource("/").getPath().substring(1)+"/fileroad.xml");
		Element root = dom.getRootElement();
		Element road = root.element(name);
		strPath = road.getText();
		return strPath;
	}
	public static String cloneProgram(String pathOld, String ext,String strTVShow) {
		
		if(fileNameMap.containsKey(pathOld)){
			return fileNameMap.get(pathOld);
		}else{
			String filename = UUID.randomUUID().toString()+ext;
			//// System.out.println(filename);
			String pathNew = "";
			try {
				String pathDir = getSystemConf("road") + strTVShow;
				File fileDir = new File(pathDir);
				if(!fileDir.isDirectory()){
					fileDir.mkdirs();
				}
				pathNew = pathDir + filename;
				fileNameMap.put(pathOld, pathNew);
				/*ThreadOut threadOut = new ThreadOut(pathOld,pathNew);
				new Thread(threadOut).start();*/
			} catch (Exception e) {
				e.printStackTrace();
				//Logger.log(e);
			}
			return "file://" + pathNew;
		}
		
	}


	/**
	 * @author hcl
	 * 
	 * 删除单个文件
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				// System.out.println("删除单个文件" + fileName + "成功");
				//Logger.log("删除单个文件" + fileName + "成功");
				return true;
			} else {
				// System.out.println("删除单个文件" + fileName + "失败,文件有可能已经被删除或者不是文件");
				//Logger.log("删除单个文件" + fileName + "失败,文件有可能已经被删除或者不是文件");
				return false;
			}
		} else {
			//Logger.log("删除单个文件失败：" + fileName + "文件已近被删除");
			// System.out.println("删除单个文件失败：" + fileName + "文件已近被删除");
			return false;
		}
	}
	
	/**
	 * @author hcl
	 * 
	 * 删除所有path下文件
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		
		File file = new File(path);
		if (!file.exists()) {
			// System.out.println(path+"目录不存在");
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
		}
		return flag;

	}

	/**
	 * @author hcl
	 * 
	 *         检测码流是否存在
	 */
	public static boolean getNodeText(Element element) {
		boolean flg = true;
		String strOuputRTMP = element.element("OuputRTMP").getText();
		String strOuputUDP = element.element("OuputUDP").getText();
		String strOuputHLS = element.element("OuputHLS").getText();
		String strText = strOuputRTMP + strOuputUDP + strOuputHLS;
		if ("".equals(strText)) {
			flg = false;
		}
		return flg;
	}

	/**
	 * @author hcl
	 * 
	 *         制造盖播单
	 */
	public static void createOverrideprgramscmd(String strFile) {
		File file = new File(strFile);
		if(file.exists()){
			file.delete();
		}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String strXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"
					+ "<root>" + "<ChannelID></ChannelID>"
					+ "<PlayType></PlayType>" + "<Programs>" + "<Program>"
					+ "<ProgramID></ProgramID>" + "<ProgramName></ProgramName>"
					+ "<ProgramPath></ProgramPath>" + "<StartTime></StartTime>"
					+ "<RunDuration></RunDuration>"
					+ "<StopAction></StopAction>" + "</Program>" + "<Program>"
					+ "<ProgramID></ProgramID>" + "<ProgramName></ProgramName>"
					+ "<ProgramPath></ProgramPath>" + "<StartTime></StartTime>"
					+ "<RunDuration></RunDuration>"
					+ "<StopAction></StopAction>" + "</Program>"
					+ "</Programs>" + "</root>";
			try {
				FileWriter fw = new FileWriter(file, false);
				fw.write(strXML);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * @author hcl
	 * 
	 *         制造点播单
	 */
	public static void createBackprogramscmd(String strFile) {
		File file = new File(strFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String strXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"
					+ "<root>" + "<ChannelID></ChannelID>"
					+ "<PlayType></PlayType>" + "<BackupPrograms>"
					+ "<BackupProgram>" + "<ProgramID></ProgramID>"
					+ "<ProgramName></ProgramName>"
					+ "<ProgramPath></ProgramPath>" + "<StartTime></StartTime>"
					+ "<RunDuration></RunDuration>"
					+ "<StopAction></StopAction>" + "</BackupProgram>"
					+ "<BackupProgram>" + "<ProgramID></ProgramID>"
					+ "<ProgramName></ProgramName>"
					+ "<ProgramPath></ProgramPath>" + "<StartTime></StartTime>"
					+ "<RunDuration></RunDuration>"
					+ "<StopAction></StopAction>" + "</BackupProgram>"
					+ "</BackupPrograms>" + "</root>";
			try {
				FileWriter fw = new FileWriter(file, false);
				// fw = new FileWriter(file, false);
				fw.write(strXML);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author hcl
	 * 
	 *         预下载垫播单
	 */


	public static boolean checkOutputcmdXml(String arr[], String strPath) {
		Document dom = getDocument(strPath);
		Element root = dom.getRootElement();
		boolean flagOut = true;
		Dom4jUtil util = new Dom4jUtil();
		util.getNodes(root);
		for (String str : arr) {
			boolean flag = false;
			for (String strNode : listNode) {
				if (str.equals(strNode)) {
					flag = true;
					// break;
				}
			}
			flagOut = flagOut && flag;
			// if(!listNode.contains(str)){
			// falg = false;
			// }

		}
		return flagOut;
	}

	public static boolean checkStartcmdXml(String arr[], String strPath) {
		Document dom = getDocument(strPath);
		Element root = dom.getRootElement();
		// System.out.println("==============");
		// System.out.println(root.asXML());
		boolean flagOut = true;
		Dom4jUtil util = new Dom4jUtil();
		util.getNodesStartcmd(root);
		for (String str : arr) {
			boolean flag = false;
			for (String strNode : listNodeStartcmd) {
				if (str.equals(strNode)) {
					flag = true;
					break;
				}
			}
			flagOut = flagOut && flag;
			// if(!listNode.contains(str)){
			// falg = false;
			// }

		}
		return flagOut;
	}

	public void getNodesStartcmd(Element node) {
		List<Element> listAttr = node.elements();// 当前节点的所有属性的list

		for (Element attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			listNodeStartcmd.add(name);
			// System.out.println("属性名称：" + name);
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodesStartcmd(e);// 递归
		}
	}

	public void getNodes(Element node) {
		List<Element> listAttr = node.elements();// 当前节点的所有属性的list

		for (Element attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			listNode.add(name);
			// System.out.println("属性名称：" + name);
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getNodes(e);// 递归
		}
	}

	public static boolean checkBackprogramscmdNodes(String arr[], Element root) {

		// System.out.println("==============");
		// System.out.println(root.asXML());
		boolean flagOut = true;
		Dom4jUtil util = new Dom4jUtil();
		util.getBackprogramscmdNodes(root);
		for (String str : arr) {
			boolean flag = false;
			for (String strNode : listNodeBackprogramscmd) {
				if (str.equals(strNode)) {
					flag = true;
					break;
				}
			}
			flagOut = flagOut && flag;
		}
		listNodeBackprogramscmd = null;
		return flagOut;
	}

	/**
	 * @author hcl
	 * 
	 *         通用，检查所有xml是否正确
	 */
	public boolean commonCheckXml(String[] arr, Element root) {
		boolean flagOut = true;
		Dom4jUtil util = new Dom4jUtil();
		List<String> list = util.commonGetDocument(root);
		// 检测XML是不是少了节点
		for (String str : arr) {
			boolean flag = false;
			for (String strNode : list) {
				if (str.equals(strNode)) {
					flag = true;
					break;
				}
			}
			flagOut = flagOut && flag;
		}

		return flagOut;
	}

	/**
	 * @author hcl
	 * 
	 *         通用得到所有节点
	 */
	public List<String> commonGetDocument(Element node) {
		List<String> list = new ArrayList<String>();
		List<Element> listAttr = node.elements();
		for (Element attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			list.add(name);
			// System.out.println("属性名称：" + name);
		}
		for (Element e : listAttr) {
			list.addAll(this.commonGetDocument(e));
		}
		return list;
	}

	/**
	 * @author hcl
	 * 
	 *         清空xml
	 */
	public void delXMLAll(String strFile) {
		Document dom = getDocument(strFile);
		Element root = dom.getRootElement();
		root.clearContent();
		xmlWriters(strFile, dom);
	}

	/**
	 * @author hcl
	 * 
	 *         得到点播单节点
	 */
	public void getBackprogramscmdNodes(Element node) {
		List<Element> listAttr = node.elements();// 当前节点的所有属性的list

		for (Element attr : listAttr) {// 遍历当前节点的所有属性
			String name = attr.getName();// 属性名称
			listNodeBackprogramscmd.add(name);
			// System.out.println("属性名称：" + name);
		}

		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		for (Element e : listElement) {// 遍历所有一级子节点
			this.getBackprogramscmdNodes(e);// 递归
		}
	}

	/**
	 * @author hcl
	 * 
	 *         检查xml文件是否正确
	 * 
	 * */
	private boolean decideXml(String str, String[] arr) {
		boolean flg = true;
		for (String arrele : arr) {
			if (str.contains(arrele))
				flg = false;
		}
		return flg;
	}

	/**
	 * @author hcl
	 * 
	 *         得到document
	 */
	public static Document getDocument(String path) {

		try {
			File file = new File(path);
			// 创建解析器
			SAXReader reader = new SAXReader();
			// 得到document
			Document document = reader.read(file.getAbsoluteFile());
			return document;
		} catch (Exception e) {
			e.printStackTrace();
			// e.printStackTrace();
			// System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * @author hcl
	 * 
	 *         回写xml
	 */
	public static void xmlWriters(String path, Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path),
					format);
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author hcl
	 * 
	 *         以GBK编码保存xml
	 */
	public static void saveXmlAsGBK(String path, Document document) {
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GBK");
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path),
					format);
			xmlWriter.write(document);
			xmlWriter.flush();
			xmlWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author hcl
	 * 
	 *         读取本地xml并转化为String
	 */
	public static String getXmlText(String strFile) {
		File file = new File(strFile);

		SAXReader saxReader = new SAXReader();
		Document dom;
		String str = "";
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			dom = saxReader.read(file);
			str = dom.asXML();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}


	public static void createPrgramscmdXML(String strFile) {
		File file = new File(strFile);
		if(file.exists()){
			file.delete();
			
		}
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String strXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"
					+ "<root><ChannelID></ChannelID>"
					+ "<Programs></Programs></root>";
			try {
				FileWriter fw = new FileWriter(file, false);
				fw.write(strXML);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
	
	public static void createPrgramscmdXML2(String strFile) {
		File file = new File(strFile);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String strXML = "<?xml version=\"1.0\" encoding=\"GBK\"?>"
					+ "<root> " + " <ChannelID></ChannelID>  "
					+ "  <FirstProgram> " + " <ProgramID></ProgramID>  "
					+ " <ProgramName></ProgramName> "
					+ "<ProgramPath></ProgramPath>  "
					+ " <StartTime></StartTime>  "
					+ " <RunDuration></RunDuration>"
					+ " <StopAction></StopAction> " + "  </FirstProgram>  "
					+ "  <NextProgram> " + "   <ProgramID></ProgramID>  "
					+ "  <ProgramName></ProgramName> "
					+ "  <ProgramPath></ProgramPath>  "
					+ "  <StartTime></StartTime>  "
					+ "<RunDuration></RunDuration>" + "  <StopAction/> "
					+ " </NextProgram> " + "	</root>";
			try {
				FileWriter fw = new FileWriter(file, false);
				// fw = new FileWriter(file, false);
				fw.write(strXML);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * @author hcl
	 * 
	 *         打造xml文件
	 */
	public static void createXmlFile(String strPath, String strXml) {
		File file = new File(strPath);
		FileWriter fw = null;
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			Document document = DocumentHelper.parseText(strXml);
			// document.save(strPath);
			// OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			// outputFormat.setLineSeparator("\r\n");
			// Writer writer = new FileWriter(strPath);
			// XMLWriter outPut = new XMLWriter(writer, outputFormat);
			// outPut.write(document);
			// outPut.close();

			fw = new FileWriter(file, false);
			fw.write(strXml);
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
 * yyyy-MM-dd
 * @return
 */
	public static String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return sdf.format(date);
	}
}
