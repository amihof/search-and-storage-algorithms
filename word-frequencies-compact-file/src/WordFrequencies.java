import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordFrequencies {
  Map<String, Integer> wordCounts = new HashMap<>();

    public WordFrequencies(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int c;
            StringBuilder word = new StringBuilder();

            while ((c = reader.read()) != -1) {
                char character = (char) c;

                if (Character.isLetter(character)) {
                    word.append(character);
                } else if (word.length() > 0) {
                    String wordStr = word.toString().toLowerCase();
                    wordCounts.put(wordStr, wordCounts.getOrDefault(wordStr, 0) + 1);
                    word.setLength(0);
                }
            }

            if (word.length() > 0) {
                String wordStr = word.toString().toLowerCase();
                wordCounts.put(wordStr, wordCounts.getOrDefault(wordStr, 0) + 1);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }


}
