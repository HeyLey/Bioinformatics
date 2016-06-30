import java.io.*;

/*
Глобальное выравнивание
сдано
 */

public class GlobalAlignment {
    public static void main(String[] args) throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader("strings.txt"));

        String s1 = fr.readLine();
        String s2 = fr.readLine();

        double match = 1.0;
        double mismatch = -0.5;
        double gap = -0.24;

        int sizeS1 = s1.length();
        int sizeS2 = s2.length();

        double[][] weight = new double[sizeS1 + 1][sizeS2 + 1];
        int[][] path = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= sizeS1; i++) {
            for (int j = 0; j <= sizeS2; j++) {
                if (i == 0 && j == 0) {
                    weight[i][j] = 0;
                } else {
                    weight[i][j] = Double.NEGATIVE_INFINITY;
                }
                if (i > 0) {
                    if ((weight[i - 1][j] + gap) > weight[i][j]) {
                        weight[i][j] = weight[i - 1][j] + gap;
                        path[i][j] = 1;
                    }
                }
                if (j > 0) {
                    if ((weight[i][j - 1] + gap) > weight[i][j]) {
                        weight[i][j] = weight[i][j - 1] + gap;
                        path[i][j] = 2;
                    }
                }
                if (i > 0 && j > 0) {
                    double w = 0;
                    if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                        w = weight[i - 1][j - 1] + match;
                    } else {
                        w = weight[i - 1][j - 1] + mismatch;
                    }
                    if (w > weight[i][j]) {
                        weight[i][j] = w;
                        path[i][j] = 3;
                    }
                }
            }

        }
        int curI = sizeS1;
        int curJ = sizeS2;
        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();

        while (curI > 0 || curJ > 0) {
            switch (path[curI][curJ]) {
                case 1:
                    curI--;
                    a1.append(s1.charAt(curI));
                    a2.append("-");
                    break;
                case 2:
                    curJ--;
                    a1.append("-");
                    a2.append(s2.charAt(curJ));
                    break;
                case 3:
                    curI--;
                    curJ--;
                    a1.append(s1.charAt(curI));
                    a2.append(s2.charAt(curJ));
                    break;
            }
        }
        System.out.println("max: " + weight[sizeS1][sizeS2] + "\n");

        System.out.println(a1.reverse().toString());
        System.out.println(a2.reverse().toString());

        System.out.println();

      /* for (int i = 0; i < path.length; i++) {
            for (int j = 0; j < path.length; j++) {
                System.out.print(path[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println();

        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight.length; j++) {
                System.out.print(weight[i][j] + " ");
            }
            System.out.println();
        } */

    }
}