package io.github.danielt3131.filesplitting;

import java.io.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        long chunkSize = 101048576;
        String inputName = "./input";
        String outputName;
        File inputFile = new File(inputName);
        if (!inputFile.exists() || !inputFile.isFile()){
            return;
        }
        byte buffer[] = new byte[(int) chunkSize];
        long numberOfChunks = inputFile.length() / chunkSize;
        System.out.printf("Size : %d\n", inputFile.length() / chunkSize);
        long remainderChunkSize = inputFile.length() % chunkSize;
        try {
            FileInputStream inputStream = new FileInputStream(inputFile);
            long i;
            for (i = 0; i < numberOfChunks; i++) {
                inputStream.read(buffer, 0, buffer.length);
                outputName = String.format("%s.%d", inputName, i);
                FileOutputStream outputStream = new FileOutputStream(outputName);
                outputStream.write(buffer, 0, buffer.length);
                outputStream.close();
            }
            if (remainderChunkSize != 0){
                inputStream.read(buffer, 0, (int) chunkSize);
                outputName = String.format("%s.%d", inputName, i);
                FileOutputStream outputStream = new FileOutputStream(outputName);
                outputStream.write(buffer, 0, (int) remainderChunkSize);
                outputStream.close();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}