package com.example.memallook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        String arg0 = args[0];
        if (arg0.equals("write")) {
            write(args[1]);
        } else if (arg0.equals("read")) {
            read();
        } else if (arg0.equals("clear")) {
            clear();
        }
        //System.out.println("Hello World: " + argsString);
    }

    public static void write(String toWrite) throws IOException {
        FileWriter fileWriter = new FileWriter("buffer.txt");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(toWrite);
        printWriter.close();
    }

    public static void read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("buffer.txt"));
        System.out.println(reader.lines().findFirst().get());
    }

    public static void clear() {

    }

    //Init: number number
    //alloc: number
    //dealloc:
    //3. `memallook show`
    //4. `memallook clear`

}
