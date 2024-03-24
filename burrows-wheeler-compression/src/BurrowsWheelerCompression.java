import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BurrowsWheelerCompression {
    public static void main(String[] args) {
        //new BurrowsWheelerTransform("abra0.txt");
        new BurrowsWheelerTransform("bible-en.txt");
    }
    public static class BurrowsWheelerTransform{
        private static int I = 0;
        private static int amountOfInts = 0;

        public BurrowsWheelerTransform(String filePath){
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("output.txt"))) {
                StringBuilder buildFile = new StringBuilder();

                String wholeFile;
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    int c;
                    while ((c = reader.read()) != -1) {
                        char character = (char) c;
                        buildFile.append(character);
                        amountOfInts++;
                    }
                    buildFile.append('$');

                    wholeFile = buildFile.toString();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String outputFromBWT = computeBWT(wholeFile);

                System.out.println(outputFromBWT);

                writer.close();

                MTF(outputFromBWT);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public static String computeBWT(String input) {
            SuffixArray suffixArray = new SuffixArray();

            char[] charArray = input.toCharArray();

            int[] inputSuffixArray = suffixArray.sufsort(charArray);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < inputSuffixArray.length; i++) {
                char character = input.charAt(((inputSuffixArray.length-1) + inputSuffixArray[i] - 1) % (inputSuffixArray.length-1));
                if (inputSuffixArray[i]  == 0){
                    I = i;
                }
                if (character != '$'){
                    sb.append(character);
                }
            }

           // System.out.println(sb.toString());

            return sb.toString();
        }

        public void MTF(String input){
            List<Integer> mtfOutput = mtfTransform(input);
          //  System.out.println(mtfOutput);

            new HuffmanCodingAltered(mtfOutput, amountOfInts, I);
        }

        public static List<Integer> mtfTransform(String input) {
            List<Integer> mtfList = initializeMTFList();
            List<Integer> mtfOutput = new ArrayList<>();

            for (char c : input.toCharArray()) {
                int index = mtfList.indexOf((int) c);
                mtfOutput.add(index);
                mtfList.remove(index);
                mtfList.addFirst((int) c);
            }

            return mtfOutput;
        }

        public static List<Integer> initializeMTFList() {
            List<Integer> mtfList = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                mtfList.add(i);
            }
            return mtfList;
        }
    }

    private static class SuffixArray{
        public int[] sufsort(char[] A) {
            int N = A.length;
            int[] I = new int[N+1]; //suffix array efter sortering
            int[] V = new int[N+1]; //sort keys (group numbers)
            int[] L = new int[N+1];  //grupper som behöver mer sortering

            var ref = new Object() {
                int h = 0;
            };

            Comparator<Integer> compareSuffixes = (s, t) -> {
                if (s == '$') return -1;
                if (t == '$') return 1;

                int skey = s + ref.h < N ? V[s + ref.h] : -1;
                int tkey = t + ref.h < N ? V[t + ref.h] : -1;
                return Integer.compare(skey, tkey);
            };

            for (int i = 0; i < N; i++){
                I[i] = i;
                V[i] = A[i];
            }

            I[N] = N;

            L[0] = N+1;

            while (L[0] > -N - 1) {
                int i;
                int k;
                int sl = 0;

                for (i = 0; i <= N; i = k){
                    if (L[i] < 0){
                        k = i - L[i];
                        sl += L[i];
                    }
                    else {
                        k = i + L[i];
                        if (sl < 0){
                            L[i+sl] = sl;
                            sl = 0;
                        }

                        Integer[] J = Arrays.stream(Arrays.copyOfRange(I, i, k)).boxed().toArray(Integer[]::new);
                        Arrays.sort(J, compareSuffixes);

                        for (int j = i; j < k; j++) {
                            I[j] = J[j - i];
                        }

                        for (int f = i, g; f < k; f = g+1){
                            g = f;

                            while (g+1 < k && compareSuffixes.compare(I[g+1], I[f]) == 0){
                                g++;
                            }

                            if (f == g){
                                L[f] = -1;
                            }
                            else {
                                L[f] = g-f+1;
                            }
                        }

                        for (int f = i, g; f < k; f = g+1){
                            g = f + Math.abs(L[f]) -1;
                            for (int j = f; j <= g; j++){
                                V[I[j]] = g;
                            }
                        }
                    }
                }
                if (sl < 0) {
                    L[i+sl] = sl;
                }
                if (ref.h == 0) {
                    ref.h = 1;
                } else {
                    ref.h = 2* ref.h;
                }
            }
            return I;
        }
    }
}