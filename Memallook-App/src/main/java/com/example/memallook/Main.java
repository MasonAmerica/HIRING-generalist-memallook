package com.example.memallook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Memallook started, please initialize with two integers 'pageSize' and 'numberOfPages':");
        Scanner in = new Scanner(System.in);
        Memallook memallook = null;
        while (true) {
            String input = in.nextLine();
            try {
                String[] params = input.split(" ");
                switch (params[0]) {
                    case "alloc": {
                        String bytesToAllocate = params[1];
                        if (memallook == null) {
                            System.out.println("Memallook is is not initialized, run with 2 numbers.");
                        } else if (!bytesToAllocate.matches("\\d+")) {
                            System.out.println("Command 'alloc' requires a single integer as a parameter. Try again. ");
                        } else {
                            char pointerToAllocated = memallook.alloc(Integer.parseInt(bytesToAllocate));
                            System.out.println(String.format("Allocated %s bytes at pointer %s.", bytesToAllocate, pointerToAllocated));
                        }
                        break;
                    }
                    case "dealloc": {
                        String pointerToDeallocate = params[1];
                        if (memallook == null) {
                            System.out.println("Memallook is is not initialized, run with 2 numbers.");
                        } else if (!pointerToDeallocate.matches("^[a-zA-Z]$")) {
                            System.out.println("Command 'dealloc' requires a single letter of the alphabet as a parameter. Try again. ");
                        } else {
                            memallook.dealloc(pointerToDeallocate.charAt(0));
                            System.out.println(String.format("Deallocated block of memory at pointer %s.", pointerToDeallocate));
                        }
                        break;
                    }
                    case "show": {
                        if (memallook == null) {
                            System.out.println("Memallook is is not initialized, run with 2 numbers.");
                        } else {
                            memallook.show(System.out);
                        }
                        break;
                    }
                    case "clear": {
                        if (memallook != null) {
                            memallook.clear();
                            System.out.println("Please re-initialize with two integers 'pageSize' and 'numberOfPages':");
                        }
                        break;
                    }
                    case "exit":
                        System.exit(0);
                    default: {
                        if (params.length != 2 || !params[0].matches("\\d+") || !params[1].matches("\\d+")) {
                            System.out.println("Initialize command requires two integers, try a known command or enter two integers");
                        } else {
                            memallook = new Memallook(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                            System.out.println("Done. Enter next command.");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(String.format("Exception parsing arguments: %s, try again", e.getMessage()));
            }
        }

    }

//    public static void main(String[] args) throws Exception {
//        String arg0 = args[0];
//        if (arg0.equals("write")) {
//            write(args[1]);
//        } else if (arg0.equals("read")) {
//            read();
//        } else if (arg0.equals("clear")) {
//            clear();
//        }
//        //System.out.println("Hello World: " + argsString);
//    }

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

}
