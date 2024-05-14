package back_end;


/**
 * seam_carving
 */
public class seam_carving {
    public int[] find_the_minimum_transverse_seam(int width, int height, int[][] energy){
        int[][] dp = new int[width][height];
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height;y++){
                int last_min = 0;
                if (x > 0) {
                    last_min = dp[x-1][y];
                    if (y > 0) last_min = Math.min(dp[x-1][y-1], last_min);
                    if (y < height-1) last_min = Math.min(dp[x-1][y+1], last_min);
                }

                dp[x][y] = energy[x][y] + last_min;
            }
        }

        

        return new int[1];
    }
    
}