package javatests.com.williamfiset.fastjavaio;

import static org.junit.Assert.*;

import com.williamfiset.fastjavaio.InputReader;
import org.junit.*;
import java.util.*;
import java.io.*;

public class TestInputReader {

  // Create an input reader with an arbitrary buffer size
  public static InputReader getReader(String s) {
    if (s == null) return null;
    InputStream is = new ByteArrayInputStream(s.getBytes());
    int bufferSize = 1 + (int)(Math.random() * 50);
    return new InputReader(is, bufferSize);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIllegalBufferSz1() {
    new InputReader(0);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIllegalBufferSz2() {
    new InputReader(-1);
  }

  @Test
  public void testInputReaderExtremeBufferSizeValues() throws IOException {

    String s = " abcde \n\n\n 77\n     -34\n 575678 AAAA %%%%^^^^^\n -78787";

    // Try all buffer sizes from [1, 128]
    for (int bufferSize = 1; bufferSize <= 128; bufferSize++ ) {
      
      InputStream is = new ByteArrayInputStream(s.getBytes());
      InputReader in = new InputReader(is, bufferSize);

      assertEquals( "abcde", in.nextString() );
      assertEquals( 77, in.nextInt() );
      assertEquals( -34, in.nextInt() );
      assertEquals( 575678, in.nextInt() );
      assertEquals( "AAAA", in.nextString() );
      assertEquals( "%%%%^^^^^", in.nextString() );
      assertEquals( -78787, in.nextInt() );

    }

  }

  @Test
  public void testNextByte() throws IOException {

    String str = "-128 127 -1 -0 0 1";
    InputReader in = getReader(str);

    assertEquals( -128, in.nextByte());
    assertEquals(  127, in.nextByte());
    assertEquals(   -1, in.nextByte());
    assertEquals(    0, in.nextByte());
    assertEquals(    0, in.nextByte());
    assertEquals(    1, in.nextByte());

  }

  @Test
  public void testNextInt() throws IOException {

    // Test non negative values
    String str = " 0  1 2    3  \n 55 345 888234\n";
    InputReader in = getReader(str);

    assertEquals( 0, in.nextInt() );
    assertEquals( 1, in.nextInt() );
    assertEquals( 2, in.nextInt() );
    assertEquals( 3, in.nextInt() );
    assertEquals( 55, in.nextInt() );
    assertEquals( 345, in.nextInt() );
    assertEquals( 888234, in.nextInt() );

    // Test negative values
    str = "  -0 -1  -2    -3  \n -55 -345 -888234\n";
    in = getReader(str);

    assertEquals( 0, in.nextInt() );
    assertEquals( -1, in.nextInt() );
    assertEquals( -2, in.nextInt() );
    assertEquals( -3, in.nextInt() );
    assertEquals( -55, in.nextInt() );
    assertEquals( -345, in.nextInt() );
    assertEquals( -888234, in.nextInt() );

  }

  @Test
  public void testNextIntExtremes() throws IOException {

    // Test integer maximum and minimum values
    String str = Integer.MAX_VALUE + " " + Integer.MIN_VALUE;
    InputReader in = getReader(str);

    assertEquals( Integer.MAX_VALUE, in.nextInt() );
    assertEquals( Integer.MIN_VALUE, in.nextInt() );

    str = (Integer.MAX_VALUE-1) + " " + (Integer.MIN_VALUE+1);
    in = getReader(str);
    
    // Test integer maximum and minimum values +- 1
    assertEquals( Integer.MAX_VALUE-1, in.nextInt() );
    assertEquals( Integer.MIN_VALUE+1, in.nextInt() );

  }

  @Test
  public void testNextLong() throws IOException {

    // Test non negative values
    String str = "  0 1  2    3  \n 55 345 888234 1111111111111  839475594569048  \n";
    InputReader in = getReader(str);

    assertEquals( 0, in.nextLong() );
    assertEquals( 1, in.nextLong() );
    assertEquals( 2, in.nextLong() );
    assertEquals( 3, in.nextLong() );
    assertEquals( 55, in.nextLong() );
    assertEquals( 345, in.nextLong() );
    assertEquals( 888234, in.nextLong() );

    // Test negative values
    str = "  -0 -1  -2    -3  \n -55 -345 -888234 -1111111111111  -839475594569048  \n";
    in = getReader(str);

    assertEquals(  0L, in.nextLong() );
    assertEquals( -1L, in.nextLong() );
    assertEquals( -2L, in.nextLong() );
    assertEquals( -3L, in.nextLong() );
    assertEquals( -55L, in.nextLong() );
    assertEquals( -345L, in.nextLong() );
    assertEquals( -888234L, in.nextLong() );
    assertEquals( -1111111111111L, in.nextLong() );
    assertEquals( -839475594569048L, in.nextLong() );

  }

  @Test
  public void testNextLongExtremes() throws IOException {

    // Test integer maximum and minimum values
    String str = Long.MAX_VALUE + " " + Long.MIN_VALUE;
    InputReader in = getReader(str);

    assertEquals( Long.MAX_VALUE, in.nextLong() );
    assertEquals( Long.MIN_VALUE, in.nextLong() );

    str = (Long.MAX_VALUE-1) + " " + (Long.MIN_VALUE+1);
    in = getReader(str);
    
    // Test Long maximum and minimum values +- 1
    assertEquals( Long.MAX_VALUE-1, in.nextLong() );
    assertEquals( Long.MIN_VALUE+1, in.nextLong() );

  }

  @Test
  public void testnextIntArray() throws IOException {

    String str = " 1     2  3  4 -5 ";
    int[] array = {1,2,3,4,-5};
    InputReader in = getReader(str);
    int[] ar = in.nextIntArray(5);

    assertEquals(array.length, ar.length);
    for (int i = 0; i < array.length; i++)
      assertEquals(array[i], ar[i]);

  }

  @Test
  public void testNextString1()  throws IOException {

    String str = "a b c";
    InputReader in = getReader(str);
    
    assertEquals("a", in.nextString());
    assertEquals("b", in.nextString());
    assertEquals("c", in.nextString());

    str = "  a   b   c   ";
    in  = getReader(str);
    assertEquals("a", in.nextString());
    assertEquals("b", in.nextString());
    assertEquals("c", in.nextString());

    str = "  abcde   bb   ccc   ";
    in  = getReader(str);
    assertEquals("abcde", in.nextString());
    assertEquals("bb", in.nextString());
    assertEquals("ccc", in.nextString());

  }

  @Test
  public void testNextStringNewLines1()  throws IOException {

    String str = "a\n b\n c\n";
    InputReader in = getReader(str);
    
    assertEquals("a", in.nextString());
    assertEquals("b", in.nextString());
    assertEquals("c", in.nextString());

    str = "  a \n  \nb \n  c\n  \n \n d ";
    in  = getReader(str);
    assertEquals("a", in.nextString());
    assertEquals("b", in.nextString());
    assertEquals("c", in.nextString());
    assertEquals("d", in.nextString());

    str = "  \n \n abcde \n \n bb  \n \nccc\n   ";
    in  = getReader(str);
    assertEquals("abcde", in.nextString());
    assertEquals("bb", in.nextString());
    assertEquals("ccc", in.nextString());

    str = "Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   ";
    in  = getReader(str);
    assertEquals( in.nextString(), "Apple" );
    assertEquals( in.nextString(), "banana" );
    assertEquals( in.nextString(), "orange" );
    assertEquals( in.nextString(), "KiWi" );
    assertEquals( in.nextString(), "dragonFRuIt" );
    assertEquals( in.nextString(), "WatERmeOn" );
    assertEquals( in.nextString(), "PEARS" );
    assertNull( in.nextString() );

  }

  @Test
  public void testNextStringExtremes()  throws IOException {

    String str = "";
    InputReader in = getReader(str);
    assertNull(in.nextString());

    str = "\n";
    in = getReader(str);
    assertNull(in.nextString());

    str = "   ";
    in = getReader(str);
    assertNull(in.nextString());

    str = "\n\n\n\n\n";
    in = getReader(str);
    assertNull(in.nextString());

    str = "\n\n\nabcdef\n\n\n";
    in = getReader(str);
    assertEquals("abcdef", in.nextString());
    assertEquals(null, in.nextString());

    str = "\n\n\n    abcdef    \n\n\n";
    in = getReader(str);
    assertEquals("abcdef", in.nextString());
    assertEquals(null, in.nextString());
    assertEquals(null, in.nextString());

  }

  @Test
  public void testNextLine() throws IOException {

    String s = "hello\nworld\n";
    InputReader in = getReader(s);
    assertEquals("hello", in.nextLine());
    assertEquals("world", in.nextLine());
    assertEquals(null, in.nextLine());

    s = "a\nb\nc\n";
    in = getReader(s);
    assertEquals("a", in.nextLine());
    assertEquals("b", in.nextLine());
    assertEquals("c", in.nextLine());
    assertEquals(null, in.nextLine());

    s = "aaaa\nbbbb\ncccc\n";
    in = getReader(s);
    assertEquals("aaaa", in.nextLine());
    assertEquals("bbbb", in.nextLine());
    assertEquals("cccc", in.nextLine());
    assertEquals(null, in.nextLine());

    s = "Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   ";
    in = getReader(s);
    assertEquals(in.nextLine(), "Apple banana orange");
    assertEquals(in.nextLine(), "");
    assertEquals(in.nextLine(), " KiWi dragonFRuIt ");
    assertEquals(in.nextLine(), "   ");
    assertEquals(in.nextLine(), "  WatERmeOn  ");
    assertEquals(in.nextLine(), "");
    assertEquals(in.nextLine(), "");
    assertEquals(in.nextLine(), "PEARS");
    assertEquals(in.nextLine(), "");
    assertEquals(in.nextLine(), "   ");
    assertNull(in.nextLine());

  }

  @Test
  public void testNextLineExtremes() throws IOException {

    String s = "";
    InputReader in = getReader(s);
    assertEquals(null, in.nextLine());
    assertEquals(null, in.nextLine());

    s = " ";
    in = getReader(s);
    assertEquals(" ", in.nextLine());
    assertEquals(null, in.nextLine());    
    assertEquals(null, in.nextLine());    
    
    s = "\n";
    in = getReader(s);
    assertEquals("", in.nextLine());  
    assertEquals(null, in.nextLine());
    assertEquals(null, in.nextLine());

    s = "\n\n\nA\n\nB\n\n";
    in = getReader(s);
    assertEquals("", in.nextLine());  
    assertEquals("", in.nextLine());  
    assertEquals("", in.nextLine());  
    assertEquals("A", in.nextLine());  
    assertEquals("", in.nextLine());  
    assertEquals("B", in.nextLine());  
    assertEquals("", in.nextLine());  
    assertEquals(null, in.nextLine());
    assertEquals(null, in.nextLine());
    assertEquals(null, in.nextLine());

    s = "\n  A\n B\n C \n  \nD";
    in = getReader(s);
    assertEquals(in.nextLine(), "");
    assertEquals(in.nextLine(), "  A");
    assertEquals(in.nextLine(), " B");
    assertEquals(in.nextLine(), " C ");
    assertEquals(in.nextLine(), "  ");
    assertEquals(in.nextLine(), "D");


  }

  @Test
  public void combinedStringTest1() throws IOException {

    String s = "  123 3.141592    abcdef    the quick brown fox\n jumps \nover\n\n the lazy dog";
    InputReader in = getReader(s);

    assertEquals( in.nextInt(), 123);
    assertEquals( in.nextDouble(), 3.141592, 0.0000001);
    assertEquals( in.nextString(), "abcdef");
    assertEquals( in.nextString(), "the");
    assertEquals( in.nextLine(), "quick brown fox");
    assertEquals( in.nextLine(), " jumps ");
    assertEquals( in.nextString(), "over");
    assertEquals( in.nextLine(), "");
    assertEquals( in.nextLine(), " the lazy dog");

    String s2 = "123  \n   \n3.45 163\n\n \n4";
    InputReader in2 = getReader(s2);
    
    assertEquals(in2.nextInt(), 123);
    assertEquals(in2.nextLine(), " ");
    assertEquals(in2.nextLine(), "   ");
    assertEquals(in2.nextDouble(),3.45, 0.00000001);
    assertEquals(in2.nextInt(), 163);
    assertEquals(in2.nextLine(), "");
    assertEquals(in2.nextLine(), " ");
    assertEquals(in2.nextInt(), 4);
    


  }


}

