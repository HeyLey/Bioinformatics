import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class ForwardBackwardAlingnment {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("strings.txt"));

        String str1 = br.readLine();
        String str2 = br.readLine();

        List<Character> alphabet = Arrays.asList('A', 'T', 'G', 'C');

        int[] s1 = toInts(str1, alphabet);  // A - 0, T - 1, G - 2, C - 3
        int[] s2 = toInts(str2, alphabet);  //

        int n = s1.length;
        int m = s2.length;

        double[][] p = new double[][]{  // вероятности появления пары (i, j)
                {8, 1, 1, 1},
                {1, 8, 1, 1},
                {1, 1, 8, 1},
                {1, 1, 1, 8},
        };

        normalize(p);

        double[] q = new double[]{1.1, 1, 1.2, 1}; // вероятность для гэпов
                            //    A    T   G   C
        normalize(q);

        double delta = 0.2;     // вероятность перехода из M в X или из M в Y
        double tau = 0.01;      // вероятность перехода в конечное состояние
        double epsilon = 0.3;   // вероятность перехода из X в X или из Y в Y

        // forward
        double[][] f_m = new double[s1.length + 1][s2.length + 1];  // вороять выровнять первые i символов 1й строки и первые j символов 2й строки и закончить в состоянии М
        double[][] f_x = new double[s1.length + 1][s2.length + 1];  // ... закончить в состоянии Х
        double[][] f_y = new double[s1.length + 1][s2.length + 1];  // ... закончить в состоянии Y

        f_m[0][0] = 1;


        // реккурентое вычисление f_m, f_x, f_y
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                if (i > 0 && j > 0) {
                    f_m[i][j] = p[s1[i - 1]][s2[j - 1]] * (
                            (1 - 2 * delta - tau) * get(f_m, i - 1, j - 1)
                                    + (1 - epsilon - tau) * (get(f_x, i - 1, j - 1) + get(f_y, i - 1, j - 1))
                    );
                }
                if (i > 0) {
                    f_x[i][j] = q[s1[i - 1]] * (delta * get(f_m, i - 1, j) + epsilon * get(f_x, i - 1, j));
                }
                if (j > 0) {
                    f_y[i][j] = q[s2[j - 1]] * (delta * get(f_m, i, j - 1) + epsilon * get(f_y, i, j - 1));
                }
            }
        }

        double f_e = tau * (f_m[n][m] + f_x[n][m] + f_y[n][m]); // вероятность получить пару строчек s1 и s2

        // backward
        double[][] b_m = new double[s1.length + 1][s2.length + 1];  // вероятность начать с состояния М и получить s1 после символа i и s2 после символа j
        double[][] b_x = new double[s1.length + 1][s2.length + 1];  // вероятность начать с Х ...
        double[][] b_y = new double[s1.length + 1][s2.length + 1];  // вероятность начать с Y ...

        b_m[n][m] = tau;
        b_x[n][m] = tau;
        b_y[n][m] = tau;

        // реккурентое вычисление b_m, b_x, b_y
        for (int i = n; i >= 0; i--) {
            for (int j = m; j >= 0; j--) {
                if (i < n && j < m) {
                    b_m[i][j] = (1 - 2 * delta - tau) * p[s1[i]][s2[j]] * b_m[i + 1][j + 1] +
                            delta * (q[s1[i]] * b_x[i + 1][j] + q[s2[j]] * b_y[i][j + 1]);

                    b_x[i][j] = (1 - epsilon - tau) * p[s1[i]][s2[j]] * b_m[i + 1][j + 1] +
                            epsilon * q[s1[i]] * b_x[i + 1][j];
                    b_y[i][j] = (1 - epsilon - tau) * p[s1[i]][s2[j]] * b_m[i + 1][j + 1] +
                            epsilon * q[s2[j]] * b_y[i][j + 1];
                }
                if (i < n && j == m) {
                    b_m[i][j] = delta * (q[s1[i]] * b_x[i + 1][j]);
                    b_x[i][j] = epsilon * q[s1[i]] * b_x[i + 1][j];
                }
                if (j < m && i == n) {
                    b_m[i][j] = delta * (q[s2[j]] * b_y[i][j + 1]);
                    b_y[i][j] = epsilon * q[s2[j]] * b_y[i][j + 1];
                }
            }
        }

        DecimalFormat format = new DecimalFormat("0.00");

        // печать результата
        for (int j = 0; j < m; j++) {
            System.out.print("    " + str2.charAt(j));
        }
        System.out.println();

        for (int i = 0; i < n; i++) {
            System.out.print(str1.charAt(i) + "  ");
            for (int j = 0; j < m; j++) {
                double prob = f_m[i+1][j+1] * b_m[i+1][j+1] / f_e;
                System.out.print(format.format(prob) + " ");
            }
            System.out.println();
        }
    }

    private static double get(double[][] f, int i, int j) { // если индекс выходит за границы массива, возвращается 0
        if (i < 0 || j < 0) {
            return 0;
        }
        return f[i][j];
    }

    private static int[] toInts(String str, List<Character> alphabet) {  // перевод строки в числа
        int[] s = new int[str.length()];

        for (int i = 0; i < str.length(); i++) {
            int index = alphabet.indexOf(str.charAt(i));
            if (index == -1) {
                throw new RuntimeException("Wrong character " + str.charAt(i));
            }
            s[i] = index;
        }
        return s;
    }

    private static void normalize(double[] q) { // нормализация гэпов
        double sum = 0;

        for (int i = 0; i < q.length; i++) {
            sum += q[i];
        }

        for (int i = 0; i < q.length; i++) {
            q[i] /= sum;
        }
    }

    private static void normalize(double[][] p) { // нормализация весов появления пары (i, j)
        double sum = 0;

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                sum += Math.pow(p[i][j], 2);
            }
        }

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p.length; j++) {
                p[i][j] /= Math.sqrt(sum);
            }
        }
    }
}