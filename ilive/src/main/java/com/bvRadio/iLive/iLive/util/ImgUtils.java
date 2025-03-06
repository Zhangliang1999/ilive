package com.bvRadio.iLive.iLive.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

public class ImgUtils {

	/**
	 * 缩放图片方法
	 * 
	 * @param srcImageFile
	 *            要缩放的图片路径
	 * @param result
	 *            缩放后的图片路径
	 * @param height
	 *            目标高度像素
	 * @param width
	 *            目标宽度像素
	 * @param bb
	 *            是否补白
	 */
	public final static void scale(String srcImageFile, String result, int height, int width, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);// bi.SCALE_SMOOTH
																				// 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
			// 计算比例
			// 高度比给定比例高  宽度比给定比例宽
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				double ratioHeight = (new Integer(height)).doubleValue() / bi.getHeight();
				double ratioWhidth = (new Integer(width)).doubleValue() / bi.getWidth();
				if (ratioHeight > ratioWhidth) {
					ratio = ratioHeight;
				} else {
					ratio = ratioWhidth;
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform// 仿射转换
						.getScaleInstance(ratio, ratio), null);// 返回表示剪切变换的变换
				itemp = op.filter(bi, null);// 转换源 BufferedImage 并将结果存储在目标
											// BufferedImage 中。
			}
			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 构造一个类型为预定义图像类型之一的
																									// BufferedImage。
				Graphics2D g = image.createGraphics();// 创建一个
														// Graphics2D，可以将它绘制到此
														// BufferedImage 中。
				g.setColor(Color.white);// 控制颜色
				g.fillRect(0, 0, width, height);// 使用 Graphics2D 上下文的设置，填充 Shape
												// 的内部区域。
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, "JPEG", new File(result)); // 输出压缩图片
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 缩放图片方法
	 * 
	 * @param srcImageFile
	 *            要缩放的图片路径
	 * @param result
	 *            缩放后的图片路径
	 * @param height
	 *            目标高度像素
	 * @param width
	 *            目标宽度像素
	 * @param bb
	 *            是否补白
	 */
	public final static void scaleNew(String srcImageFile, String result, int height, int width, boolean bb) {
		try {
			double ratio = 0.0; // 缩放比例
			File f = new File(srcImageFile);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, bi.SCALE_SMOOTH);// bi.SCALE_SMOOTH
																				// 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。
			
			if(bi.getHeight()<height && bi.getWidth()<width) {
				return;
			}
			
			// 计算比例
			// 高度比给定比例高  宽度比给定比例宽
			if ((bi.getHeight() > height) && (bi.getWidth() > width)) {
				double ratioHeight = (new Integer(height)).doubleValue() / bi.getHeight();
				double ratioWhidth = (new Integer(width)).doubleValue() / bi.getWidth();
				if (ratioHeight > ratioWhidth) {
					ratio = ratioHeight;
				} else {
					ratio = ratioWhidth;
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform// 仿射转换
						.getScaleInstance(ratio, ratio), null);// 返回表示剪切变换的变换
				itemp = op.filter(bi, null);// 转换源 BufferedImage 并将结果存储在目标
											// BufferedImage 中。
			}
			
			/**
			 * 
			 */
			if((bi.getHeight() > height) && (bi.getWidth() < width)) {
				
				
				
			}
			
			/**
			 * 
			 */
			if((bi.getHeight() < height) && (bi.getWidth() > width)) {
				
				
			}
			
			
			
			if (bb) {// 补白
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);// 构造一个类型为预定义图像类型之一的
																									// BufferedImage。
				Graphics2D g = image.createGraphics();// 创建一个
														// Graphics2D，可以将它绘制到此
														// BufferedImage 中。
				g.setColor(Color.white);// 控制颜色
				g.fillRect(0, 0, width, height);// 使用 Graphics2D 上下文的设置，填充 Shape
												// 的内部区域。
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, "JPEG", new File(result)); // 输出压缩图片
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁剪图片方法
	 * 
	 * @param bufferedImage
	 *            图像源
	 * @param startX
	 *            裁剪开始x坐标
	 * @param startY
	 *            裁剪开始y坐标
	 * @param endX
	 *            裁剪结束x坐标
	 * @param endY
	 *            裁剪结束y坐标
	 * @return
	 */
	public static BufferedImage cropImage(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		if (startX == -1) {
			startX = 0;
		}
		if (startY == -1) {
			startY = 0;
		}
		if (endX == -1) {
			endX = width - 1;
		}
		if (endY == -1) {
			endY = height - 1;
		}
		BufferedImage result = new BufferedImage(endX - startX, endY - startY, 4);
		for (int x = startX; x < endX; ++x) {
			for (int y = startY; y < endY; ++y) {
				int rgb = bufferedImage.getRGB(x, y);
				result.setRGB(x - startX, y - startY, rgb);
			}
		}
		return result;
	}
	
	public static void cutPic(String orgPath,String destPath,int givenWidth,int givenHeight) throws IOException
	{
		File newfile = new File(orgPath);
		BufferedImage bufferedimage = ImageIO.read(newfile);
		int width = bufferedimage.getWidth();
		int height = bufferedimage.getHeight();
		// 目标将图片裁剪成 宽240，高160
		if (width > givenWidth) {
			/* 开始x坐标 开始y坐标 结束x坐标 结束y坐标 */
			bufferedimage = ImgUtils.cropImage(bufferedimage, (int) ((width - givenWidth) / 2), 0,
					(int) (width - (width - givenWidth) / 2), (int) (height));
			if (height > givenHeight) {
				bufferedimage = ImgUtils.cropImage(bufferedimage, 0, (int) ((height - givenHeight) / 2),givenWidth,
						(int) (height - (height - givenHeight) / 2));
			}
		} else {
			if (height > givenHeight) {
				bufferedimage = ImgUtils.cropImage(bufferedimage, 0, (int) ((height - givenHeight) / 2), (int) (width),
						(int) (height - (height - givenHeight) / 2));
			}
		}
		ImageIO.write(bufferedimage, "jpg", new File(destPath)); // 输出裁剪图片
		
	}
	public static void main(String[] args) throws IOException {
		
//		int widthW = 480;
//		int heightH = 270;
//		String path = "X:\\Administrator\\workspace5\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\ilive\\temp\\20161223204711048.jpg"; // 输入图片 测试要在C盘放一张图片1.jpg
//		String destPath = "X:\\Administrator\\workspace5\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\ilive\\temp\\1525421991326_thumb11.jpg"; // 输入图片 测试要在C盘放一张图片1.jpg
//		String cutPath = "X:\\Administrator\\workspace5\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\ilive\\temp\\1525421991326_thumb11_cut.jpg"; // 输入图片 测试要在C盘放一张图片1.jpg
//		ImgUtils.scale(path, destPath, heightH, widthW, true);// 等比例缩放

	}

	
	
