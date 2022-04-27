import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ProcessFiles {

    public static void processFilesForValidation(Scanner[] sc) {

        int validFilesCount = 10;

        for (int i = 1; i < 11; i++) {

            try {
                ArrayList<String> authors = new ArrayList<String>();
                ArrayList<String> journals = new ArrayList<String>();
                ArrayList<String> titles = new ArrayList<String>();
                ArrayList<String> years = new ArrayList<String>();
                ArrayList<String> volumes = new ArrayList<String>();
                ArrayList<String> numbers = new ArrayList<String>();
                ArrayList<String> pages = new ArrayList<String>();
                ArrayList<String> dois = new ArrayList<String>();
                ArrayList<String> issns = new ArrayList<String>();
                ArrayList<String> months = new ArrayList<String>();

                int articleCounter = 0;

                String str;

                // loop to read the .bib file
                while (sc[i].hasNext() == true) {

                    str = sc[i].nextLine();
                    if (str.contains("@ARTICLE")) {
                        articleCounter++;
                    }
                    if (str.contains("{}")) {

                        String problem = findProblem(str);
                        String errorMessage = "Error: Detected Empty field!\n******************************\n\n" +
                                "\nProblem detected with input file: Latex" + i + ".bib\n" +
                                "File is invalid: Field \"" + problem + "\" is empty. Processing stopped at this point other empty fields may be present as well!\n";
                        validFilesCount --;

                        // delete already created file
                        File myIEEEFile = new File("./output/IEEE" + i + ".json");
                        File myACMFile = new File("./output/ACM" + i + ".json");
                        File myNJFile = new File("./output/NJ" + i + ".json");
                        myACMFile.delete();
                        myNJFile.delete();
                        myIEEEFile.delete();

                        //close scanner
                        sc[i].close();

                        //throw exception with appropriate message
                        throw new FileInvalidException(errorMessage);
                    }
                    if (str.contains("author={")) {
                        String st1 = str.substring(8, str.length() - 3);
                        authors.add(st1);
                    }
                    if (str.contains("journal={")) {
                        String st1 = str.substring(9, str.length() - 3);
                        journals.add(st1);
                    }
                    if (str.contains("title={")) {
                        String st1 = str.substring(7, str.length() - 3);
                        titles.add(st1);
                    }
                    if (str.contains("year={")) {
                        String st1 = str.substring(6, str.length() - 3);
                        years.add(st1);
                    }
                    if (str.contains("volume={")) {
                        String st1 = str.substring(8, str.length() - 3);
                        volumes.add(st1);
                    }
                    if (str.contains("number={")) {
                        String st1 = str.substring(8, str.length() - 3);
                        numbers.add(st1);
                    }
                    if (str.contains("pages={")) {
                        String st1 = str.substring(7, str.length() - 3);
                        pages.add(st1);
                    }
                    if (str.contains("doi={")) {
                        String st1 = str.substring(5, str.length() - 3);
                        dois.add(st1);
                    }
                    if (str.contains("ISSN={")) {
                        String st1 = str.substring(6, str.length() - 3);
                        issns.add(st1);
                    }
                    if (str.contains("month={")) {
                        String st1 = str.substring(7, str.length() - 2);
                        months.add(st1);
                    }
                }

                sc[i].close();

                // write to file with array list values

                BufferedWriter bwIEEE = new BufferedWriter(
                        new FileWriter("./output/IEEE" + i + ".json", false));
                BufferedWriter bwACM = new BufferedWriter(
                        new FileWriter("./output/ACM" + i + ".json", false));
                BufferedWriter bwNJ = new BufferedWriter(
                        new FileWriter("./output/NJ" + i + ".json", false));

                for (int k = 1; k <= articleCounter; k++) {

                    // IEEE
                    bwIEEE.write(authors.get(k - 1).replace(" and", ",") + ". \"" + titles.get(k - 1) + "\", " + journals.get(k - 1) + ", vol. " + volumes.get(k - 1) + ", no. " + numbers.get(k - 1) + ", p. " + pages.get(k - 1) + ", " + months.get(k - 1) + " " + years.get(k - 1) + ".");
                    bwIEEE.write("\n\n");

                    // ACM
                    bwACM.write("[" + k + "]  " + " " + authors.get(k - 1).split("and")[0] + "et al. " + years.get(k - 1) + ". " + titles.get(k - 1) + ". " + journals.get(k - 1) + ". " + volumes.get(k - 1) + ", " + numbers.get(k - 1) + " (" + years.get(k - 1) + "), " + pages.get(k - 1) + ". " + "DOI:https://doi.org/" + dois.get(k - 1) + ".");
                    bwACM.write("\n\n");

                    // NJ
                    bwNJ.write(authors.get(k - 1).replace("and", "&") + ". " + titles.get(k - 1) + ". " + journals.get(k - 1) + ". " + volumes.get(k - 1) + ", " + pages.get(k - 1) + "(" + years.get(k - 1) + ").");
                    bwNJ.write("\n\n");

                }

                bwIEEE.close();
                bwACM.close();
                bwNJ.close();

            } catch (FileInvalidException fileInvalidException) {
                System.out.println(fileInvalidException.getMessage());
            } catch (IOException ioException) {
                System.out.println(ioException.getMessage());
            }
        }  // end of main for-loop


        System.out.println("A total of "+ (10 - validFilesCount) + " were invalid, and could not be processed. All other " + validFilesCount + " \"Valid\" files have been created");
    }   // end of function

    // function to find the missing field in file

    public static String findProblem(String str) {

        if (str.contains("author")) {
            return "author";
        }
        if (str.contains("journal")) {
            return "journal";
        }
        if (str.contains("year")) {
            return "year";
        }
        if (str.contains("title")) {
            return "title";
        }
        if (str.contains("volume")) {
            return "volume";
        }
        if (str.contains("number")) {
            return "number";
        }
        if (str.contains("pages")) {
            return "pages";
        }
        if (str.contains("keywords")) {
            return "keywords";
        }
        if (str.contains("doi")) {
            return "doi";
        }
        if (str.contains("ISSN")) {
            return "ISSN";
        }
        if (str.contains("month")) {
            return "month";
        }
        return "null";
    }
}
