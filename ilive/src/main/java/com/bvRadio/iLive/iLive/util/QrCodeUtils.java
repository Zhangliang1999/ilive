package com.bvRadio.iLive.iLive.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import com.swetake.util.Qrcode;

/**
 * @author ysf
 */
public class QrCodeUtils {
	public static boolean createQRCodeWithHttpLogo(String content, String imgPath, String logoUrl) {
		try {
			URL url = new URL(logoUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("content-type", "text/html");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("GET");
			InputStream inputStream = conn.getInputStream();
			return createQRCode(content, imgPath, ImageIO.createImageInputStream(inputStream));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createQRCodeWithFileLogo(String content, String imgPath, String logoPath) {
		try {
			File logoFile = new File(logoPath);
			return createQRCode(content, imgPath, ImageIO.createImageInputStream(logoFile));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createQRCode(String content, String imgPath, ImageInputStream logoInputStream) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			qrcodeHandler.setQrcodeErrorCorrect('M');// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeEncodeMode('B');// N代表数字,A代表字符a-Z,B代表其他字符
			qrcodeHandler.setQrcodeVersion(9);// 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
			byte[] contentBytes = content.getBytes("utf-8");// 设置编码格式为UTF-8
			BufferedImage bufImg = new BufferedImage(168, 168, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			gs.setBackground(Color.white); // 设置背景色为白色
			gs.clearRect(0, 0, 168, 168);
			gs.setColor(Color.BLACK); // 设定图像颜色 为黑色
			int pixoff = 4; // 设置偏移量 不设置可能导致解析出错
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 150) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				return false;
			}
			Image img = ImageIO.read(logoInputStream); // 实例化一个Image对象。
			gs.drawImage(img, 60, 60, 45, 45, null); // 60,60是距离gs两个边的距离，45,45是中间logo的大小
			gs.dispose();
			bufImg.flush();
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "png", imgFile);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
