import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Viterbi {
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String s = br.readLine(); // test 0010101010100000000111010111

        double[][] matrix = new double[s.length()][2];   // - +

        int[][] path = new int[s.length()][2];

        double[] emissionM = new double[]{0.9, 0.1};   // нечестная
        double[] emissionP = new double[]{0.5, 0.5};   // честная

        double[][] transition = new double[][] {
                {0.5, 0.5},  // 0.7 ( - -> -)       0.3 ( - -> +)
                {0.5, 0.5}   // 0.2 ( + -> -)       0.3 ( + -> +)
        };

        matrix[0][0] = 0.5 * emissionM[s.charAt(0) - '0'];
        matrix[0][1] = 0.5 * emissionP[s.charAt(0) - '0'];

        for (int i = 1; i < s.length(); i++) {
            double p0 = matrix[i - 1][0] * transition[0][0];
            double p1 = matrix[i - 1][1] * transition[1][0];
            if (p0 > p1) {
                matrix[i][0] = emissionM[s.charAt(i) - '0'] * p0;
                path[i][0] = 0;
            } else {
                matrix[i][0] = emissionM[s.charAt(i) - '0'] * p1;
                path[i][0] = 1;
            }

            p0 = matrix[i - 1][0] * transition[0][1];
            p1 = matrix[i - 1][1] * transition[1][1];
            if (p0 > p1) {
                matrix[i][1] = emissionP[s.charAt(i) - '0'] * p0;
                path[i][1] = 0;
            } else {
                matrix[i][1] = emissionP[s.charAt(i) - '0'] * p1;
                path[i][1] = 1;
            }
        }

        StringBuilder result = new StringBuilder();
        int currentState = 0;

        if (matrix[s.length() - 1][0] > matrix[s.length() - 1][1]) {
            currentState = 0;   // -
        } else {
            currentState = 1;   // +
        }

        for (int i = s.length() - 1; i >= 0; i--) {
            if (currentState == 0) {
                result.append("-");
            } else {
                result.append("+");
            }
            currentState = path[i][currentState];
        }

        System.out.println(result.reverse());
    }
}
