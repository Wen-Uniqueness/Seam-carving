package back_end;

import edu.princeton.cs.algs4.Picture;

public class Stablize_SC {
    // public static Picture shrinking(Picture picture, int new_width, int new_height) {
    //     return (new SeamCarver(picture)).shrinking(new_width, new_height).picture();
    // }

    // public static Picture expandingWidth(Picture picture, int new_width) {
    //     return new SeamCarver(picture).expandingWidth(new_width);
    // }

    // public static Picture expandingHeight(Picture picture, int new_height) {
    //     return new SeamCarver(picture).expandingHeight(new_height);
    // }

    // public static Picture shrinking(Picture picture, int[][] protect, int new_width, int new_height) {
    //     return new SeamCarver(picture, protect).shrinking(new_width, new_height).picture();
    // }

    // public static Picture expandingWidth(Picture picture, int[][] protect, int new_width) {
    //     return new SeamCarver(picture, protect).expandingWidth(new_width);
    // }

    // public static Picture expandingHeight(Picture picture, int[][] protect, int new_height) {
    //     return new SeamCarver(picture, protect).expandingHeight(new_height);
    // }

    public static Picture bs(Picture picture, int[][] protect, int new_width, int new_height) {
        SeamCarver result = new SeamCarver(picture, protect);
        if (new_height > picture.height()) {
            result = result.expandingHeight(new_height);
        }
        if (new_width > picture.width()) {
            result = result.expandingWidth(new_width);
        }
        result = result.shrinking(new_width, new_height);
        return result.picture();
    }

    // public static Picture bs(Picture picture, int new_width, int new_height) {
    //     if (new_height > picture.height()) {
    //         picture = expandingHeight(picture, new_height);
    //     }
    //     if (new_width > picture.width()) {
    //         picture = expandingWidth(picture, new_width);
    //     }
    //     picture = shrinking(picture, new_width, new_height);
    //     return picture;
    // }
}
