
package com.enewschamp.utils;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static String JPG_IMAGE_TYPE = "jpg";
	public static String PNG_IMAGE_TYPE = "png";
	public static String GIF_IMAGE_TYPE = "gif";

	public static void main(String[] args) throws IOException {

		String folderPath = "/Users/mayu/Quiz App/workspace/enewschamp/src/test/resources/images/";
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
		// System.out.println("Total No of Files:" + listOfFiles.length);
		Image img = null;
		BufferedImage tempPNG = null;
		BufferedImage tempJPG = null;
		File newFilePNG = null;
		File newFileJPG = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && !listOfFiles[i].getName().startsWith(".")) {
				// System.out.println("File " + listOfFiles[i].getName());
				img = ImageIO.read(new File(folderPath + listOfFiles[i].getName()));

				BufferedImage origImg = ImageIO.read(new File(folderPath + listOfFiles[i].getName()));

				// System.out.println(origImg.getHeight() + " X " + origImg.getWidth());

				Dimension imgSize = new Dimension(origImg.getWidth(), origImg.getHeight());
				Dimension boundary = new Dimension(400, 200);

				Dimension newDimension = getScaledDimension(imgSize, boundary);

				tempPNG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue());
				tempJPG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue());
				newFilePNG = new File(folderPath + "resize/" + listOfFiles[i].getName() + "_New.png");
				newFileJPG = new File(folderPath + "resize/" + listOfFiles[i].getName() + "_New.jpg");
				ImageIO.write(tempPNG, "png", newFilePNG);
				ImageIO.write(tempJPG, "jpg", newFileJPG);
			}
		}
		// System.out.println("DONE");
	}

	public static void resizeImage(File file, Dimension size, String imageType, String outputFileNameWithoutExtension) {
		BufferedImage img;
		try {
			img = ImageIO.read(file);
			Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
			Dimension newDimension = getScaledDimension(imgSize, size);
			if (imageType.equals(JPG_IMAGE_TYPE)) {
				BufferedImage tempJPG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue());
				File newFileJPG = new File(outputFileNameWithoutExtension + "." + JPG_IMAGE_TYPE);
				newFileJPG.mkdirs();
				ImageIO.write(tempJPG, JPG_IMAGE_TYPE, newFileJPG);
			}
			if (imageType.equals(PNG_IMAGE_TYPE)) {
				BufferedImage tempPNG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue());
				File newFilePNG = new File(outputFileNameWithoutExtension + "." + PNG_IMAGE_TYPE);
				newFilePNG.mkdirs();
				ImageIO.write(tempPNG, PNG_IMAGE_TYPE, newFilePNG);
			}
			if (imageType.equals(GIF_IMAGE_TYPE)) {
				BufferedImage tempPNG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue());
				File newFilePNG = new File(outputFileNameWithoutExtension + "." + GIF_IMAGE_TYPE);
				newFilePNG.mkdirs();
				ImageIO.write(tempPNG, GIF_IMAGE_TYPE, newFilePNG);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			img = null;
		}
	}

	private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		// first check if we need to scale width
		if (original_width > bound_width) {
			// scale width to fit
			new_width = bound_width;
			// scale height to maintain aspect ratio
			new_height = (new_width * original_height) / original_width;
		}

		// then check if we need to scale even with the new height
		if (new_height > bound_height) {
			// scale height to fit instead
			new_height = bound_height;
			// scale width to maintain aspect ratio
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	/**
	 * This function resize the image file and returns the BufferedImage object that
	 * can be saved to file system.
	 */
	private static BufferedImage resizeImage(final Image image, int width, int height) {
		final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D graphics2D = bufferedImage.createGraphics();
		graphics2D.setComposite(AlphaComposite.Src);
		// below three lines are for RenderingHints for better image quality at cost of
		// higher processing time
		graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(image, 0, 0, width, height, null);
		graphics2D.dispose();
		return bufferedImage;
	}
}
