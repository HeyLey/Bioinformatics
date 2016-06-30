


/*
Проведите поиск с помощью BLAST (nucleotide blast) поиск случайной последовательности (длиной 100, 1000, 100000)
в базе данных последовательностей нуклеотидов (nucleotide collection).

http://blast.ncbi.nlm.nih.gov/Blast.cgi
*/

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class BlastGenerator {

    public static void main(String[] args) throws IOException {

        int n[] = {100, 1000, 100000, 10000};

        for (int l : n) {
            FileWriter writer = new FileWriter(l + "output.txt");

            Random random = new Random();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < l; i++) {
                sb.append(new char[]{'A', 'T', 'G', 'C'}[random.nextInt(4)]);
            }
            String text = String.valueOf(sb);
            writer.write(text);
            writer.close();
        }

    }
}
