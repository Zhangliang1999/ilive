package com.bvRadio.iLive.iLive.web;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostMan {

	/**
	 * 将一段json传入http链接 请求中
	 * 
	 * @param url
	 * @param method
	 * @param encode
	 * @param ins
	 * @return
	 */
	public static String postJson(String url, String method, String encode,
			String jsonStr) {
		String strRet = null;
		if (url != null) {
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try {
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("content-type",
						"application/json;charset=" + encode);
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				OutputStream outStream = httpTarget.getOutputStream();
				outStream.write(jsonStr.getBytes(encode));
				outStream.flush();
				outStream.close();
				reader = new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(), encode));

				StringBuffer strBuffer = new StringBuffer(1024);
				String line = reader.readLine();
				while (line != null) {
					strBuffer.append(line + "\n");
					line = reader.readLine();
				}
				strRet = strBuffer.toString();
			} catch (Exception ex) {
				System.out.println("downloadURL  " + ex);
				ex.printStackTrace();
				strRet = null;
				throw new RuntimeException(ex.getMessage());
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (httpTarget != null) {
					httpTarget.disconnect();
				}
			}
		}
		return strRet;
	}

	/**
	 * 将一段输入流传入http链接 请求中
	 * 
	 * @param url
	 * @param method
	 * @param encode
	 * @param ins
	 * @return
	 */
	public static String downloadUrlWithinputStream(String url, String method,
			String encode, InputStream ins) {
		String strRet = null;
		if (url != null) {
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try {
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setRequestMethod("POST");
				httpTarget.setUseCaches(false);
				httpTarget.setRequestProperty("Content-Type", "text/xml");
				httpTarget.setDoInput(true);
				httpTarget.setDoOutput(true);
				OutputStream os = httpTarget.getOutputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
				reader = new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(), encode));
				StringBuffer strBuffer = new StringBuffer(1024);
				String line = reader.readLine();
				while (line != null) {
					strBuffer.append(line + "\n");
					line = reader.readLine();
				}
				strRet = strBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
				strRet = null;
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (httpTarget != null) {
					httpTarget.disconnect();
				}
			}
		}
		return strRet;
	}

	public static String downloadUrl(String url, String method, String encode) {
		String strRet = null;
		if (url != null) {
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try {
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setRequestMethod(method);
				httpTarget.setDoInput(true);

				reader = new BufferedReader(new InputStreamReader(httpTarget
						.getInputStream(), encode));

				StringBuffer strBuffer = new StringBuffer(1024);
				String line = reader.readLine();
				while (line != null) {
					strBuffer.append(line + "\n");
					line = reader.readLine();
				}
				strRet = strBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
				strRet = null;
				throw new RuntimeException(ex.getMessage());
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (httpTarget != null) {
					httpTarget.disconnect();
				}
			}
		}
		return strRet;
	}

	public static String downloadUrl(String url, String method, String encode,
			String destPath) {
		String strRet = null;
		if (url != null) {
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try {
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setRequestMethod(method);
				httpTarget.setDoInput(true);
				InputStream inputStream = httpTarget.getInputStream();
				saveImageToDisk(inputStream,destPath);
				// reader=new BufferedReader(new InputStreamReader(,encode));
				//
				// StringBuffer strBuffer=new StringBuffer(1024);
				// String line=reader.readLine();
				// while(line!=null)
				// {
				// strBuffer.append(line+"\n");
				// line=reader.readLine();
				// }
				// strRet=strBuffer.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
				strRet = null;
				throw new RuntimeException(ex.getMessage());
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (httpTarget != null) {
					httpTarget.disconnect();
				}
			}
		}
		return strRet;
	}

	public static void saveImageToDisk(InputStream inputStream, String destPath) {
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(destPath);
			while ((len = inputStream.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * 下载指定URL的内容GET方式请求
	 * 
	 * @param url
	 *            要下载的URL
	 * @return 指定URL的内容，如果失败则返回null
	 */
	public static String downloadUrl(String url)
	{
		String strRet = null;
		if (url != null)
		{
			BufferedReader reader = null;
			HttpURLConnection httpTarget = null;
			try
			{
				URL target = new URL(url);
				httpTarget = (HttpURLConnection) target.openConnection();
				httpTarget.setDoInput(true);
				reader = new BufferedReader(new InputStreamReader(httpTarget.getInputStream(), "UTF-8"));

				StringBuffer strBuffer = new StringBuffer(1024);
				String line = reader.readLine();
				while (line != null)
				{
					strBuffer.append(line + "\n");
					line = reader.readLine();
				}
				strRet = strBuffer.toString();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				strRet = null;
			}
			finally
			{
				if(reader!=null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(httpTarget!=null) {
					httpTarget.disconnect();
				}
			}
		}
		return strRet;
	}
	
	
	public static String readerByInputStream(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuffer strBuffer=new StringBuffer(1024);
			String line=reader.readLine();
			while(line!=null)
			{
				strBuffer.append(line+"\n");
				line=reader.readLine();
			}
			line=strBuffer.toString();
			return line;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(reader!=null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
