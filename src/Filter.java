import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.imageio.ImageIO;
import java.util.Scanner;

public class Filter {

    public static void main(String[] args) {

        int width = 600;
        int height = 400;
        BufferedImage image = null;
        File f = null;
        BufferedImage resultFile = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Scanner scan = new Scanner(System.in);

        // Get the Name of the File to be filtered
        String imageName = getImageName(scan);     

        // read in the Image
        try {
            f = new File("C:/Users/misch/Desktop/Uni/OnlineCourse/PracticeProjects/JavaFilter/img/" + imageName + ".bmp");
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(f);
            System.out.println("Succesfully loaded in File");
        } catch (IOException e) {
            System.out.println("Error reading in File: " + e);
            e.printStackTrace();
        }
        
        
        String filterName = getFilter(scan); 

        resultFile = filter(width, height, image, filterName);
        
        // put out image
        try {
            f = new File("C:/Users/misch/Desktop/Uni/OnlineCourse/PracticeProjects/JavaFilter/img/output.bmp");
            ImageIO.write(resultFile, "bmp", f);
        } catch (IOException e) {
            System.out.println("Error printing File: " + e);
        }
        System.out.println("Succesfully print File");

    }

    /*
     * Ask the user for the Filename.
     */
    private static String getImageName(Scanner scan) {
        
        String name;

        /*
         * If the input is invalid, ask again.
         */
        do {
            System.out.println("Please choose one of the following Images:");
            System.out.println("\"courtyard\", \"stadium\", \"tower\" or \"yard\"");
            System.out.print("Image name: ");
            name = scan.nextLine();
        } while(!name.equals("courtyard") && !name.equals("stadium") &&
        !name.equals("tower") && !name.equals("yard"));

        return name;
    }

    /*
     * Ask the user for the filter. 
     */
    private static String getFilter(Scanner scan) {

        String filter;

        /*
         * If the input is invalid, ask again.
         */
        do {
            System.out.println("Please choose a filter:");
            System.out.println("grayscale: \"g\", sepia \"s\", reflect: \"r\", blur: \"b\", edge: \"e\"");
            System.out.print("Filter: ");
            filter = scan.nextLine();
        } while(!filter.equals("g") && !filter.equals("s") && !filter.equals("r")
        && !filter.equals("b") && !filter.equals("e"));

        // Close the scanner so no data leaks occur
        //scan.close();
        return filter;
    }

    private static BufferedImage filter(int width, int height, BufferedImage inFile, String filter) {

        BufferedImage outFile = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        switch(filter) {
            case "g":
                outFile = grayscale(width, height, inFile);
                break;
            case "s":
                outFile = sepia(width, height, inFile);
                break;
            case "r":
                outFile = reflect(width, height, inFile);
                break;
            case "b":
                outFile = blur(width, height, inFile);
                break;
            case "e":
                outFile = edge(width, height, inFile);
                break;
            default: 
                System.out.println("Could not filter File in Switch");
                return inFile;
        }

        return outFile;
    }

