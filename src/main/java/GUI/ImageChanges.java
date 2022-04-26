/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JOptionPane;

public class ImageChanges {

    public static Image getScaledImage(Image srcImg, int w) {
        int width = srcImg.getWidth(null);
        int height = srcImg.getHeight(null);
        double ratio = ((double) w) / ((double) width);
        int h = (int) (height * ratio);

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    public static void copyFile(String in, String out) {
        try {
            InputStream inStream;
            OutputStream outStream;


            File afile = new File(in);
            File bfile = new File(out);
            if (bfile.exists()) {
                JOptionPane.showMessageDialog(null, "Error!!\n"
                        + "An image with that name already exists" + "\n"
                        + "Please rename");

            } else {
                inStream = new FileInputStream(afile);
                outStream = new FileOutputStream(bfile);

                byte[] buffer = new byte[1024];

                int length;
                //copy the file content in bytes 
                while ((length = inStream.read(buffer)) > 0) {

                    outStream.write(buffer, 0, length);

                }

                inStream.close();
                outStream.close();

                JOptionPane.showMessageDialog(null, "Image copied");
            }

        } catch (Exception e) {
        }
    }
}