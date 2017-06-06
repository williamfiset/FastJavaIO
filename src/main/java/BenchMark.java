/**
 * This benchmark file tests the performance of the InputReader
 * relative to Java's BufferedReader class
 * @author William Fiset
 **/

import java.io.*;
import fastjavaio.InputReader;

public class BenchMark {

  static boolean readingFromSTDIN = true;

  final static int TRIALS = 20;

  final static String INT_FILE = "../resources/integer_data/integers.txt";
  final static String DOUBLE_FILE = "../resources/double_data/doubles.txt";
  final static String STR_FILE = "../resources/string_data/short_strings_spaces.txt";

  static void readFile_BufferedReader_readLine() throws IOException {

    String line;
    double time = 0;

    for (int i = 0; i < TRIALS; i++ ) {

      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      if (!readingFromSTDIN) {
        File f = new File(STR_FILE);
        br = new BufferedReader(new FileReader(f));
      }

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

      BufferedReader br;
      if (readingFromSTDIN) {
        br = new BufferedReader(new InputStreamReader(System.in));
      } else {
        File f = new File(STR_FILE);
        br = new BufferedReader(new FileReader(f));
      }

      long start = System.nanoTime();
      while( (line=br.readLine()) != null ) {
        split = line.split(" ");
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println( "BufferedReader .readLine() and linesplit: " + time);

  }

  static void readFile_InputReader_nextLine() throws IOException  {

    double time = 0;
    String line;
    for (int i = 0; i < TRIALS; i++ ) {

      InputReader in = new InputReader();
      File f = new File(STR_FILE);
      if (!readingFromSTDIN) in = new InputReader(new FileInputStream(f));

      long start = System.nanoTime();
      while( (line=in.nextLine()) != null ) {
        // System.out.println(line);
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println("InputReader .nextLine: " + time);
  }

  static void readFile_InputReader_nextString() throws IOException  {

    String str;
    double time = 0;

    for (int i = 0; i < TRIALS; i++ ) {

      File f = new File(STR_FILE);
      InputReader in = new InputReader();
      if (!readingFromSTDIN)
        in = new InputReader(new FileInputStream(f));

      long start = System.nanoTime();
      while( (str=in.nextString()) != null ) {
        // System.out.println(line);
      }
      long end = System.nanoTime();
      time += ((end-start)/1e9);

    }

    System.out.println("InputReader .nextString: " + time);

  }

  static void readFile_BufferedReader_readLine_Double_dot_valueOf() throws IOException {

    // File f = new File(DOUBLE_FILE);
    // BufferedReader br = new BufferedReader(new FileReader(f));

    File f = new File(DOUBLE_FILE);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    if (!readingFromSTDIN)
      br = new BufferedReader(new FileReader(f));

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
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    if (!readingFromSTDIN)
      br = new BufferedReader(new FileReader(f));

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

  static void readFile_InputReader_nextInt() throws IOException {

    File f = new File(INT_FILE);
    InputReader in = new InputReader(System.in);
    if (!readingFromSTDIN)
      in = new InputReader(new FileInputStream(f));

    String line;

    long start = System.nanoTime();
    long sum = 0, i = 0;

    try {
      while(true) {
        int integer = in.nextInt();
        sum += integer;
        // System.out.println(sum);
        // i++;
        // if (i%1000 == 0) System.out.println(i + " " + integer);
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .nextInt(): " + (end-start)/1e9 );

  }

  static void readFile_InputReader_nextDouble() throws IOException {

    File f = new File(DOUBLE_FILE);
    InputReader in = new InputReader(System.in);
    if (!readingFromSTDIN)
      in = new InputReader(new FileInputStream(f));

    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        double d = in.nextDouble();
        sum += d;
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .nextDouble(): " + (end-start)/1e9 );

  }  

  static void readFile_InputReader_nextDoubleFast() throws IOException {

    File f = new File(DOUBLE_FILE);
    InputReader in = new InputReader(System.in);
    if (!readingFromSTDIN)
      in = new InputReader(new FileInputStream(f));

    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        double d = in.nextDoubleFast();
        sum += d;
      }
    } catch (java.io.IOException e) { }

    long end = System.nanoTime();
    System.out.println( "InputReader .nextDoubleFast(): " + (end-start)/1e9 );

  }  

  public static void main(String[] args) throws IOException {

    // Standard Input Tests
    // timeReadingDoubleDataFromSTDIN();
    // timeReadingIntDataFromSTDIN();
    // timeReadingStringDataFromSTDIN();

    readingFromSTDIN = false;

    // Reading from Files test
    timeReadingStringDataFromFile();
    timeReadingIntDataFromFile();
    timeReadingDoubleDataFromFile();

  }

  static void timeReadingStringDataFromFile() throws IOException {
    System.out.println("\nPerformance of reading string data from a file: ");
    readFile_BufferedReader_readLine();
    readFile_BufferedReader_readLine_with_linesplit();
    readFile_InputReader_nextString();
    readFile_InputReader_nextLine();
  }

  static void timeReadingIntDataFromFile() throws IOException {
    System.out.println("\nPerformance of reading int data from a file: ");
    readFile_BufferedReader_readLine_parseInt();
    readFile_InputReader_nextInt();
  }

  static void timeReadingDoubleDataFromFile() throws IOException {
    System.out.println("\nPerformance of reading double data from a file: ");
    readFile_BufferedReader_readLine_Double_dot_valueOf();
    readFile_InputReader_nextDouble();
    readFile_InputReader_nextDoubleFast();
  }

  static void timeReadingStringDataFromSTDIN() throws IOException {

    System.out.println("\nPerformance of reading string data from STDIN: ");

    System.in.mark(Integer.MAX_VALUE);
    readFile_BufferedReader_readLine();
    System.in.reset();
    
    System.in.mark(Integer.MAX_VALUE);
    readFile_BufferedReader_readLine_with_linesplit();
    System.in.reset();

    System.in.mark(Integer.MAX_VALUE);
    readFile_InputReader_nextString();
    System.in.reset();
    
    System.in.mark(Integer.MAX_VALUE);
    readFile_InputReader_nextLine();
    System.in.reset();

  }

  static void timeReadingIntDataFromSTDIN() throws IOException {

    System.out.println("\nPerformance of reading int data from STDIN: ");
    
    System.in.mark(Integer.MAX_VALUE);
    readFile_BufferedReader_readLine_parseInt();
    System.in.reset();
    
    System.in.mark(Integer.MAX_VALUE);
    readFile_InputReader_nextInt();
    System.in.reset();

  }

  static void timeReadingDoubleDataFromSTDIN() throws IOException {

    System.out.println("\nPerformance of reading double data from STDIN: ");
    
    System.in.mark(Integer.MAX_VALUE);
    readFile_BufferedReader_readLine_Double_dot_valueOf();
    System.in.reset();

    System.in.mark(Integer.MAX_VALUE);
    readFile_InputReader_nextDouble();
    System.in.reset();

    System.in.mark(Integer.MAX_VALUE);
    readFile_InputReader_nextDoubleFast();
    System.in.reset();

  }


}

