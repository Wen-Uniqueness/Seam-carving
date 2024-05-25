package back_end;

import edu.princeton.cs.algs4.Picture;

public class Stablize_SC {
    public static Picture shinking(Picture picture, int new_width, int new_height){
        return new SeamCarver(picture).shrinking(new_width, new_height).picture();
    }

    public static Picture expandingWidth(Picture picture, int new_width){
        return new SeamCarver(picture).expandingWidth(new_width);
    }
}
