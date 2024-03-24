import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        new WordFrequencies("testFile");

        new CompactFile("abba-1", "outputFileTest", 1,7);
    }
}