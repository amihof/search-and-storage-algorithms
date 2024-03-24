import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class RadixSorting {

    public static String[] readStringsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            return br.lines().toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static void main(String[] args) {
        RadixSort radixSort = new RadixSort();

        //  radixSort.getDigit("abcabc", 2, 7);


        //String[] array = readStringsFromFile("strangestrings.txt");
        //String[] array = readStringsFromFile("someones.txt");
        //String[] array = readStringsFromFile("ints.txt");
        String[] array = readStringsFromFile("bible-lines.txt");


        //Arrays.stream(array).forEach(System.out::println);

        MSD.sort(array, radixSort);

        Arrays.stream(array).forEach(System.out::println);

    }
    public static class RadixSort implements DigitGetter<Object> {


        /*
        Digit i of elem, counting from zero,
        when i is smaller than the length (in digits) in the sort key of elem.
        Digit 0 is most significant, digit 1 second most significant, and so on, up to the length.
        By a digit we mean a chunk consisting of bitsPerDigit bits:
        an integer from zero to 2^bitsPerDigit−1.

        −1 when i is greater than or equal to the length of elem.
         */
        @Override
        public int getDigit(Object element, int i, int bitsPerDigit) {
            String str = element.toString();

            if (i >= str.length())
                return -1;

            int digit = (int) Math.pow(2, bitsPerDigit) - 1;

            int charValue = str.charAt(i);

            return charValue & digit;
        }
    }


    public static class MSD {
        public static <T> void sort(String[] a, DigitGetter<T> dg) {
            int N = a.length;
            String[] aux = new String[N];

            //börjar med hela arrayen
            sort(a, aux, dg, 0, a.length - 1, 0);

        }

        private static <T> void sort(String[] a, String[] aux, DigitGetter<T> dg, int lo, int hi, int d) {
            if (hi <= lo) return;

            int R;
            int BITS_PER_CHAR = 8;

            if (hi - lo <= 1500){
                R = 256;
                BITS_PER_CHAR = 8;
            }else {
                R = 65536;
                BITS_PER_CHAR = 16;
            }

            int[] count = new int[R + 2];

            for (int i = lo; i <= hi; i++) {
                int digit = dg.getDigit((T) a[i], d, BITS_PER_CHAR);
                count[digit + 2]++;
            }

            //så vi vet vart vi ska starta när vi lägger ut nya
            for (int r = 0; r < R + 1; r++) {
                count[r + 1] += count[r];
            }

            for (int i = lo; i <= hi; i++) {
                int digit = dg.getDigit((T) a[i], d, BITS_PER_CHAR);
                aux[count[digit + 1]++] = a[i];
            }

            for (int i = lo; i <= hi; i++) {
                a[i] = aux[i - lo];
            }

            //rekursivt sortera beroende på nästa digit
            for (int r = 0; r < R; r++) {
                sort(a, aux, dg, lo + count[r], lo + count[r + 1] - 1, d + 1);
            }

        }

    }

    public interface DigitGetter<T> {
        int getDigit(T element, int i, int bitsPerDigit);
    }
}