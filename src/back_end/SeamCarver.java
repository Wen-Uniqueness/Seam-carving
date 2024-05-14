package back_end;

import edu.princeton.cs.algs4.Picture;
import back_end.Arithmetic;

public class SeamCarver {
    private Picture m_picture;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        m_picture = picture;
    }

    // current picture
    public Picture picture() {
        return m_picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
   public double energy(int x, int y){
        if (x < 0 || x > width-1 || y < 0 || y > height-1) throw new IllegalArgumentException();
        if (x == 0 || x == width-1 || y == 0 || y == height-1) return 1000;

        double power_square = 0;
        power_square += Math.pow(m_picture.get(x+1, y).getRed() - m_picture.get(x-1, y).getRed(), 2);
        power_square += Math.pow(m_picture.get(x+1, y).getBlue() - m_picture.get(x-1, y).getBlue(), 2);
        power_square += Math.pow(m_picture.get(x+1, y).getGreen() - m_picture.get(x-1, y).getGreen(), 2);
        power_square += Math.pow(m_picture.get(x, y+1).getRed() - m_picture.get(x, y-1).getRed(), 2);
        power_square += Math.pow(m_picture.get(x, y+1).getBlue() - m_picture.get(x, y-1).getBlue(), 2);
        power_square += Math.pow(m_picture.get(x, y+1).getGreen() - m_picture.get(x, y-1).getGreen(), 2);

        return Math.sqrt(power_square);
   }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        Double[][] dp = new Double[width][height];
        
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if (x == 0) dp[0][y] = (double) 1000;
                else {
                    double last_min = dp[x-1][y];
                    if (y > 0) last_min = Math.min(last_min, dp[x-1][y-1]);
                    if (y < height-1) last_min = Math.min(last_min, dp[x-1][y+1]);
                    dp[x][y] = energy(x, y) + last_min;
                }
            }
        }

        int[] seam = new int[width];
        seam[width-1] = Arithmetic.find_min(dp[-1]);
        for (int i = width-2; i >= 0; i--){
            int index = seam[i+1];
            if (seam[i+1]>0 && dp[i][seam[i+1]-1] < dp[i][index]) index = seam[i+1]-1;
            if (seam[i+1]<height-1 && dp[i][seam[i+1]+1] < dp[i][index]) index = seam[i+1]+1;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width || height <= 1) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++){
            if (Math.abs(seam[i-1] - seam[i]) > 1) throw new IllegalArgumentException();
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height || width <= 1) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++){
            if (Math.abs(seam[i-1] - seam[i]) > 1) throw new IllegalArgumentException();
        }
    }

    // unit testing (optional)
    public static void main(String[] args){

    }
}