//	 public static void saveMinPhoto(String srcURL, String deskURL, double comBase,
//	            double scale) throws Exception {
//	        File srcFile = new java.io.File(srcURL);
//	        Image src = ImageIO.read(srcFile);
//	        int srcHeight = src.getHeight(null);
//	        int srcWidth = src.getWidth(null);
//	        int deskHeight = 0;// 缩略图高
//	        int deskWidth = 0;// 缩略图宽
//	        double srcScale = (double) srcHeight / srcWidth;
//	        /**缩略图宽高算法*/
//	        if ((double) srcHeight > comBase || (double) srcWidth > comBase) {
//	            if (srcScale >= scale || 1 / srcScale > scale) {
//	                if (srcScale >= scale) {
//	                    deskHeight = (int) comBase;
//	                    deskWidth = srcWidth * deskHeight / srcHeight;
//	                } else {
//	                    deskWidth = (int) comBase;
//	                    deskHeight = srcHeight * deskWidth / srcWidth;
//	                }
//	            } else {
//	                if ((double) srcHeight > comBase) {
//	                    deskHeight = (int) comBase;
//	                    deskWidth = srcWidth * deskHeight / srcHeight;
//	                } else {
//	                    deskWidth = (int) comBase;
//	                    deskHeight = srcHeight * deskWidth / srcWidth;
//	                }
//	            }
//	        } else {
//	            deskHeight = srcHeight;
//	            deskWidth = srcWidth;
//	        }
//	        BufferedImage tag = new BufferedImage(deskWidth, deskHeight, BufferedImage.TYPE_3BYTE_BGR);
//	        tag.getGraphics().drawImage(src, 0, 0, deskWidth, deskHeight, null); //绘制缩小后的图
//	        FileOutputStream deskImage = new FileOutputStream(deskURL); //输出到文件流
//	        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(deskImage);
//	        encoder.encode(tag); //近JPEG编码
//	        deskImage.close();
//	    }

	 
	 /**
	  * 重新设置图片大小
	  * @param srcImage
	  * @return
	  */
	 private void resize(String fromPic,String topPic) {
		 try {
			Thumbnails.of(fromPic).size(400,500).toFile(new File(topPic));
		} catch (IOException e) {
			e.printStackTrace();
		}
		 //变为400*300,遵循原图比例缩或放到400*某个高度
	 }

}