    /*
     * Turns the image into black-grey-white tones.
     */
    private static BufferedImage grayscale(int width, int height, BufferedImage inFile) {

        BufferedImage filteredFile = copyImage(inFile);
        Color color, newPixel;
        int oldPixel, red, green, blue, avg;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get the original Pixel
                oldPixel = inFile.getRGB(i, j);
                // Turn it into a color
                color = new Color(oldPixel);
                // Get the individual values
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();
                // Calculate the average
                avg = ((int) (Math.round((red + green + blue) / 3.0)) > 255) ? 255 : (int) (Math.round((red + green + blue) / 3.0));
                // Create new Pixel with the average values
                newPixel = new Color(avg, avg, avg);
                // Set new Pixel
                filteredFile.setRGB(i, j, newPixel.getRGB());
            }
        }

        return filteredFile;
    }

    /*
     * Applies a sepia filter to the image.
     * The image will be in all brown-tones.
     */
    private static BufferedImage sepia(int width, int height, BufferedImage inFile) {

        BufferedImage filteredFile = copyImage(inFile);
        Color color, newPixel;
        int oldPixel, red, green, blue, sepiaR, sepiaG, sepiaB;

        /*
         * Calculation Method (CS50x 2022 Week 4) 
         * sepiaRed = .393 * originalRed + .769 * originalGreen + .189 * originalBlue
         * sepiaGreen = .349 * originalRed + .686 * originalGreen + .168 * originalBlue
         * sepiaBlue = .272 * originalRed + .534 * originalGreen + .131 * originalBlue
         */
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                // Get the original Pixel
                oldPixel = inFile.getRGB(i, j);
                // Turn it into a color
                color = new Color(oldPixel);
                // Get the individual values
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();
                // Calculate the sepia values
                sepiaR = ((int) (Math.round(.393 * red + .769 * green + .189 * blue)) > 255) ? 255 : (int) (Math.round(.393 * red + .769 * green + .189 * blue));
                sepiaG = ((int) (Math.round(.349 * red + .686 * green + .168 * blue)) > 255) ? 255 : (int) (Math.round(.349 * red + .686 * green + .168 * blue));
                sepiaB = ((int) (Math.round(.272 * red + .534 * green + .131 * blue)) > 255) ? 255 : (int) (Math.round(.272 * red + .534 * green + .131 * blue));
                // Create new Pixel with the average values
                newPixel = new Color(sepiaR, sepiaG, sepiaB);
                // Set new Pixel
                filteredFile.setRGB(i, j, newPixel.getRGB());
            }
        }
        return filteredFile;
    }

    /*
     * Reflects the image by it's horizontal axis.
     */
    private static BufferedImage reflect(int width, int height, BufferedImage inFile) {

        int firstPixel, secondPixel;
        BufferedImage filteredFile = copyImage(inFile);

        /*
         * Iterate through the images pixels and swap the 
         * left side with the right side.
         */
        for (int i = 0; i < width/2; i++) {
            for (int j = 0; j < height; j++) {
                // Get the original Pixels from left and right
                firstPixel = inFile.getRGB(i, j);
                secondPixel = inFile.getRGB(width-1-i, j);
                // Switches the values in the output file.
                filteredFile.setRGB(i, j, secondPixel);
                filteredFile.setRGB(width-1-i, j, firstPixel);
            }
        }
        return filteredFile;
    }

    /*
     * Blurs the image in a 3x3 grid
     */
    private static BufferedImage blur(int width, int height, BufferedImage inFile) {

        BufferedImage filteredFile = copyImage(inFile);
        Color current = new Color(inFile.getRGB(1,1));

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                // Blur the current pixel
                current = blurPixel(width, height, inFile, i, j);
                filteredFile.setRGB(i, j, current.getRGB());
            }
        }
        return filteredFile;
    }

    /*
     * Blurs a pixel in a 3x3 grind. 
     */
    private static Color blurPixel(int width, int height, BufferedImage inFile,int posX, int posY) {

        Color blurredPixel, currentPixel;
        int red = 0;
        int green = 0;
        int blue = 0;
        double count = 0.0;

        for(int i = posX-1; i < posX+2; i++) {
            for(int j = posY-1; j < posY+2; j++) {
                // if out of bounds
                if(i < 0 || j < 0 || i >= width || j >= height) {
                    continue;
                }
                currentPixel = new Color(inFile.getRGB(i, j));
                red += currentPixel.getRed();
                green += currentPixel.getGreen();
                blue += currentPixel.getBlue();
                count++;

            }
        }
        // Calculate the average of each color.
        red = ((int) (Math.round(red/count)) > 255) ? 255 : (int) (Math.round(red/count));
        green = ((int) (Math.round(green/count)) > 255) ? 255 : (int) (Math.round(green/count));
        blue = ((int) (Math.round(blue/count)) > 255) ? 255 : (int) (Math.round(blue/count));

        blurredPixel = new Color(red, green, blue);   
        return blurredPixel;
        
    }

    /*
     * Detects edges and highlight them, by applying the sobel operator.
     */
    private static BufferedImage edge(int width, int height, BufferedImage inFile) {

        BufferedImage filteredFile = copyImage(inFile);
        Color currentPixel;

        // Sobelmatrixes
        int[] gy = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        int[] gx = {-1, -2, -1, 0, 0, 0, 1, 2, 1};
        int counter = 0;

        int redX = 0;
        int greenX = 0;
        int blueX = 0;
        int redY = 0;
        int greenY = 0;
        int blueY = 0;
        int resultRed, resultGreen, resultBlue;

        // Iterate through the pixels
        for(int posX = 0; posX < width-1; posX++) {
            for(int posY = 0; posY < height-1; posY++) {

                // Calculate the edge for that pixel
                for(int i = posX-1; i < posX+2; i++) {
                    for(int j = posY-1; j < posY+2; j++) {
                        // if out of bounds
                        if(i < 0 || j < 0 || i >= width || j >= height) {
                            counter++;
                            continue;
                        }  

                        // Calculate the "Gx" values
                        currentPixel = new Color(inFile.getRGB(i, j));
                        redX += gx[counter] * currentPixel.getRed();
                        greenX += gx[counter] * currentPixel.getGreen();
                        blueX += gx[counter] * currentPixel.getBlue();

                        //Calculate the "Gy" values
                        redY += gy[counter] * currentPixel.getRed();
                        greenY += gy[counter] * currentPixel.getGreen();
                        blueY += gy[counter] * currentPixel.getBlue();

                        counter++;
                    }
                }

                // Calculate the square root of Gx^2 + Gy^2 for each color
                resultRed = (int) (Math.round(Math.sqrt(Math.pow(redX, 2) + Math.pow(redY, 2))));
                resultGreen = (int) (Math.round(Math.sqrt(Math.pow(greenX, 2) + Math.pow(greenY, 2))));
                resultBlue = (int) (Math.round(Math.sqrt(Math.pow(blueX, 2) + Math.pow(blueY, 2))));

                // Check if the new value is out of bounds.
                resultRed = (resultRed > 255) ? 255 : resultRed;
                resultGreen = (resultGreen > 255) ? 255 : resultGreen;
                resultBlue = (resultBlue > 255) ? 255 : resultBlue;
                
                // Set the new values to the new pixel 
                currentPixel = new Color(resultRed, resultGreen, resultBlue);  
                filteredFile.setRGB(posX, posY, currentPixel.getRGB());

                // Reset the values
                redX = 0;
                greenX = 0;
                blueX = 0;
                redY = 0;
                greenY = 0;
                blueY = 0;
                counter = 0;
            }
        }
        return filteredFile;
    }

    // Copies one BufferedImage file to the other.
    // Source https://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage 
    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
}