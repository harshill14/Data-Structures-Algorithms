/*************************************************************************
*  Compilation:  javac ArtCollage.java
*  Execution:    java ArtCollage Flo2.jpeg
*
*  @author: Harshil
*
*************************************************************************/

import java.awt.Color;

public class ArtCollage {

   // The orginal picture
   private Picture original;

   // The collage picture
   private Picture collage;

   // The collage Picture consists of collageDimension X collageDimension tiles
   private int collageDimension;

   // A tile consists of tileDimension X tileDimension pixels
   private int tileDimension;
  
   /*
    * One-argument Constructor
    * 1. set default values of collageDimension to 4 and tileDimension to 100
    * 2. initializes original with the filename image
    * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension,
    *    where each pixel is black (see all constructors for the Picture class).
    * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
    *
    * @param filename the image filename
    */
   public ArtCollage (String filename) {
     this.collageDimension = 4;
     this.tileDimension = 100;
     this.original = new Picture(filename);
     this.collage = new Picture(400, 400);

     for (int c = 0; c < 400; c++) {
        for (int r = 0; r < 400; r++) {
           int newC = c * original.width() / 400;
           int newR = r * original.height() / 400;
           Color color = original.get(newC, newR);
           collage.set(c, r, color);
        }
     }
   }

   /*
    * Three-arguments Constructor
    * 1. set default values of collageDimension to cd and tileDimension to td
    * 2. initializes original with the filename image
    * 3. initializes collage as a Picture of tileDimension*collageDimension x tileDimension*collageDimension,
    *    where each pixel is black (see all constructors for the Picture class).
    * 4. update collage to be a scaled version of original (see scaling filter on Week 9 slides)
    *
    * @param filename the image filename
    */
   public ArtCollage (String filename, int td, int cd) {
 this.original = new Picture(filename);
      this.collageDimension = cd;
      this.tileDimension = td;
      this.collage = new Picture(td*cd, td*cd);

      for (int c = 0; c < td*cd; c++) {
        for (int r = 0; r < td*cd; r++) {
           int newC = c * original.width() / (td*cd);
           int newR = r * original.height() / (td*cd);
           Color color = original.get(newC, newR);
           collage.set(c, r, color);
        }
     }

   }

   /*
    * Returns the collageDimension instance variable
    *
    * @return collageDimension
    */
   public int getCollageDimension() {
       return collageDimension;

   }

   /*
    * Returns the tileDimension instance variable
    *
    * @return tileDimension
    */
   public int getTileDimension() {
       return tileDimension;

   }

   /*
    * Returns original instance variable
    *
    * @return original
    */
   public Picture getOriginalPicture() {
       return original;

   }

   /*
    * Returns collage instance variable
    *
    * @return collage
    */
   public Picture getCollagePicture() {
       return collage;

   }
  
   /*
    * Display the original image
    * Assumes that original has been initialized
    */
   public void showOriginalPicture() {
       original.show();

   }

   /*
    * Display the collage image
    * Assumes that collage has been initialized
    */
   public void showCollagePicture() {
       collage.show();

   }

   /*
    * Replaces the tile at collageCol,collageRow with the image from filename
    * Tile (0,0) is the upper leftmost tile
    *
    * @param filename image to replace tile
    * @param collageCol tile column
    * @param collageRow tile row
    */
   public void replaceTile (String filename,  int collageCol, int collageRow) {
       Picture replaced = new Picture(filename);

      for (int tc=0; tc<tileDimension; tc++) {
        for (int tr=0; tr<tileDimension; tr++) {

           int colunm = tc * replaced.width() / tileDimension;
            int row = tr * replaced.height() / tileDimension;
           collage.set(collageCol*tileDimension+tc, collageRow*tileDimension+tr, replaced.get(colunm, row));
        }
     }
   }
  
   /*
    * Makes a collage of tiles from original Picture
    * original will have collageDimension x collageDimension tiles, each tile
    * has tileDimension X tileDimension pixels
    */
   public void makeCollage () {
      for (int collageCol=0; collageCol<collageDimension; collageCol++) {
        for (int collageRow = 0; collageRow < collageDimension; collageRow++) {

           for (int tc = 0; tc < tileDimension; tc++) {
              for (int tr = 0; tr < tileDimension; tr++) {

                 int colunm = tc * original.width() / tileDimension;
                 int row = tr * original.height() / tileDimension;
                 collage.set(collageCol * tileDimension + tc, collageRow * tileDimension + tr, original.get(colunm, row));
              }
           }
        }
     }
   }

   /*
    * Colorizes the tile at (collageCol, collageRow) with component
    * (see CS111 Week 9 slides, the code for color separation is at the
    *  book's website)
    *
    * @param component is either red, blue or green
    * @param collageCol tile column
    * @param collageRow tile row
    */
   public void colorizeTile (String component,  int collageCol, int collageRow) {
     for (int tc=0; tc<tileDimension; tc++) {
        for (int tr=0; tr<tileDimension; tr++) {

           Color color = collage.get(collageCol*tileDimension+tc, collageRow*tileDimension+tr);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            if (component == "red") {
               g = 0;
               b = 0;
            }
            else if (component == "green") {
               r = 0;
               b = 0;
            }
            else if (component == "blue") {
               r = 0;
               g = 0;
            }

           collage.set(collageCol*tileDimension+tc, collageRow*tileDimension+tr, new Color(r,g,b));

        }
     }
   }

   /*
    * Grayscale tile at (collageCol, collageRow)
    * (see CS111 Week 9 slides, the code for luminance is at the book's website)
    *
    * @param collageCol tile column
    * @param collageRow tile row
    */

   public void grayscaleTile (int collageCol, int collageRow) {

      for(int c = collageCol*tileDimension; c<collageCol*tileDimension+tileDimension; c++){
           for(int r = collageRow*tileDimension; r < collageRow*tileDimension+tileDimension; r++){
             Color diff = collage.get(c,r);
             Color gray = Luminance.toGray(diff);
             collage.set(c, r, gray);
           }
         }
   }


   /*
    *
    *  Test client: use the examples given on the assignment description to test your ArtCollage
    */
   public static void main (String[] args) {

       ArtCollage art = new ArtCollage(args[0]);
      // art.showCollagePicture();
   }
}

