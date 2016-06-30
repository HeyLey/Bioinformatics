import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/*
Матрица весов
*/

public class GlobalAlingnmentWithWM {
    public static void main(String[] args) throws IOException {
        BufferedReader fr = new BufferedReader(new FileReader("strings.txt"));

        String s1 = fr.readLine();
        String s2 = fr.readLine();

        double matrix[][] =  {
                //         A   R   N   K  gap
           /*  A */      { 5, -2, -1, -1, -1},
           /*  R */      {-2,  7, -1, -3, -1},
           /*  N */      {-1, -1,  7,  0, -1},
           /*  K */      {-1, -3,  0,  6, -1},
           /* gap */     {-1, -1, -1, -1,  0}
        };

        List<Character> amino = Arrays.asList('A', 'R', 'N', 'K');

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
                    char ch1 = s1.charAt(i - 1);
                    double gap = matrix[amino.indexOf(ch1)][4];
                    if ((weight[i - 1][j] + gap) > weight[i][j]) {
                        weight[i][j] = weight[i - 1][j] + gap;
                        path[i][j] = 1;
                    }
                }
                if (j > 0) {
                    char ch2 = s2.charAt(j - 1);
                    double gap = matrix[4][amino.indexOf(ch2)];
                    if ((weight[i][j - 1] + gap) > weight[i][j]) {
                        weight[i][j] = weight[i][j - 1] + gap;
                        path[i][j] = 2;
                    }
                }
                if (i > 0 && j > 0) {
                    double w = 0;
                    char ch1 = s1.charAt(i - 1);
                    char ch2 = s2.charAt(j - 1);

                    w = weight[i - 1][j - 1] + matrix[amino.indexOf(ch1)][amino.indexOf(ch2)];

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

      /*  for (int i = 0; i < path.length; i++) {
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
