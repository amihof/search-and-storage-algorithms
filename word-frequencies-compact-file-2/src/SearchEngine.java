import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SearchEngine {

    public SearchEngine(){
        String filePath = "oldhouse.txt";

        TrieST<Set<Integer>> invertedIndex = buildInvertedIndex(filePath);

        queryLoop(invertedIndex);
    }

    public static void queryLoop(TrieST<Set<Integer>> invertedIndex) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.print("search?> ");
                String input = reader.readLine().trim();

                String[] queryWords = input.split("\\s+");

                Set<Integer> outputList = new HashSet<>();

                for (String queryWord : queryWords) {
                    Set<Integer> wordSet = invertedIndex.get(queryWord);
                    if (wordSet != null) {
                        if (outputList.isEmpty()) {
                            outputList.addAll(wordSet);
                        } else {
                            outputList.retainAll(wordSet);
                        }
                    } else {
                        outputList.clear();
                        break;
                    }
                }

                System.out.print("matches: ");
                boolean first = true;
                for (int record : outputList) {
                    if (!first) {
                        System.out.print(",");
                    }
                    System.out.print(record);
                    first = false;
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TrieST<Set<Integer>> buildInvertedIndex(String filePath) {
        TrieST<Set<Integer>> invertedIndex = new TrieST<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;

                String[] words = line.split("\\s+");
                for (String word : words) {
                    Set<Integer> lineNumbers = invertedIndex.get(word);
                    if (lineNumbers == null) {
                        lineNumbers = new HashSet<>();
                        invertedIndex.put(word, lineNumbers);
                    }
                    lineNumbers.add(lineNumber);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invertedIndex;
    }

    public static class TrieST<Value>{

        private class Node {
            private Value val;
            private HashMap<Character, Node> children;

            Node() {
                children = new HashMap<>();
            }
        }

        private Node root;

        public TrieST() {
            root = new Node();
        }

        public void put(String key, Value value) {
            root = put(root, key, value, 0);
        }

        private Node put(Node x, String key, Value value, int d) {
            if (x == null) {
                x = new Node();
            }
            if (d == key.length()) {
                x.val = value;
                return x;
            }
            char c = key.charAt(d);
            x.children.put(c, put(x.children.get(c), key, value, d + 1));
            return x;
        }

        public Value get(String key) {
            Node x = get(root, key, 0);
            return x == null ? null : x.val;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.children.get(c), key, d + 1);
        }

    }



}
