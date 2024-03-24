import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class HuffmanCodingAltered {

    public static int totalOutput = 0;
    public static String[] codewordArray = new String[256];

    public HuffmanCodingAltered(List<Integer> input, int amountOfInts, int I){
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream("output.txt"))) {
            StringBuilder sb = new StringBuilder();
            for (int i = 31; i >= 0; i--) {
                sb.append((amountOfInts >> i) & 1);
            }

            writer.write(sb.toString() + "\n");
            totalOutput += sb.toString().length();

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 31; i >= 0; i--) {
                stringBuilder.append((I >> i) & 1);
            }

            System.out.println(stringBuilder.toString());
            writer.write(stringBuilder.toString() + "\n");
            totalOutput += stringBuilder.toString().length();


            Map<Integer, Integer> charFrequencyHashMap = countCharacterFrequency(input);

            List<Map.Entry<Integer, Integer>> charFrequencyList = new ArrayList<>(charFrequencyHashMap.entrySet());

            charFrequencyList.sort(Comparator.comparing(Map.Entry::getValue));

            StringBuilder stringBuilder1 = new StringBuilder();
            for (int i = 31; i >= 0; i--) {
                stringBuilder1.append((amountOfChars >> i) & 1);
            }

            totalOutput += stringBuilder1.toString().length();

            PriorityQueue<Node> priorityQueue = new PriorityQueue<>(charFrequencyList.size(), new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.frequency - o2.frequency;
                }
            });


            for (Map.Entry value: charFrequencyList) {
                Node newNode = new Node((Integer) value.getKey(), (Integer) value.getValue());
                priorityQueue.add(newNode);
            }

            Node root = null;

            while (priorityQueue.size() > 1){
                Node node1 = priorityQueue.poll();

                Node node2 = priorityQueue.poll();

                Node parent = new Node(-1, node1.frequency + node2.frequency);
                parent.leftChild = node1;
                parent.rightChild = node2;

                node1.depth = parent.depth + 1;
                node2.depth = parent.depth + 1;

                priorityQueue.add(parent);
            }

            root = priorityQueue.poll();

            createCodeWord(root, "");

            System.out.println(Arrays.toString(codewordArray));

            for (int c = 0; c < 256; c++){
                if (codewordArray[c] == null){
                    System.out.print("0");
                    writer.write("0");

                    totalOutput += 1;
                } else {
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
                    writer.write(codewordArray[c] + "\n");

                    totalOutput += codewordArray[c].length();
                }

            }

            System.out.println();
            writer.write("\n");

            for (Integer integer : input) {
                System.out.println(codewordArray[integer]);
                writer.write(codewordArray[integer] + "\n");
                totalOutput += codewordArray[integer].length();
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

        if (root.leftChild == null && root.rightChild == null && root.character != -1) {
            codewordArray[root.character] = s;
            return;
        }

        createCodeWord(root.leftChild, s + "0");
        createCodeWord(root.rightChild, s + "1");
    }

    public static int amountOfChars = 0;

    public static Map<Integer, Integer> countCharacterFrequency(List<Integer> intList) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        for (int intValue : intList) {
            if (frequencyMap.containsKey(intValue)) {
                frequencyMap.put(intValue, frequencyMap.get(intValue) + 1);
            } else {
                frequencyMap.put(intValue, 1);
            }
        }

        return frequencyMap;
    }

    public static class Node {
        public Node leftChild;
        public Node rightChild;
        public int character;
        public int frequency;

        public int depth = 1;

        public Node(int character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }
    }

}