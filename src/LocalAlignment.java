import java.io.*;

/*
Локальное выравнивание
сдано
 */
public class LocalAlignment {
    public static void main(String[] args) throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader("strings.txt"));

        String s1 = fr.readLine();
        String s2 = fr.readLine();

        double match = 1.0;
        double mismatch = -0.5;
        double gap = -1.5;

        int sizeS1 = s1.length();
        int sizeS2 = s2.length();

        double[][] weight = new double[sizeS1 + 1][sizeS2 + 1];
        int[][] path = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= sizeS1; i++) {
            for (int j = 0; j <= sizeS2; j++) {

                weight[i][j] = 0;
                path[i][j] = -1;

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
        double best = Double.NEGATIVE_INFINITY;

        for (int i = 0; i <= sizeS1; i++) {
            for (int j = 0; j <= sizeS2; j++) {
                if (weight[i][j] > best) {
                    best = weight[i][j];
                    curI = i;
                    curJ = j;
                }
            }
        }

        StringBuilder a1 = new StringBuilder();
        StringBuilder a2 = new StringBuilder();

        a1.append(new StringBuilder(s1.substring(curI).toLowerCase()).reverse());
        a2.append(new StringBuilder(s2.substring(curJ).toLowerCase()).reverse());

        while (path[curI][curJ] != -1) {
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

        a1.append(new StringBuilder(s1.substring(0, curI).toLowerCase()).reverse());
        a2.append(new StringBuilder(s2.substring(0, curJ).toLowerCase()).reverse());

        if (curI < curJ) {
            for (int i = 0; i < curJ - curI; i++) {
                a1.append(" ");
            }
        } else {
            for (int i = 0; i < curI - curJ; i++) {
                a2.append(" ");
            }
        }


       // System.out.println(weight[sizeS1][sizeS2]);

        System.out.println(a1.reverse().toString());
        System.out.println(a2.reverse().toString());

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
        }*/

    }
}
