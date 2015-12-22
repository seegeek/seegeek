package com.seegeek.cms.utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RandomCode extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int number = 0;
		int max = 0;
		response.setContentType("image/jpeg");
		ImageCode image = new ImageCode();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			BufferedImage img = null;
			if (number != 0) {
				if (max != 0) {
					img = image.creatImage3D(number, max);
				} else {
					img = image.creatImage(number);
				}
			} else {
				img = image.creatImage();
				request.getSession().setAttribute("rand", image.getSRand());

			}
			ImageIO.write(img, "JPEG", response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
		e.printStackTrace();
		}
	}

	class ImageCode {

		public String sRand = "";

		public Color getRandColor(int fc, int bc) {// ��Χ��������ɫ
			Random random = new Random();
			if (fc > 255)
				fc = 255;
			if (bc > 255)
				bc = 255;
			int r = fc + random.nextInt(bc - fc);
			int g = fc + random.nextInt(bc - fc);
			int b = fc + random.nextInt(bc - fc);
			return new Color(r, g, b);
		}

		public BufferedImage creatImage() {
			// define the image 
			int width = 60, height = 20;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			// create  a cavans
			Graphics g = image.getGraphics();
			// random number
			Random random = new Random();
			// color 
			g.setColor(getRandColor(200, 250));
			g.fillRect(0, 0, width, height);
			//font type
			g.setFont(new Font("Times New Roman", Font.PLAIN, 18));
			g.setColor(getRandColor(160, 200));
			for (int i = 0; i < 155; i++) {
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, x + xl, y + yl);
			}
		
			for (int i = 0; i < 4; i++) {
				String rand = String.valueOf(random.nextInt(10));
				sRand += rand;
				g.setColor(new Color(20 + random.nextInt(110), 20 + random
						.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(rand, 13 * i + 6, 16);
			}
			g.dispose();
			return image;
		}

		/**
		 * @return Returns the sRand.
		 */
		public String getSRand() {
			return sRand;
		}

		public BufferedImage creatImage(int number) {
			String num = String.valueOf(number);
			char[] vs = num.toCharArray();

			int width = vs.length * 60 / 4, height = 18;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			Random random = new Random();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Impact", Font.PLAIN, 14));
			for (int i = 0; i < vs.length; i++) {
				String rand = String.valueOf(vs[i]);
				sRand += rand;
				g.setColor(new Color(20 + random.nextInt(110), 20 + random
						.nextInt(110), 20 + random.nextInt(110)));
				g.drawString(rand, 13 * i + 6, 16);

			}
			g.dispose();
			return image;
		}

		public BufferedImage creatImage3D(int number, int max) {
			int width = 60, height = 10;
			int x = number * width / max;
			System.out.println(x);
			BufferedImage image = new BufferedImage(x, height,
					BufferedImage.TYPE_INT_RGB);

			Graphics g = image.getGraphics();
			// g.fillRect(0, 0, width, height);

			g.setColor(this.getRandColor(100, 200));
			g.draw3DRect(0, 0, x, height, true);
			g.fill3DRect(0, 0, x, height, true);
			g.dispose();
			return image;
		}
	}

}
