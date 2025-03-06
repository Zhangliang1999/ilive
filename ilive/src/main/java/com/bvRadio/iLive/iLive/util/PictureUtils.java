package com.bvRadio.iLive.iLive.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 图片处理工具
 * 
 * @author administrator
 */
public enum PictureUtils {

	INSTANCE;

	static Logger log = LoggerFactory.getLogger(PictureUtils.class);

	/**
	 * 横图
	 */
	private static final int Landscape_Pic = 0;

	/**
	 * 竖图
	 */
	private static final int Vertical_Pic = 1;

	/**
	 * 获取图片宽高
	 * 
	 * @return
	 */
	public Map<String, Integer> getPicWH(File picFile) {
		if (picFile.exists()) {
			try {
				BufferedImage Bi = ImageIO.read(picFile);
				Map<String, Integer> map = new HashMap<>();
				int width = Bi.getWidth();
				int height = Bi.getHeight();
				map.put("width", width);
				map.put("height", height);
				return map;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 判断图片是横图还是竖图 0横图 1竖图
	 */
	public int jungePicDirect(Map<String, Integer> map) {
		int width = map.get("width");
		int height = map.get("height");
		if (width >= height) {
			return Landscape_Pic;
		} else {
			return Vertical_Pic;
		}
	}

	/**
	 * 进行裁剪缩略图
	 */
	public File generatorThumb(File orgFile, int givenHeight, int givenWidth) {
		// File orgFile = rotateImage(tFile);
		Map<String, Integer> picWH = getPicWH(orgFile);
		String orgPath = orgFile.getPath();
		String fileExt = orgPath.substring(orgPath.lastIndexOf("."));
		String fileName = System.currentTimeMillis() + fileExt;
		File thumbFile = new File(orgFile.getParentFile(), fileName);
		File cutFile = null;
		if (picWH != null) {
			int jungePicDirect = jungePicDirect(picWH);
			int width = picWH.get("width");
			int height = picWH.get("height");
			if (width > givenHeight || height > givenHeight) {
				if (jungePicDirect == Landscape_Pic) {
					try {
						Thumbnails.of(orgFile).size(Math.round((width * givenHeight) / (float) height), givenHeight).outputFormat("png").// 缩放大小
								toFile(thumbFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String path = orgFile.getAbsolutePath();
					String pathPrefix = path.substring(0, path.lastIndexOf("."));
					String pathSuffix = path.substring(path.lastIndexOf("."));
					pathPrefix = pathPrefix + "_thumb";
					String thumbPath = pathPrefix + pathSuffix;
					cutFile = new File(thumbPath);
					try {
						String absolutePath = thumbFile.getAbsolutePath();
						ImgUtils.cutPic(absolutePath, thumbPath, givenWidth, givenHeight);
						return cutFile;
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (jungePicDirect == Vertical_Pic) {
					try {
						int thumbWidth = givenWidth;
						int thumbHeight = Math.round((height * givenWidth) / (float) width);
						Thumbnails.of(orgFile).size(thumbWidth, thumbHeight). // 缩放大小
								toFile(thumbFile);
						String path = orgFile.getAbsolutePath();
						String pathPrefix = path.substring(0, path.lastIndexOf(".") + 1);
						String pathSuffix = path.substring(path.lastIndexOf("."));
						pathPrefix = pathPrefix + "_thumb";
						String thumbPath = pathPrefix + pathSuffix;
						cutFile = new File(thumbPath);
						try {
							ImgUtils.cutPic(thumbFile.getPath(), thumbPath, givenWidth, givenHeight);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return cutFile;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return orgFile;
	}

	public static void main(String[] args) {
		try {
			// int deskHeight = 0;// 缩略图高
			// int deskWidth = 0;// 缩略图宽
			// int srcHeight = picWH.get("height");
			// int srcWidth = picWH.get("width");
			// double srcScale = (double) srcHeight / srcWidth;
			// File generatorThumb = PictureUtils.INSTANCE
			// .generatorThumb(new
			// File("Z:\\aaa\\testpic\\1_lovesummerforever.jpg"), 300, 300);
			Thumbnails.of("Z:\\aaa\\testpic\\1_lovesummerforever.jpg").size(600, 600). // 缩放大小
					toFile("Z:\\aaa\\testpic\\1_lovesummerforever11111.jpg");
			// // System.out.println(generatorThumb.getAbsolutePath());
			// Thumbnails
			// .of("X:\\Administrator\\workspace5\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\ilive\\temp\\1525421235653.jpg")
			// .sourceRegion(Positions.CENTER, 320,
			// 180).scale(0.8).outputQuality(0.8)
			// .toFile("X:\\Administrator\\workspace5\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\ilive\\temp\\123.jpg");
			// Thumbnails.of("Z:\\aaa\\testpic\\20161223204711048_ktweidt_1.jpg").size(400,
			// 402)
			// .toFile(new
			// File("Z:\\aaa\\testpic\\20161223204711048_ktweidt_2.jpg"));//
			// 变为400*300,遵循原图比例缩或放到400*某个高度
			// ImgUtils.cutPic("Z:\\aaa\\testpic\\20161223204711048_ktweidt_2.jpg",
			// "Z:\\aaa\\testpic\\20161223204711048_ktweidt_2_cut.jpg", 400,
			// 300);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static File rotateImage(File imgFile) {
		log.info("开始旋转图片。");
		File newImageFile = null;
		if (null != imgFile && imgFile.exists() && imgFile.isFile()) {
			String fileExt = FileUtils.getFileExt(imgFile.getName());
			String newFileName = System.currentTimeMillis() + "_rotate." + fileExt;
			String tempPath = imgFile.getParent();
			try {
				Integer orientation = getOrientation(imgFile);
				BufferedImage oldImg = (BufferedImage) ImageIO.read(imgFile);
				int width = oldImg.getWidth();
				int height = oldImg.getHeight();
				double xRot = width / 2.0;
				double yRot = height / 2.0;
				if (null != orientation && orientation == 3) {
					BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
					Graphics2D g2d = newImg.createGraphics();
					AffineTransform origXform = g2d.getTransform();
					AffineTransform newXform = (AffineTransform) (origXform.clone());
					newXform.rotate(Math.toRadians(180.0), xRot, yRot);
					g2d.setTransform(newXform);
					g2d.drawImage(oldImg, 0, 0, null);
					newImageFile = new File(tempPath + "/" + newFileName);
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(newImageFile);
						ImageIO.write(newImg, "JPG", out);
					} finally {
						if (null != out) {
							out.close();
						}
					}
				} else if (null != orientation && orientation == 6) {
					BufferedImage newImg = new BufferedImage(height, width, BufferedImage.TYPE_INT_BGR);
					Graphics2D g2d = newImg.createGraphics();
					AffineTransform origXform = g2d.getTransform();
					AffineTransform newXform = (AffineTransform) (origXform.clone());
					newXform.rotate(Math.toRadians(90.0), yRot, yRot);
					g2d.setTransform(newXform);
					g2d.drawImage(oldImg, 0, 0, null);
					newImageFile = new File(tempPath + "/" + newFileName);
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(newImageFile);
						ImageIO.write(newImg, "JPG", out);
					} finally {
						if (null != out) {
							out.close();
						}
					}
				} else {
					BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
					Graphics2D g2d = newImg.createGraphics();
					g2d.drawImage(oldImg, 0, 0, null);
					newImageFile = new File(tempPath + "/" + newFileName);
					FileOutputStream out = null;
					try {
						out = new FileOutputStream(newImageFile);
						ImageIO.write(newImg, "JPG", out);
					} finally {
						if (null != out) {
							out.close();
						}
					}
				}
				log.info("旋转图片成功。newImageFile={}", newImageFile);
			} catch (Exception e) {
				log.error("保存旋转后的图片出错。", e);
			}
		}
		return newImageFile;
	}

	public static Integer getOrientation(File imgFile) {
		Integer orientation = null;
		try {
			log.info("开始获取图片EXIF中的拍摄方向信息。imgFile.getAbsolutePath() = {}", imgFile.getAbsolutePath());
			Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
			ExifIFD0Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			if (null != directory && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (Exception e) {
			log.error("获取图片EXIF中的拍摄方向信息出错。", e);
		}
		log.info("获取图片EXIF中的拍摄方向信息成功，orientation = {}", orientation);
		return orientation;
	}

}
