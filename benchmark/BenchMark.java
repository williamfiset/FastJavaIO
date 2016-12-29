import static java.lang.Math.*;
import java.util.*;
import java.io.*;

public class BenchMark {

  final static int TRAILS = 20;

  // integers_small.txt
  final static String INT_FILE = "integer_data/integers.txt";

  final static String STR_FILE = "string_data/short_strings_spaces.txt";

  static void readFile_BufferedReader_readLine() throws IOException {

    String line;
    double time = 0;

    for (int i = 0; i < TRAILS; i++ ) {

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

    for (int i = 0; i < TRAILS; i++ ) {

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

  static void readFile_InputReader_readAll() throws IOException  {

    double time = 0;

    for (int i = 0; i < TRAILS; i++ ) {

      File f = new File(STR_FILE);
      InputReader in = new InputReader(new FileInputStream(f));
      long start = System.nanoTime();
      in.readAll();
      long end = System.nanoTime();
      time += ((end-start)/1e9);
      
    }

    System.out.println("InputReader .readAll: " + time);

  }

  static void readFile_InputReader_readLine() throws IOException  {

    double time = 0;
    String line;
    for (int i = 0; i < TRAILS; i++ ) {

      File f = new File(STR_FILE);
      InputReader in = new InputReader(new FileInputStream(f));
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
    for (int i = 0; i < TRAILS; i++ ) {

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
    System.out.println( (end-start)/1e9 );
    System.out.println(sum);

  }

  static void readFile_InputReader_readInt() throws IOException {

    File f = new File(INT_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        int integer = in.readInt();
        sum += integer;
      }
    } catch (java.util.InputMismatchException e) { }

    long end = System.nanoTime();
    System.out.println( (end-start)/1e9 );
    System.out.println(sum);

  }  

  public static void main(String[] args) throws IOException {

    readFile_BufferedReader_readLine();
    readFile_BufferedReader_readLine_with_linesplit();
    readFile_InputReader_readStr();
    readFile_InputReader_readLine();
    readFile_InputReader_readAll();

  }

}

