import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SubstringSearch {
    public static void main(String[] args) {

        SuffixArray sufArray = new SuffixArray();

        String fileName = "mississippi";
        //String fileName = "mississippi";

        String str = readFromFile(fileName);

        char[] A = (str + "$").toCharArray();

        int[] suffixArray = sufArray.sufsort(A);

        //System.out.println(Arrays.toString(suffixArray));

        search(suffixArray);

    }

    private static void search(int[] suffixArray) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("search?> ");
                String input = reader.readLine();

                int lowerBoundIndex = lowerBound(input, suffixArray);
                int upperBoundIndex = upperBound(input, suffixArray);

                if (lowerBoundIndex < text.length() && text.substring(suffixArray[lowerBoundIndex], Math.min(suffixArray[lowerBoundIndex] + input.length(), text.length())).equals(input)) {
                    for (int i = lowerBoundIndex; i < upperBoundIndex; i++) {
                        int end = Math.min(suffixArray[i] + 10 + input.length(), text.length());
                        String followingChars = text.substring(suffixArray[i] + input.length(), end);
                        System.out.println(suffixArray[i] + ": " + text.substring(suffixArray[i], suffixArray[i] + input.length()) + followingChars);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static int lowerBound(String query, int[] suffixArray) {
        int low = 0;
        int high = text.length();

        while (low < high) {
            int mid = (low + high) / 2;
            if (text.substring(suffixArray[mid], Math.min(suffixArray[mid] + query.length(), text.length())).compareTo(query) < 0) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    private static int upperBound(String query, int[] suffixArray) {
        int low = 0;
        int high = text.length();

        while (low < high) {
            int mid = (low + high) / 2;
            if (text.substring(suffixArray[mid], Math.min(suffixArray[mid] + query.length(), text.length())).compareTo(query) <= 0) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    private static String text;
    public static String readFromFile(String filePath){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int c;
            while ((c = br.read()) != -1) {
                sb.append((char) c);
            }
            text = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static class SuffixArray{
        public int[] sufsort(char[] A) {
            int N = A.length;
            int[] I = new int[N+1]; //suffix array efter sortering
            int[] V = new int[N+1]; //sort keys (group numbers)
            int[] L = new int[N+1];  //grupper som behöver mer sortering

            var ref = new Object() {
                int h = 0;
            };

            Comparator<Integer> compareSuffixes = (s, t) -> {
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