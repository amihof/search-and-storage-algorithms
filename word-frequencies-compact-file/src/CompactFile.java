import java.io.*;

public class CompactFile {

    private OutputStream outputStream;
    int bitbuf;
    int bitsinbuf;

    /**
     *
     * @param filePath The name of a file containing non-negative integers
     * @param outputFilePath The output path
     * @param B number of bytes per integer in the file. Possible values are 1, 2, 3, or 4.
     *          (Corresponding to 8, 16, 24, or 32 bits.)
     * @param bo number of bits per integer in the output
     */
    public CompactFile(String filePath, String outputFilePath, int B, int bo){
        try {
            outputStream = new FileOutputStream(outputFilePath);

            FileInputStream fileInput = new FileInputStream(filePath);

            int input;

            bitbuf = 0;
            bitsinbuf = 0;

            while ((input = fileInput.read()) != -1){
                produceBits(input, bo * B);
            }

            finish();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
    lägger till b bits till höger och lägger in de nya bitsen
     */
    public void produceBits(int x, int b) throws IOException {
        bitbuf = (bitbuf << b) | (x & ((1 << b) - 1));
        bitsinbuf += b;
        flushBits();
    }

    /*
    hämtar de 8 mest significant bits
     */
    public void flushBits() throws IOException {
        while (bitsinbuf >= 8) {
            int output = bitbuf >>> (bitsinbuf - 8);

            System.out.println(output & 0xFF);

            outputStream.write(output);
            bitsinbuf -= 8;
        }
    }

    public void finish() throws IOException {
        flushBits();
        if (bitsinbuf > 0) {
             bitbuf <<= (8 - bitsinbuf);
            outputStream.write(bitbuf);
        }
        outputStream.close();
    }
}
