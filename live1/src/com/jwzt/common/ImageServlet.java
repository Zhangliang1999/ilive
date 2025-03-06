package com.jwzt.common;

import java.io.IOException;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jwzt.common.AppTools;
/**
 * @author 徐勤
 */
public class ImageServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException ,IOException
	{
		resp.setContentType("image/jpeg");
		
		//在内存中创建图像
		int iWidth = 60,iHeight = 20;
		BufferedImage image = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_RGB);
		
		//获取图形上下文
		Graphics g = image.getGraphics();
		
		//设定背景颜色
		g.setColor(Color.white);
		g.fillRect(0, 0, iWidth, iHeight);
		
		//画边框
		g.setColor(Color.black);
		g.drawRect(0, 0, iWidth-1, iHeight-1);
		
		//取随机产生的认证码(4位数字)
		String rand = AppTools.createRandomNum(4, 1);
		
		//将认证码存入SESSION
		HttpSession session = req.getSession(true);
		
		session.setAttribute("Rand", rand);
		
		//将认证码显示到图像中
		g.setColor(Color.black);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		g.drawString(rand, 8, 15);
		
		//随机产生88个干扰点,使图像中的认证码不易被其它程序探测到
		Random random = new Random();
		for(int iIndex=0; iIndex<80; iIndex++)
		{
			int x=random.nextInt(iWidth);
			int y=random.nextInt(iHeight);
			g.drawLine(x, y, x, y);
		}
		
		//图形生效
		g.dispose();
		
		//输出图像到页面
		ImageIO.write(image, "JPEG", resp.getOutputStream());
	}
}
