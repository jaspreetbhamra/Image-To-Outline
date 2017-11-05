import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

class Project {

	public static final int BLACK = 0x000000, WHITE = 0xffffff;
	public static final int Sobel[][] = {{-2, -2, 0}, {-2, 0, 2}, {0, 2, 2}};

	public static BufferedImage toGreyScale(BufferedImage image, int width, int height) {
		for(int i=0; i<height; i++) {
            for(int j=0; j<width; j++) {
               Color c = new Color(image.getRGB(j, i));
               int red = (int)(c.getRed() * 0.299);
               int green = (int)(c.getGreen() * 0.587);
               int blue = (int)(c.getBlue() * 0.114);
               Color newColor = new Color(red+green+blue, red+green+blue, red+green+blue);
               image.setRGB(j, i, newColor.getRGB());
            }
        }
        return image;
	}
	
	public static BufferedImage threshold(BufferedImage img, int width, int height) {
		for (int y = 0; y < height; y++){
        	for (int x = 0; x < width; x++){   
            	Color p = new Color(img.getRGB(x, y));
            	int r = p.getRed();
            	int g = p.getGreen();
            	int b = p.getBlue();
            	int a = p.getAlpha();
            	r = 255 - r;
            	g = 255 - g;
            	b = 255 - b;
            	int changed = (a<<24) | (r<<16) | (g<<8) | b;
            	img.setRGB(x, y, changed);
        	}
    	}
    	return img;
	}

	public static int getGrayScale(int rgb) {
	        int r = (rgb >> 16) & 0xff;
	        int g = (rgb >> 8) & 0xff;
	        int b = (rgb) & 0xff;
	        int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
	        return gray;
	}

	public static BufferedImage performOperation(BufferedImage img) {
		int gMax = 0;
		int width = img.getWidth();
		int height = img.getHeight();
		// BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int [][] imageMatrix = new int[width][height];
		// img = toGreyScale(img, width, height);
		// int arr[][] = new int[3][3];
		for(int j = 1; j < height-2; ++j) {
			for(int i = 1; i < width-2; ++i) {
				int gx = (2*getGrayScale(img.getRGB(i+1,j))+getGrayScale(img.getRGB(i+1,j-1))+getGrayScale(img.getRGB(i+1,j+1)))-(2*getGrayScale(img.getRGB(i-1,j))+getGrayScale(img.getRGB(i-1,j-1))+getGrayScale(img.getRGB(i-1,j+1)));
				int gy = (2*getGrayScale(img.getRGB(i,j+1))+getGrayScale(img.getRGB(i-1,j+1))+getGrayScale(img.getRGB(i+1,j+1)))-(2*getGrayScale(img.getRGB(i,j-1))+getGrayScale(img.getRGB(i-1,j-1))+getGrayScale(img.getRGB(i+1,j-1)));
				gx = gx * gx;
				gy = gy * gy;
				int f = (int) (Math.sqrt(gx+gy));
				if(f > gMax)
					gMax = f;
				imageMatrix[i][j] = f;
			}
		}
		double scale = 255.0 / gMax;
		for (int x = 1; x < width - 1; x++) {
	        for (int y = 1; y < height - 1; y++) {
		        int color = imageMatrix[x][y];
		        // if(color > 210)
		        // 	color = 210;
		        // else
		        // 	color = 10;
	            color = (int)(color * scale);
	            color = 0xff000000 | (color << 16) | (color << 8) | color;
	            img.setRGB(x, y, color);
            }
        }
        // remove the following line to obtain an image with a black background and white image outlines.......
		img = threshold(img, width, height);
		return img;
	}

	public static void main(String args[]) {
		BufferedImage img = null;
		File f = null;
		try {
			f = new File("path\\image.jpg");
			img = ImageIO.read(f);
			img = performOperation(img);
			f = new File("path\\newImage.jpg");
			ImageIO.write(img, "jpg", f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
