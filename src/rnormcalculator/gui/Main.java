package rnormcalculator.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class Main {

    public static void main(String args[]) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder("\"C:\\Program Files\\R\\R-3.3.3\\bin\\x64\\Rscript.exe\" \"C:\\Program Files\\R\\R-3.3.3\\bin\\x64\\ciao.txt\"");

        //        builder.command(
//                ""
//        );

        Process process = builder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        process.waitFor();

        String substring = reader.readLine().substring(4);
        System.out.println(substring);



    }

}