import static java.lang.Math.*;
import java.util.*;
import java.io.*;

public class BenchMark {

  final static String INT_FILE = "integers.txt"; // integers_small.txt
  final static String STR_FILE = "strings_small.txt";

  static void readFileBufferedReader() throws IOException {

    File f = new File(STR_FILE);
    BufferedReader br = new BufferedReader(new FileReader(f));
    String line;

    long start = System.nanoTime();

    while( (line=br.readLine()) != null ) {
      // System.out.println(line);
    }
    // System.out.println( br.readLine() );

    long end = System.nanoTime();
    System.out.println( (end-start)/1e9 );

  }

  static void readFileInputReader() throws IOException  {

    File f = new File(STR_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    long start = System.nanoTime();
    String all = in.readAll();
    // System.out.println(all);
    // try {
    //   while( in.readLine() != null ) { }
    // } catch (java.util.InputMismatchException e) { }
    long end = System.nanoTime();
    System.out.println( (end-start)/1e9 );

  }

  static void readFileBufferedReaderIntegers() throws IOException {

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

  static void readFileInputReaderIntegers() throws IOException {

    File f = new File(INT_FILE);
    InputReader in = new InputReader(new FileInputStream(f));
    String line;

    long start = System.nanoTime();
    long sum = 0;

    try {
      while(true) {
        int integer = in.readInt();
        System.out.println(integer);
        sum += integer;
      }
    } catch (java.util.InputMismatchException e) { }

    long end = System.nanoTime();
    System.out.println( (end-start)/1e9 );
    System.out.println(sum);

  }  

  public static void main(String[] args) throws IOException {

    readFileBufferedReader();
    readFileInputReader();

    // readFileInputReaderIntegers();
    // readFileBufferedReaderIntegers();

  }

}

