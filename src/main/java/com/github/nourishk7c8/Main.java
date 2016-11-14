package com.github.nourishk7c8;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Nourish Khan (nourish.khan@gmail.com) on 11/7/2016.
 */
public class Main {

    private static final double PI = 3.14;
    private static final double PSI = 6.39485;
    private static final double ZETA = 3.2;
    private static final String patternControlRecord = "^[*][0-9]+[ ]+[2-3]$";
    private static final String patternThreeParam = "[0-9]+[ ]+[0-9]+[a-z]";
    private static final String patternTwoParam = "[0-9]+[ ]+[0-9]+";

    public static void main(String[] args) throws Exception {


        String fileName = "src/test/input.txt";
        try (Scanner fileScanner = new Scanner(new FileReader(fileName)))
        {
            if (fileScanner.hasNextInt()) {
                int totalSamples = fileScanner.nextInt();
                fileScanner.nextLine();
                String s;

                char[][]data;
                double[] results;
                double[] avg;
                Scanner stringScanner;
                while (totalSamples > 0 && fileScanner.hasNextLine()) {
                    s = fileScanner.nextLine();
                    if (Pattern.matches(patternControlRecord, s)) {
                        System.out.println("\nControl set: " + s);
                        stringScanner = new Scanner(s);
                        int rows = stringScanner.skip("[*]").nextInt();
                        int cols = stringScanner.nextInt();
                        if (cols != 2 && cols != 3)
                            throw new Exception("Records may only have 2 or 3 parameters; encountered " + cols +" Program terminated");
                        //TODO: remove exception, change to sop, gets to next matching control pattern? new method to find matching pattern?
                        results = new double[rows];
                        avg = new double[rows];
                        data = new char[rows][cols];
                        processRecords(data, results, avg, fileScanner);
                        //if(results != null && avg != null) printResults(results, avg, data);
                    } else {
                        throw new Exception("Expected an input of the form: \"*# #\", but found \"" + s + "\". " +
                                "Program terminated.");
                        //TODO: remove exception, change to sop, gets to next matching control pattern?
                    }
                    totalSamples--;
                }
            } else
                throw new Exception("Expected an integer value at the start of file. Program terminated");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }//end catch


    }//end main

    public static double calc(int num1, int num2) {
        return PI * num1 + PSI * (1.34 + Math.pow(num2, 3));
    }//end calc

    public static double calc(int num1, int num2, char c) {
        int e;
        switch (c) {
            case 'b':
                e = 4;
                break;
            case 'c':
                e = 6;
                break;
            default:
                e = 3;
        }
        return PI * num1 + PSI * (1.34 + Math.pow(num2, 3)) + Math.pow(ZETA, e);
    }//end calc

    public static void processRecords(char[][] data, double[]results, double[] avg,Scanner fileScanner) throws Exception{
        boolean error = false;
        int index = 0;
        int r = data.length;
        int c = data[0].length;
        Scanner stringScanner;
        for (int i = 0; i < r && fileScanner.hasNextLine(); i++) {
            String record = fileScanner.nextLine();
            if (c == 2 && Pattern.matches(patternTwoParam, record)) {
                stringScanner = new Scanner(record);
                int num1 = stringScanner.nextInt();
                int num2 = stringScanner.nextInt();
                data[index][0] = (char) num1;
                data[index][1] = (char) num1;
                results[index] = calc(num1, num2);
                avg[index++] = (num1+num2)/2;

            } else if (c == 3 && Pattern.matches(patternThreeParam, record)) {
                stringScanner = new Scanner(record);
                int num1 = stringScanner.nextInt();
                int num2 = stringScanner.nextInt();
                char e = stringScanner.next().charAt(0);
                data[index][0] = (char) num1;
                data[index][1] = (char) num1;
                data[index][2] = e;
                results[index] = calc(num1, num2, e);
                avg[index++] = (num1+num2)/2;

            } else {
                throw new Exception("Expected number of columns does not match actual number of columns. Program terminated");
/*                System.out.println("Expected number of columns does not match actual number of columns. Skipping this data set.");
                error = true;
                while(i < r-1 && fileScanner.hasNextLine()){
                    i++;
                    fileScanner.nextLine();
                }*/
            }
        }//end for
        if(!error)
            printResults(results, avg, data);
    }//end processRecords

    public static void printResults(double[] results, double[] avg, char[][] data){
        int cols = data[0].length;
        if(cols == 2){
            for(int i = 0; i < results.length; i++){
                System.out.println(String.format("AVG = %,.3f", avg[i]));
                System.out.println(String.format("FORM = %,.3f", results[i]));
            }
        }
        else {
            for(int i = 0; i < results.length; i++){
                System.out.println(String.format("AVG 3 = %,.3f", avg[i]));
                System.out.println(String.format("FORM 3 = %,.3f", results[i]));
            }
        }
    }//end printResults
}
