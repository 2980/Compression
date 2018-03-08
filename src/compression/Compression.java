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
import java.util.ArrayList;
import java.util.List;
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
            
            String[] allPixels = new String[width*height];
            int index = 0;
            
            for(int y = 0; y < height; y ++)
            {
                for(int x = 0; x < width; x ++)
                {
                    Color pixel = new Color(original.getRGB(x, y));
                    
                    String code;
                    if(pixel.getRed() > 0)
                    {
                        code = "1";
                    }
                    else
                    {
                        code = "0";
                    }
                    allPixels[index] = code;
                    index++;
                }
                
                
            }
            
            //Loop is done. Now see what kind of streaks we have.
            //Let's assume we start with black.
            
            List<Integer> runs = new ArrayList<Integer>();
            int currentRun = 0;
            String currentColor = "0";
            for(int i = 0; i < allPixels.length; i++)
            {
                if(allPixels[i].equals(currentColor))
                {
                    currentRun++;
                }
                else
                {
                    runs.add(currentRun);
                    currentRun = 1;
                    currentColor = currentColor.equals("0") ? "1" : "0";
                }
            }
            //Make sure we add the last run.
            runs.add(currentRun);
            
            for(int i = 0;  i < runs.size(); i++)
            {
                br.write("" + runs.get(i));
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
            
            line = br.readLine();

            String[] pixelStrings = line.split("");
            
            int[] runs = new int[pixelStrings.length];
            
            for(int i = 0; i < pixelStrings.length; i++)
            {
                runs[i] = Integer.parseInt(pixelStrings[i]);
            }
            
            List<String> oneDImage = new ArrayList<String>();
            
            String currentColor = "0";
            for(int i = 0; i < runs.length; i++)
            {
                int length = runs[i];
                for(int j = 0; j < length; j++)
                {
                    oneDImage.add(currentColor);
                }
                 currentColor = currentColor.equals("0") ? "1" : "0";
            }
            
            int index = 0;
            
            for(int y = 0; y < height; y ++)
            {
                for(int x = 0; x < width; x ++)
                {
                    
                    
                    
                    Color color;
                    
                    if(oneDImage.get(index).equals("0"))
                        color = Color.BLACK;
                    
                    else
                        color = Color.WHITE;
                    
                    
                    bi.setRGB(x, y, color.getRGB());
                    
                    index++;
                    
                }
               
            }
            
            
        }catch(IOException ex){
            
        }
                
        
       return bi;
        
    }
    
}
