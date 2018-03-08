/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compression;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class Compression {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            
            BufferedImage original = ImageIO.read(new File("Original.png"));
            
            String compressedFilename = compress(original);
            
            BufferedImage decompressed = decompress(compressedFilename);
            
            ImageIO.write(decompressed, "PNG", new File("Copy.png"));
        } catch (IOException ex) {
            Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String compress(BufferedImage original) {
        
        String filename = "compressed.txt";
        
        try (BufferedWriter br = new BufferedWriter(new FileWriter(filename))) 
        {
            int width = original.getWidth();
            int height = original.getHeight();
            br.write("" + width);
            br.write(" ");
            br.write("" + height);
            br.write("\r\n");
            
            for(int y = 0; y < height; y ++)
            {
                for(int x = 0; x < width; x ++)
                {
                    Color pixel = new Color(original.getRGB(x, y));
                    br.write("" + pixel.getRed() + " " + pixel.getGreen() + " " + pixel.getBlue() + ",");
                }
                br.write("\r\n");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Compression.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return filename;
        
    }

    private static BufferedImage decompress(String compressed) {
        
        
        BufferedImage bi = null;
        int width, height;
        
        try(BufferedReader br = new BufferedReader(new FileReader(compressed))){
            
            String line = br.readLine();
            
            String[] dimensionsString = line.split(" ");
            
            width = Integer.parseInt(dimensionsString[0]);
            height = Integer.parseInt(dimensionsString[1]);
            
            bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            
            
            for(int y = 0; y < height; y ++)
            {
                line = br.readLine();
                
                String[] pixelStrings = line.split(",");
                for(int x = 0; x < width; x ++)
                {
                    String pixelString = pixelStrings[x].trim();
                    String[] rgb = pixelString.split(" ");
                    int r = Integer.parseInt(rgb[0]);
                    int g = Integer.parseInt(rgb[1]);
                    int b = Integer.parseInt(rgb[2]);
                    
                    bi.setRGB(x, y, new Color(r,g,b).getRGB());
                    
                }
               
            }
            
            
        }catch(IOException ex){
            
        }
                
        
       return bi;
        
    }
    
}
