package com.hanweb.jmp.global.service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 验证码生成工具类
 * @author WangFei
 */
public class ValidCodeService {
	
	/**
	 * 验证码宽度
	 */
	public static int width = 60;
	
	/**
	 * 验证码高度
	 */
	public static int height = 20;

	/**
	 * 验证码背景颜色colorfcbg 应当小于colorbcbg
	 */
	public static int colorfcbg = 200;

	/**
	 * 验证码背景颜色colorfcbg 应当小于colorbcbg
	 */
	public static int colorbcbg = 250;

	/**
	 * 验证码背景干扰线颜色colorfcline 应当小于colorbcline
	 */
	public static int colorfcline = 160;

	/**
	 * 验证码背景干扰线颜色colorfcline 应当小于colorbcline
	 */
	public static int colorbcline = 200;

	/**
	 * 验证码颜色colorfccode 应当小于colorbccode
	 */
	public static int colorfccode = 20;

	/**
	 * 验证码颜色colorfccode 应当小于colorbccode
	 */
	public static int colorbccode = 170;

	/**
	 * 生成在指定范围内的颜色
	 * @param fc
	 *            范围fc color值 小于255
	 * @param bc
	 *            范围bc color值 小于255
	 * @return Color
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc < 0){
			fc = 0;
		}
		if (bc < 0){
			bc = 1;
		}
		if (fc > 255){
			fc = 255;
		}
		if (bc > 255){
			bc = 255;
		}
		if (bc == fc){
			bc += 10;
		}
		int temp = 0;
		if (bc < fc) {
			temp = bc;
			bc = fc;
			fc = temp;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 生成图片方法
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return boolean
	 * @throws Exception 异常
	 */
	public static boolean getImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.reset();
		response.setContentType("image/jpeg");
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 在内存中创建图象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics img = image.getGraphics();
		// 生成随机类
		Random random = new Random();

		// 设定背景色
		img.setColor(getRandColor(colorfcbg, colorbcbg));
		img.fillRect(0, 0, width, height);

		// 设定字体
		img.setFont(new Font("Times New Roman", Font.PLAIN, 18));

		// 画边框
		// g.setColor(new Color());
		// g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		img.setColor(getRandColor(colorfcline, colorbcline));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(height);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			img.drawLine(x, y, x + xl, y + yl);
		}

		// 取随机产生的认证码(4位数字)
		String codeValue = "";
		for (int i = 0; i < 4; i++) {
			// String rand = String.valueOf(random.nextInt(10));
			String rand = getRandomChar();
			codeValue = codeValue.concat(rand);
			// 随机字体
			img.setFont(getRandomFont());
			// 将认证码显示到图象中
			img.setColor(getRandColor(colorfccode, colorbccode));
			img.drawString(rand, 13 * i + 6, 16);
		}
		request.getSession().setAttribute("codeValue", codeValue);
		// 图象生效
		img.dispose();
		// 输出图象到页面
		return ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 * 随机生成字符，含大写、小写、数字
	 * @return String
	 */
	private static String getRandomChar() {
		int index = (int) Math.round(Math.random() * 2);
		String randChar = "";
		switch (index) {
		case 0:// 大写字符
			randChar = String.valueOf((char) Math.round(Math.random() * 25 + 65));
			break;
		case 1:// 小写字符
			randChar = String.valueOf((char) Math.round(Math.random() * 25 + 97));
			break;
		default:// 数字
			randChar = String.valueOf(Math.round(Math.random() * 9));
			break;
		}
		return randChar;
	}

	/**
	 * 随机生成字体、文字大小
	 * @return Font
	 */
	private static Font getRandomFont() {
		String[] fonts = { "Georgia", "Verdana", "Arial", "Tahoma", 
				"Time News Roman", "Courier New", "Arial Black", "Quantzite" };
		int fontIndex = (int) Math.round(Math.random() * (fonts.length - 1));
		int fontSize = (int) Math.round(Math.random() * 4 + 16);
		return new Font(fonts[fontIndex], Font.PLAIN, fontSize);
	}
	
}