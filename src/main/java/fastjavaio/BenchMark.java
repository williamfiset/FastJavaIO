import static java.lang.Math.*;
import java.util.*;
import java.io.*;

import fastjavaio.InputReader;

public class BenchMark {

  final static int TRIALS = 20;

  // integers_small.txt
  final static String INT_FILE = "../resources/integer_data/integers.txt";

  final static String DOUBLE_FILE = "../resources/double_data/doubles.txt";

  final static String STR_FILE = "../resources/string_data/short_strings_spaces.txt";

  static void readFile_BufferedReader_readLine() throws IOException {

    String line;
    double time = 0;

    for (int i = 0; i < TRIALS; i++ ) {

      File f = new File(STR_FILE);
      BufferedReader br = new BufferedReader(new FileReader(f));

      long start = System.nanoTime();
      while( (line=br.readLine()) != null ) {
        // System.out.println(line);
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println( "BufferedReader .readLine(): " + time);

  }

  static void readFile_BufferedReader_readLine_with_linesplit() throws IOException {

    String line;
    String[] split;
    double time = 0;

    for (int i = 0; i < TRIALS; i++ ) {

      File f = new File(STR_FILE);
      BufferedReader br = new BufferedReader(new FileReader(f));

      long start = System.nanoTime();
      while( (line=br.readLine()) != null ) {
        split = line.split(" ");
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println( "BufferedReader .readLine() and linesplit: " + time);

  }

  static void readFile_InputReader_readLine() throws IOException  {

    double time = 0;
    String line;
    for (int i = 0; i < TRIALS; i++ ) {

      File f = new File(STR_FILE);
      InputReader in = new InputReader(new FileInputStream(f));
      // InputReader in = new InputReader(new BufferedInputStream(new FileInputStream(f),1<<16));
      long start = System.nanoTime();
      while( (line=in.readLine()) != null ) {
        // System.out.println(line);
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println("InputReader .readLine: " + time);
  }

  static void readFile_InputReader_readStr() throws IOException  {

    double time = 0;
    String str;
    for (int i = 0; i < TRIALS; i++ ) {

      File f = new File(STR_FILE);
      InputReader in = new InputReader(new FileInputStream(f));
      long start = System.nanoTime();
      while( (str=in.readStr()) != null ) {
        // System.out.println(line);
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println("InputReader .readStr: " + time);

  }

  static void readFile_BufferedReader_readLine_Double_dot_valueOf() throws IOException {

    File f = new File(DOUBLE_FILE);
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line;

    long start = System.nanoTime();
    String[] split;
    double sum = 0;

    while( (line=br.readLine()) != null ) {
      split = line.split(" ");
      for (int i = 0; i < split.length; i++ ) {
        sum += Double.valueOf(split[i]);
      }
    }

    long end = System.nanoTime();
    System.out.println("BufferedReader split() and Double.valueOf(): " + (end-start)/1e9 );
    // System.out.println(sum);

  }

  static void readFile_BufferedReader_readLine_parseInt() throws IOException {

    File f = new File(INT_FILE);
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line;

    long start = System.nanoTime();
    String[] split;
    long sum = 0;

    while( (line=br.readLine()) != null ) {
      split = line.split(" ");
      for (int i = 0; i < split.length; i++ ) {
        sum += Integer.parseInt(split[i]);
      }
    }

    long end = System.nanoTime();
    System.out.println("BufferedReader split() and parseInt(): " + (end-start)/1e9 );

  }

  static void readFile_InputReader_readInt() throws IOException {

    File f = new File(INT_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    String line;

    long start = System.nanoTime();
    long sum = 0, i = 0;

    try {
      while(true) {
        int integer = in.readInt();
        sum += integer;
        // System.out.println(sum);
        // i++;
        // if (i%1000 == 0) System.out.println(i + " " + integer);
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .readInt(): " + (end-start)/1e9 );

  }

  static void readFile_InputReader_readDouble() throws IOException {

    File f = new File(DOUBLE_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        double d = in.readDouble();
        sum += d;
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .readDouble(): " + (end-start)/1e9 );

  }  

  static void readFile_InputReader_readDoubleFast() throws IOException {

    File f = new File(DOUBLE_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        double d = in.readDoubleFast();
        sum += d;
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .readDoubleFast(): " + (end-start)/1e9 );

  }  

  public static void main(String[] args) throws IOException {

    System.out.println("Double reading performance BenchMark: ");
    readFile_BufferedReader_readLine_Double_dot_valueOf();
    readFile_InputReader_readDouble();
    readFile_InputReader_readDoubleFast();

    System.out.println("\nInteger reading performance BenchMark: ");
    readFile_BufferedReader_readLine_parseInt();
    readFile_InputReader_readInt();

    System.out.println("\nString reading performance BenchMark: ");
    readFile_BufferedReader_readLine();
    readFile_BufferedReader_readLine_with_linesplit();
    readFile_InputReader_readStr();
    readFile_InputReader_readLine();

  }

}

