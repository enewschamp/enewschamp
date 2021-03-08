
package com.enewschamp.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SaveFileUtils {

	public static String JPG_IMAGE_TYPE = "jpg";
	public static String JPEG_IMAGE_TYPE = "jpeg";
	public static String PNG_IMAGE_TYPE = "png";
	public static String GIF_IMAGE_TYPE = "gif";
	public static String TIFF_IMAGE_TYPE = "tiff";
	public static String WAV_AUDIO_TYPE = "wav";
	public static String MP3_AUDIO_TYPE = "mp3";
	public static String MP4_AUDIO_TYPE = "mp4";
	public static String M4A_AUDIO_TYPE = "m4a";
	public static String FLAC_AUDIO_TYPE = "flac";
	public static String WMA_AUDIO_TYPE = "wma";
	public static String AAC_AUDIO_TYPE = "aac";

	public static void saveAudioFile(File file, String fileType, String outputFileNameWithoutExtension) {
		try {
			if (fileType.equals(WAV_AUDIO_TYPE) || fileType.equals(MP3_AUDIO_TYPE) || fileType.equals(MP4_AUDIO_TYPE)
					|| fileType.equals(M4A_AUDIO_TYPE) || fileType.equals(FLAC_AUDIO_TYPE)
					|| fileType.equals(WMA_AUDIO_TYPE) || fileType.equals(AAC_AUDIO_TYPE)) {
				File newFile = new File(outputFileNameWithoutExtension + "." + fileType);
				File dir = newFile.getParentFile();
				dir.mkdirs();
				InputStream is = null;
				OutputStream os = null;
				try {
					is = new FileInputStream(file);
					os = new FileOutputStream(newFile);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0) {
						os.write(buffer, 0, length);
					}
				} finally {
					if (is != null) {
						is.close();
					}
					if (os != null) {
						os.close();
					}
				}
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void resizeImage(File file, Dimension size, String imageType, String outputFileNameWithoutExtension) {
		BufferedImage img;
		try {
			if (imageType.equals(JPG_IMAGE_TYPE) || imageType.equals(JPEG_IMAGE_TYPE)
					|| imageType.equals(PNG_IMAGE_TYPE)) {
				img = ImageIO.read(file);
				Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
				Dimension newDimension = getScaledDimension(imgSize, size);
				BufferedImage tempJPG = resizeImage(img, Double.valueOf(newDimension.getWidth()).intValue(),
						Double.valueOf(newDimension.getHeight()).intValue(), imageType);
				File newFile = new File(outputFileNameWithoutExtension + "." + imageType);
				File dir = newFile.getParentFile();
				dir.mkdirs();
				ImageIO.write(tempJPG, imageType, newFile);
			} else if (imageType.equals(GIF_IMAGE_TYPE)) {
				ImageIcon icon = new ImageIcon(file.toURL());
				Dimension imgSize = new Dimension(icon.getImage().getWidth(null), icon.getImage().getHeight(null));
				Dimension newDimension = getScaledDimension(imgSize, size);
				int width = Double.valueOf(newDimension.getWidth()).intValue();
				int height = Double.valueOf(newDimension.getHeight()).intValue();
				try {
					FileInputStream fiStream = new FileInputStream(file);
					GifDecoder d = new GifDecoder();
					d.read(fiStream);
					int n = d.getFrameCount();
					if (n > 0) {
						File newFile = new File(outputFileNameWithoutExtension + "." + imageType);
						File dir = newFile.getParentFile();
						dir.mkdirs();
						ImageOutputStream output = new FileImageOutputStream(newFile);
						GifSequenceWriter writer = new GifSequenceWriter(output, d.getFrame(0).getType(), d.getDelay(0),
								true);
						BufferedImage frame = null;
						frame = resizeImage(d.getFrame(0), width, height, imageType);
						writer.writeToSequence(frame, d.getDelay(0));
						frame = null;
						for (int i = 1; i < n; i++) {
							frame = resizeImage(d.getFrame(i), width, height, imageType);
							writer.writeToSequence(frame, d.getDelay(i));
							frame = null;
						}
						writer.close();
						output.close();
					}
					fiStream.close();
				} catch (FileNotFoundException e) {
					System.out.println("File not found");
				} catch (IOException e) {
					System.out.println("IO Exception");
				}
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
	private static BufferedImage resizeImage(final Image image, int width, int height, String imageType) {
		final BufferedImage bufferedImage = new BufferedImage(width, height,
				("gif".equals(imageType) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB));
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

	private static ImageFrame[] readGif(InputStream stream) throws IOException {
		ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);

		ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
		reader.setInput(ImageIO.createImageInputStream(stream));

		int lastx = 0;
		int lasty = 0;

		int width = -1;
		int height = -1;

		IIOMetadata metadata = reader.getStreamMetadata();

		Color backgroundColor = null;
		System.out.println("???metadata?????" + metadata);
		if (metadata != null) {
			IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

			NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
			NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

			if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0) {
				IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);

				if (screenDescriptor != null) {
					width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
					height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
				}
			}

			if (globalColorTable != null && globalColorTable.getLength() > 0) {
				IIOMetadataNode colorTable = (IIOMetadataNode) globalColorTable.item(0);

				if (colorTable != null) {
					String bgIndex = colorTable.getAttribute("backgroundColorIndex");

					IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
					while (colorEntry != null) {
						if (colorEntry.getAttribute("index").equals(bgIndex)) {
							int red = Integer.parseInt(colorEntry.getAttribute("red"));
							int green = Integer.parseInt(colorEntry.getAttribute("green"));
							int blue = Integer.parseInt(colorEntry.getAttribute("blue"));

							backgroundColor = new Color(red, green, blue);
							break;
						}

						colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
					}
				}
			}
		}

		BufferedImage master = null;
		boolean hasBackround = false;

		for (int frameIndex = 0;; frameIndex++) {
			BufferedImage image;
			try {
				image = reader.read(frameIndex);
			} catch (IndexOutOfBoundsException io) {
				break;
			}

			if (width == -1 || height == -1) {
				width = image.getWidth();
				height = image.getHeight();
			}

			IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex)
					.getAsTree("javax_imageio_gif_image_1.0");
			IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
			NodeList children = root.getChildNodes();

			int delay = Integer.valueOf(gce.getAttribute("delayTime"));

			String disposal = gce.getAttribute("disposalMethod");

			if (master == null) {
				master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				master.createGraphics().setColor(backgroundColor);
				master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());

				hasBackround = image.getWidth() == width && image.getHeight() == height;

				master.createGraphics().drawImage(image, 0, 0, null);
			} else {
				int x = 0;
				int y = 0;
				for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++) {
					Node nodeItem = children.item(nodeIndex);
					if (nodeItem.getNodeName().equals("ImageDescriptor")) {
						NamedNodeMap map = nodeItem.getAttributes();
						x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
						y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
					}
				}
				if (disposal.equals("restoreToPrevious")) {
					BufferedImage from = null;
					for (int i = frameIndex - 1; i >= 0; i--) {
						if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0) {
							from = frames.get(i).getImage();
							break;
						}
					}
					{
						ColorModel model = from.getColorModel();
						boolean alpha = from.isAlphaPremultiplied();
						WritableRaster raster = from.copyData(null);
						master = new BufferedImage(model, raster, alpha, null);
					}
				} else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null) {
					if (!hasBackround || frameIndex > 1) {
						master.createGraphics().fillRect(lastx, lasty, frames.get(frameIndex - 1).getWidth(),
								frames.get(frameIndex - 1).getHeight());
					}
				}
				master.createGraphics().drawImage(image, x, y, null);

				lastx = x;
				lasty = y;
			}

			{
				BufferedImage copy;

				{
					ColorModel model = master.getColorModel();
					boolean alpha = master.isAlphaPremultiplied();
					WritableRaster raster = master.copyData(null);
					copy = new BufferedImage(model, raster, alpha, null);
				}
				frames.add(new ImageFrame(copy, delay, disposal, image.getWidth(), image.getHeight()));
			}

			master.flush();
		}
		reader.dispose();

		return frames.toArray(new ImageFrame[frames.size()]);
	}
}
