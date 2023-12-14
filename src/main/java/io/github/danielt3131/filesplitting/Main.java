/*
 * Copyright (c) 2023 Daniel J. Thompson.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 or later.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.danielt3131.filesplitting;

import java.io.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        long chunkSize = 1000 * 1000;  // 1 MiB
        String inputName = "./input";
        String outputName;
        Scanner scanner = new Scanner(System.in);
        byte buffer[] = new byte[(int) chunkSize];  // 1 MiB buffer
        System.out.println("Press 1 to split the file input or press 2 to merge the split files back together");
        int selection =  scanner.nextInt();
        if (selection == 1){
            File inputFile = new File(inputName);
            if (!inputFile.exists() || !inputFile.isFile()){
                return;
            }
            long numberOfChunks = inputFile.length() / chunkSize;
            System.out.printf("Size : %d\n", inputFile.length() / chunkSize);
            long remainderChunkSize = inputFile.length() % chunkSize;
            try {
                FileInputStream inputStream = new FileInputStream(inputFile);
                long i;
                for (i = 0; i < numberOfChunks; i++) {
                    inputStream.read(buffer, 0, buffer.length);
                    outputName = String.format("%s.%d.bin", inputName, i + 1);
                    FileOutputStream outputStream = new FileOutputStream(outputName);
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.close();
                }
                if (remainderChunkSize != 0){
                    inputStream.read(buffer, 0, (int) chunkSize);
                    outputName = String.format("%s.%d.bin", inputName, i + 1);
                    FileOutputStream outputStream = new FileOutputStream(outputName);
                    outputStream.write(buffer, 0, (int) remainderChunkSize);
                    outputStream.close();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (selection == 2){
            outputName = "./merged";
            FileOutputStream mergedFile = null;
            try {
                mergedFile = new FileOutputStream(outputName);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            long i = 0;
            String splitFileName;
            long splitFileSize = 0;
            boolean isCompleted = false;
            while (isCompleted == false){
                splitFileName = String.format("%s.%d.bin", inputName, i + 1);
                try {
                    File splitFile = new File(splitFileName);
                    if (splitFile.exists()) {
                        FileInputStream splitFileStream = new FileInputStream(splitFile);
                        splitFileSize = splitFile.length();
                        splitFileStream.read(buffer, 0, (int) splitFileSize);
                        mergedFile.write(buffer, 0, (int) splitFileSize);
                    } else {
                        isCompleted = true;
                        break;
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        }
    }
}