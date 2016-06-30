/*
Афинные гэпы
сдано
 */

import java.io.*;

public class ThreeLevelManhattan {
        public static void main(String[] args) throws IOException {
            BufferedReader fr = new BufferedReader(new FileReader("strings.txt"));

            String s1 = fr.readLine();
            String s2 = fr.readLine();

            double match = 1.0;
            double mismatch = -0.5;
            double ro = 0.0;
            double sigma = -0.1;

            double[][] level2 = new double[s1.length() + 1][s2.length() + 1];
            double[][] level1 = new double[s1.length() + 1][s2.length() + 1];
            double[][] level3 = new double[s1.length() + 1][s2.length() + 1];

            int[][] path2 = new int[s1.length() + 1][s2.length() + 1];
            int[][] path1 = new int[s1.length() + 1][s2.length() + 1];
            int[][] path3 = new int[s1.length() + 1][s2.length() + 1];

            for (int i = 0; i <= s1.length(); i++) {
                for (int j = 0; j <= s2.length(); j++) {
                    if (i > 0) {
                        double w1 = level1[i - 1][j] + sigma;
                        double w2 = level2[i - 1][j] + ro + sigma;
                        if (w1 > w2) {
                            level1[i][j] = w1;
                            path1[i][j] = 1;
                        } else {
                            level1[i][j] = w2;
                            path1[i][j] = 2;
                        }
                    } else {
                        level1[i][j] = Double.NEGATIVE_INFINITY;
                    }

                    if (j > 0) {
                        double w1 = level3[i][j - 1] + sigma;
                        double w2 = level2[i][j - 1] + ro + sigma;
                        if (w1 > w2) {
                            level3[i][j] = w1;
                            path3[i][j] = 1;
                        } else {
                            level3[i][j] = w2;
                            path3[i][j] = 2;
                        }
                    } else {
                        level3[i][j] = Double.NEGATIVE_INFINITY;
                    }

                    if (i == 0 && j == 0) {
                        level2[i][j] = 0;
                    } else {
                        level2[i][j] = Double.NEGATIVE_INFINITY;
                    }
                    if (i > 0) {
                        if (level1[i][j] > level2[i][j]) {
                            level2[i][j] = level1[i][j];
                            path2[i][j] = 1;
                        }
                    }
                    if (j > 0) {
                        if ((level3[i][j]) > level2[i][j]) {
                            level2[i][j] = level3[i][j];
                            path2[i][j] = 2;
                        }
                    }
                    if (i > 0 && j > 0) {
                        double w = 0;
                        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                            w = level2[i - 1][j - 1] + match;
                        } else {
                            w = level2[i - 1][j - 1] + mismatch;
                        }
                        if (w > level2[i][j]) {
                            level2[i][j] = w;
                            path2[i][j] = 3;
                        }
                    }
                }

            }
            int curI = s1.length();
            int curJ = s2.length();
            StringBuilder a1 = new StringBuilder();
            StringBuilder a2 = new StringBuilder();

            int level = 0;

            while (curI > 0 || curJ > 0) {
                if (level == 0) {
                    switch (path2[curI][curJ]) {
                        case 1:
                            level = 1;
                            break;
                        case 2:
                            level = 2;
                            break;
                        case 3:
                            curI--;
                            curJ--;
                            a1.append(s1.charAt(curI));
                            a2.append(s2.charAt(curJ));
                            break;
                    }
                } else if (level == 1) {
                    if (path1[curI][curJ] == 2) {
                        level = 0;
                    }
                    curI--;
                    a1.append(s1.charAt(curI));
                    a2.append("-");

                } else if (level == 2) {
                    if (path1[curI][curJ] == 2) {
                        level = 0;
                    }
                    curJ--;
                    a1.append("-");
                    a2.append(s2.charAt(curJ));
                }
            }
            //System.out.println(matrix[s1.length()][s2.length()]);

            System.out.println(a1.reverse().toString());
            System.out.println(a2.reverse().toString());
        }
    }
