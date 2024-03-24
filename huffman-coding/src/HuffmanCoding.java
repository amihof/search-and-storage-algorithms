import java.io.*;
import java.util.*;

public class HuffmanCoding {
    public static void main(String[] args) {
        new HuffmanCoding();
    }

    public static class HuffmanCoding{

        public static int totalOutput = 0;
        public static String[] codewordArray = new String[256];

        public HuffmanCoding(){
            String filePath = "abra.txt";
            //String filePath = "bible-en.txt";

            Map<Character, Integer> charFrequencyHashMap = countCharacterFrequency(filePath);

            List<Map.Entry<Character, Integer>> charFrequencyList = new ArrayList<>(charFrequencyHashMap.entrySet());

            charFrequencyList.sort(Comparator.comparing(Map.Entry::getValue));

            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("output.txt"))) {
                // skriva ut antalet characters i filen
                StringBuilder sb = new StringBuilder();
                for (int i = 31; i >= 0; i--) {
                    sb.append((amountOfChars >> i) & 1);
                }

                writer.write(sb.toString() + "\n");

                System.out.println(sb.toString());

                totalOutput += sb.toString().length();

                PriorityQueue<Node> priorityQueue = new PriorityQueue<>(charFrequencyList.size(), new Comparator<Node>() {
                    @Override
                    public int compare(Node o1, Node o2) {
                        return o1.frequency - o2.frequency;
                    }
                });

                for (Map.Entry value: charFrequencyList) {
                    Node newNode = new Node((Character) value.getKey(), (Integer) value.getValue());
                    priorityQueue.add(newNode);
                }

                Node root = null;

                while (priorityQueue.size() > 1){
                    Node node1 = priorityQueue.poll();

                    Node node2 = priorityQueue.poll();

                    Node parent = new Node(null, node1.frequency + node2.frequency);
                    parent.leftChild = node1;
                    parent.rightChild = node2;

                    priorityQueue.add(parent);
                }

                root = priorityQueue.poll();

                createCodeWord(root, "");

                for (int c = 0; c < 256; c++){
                    if (codewordArray[c] == null){
                        System.out.print("0");
                        writer.write("0");
                        totalOutput += 1;
                    }
                    else {
                        if (c != 0){
                            if (codewordArray[c-1] == null){
                                System.out.println();
                                writer.write("\n");
                            }
                        }
                        int length = codewordArray[c].length();
                        for (int i = 0; i < length; i++){
                            System.out.print("1");
                            writer.write("1");
                            totalOutput += 1;
                        }
                        System.out.println("0");
                        writer.write("0\n");
                        System.out.println(codewordArray[c]);
                        writer.write(codewordArray[c]+"\n");
                        totalOutput += codewordArray[c].length();
                    }
                }

                System.out.println();
                writer.write("\n");

                for (int i = 0; i < wholeFile.length(); i++) {
                    System.out.println(codewordArray[wholeFile.charAt(i)]);
                    writer.write(codewordArray[wholeFile.charAt(i)] + "\n");
                    totalOutput+= codewordArray[wholeFile.charAt(i)].length();
                }

                int next8multiple = (next8(totalOutput));

                int padding = next8multiple - totalOutput;

                for (int i = 0; i < padding; i++){
                    System.out.print("0");
                    writer.write("0");
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        int next8(int n) {
            int bits = n & 7; // give us the distance to the previous 8
            if (bits == 0) return n;
            return n + (8-bits);
        }

        public static void createCodeWord(Node root, String s) {
            if (root == null) {
                return;
            }

            if (root.leftChild == null && root.rightChild == null && (root.character != null)) {
                codewordArray[(int) root.character] = s;
                return;
            }

            createCodeWord(root.leftChild, s + "0");
            createCodeWord(root.rightChild, s + "1");
        }

        public static int amountOfChars = 0;
        public static String wholeFile;

        public static Map<Character, Integer> countCharacterFrequency(String filePath) {
            Map<Character, Integer> charFrequencyMap = new HashMap<>();
            StringBuilder buildFile = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                int c;
                while ((c = reader.read()) != -1) {
                    char character = (char) c;
                    buildFile.append(character);
                    amountOfChars++;
                    if (charFrequencyMap.containsKey(character)) {
                        charFrequencyMap.put(character, charFrequencyMap.get(character) + 1);
                    } else {
                        charFrequencyMap.put(character, 1);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            wholeFile = buildFile.toString();

            return charFrequencyMap;
        }

        public static class Node {
            public Node leftChild;
            public Node rightChild;
            public Character character;
            public int frequency;

            public Node(Character character, int frequency) {
                this.character = character;
                this.frequency = frequency;
            }
        }

    }
}