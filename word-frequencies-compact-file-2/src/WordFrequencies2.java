import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class WordFrequencies2 {

    public void printTrie() {
        printTrie(trie.root, new StringBuilder());
    }

    private void printTrie(TrieST.Node node, StringBuilder prefix) {
        if (node == null) return;
        if (node.val != null) {
            System.out.println(prefix.toString() + " - " + node.val);
        }
        for (Object c : node.children.keySet()) {
            prefix.append(c);
            printTrie((TrieST.Node) node.children.get(c), prefix);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    public TrieST<Integer> trie = new TrieST<>();
    public WordFrequencies2(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            int c;
            StringBuilder word = new StringBuilder();

            while ((c = reader.read()) != -1) {
                char character = (char) c;

                if (Character.isLetter(character)) {
                    word.append(character);
                } else if (word.length() > 0) {
                    String wordStr = word.toString().toLowerCase();
                    trie.put(wordStr, trie.get(wordStr) == null ? 1 : trie.get(wordStr) + 1);
                    word.setLength(0);
                }
            }

            if (word.length() > 0) {
                String wordStr = word.toString().toLowerCase();
                trie.put(wordStr, trie.get(wordStr) == null ? 1 : trie.get(wordStr) + 1);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        printTrie();
    }

    public class TrieST<Value>{

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
