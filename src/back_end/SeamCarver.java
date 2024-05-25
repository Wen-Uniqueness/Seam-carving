package back_end;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture m_picture;
    private int width;
    private int height;
    private int[][] protect;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        m_picture = picture;
        width = picture.width();
        height = picture.height();
        protect = null;
    }

    public SeamCarver(Picture picture, int[][] protect) {
        m_picture = picture;
        width = picture.width();
        height = picture.height();
        if (protect.length != width || protect[0].length != height) throw new IllegalArgumentException();
        this.protect = protect;
    }

    public SeamCarver(SeamCarver SC) {
        m_picture = new Picture(SC.picture());
        width = m_picture.width();
        height = m_picture.height();
        protect = SC.protect;
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

        if (protect != null && protect[x][y] > 0) return 1000;
        if (protect != null && protect[x][y] < 0) return 0;

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
        Double[][] dp = findHorizontalSeamEnergy();

        return getHorizontalSeam(dp, Arithmetic.find_min(dp[dp.length-1]));
    }

    private Double[][] findHorizontalSeamEnergy(){
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
        return dp;
    }

    private int[] getHorizontalSeam(Double[][] dp, int start_index){
        int[] seam = new int[width];
        seam[width-1] = start_index;
        for (int i = width-2; i >= 0; i--){
            int index = seam[i+1];
            if (seam[i+1]>0 && dp[i][seam[i+1]-1] < dp[i][index]) index = seam[i+1]-1;
            if (seam[i+1]<height-1 && dp[i][seam[i+1]+1] < dp[i][index]) index = seam[i+1]+1;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        Double[][] dp = findVerticalSeamEnergy();

        Double[] last_col = new Double[height];
        for (int i = 0; i < height; i ++) last_col[i] = dp[i][height-1];

        return getVertivalSeam(dp, Arithmetic.find_min(last_col));
    }

    private Double[][] findVerticalSeamEnergy(){
        Double[][] dp = new Double[width][height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if (y == 0) dp[x][0] = (double) 1000;
                else {
                    double last_min = dp[x][y-1];
                    if (x > 0) last_min = Math.min(last_min, dp[x-1][y-1]);
                    if (x < height-1) last_min = Math.min(last_min, dp[x+1][y-1]);
                    dp[x][y] = energy(x, y) + last_min;
                }
            }
        }
        return dp;
    }

    private int[] getVertivalSeam(Double[][] dp, int start_index){
        int[] seam = new int[width];
        seam[width-1] = start_index;
        for (int i = width-2; i >= 0; i--){
            int index = seam[i+1];
            if (seam[i+1]>0 && dp[i][seam[i+1]-1] < dp[i][index]) index = seam[i+1]-1;
            if (seam[i+1]<height-1 && dp[i][seam[i+1]+1] < dp[i][index]) index = seam[i+1]+1;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width || height <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++){
            if ((i > 0 && Math.abs(seam[i-1] - seam[i]) > 1) || seam[i] < 0 || seam[i] >= height) throw new IllegalArgumentException();
        }

        Picture new_picture = new Picture(width, height-1);
        int[][] new_protect = new int[width][height-1];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height-1; j++){
                int h = (j >= seam[i]) ? j : j+1;
                new_picture.set(i, j, m_picture.get(i, h));
                new_protect[i][j] = protect[i][h];
            }
        }

        m_picture = new_picture;
        height = height-1;
        protect = new_protect;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height || width <= 1) throw new IllegalArgumentException();
        for (int i = 1; i < seam.length; i++){
            if ((i > 0 && Math.abs(seam[i-1] - seam[i]) > 1)|| seam[i] < 0 || seam[i] >= width) throw new IllegalArgumentException();
        }

        Picture new_picture = new Picture(width-1, height);
        int[][] new_protect = new int[width-1][height];
        for (int i = 0; i < width-1; i++){
            for (int j = 0; j < height; j++){
                int w = (i >= seam[j]) ? i : i+1;
                new_picture.set(i, j, m_picture.get(w,j));
                new_protect[i][j] = protect[w][j];
            }
        }

        m_picture = new_picture;
        width = width-1;
        protect = new_protect;
    }

    // shrinking the picture
    public SeamCarver shrinking(int new_width, int new_height){
        if (new_height <= 0 || new_height > height || new_width <= 0 || new_width > width) throw new IllegalArgumentException();
        if (new_height == height && new_width == width) return this;
        int c = width - new_width;
        int r = height = new_height;
        Double[][] T = new Double[c][r];
        T[0][0] = 0.;
        SeamCarver[][] Item = new SeamCarver[c][r];
        Item[0][0] = this;

        for (int j = 1; j < r; j++){
            Double[][] dp = Item[0][j-1].findHorizontalSeamEnergy();
            int index = Arithmetic.find_min(dp[dp.length-1]);
            T[0][j] = T[0][j-1] + dp[dp.length-1][index];
            int[] seam = getHorizontalSeam(dp, index);
            Item[0][j] = new SeamCarver(Item[0][j-1]);
            Item[0][j].removeHorizontalSeam(seam);
        }

        for (int i = 1; i < c; i++){
            Double[][] dp_c = Item[i-1][0].findVerticalSeamEnergy();
            Double[] last_col = new Double[width-i];
            for (int k = 0; k < width-i; k++) last_col[k] = dp_c[k][dp_c[k].length-1];
            int index_c = Arithmetic.find_min(last_col);
            int[] seam_c = getHorizontalSeam(dp_c, index_c);
            Item[i][0] = new SeamCarver(Item[i-1][0]);
            Item[i][0].removeHorizontalSeam(seam_c);
        }

        for (int i = 1; i < c; i++){
            for (int j = 1; j < r; j++){
                Double[][] dp_r = Item[i][j-1].findHorizontalSeamEnergy();
                int index_r = Arithmetic.find_min(dp_r[dp_r.length-1]);
                Double min_r = T[i][j-1] + dp_r[dp_r.length-1][index_r];
                int[] seam_r = getHorizontalSeam(dp_r, index_r);

                Double[][] dp_c = Item[i-1][j].findVerticalSeamEnergy();
                Double[] last_col = new Double[width-i];
                for (int k = 0; k < width-i; k++) last_col[k] = dp_c[k][dp_c[k].length-1];
                int index_c = Arithmetic.find_min(last_col);
                Double min_c = T[i-1][j] + dp_c[index_c][dp_c[index_c].length-1];
                int[] seam_c = getHorizontalSeam(dp_c, index_c);

                if (min_r < min_c){
                    T[i][j] = min_r;
                    Item[i][j] = new SeamCarver(Item[i][j-1]);
                    Item[i][j].removeHorizontalSeam(seam_r);
                }else{
                    T[i][j] = min_c;
                    Item[i][j] = new SeamCarver(Item[i-1][j]);
                    Item[i][j].removeVerticalSeam(seam_c);
                }
            }
        }
        return Item[c-1][r-1];
    }

    // expand the picture
    public Picture expandingWidth(int new_width){
        if (new_width <= width) throw new IllegalArgumentException();
        int k = new_width - width;
        Double[][] dp = findVerticalSeamEnergy();

        Double[] last_col = new Double[height];
        for (int i = 0; i < height; i ++) last_col[i] = dp[i][height-1];

        // 找前k个最小的缝隙
        int[] indexes = Arithmetic.getKSmallestIndex(last_col, k);

        int[][] seams = new int[k][height];
        for (int i =0; i < k; i++) seams[i] = getVertivalSeam(dp, indexes[i]);

        int[][] how_to_expand = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                how_to_expand[i][j] = 0;
            }
        }
        for (int i = 0; i< k; i++){
            for (int y = 0; y<height; y++){
                how_to_expand[seams[i][y]][y] += 1;
            }
        }

        // 生成一个新的图片
        Picture picture = new Picture(width+k, height);
        int x = 0;
        for (int i = 0; i < width+k; i++){
            for (int j = 0; j < height; j++){
                picture.set(i, j, m_picture.get(x, j));
                if (how_to_expand[x][j] > 0) {
                    
                    how_to_expand[x][j] -= 1;
                }else{
                    x+=1;
                }
            }
        }

        return picture;
    }

    public Picture expandingHeight(int new_height){
        if (new_height <= height) throw new IllegalArgumentException();
        int k = new_height - height;
        Double[][] dp = findHorizontalSeamEnergy();

        // 找前k个最小的缝隙
        int[] indexes = Arithmetic.getKSmallestIndex(dp[dp.length-1], k);

        int[][] seams = new int[k][width];
        for (int i =0; i < k; i++) seams[i] = getHorizontalSeam(dp, indexes[i]);

        int[][] how_to_expand = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                how_to_expand[i][j] = 0;
            }
        }
        for (int i = 0; i< k; i++){
            for (int x = 0; x<width; x++){
                how_to_expand[x][seams[i][x]] += 1;
            }
        }

        // 生成一个新的图片
        Picture picture = new Picture(width, height+k);
        int y = 0;
        for (int i = 0; i < width+k; i++){
            for (int j = 0; j < height; j++){
                picture.set(i, j, m_picture.get(i, y));
                if (how_to_expand[i][y] > 0) {
                    how_to_expand[i][y] -= 1;
                }
                else{
                    y+=1;
                }
            }
        }

        return picture;
    }



    // unit testing (optional)
    public static void main(String[] args){

    }
}
