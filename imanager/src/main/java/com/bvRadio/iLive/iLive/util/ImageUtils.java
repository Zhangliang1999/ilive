package com.bvRadio.iLive.iLive.util;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

public class ImageUtils {

	private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

	public static File rotateImage(File imgFile) {
		log.info("开始旋转图片。");
		File newImageFile = null;
		if (null != imgFile && imgFile.exists() && imgFile.isFile()) {
			String fileExt = FileUtils.getFileExt(imgFile.getName());
			String newFileName = System.currentTimeMillis() + "." + fileExt;
			Integer orientation = getOrientation(imgFile);
			String tempPath = imgFile.getParent();
			try {
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
				log.info("旋转图片成功。");
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
