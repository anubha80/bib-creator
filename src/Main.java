import netscape.javascript.JSObject;

import java.io.BufferedReader;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        System.out.println("Welcome to BibCreator!\n\n");
        Scanner[] sc = new Scanner[11];
        int i = 0;
        try {

            // open all .bib files

            for (i = 1; i < 11; i++) {
                String fileName = "./input/Latex" + i + ".bib";
                File currentFile = new File(fileName);
                if (!currentFile.exists()) {
                    throw new FileNotFoundException("Could not open input file Latex" + i + ".bib for reading. \n\nPlease check if it exists! Program will terminate after closing any opened files.\n\n");
                }
                sc[i] = new Scanner(new FileInputStream(fileName));
            }
        } catch (NullPointerException npe) {
            System.out.println(npe.getMessage());
            System.exit(0);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println(fileNotFoundException.getMessage());
            closeAllFilesBeforeExit(sc, i);
            System.exit(0);
        }

        // create  IEEE ACM NJ format files for all .bib files

        for (i = 1; i < 11; i++) {

            try {
                File myIEEEFile = new File("./output/IEEE" + i + ".json");
                File myACMFile = new File("./output/ACM" + i + ".json");
                File myNJFile = new File("./output/NJ" + i + ".json");

                myIEEEFile.createNewFile();
                myACMFile.createNewFile();
                myNJFile.createNewFile();

            } catch (NullPointerException npe) {
                System.out.println(npe.getMessage());
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }

        ProcessFiles processFiles = new ProcessFiles();
        processFiles.processFilesForValidation(sc);


        System.out.println("\nPlease enter the name of one of the files you want to review: ");
        Scanner scn = new Scanner(System.in);
        String nameOfFileToBeRead;
        int chances = 2;
        while (scn.hasNext()) {
            nameOfFileToBeRead = scn.next();
            boolean isSuccessfulRead = readAndPrintFile("./output/" + nameOfFileToBeRead);

            if (!isSuccessfulRead) {
                chances--;
                if (chances <= 0) {
                    System.out.println("\nSorry! I am unable to display your desired files! Program will exit!\n");
                    break;
                }
                System.out.println("However, you will be allowed another chance to enter another filename");
            } else {
                chances = 2;
            }
            System.out.println("\nPlease enter the name of one of the files you want to review: ");
        }
    }

    public static boolean readAndPrintFile(String nameOfFileToBeRead) {

        File file = new File(nameOfFileToBeRead);

        if (!file.exists()) {
            System.out.println("Could not open input file. File does not exist; possibly it could not have been created.\n\n");
            return false;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
            return false;
        }
        return true;
    }

    public static void closeAllFilesBeforeExit(Scanner[] sc, int end) {
        for (int i = 1; i < end; i++) {
            sc[i].close();
        }
    }
}