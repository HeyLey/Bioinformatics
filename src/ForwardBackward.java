import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class ForwardBackward {
    public static void main(String[] args) throws IOException {
        BufferedReader fr = new BufferedReader(new InputStreamReader(System.in));
        String line = fr.readLine();

        int[] o = new int[line.length()];
        for (int i = 0; i < o.length; i++) {
            o[i] = line.charAt(i) - '0';
        }

        double[] emissionM = new double[]{0.9, 0.1};  // вер-ть наблюдения 0 и 1 в состоянии -
        double[] emissionP = new double[]{0.1, 0.9};  // вер-ть наблюдения 0 и 1 в состоянии +

        // 0 - состояние -
        // 1 - состояние +
        double[][] transition = new double[][]{   // вероятность перехода
                {0.7, 0.3},   // 0.7 ( - -> -)       0.3 ( - -> +)
                {0.2, 0.8}    // 0.2 ( + -> -)       0.8 ( + -> +)
        };

        double[][] alpha = new double[line.length()][2];
        double[][] betta = new double[line.length() + 1][2];

        alpha[0][0] = 0.5 * emissionM[o[0]];
        alpha[0][1] = 0.5 * emissionP[o[0]];

        // Forward
        for (int i = 1; i < o.length; i++) {
            for (int s = 0; s < 2; s++) {
                double sum = 0.0;
                for (int j = 0; j < 2; j++) {
                    sum += alpha[i - 1][j] * transition[j][s];
                }
                alpha[i][s] = sum;
            }
            alpha[i][0] *= emissionM[o[i]];
            alpha[i][1] *= emissionP[o[i]];
        }

        betta[o.length][0] = 1;
        betta[o.length][1] = 1;


        // Backward
        for (int i = o.length - 1; i >= 0; i--) {
            for (int s = 0; s < 2; s++) {
                double sum = 0.0;
                for (int j = 0; j < 2; j++) {
                    sum += betta[i + 1][j] * transition[s][j];
                }
                betta[i][s] = sum;
            }
            betta[i][0] *= emissionM[o[i]];
            betta[i][1] *= emissionP[o[i]];
        }

        alpha[0][0] = 0.5 * emissionM[o[0]];
        alpha[0][1] = 0.5 * emissionP[o[0]];

        double[][] p = new double[line.length()][2];

        // Вычисление вероятности p'
        for (int i = 0; i < o.length; i++) {
            for (int s = 0; s < 2; s++) {
                double sum = 0.0;
                for (int j = 0; j < 2; j++) {
                    sum += betta[i + 1][j] * transition[s][j];
                }
                p[i][s] = sum * alpha[i][s];
            }

            final double sum = p[i][0] + p[i][1];
            // нормирование
            for (int s = 0; s < 2; s++) {
                p[i][s] /= sum;
            }
        }

        DecimalFormat format = new DecimalFormat("0.00");

        System.out.print("- ");

        // ответ для -
        for (int i = 0; i < o.length; i++) {
            System.out.print(format.format(p[i][0]) + " ");
        }
        System.out.println();

        System.out.print("+ ");

        // ответ для +
        for (int i = 0; i < o.length; i++) {
            System.out.print(format.format(p[i][1]) + " ");
        }
        System.out.println();
    }
}

//Test 01010101010000000000101010101