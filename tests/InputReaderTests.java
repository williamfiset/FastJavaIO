
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import java.io.*;

public class InputReaderTests {

  // Create an input reader with an arbitrary buffer size
  public static InputReader getReader(String s) {
    if (s == null) return null;
    InputStream is = new ByteArrayInputStream(s.getBytes());
    int buffer_size = 1 + 2; // (int)(Math.random() * 50);
    return new InputReader(is, buffer_size);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIllegalBufferSz1() {
    new InputReader( 0 );
  }

  @Test(expected=IllegalArgumentException.class)
  public void testIllegalBufferSz2() {
    new InputReader( -1 );
  }

  @Test
  public void testInputReaderExtremeBufferSizeValues() throws IOException {

    String s = " abcde \n\n\n 77\n     -34\n 575678 AAAA %%%%^^^^^\n -78787";

    // Try all buffer sizes from [1, 128]
    for (int buffer_size = 1; buffer_size <= 128; buffer_size++ ) {
      
      InputStream is = new ByteArrayInputStream(s.getBytes());
      InputReader in = new InputReader(is, buffer_size);

      assertEquals( "abcde", in.readStr() );
      assertEquals( 77, in.readInt() );
      assertEquals( -34, in.readInt() );
      assertEquals( 575678, in.readInt() );
      assertEquals( "AAAA", in.readStr() );
      assertEquals( "%%%%^^^^^", in.readStr() );
      assertEquals( -78787, in.readInt() );

    }

  }

  @Test
  public void testReadByte() throws IOException {

    String str = "-128 127 -1 -0 0 1";
    InputReader in = getReader(str);

    assertEquals( -128, in.readByte());
    assertEquals(  127, in.readByte());
    assertEquals(   -1, in.readByte());
    assertEquals(    0, in.readByte());
    assertEquals(    0, in.readByte());
    assertEquals(    1, in.readByte());

  }

  @Test
  public void testReadInt() throws IOException {

    // Test non negative values
    String str = " 0  1 2    3  \n 55 345 888234\n";
    InputReader in = getReader(str);

    assertEquals( 0, in.readInt() );
    assertEquals( 1, in.readInt() );
    assertEquals( 2, in.readInt() );
    assertEquals( 3, in.readInt() );
    assertEquals( 55, in.readInt() );
    assertEquals( 345, in.readInt() );
    assertEquals( 888234, in.readInt() );

    // Test negative values
    str = "  -0 -1  -2    -3  \n -55 -345 -888234\n";
    in = getReader(str);

    assertEquals( 0, in.readInt() );
    assertEquals( -1, in.readInt() );
    assertEquals( -2, in.readInt() );
    assertEquals( -3, in.readInt() );
    assertEquals( -55, in.readInt() );
    assertEquals( -345, in.readInt() );
    assertEquals( -888234, in.readInt() );

  }

  @Test
  public void testReadIntExtremes() throws IOException {

    // Test integer maximum and minimum values
    String str = Integer.MAX_VALUE + " " + Integer.MIN_VALUE;
    InputReader in = getReader(str);

    assertEquals( Integer.MAX_VALUE, in.readInt() );
    assertEquals( Integer.MIN_VALUE, in.readInt() );

    str = (Integer.MAX_VALUE-1) + " " + (Integer.MIN_VALUE+1);
    in = getReader(str);
    
    // Test integer maximum and minimum values +- 1
    assertEquals( Integer.MAX_VALUE-1, in.readInt() );
    assertEquals( Integer.MIN_VALUE+1, in.readInt() );

  }

  @Test
  public void testReadLong() throws IOException {

    // Test non negative values
    String str = "  0 1  2    3  \n 55 345 888234 1111111111111  839475594569048  \n";
    InputReader in = getReader(str);

    assertEquals( 0, in.readLong() );
    assertEquals( 1, in.readLong() );
    assertEquals( 2, in.readLong() );
    assertEquals( 3, in.readLong() );
    assertEquals( 55, in.readLong() );
    assertEquals( 345, in.readLong() );
    assertEquals( 888234, in.readLong() );

    // Test negative values
    str = "  -0 -1  -2    -3  \n -55 -345 -888234 -1111111111111  -839475594569048  \n";
    in = getReader(str);

    assertEquals(  0L, in.readLong() );
    assertEquals( -1L, in.readLong() );
    assertEquals( -2L, in.readLong() );
    assertEquals( -3L, in.readLong() );
    assertEquals( -55L, in.readLong() );
    assertEquals( -345L, in.readLong() );
    assertEquals( -888234L, in.readLong() );
    assertEquals( -1111111111111L, in.readLong() );
    assertEquals( -839475594569048L, in.readLong() );

  }

  @Test
  public void testReadLongExtremes() throws IOException {

    // Test integer maximum and minimum values
    String str = Long.MAX_VALUE + " " + Long.MIN_VALUE;
    InputReader in = getReader(str);

    assertEquals( Long.MAX_VALUE, in.readLong() );
    assertEquals( Long.MIN_VALUE, in.readLong() );

    str = (Long.MAX_VALUE-1) + " " + (Long.MIN_VALUE+1);
    in = getReader(str);
    
    // Test Long maximum and minimum values +- 1
    assertEquals( Long.MAX_VALUE-1, in.readLong() );
    assertEquals( Long.MIN_VALUE+1, in.readLong() );

  }

  @Test
  public void testReadIntArray() throws IOException {

    String str = " 1     2  3  4 -5 ";
    int[] array = {1,2,3,4,-5};
    InputReader in = getReader(str);
    int[] ar = in.readIntArray(5);

    assertEquals(array.length, ar.length);
    for (int i = 0; i < array.length; i++)
      assertEquals(array[i], ar[i]);

  }

  @Test
  public void testReadString1()  throws IOException {

    String str = "a b c";
    InputReader in = getReader(str);
    
    assertEquals("a", in.readStr());
    assertEquals("b", in.readStr());
    assertEquals("c", in.readStr());

    str = "  a   b   c   ";
    in  = getReader(str);
    assertEquals("a", in.readStr());
    assertEquals("b", in.readStr());
    assertEquals("c", in.readStr());

    str = "  abcde   bb   ccc   ";
    in  = getReader(str);
    assertEquals("abcde", in.readStr());
    assertEquals("bb", in.readStr());
    assertEquals("ccc", in.readStr());

  }

  @Test
  public void testReadStringNewLines1()  throws IOException {

    String str = "a\n b\n c\n";
    InputReader in = getReader(str);
    
    assertEquals("a", in.readStr());
    assertEquals("b", in.readStr());
    assertEquals("c", in.readStr());

    str = "  a \n  \nb \n  c\n  \n \n d ";
    in  = getReader(str);
    assertEquals("a", in.readStr());
    assertEquals("b", in.readStr());
    assertEquals("c", in.readStr());
    assertEquals("d", in.readStr());

    str = "  \n \n abcde \n \n bb  \n \nccc\n   ";
    in  = getReader(str);
    assertEquals("abcde", in.readStr());
    assertEquals("bb", in.readStr());
    assertEquals("ccc", in.readStr());

    str = "Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   ";
    in  = getReader(str);
    assertEquals( in.readStr(), "Apple" );
    assertEquals( in.readStr(), "banana" );
    assertEquals( in.readStr(), "orange" );
    assertEquals( in.readStr(), "KiWi" );
    assertEquals( in.readStr(), "dragonFRuIt" );
    assertEquals( in.readStr(), "WatERmeOn" );
    assertEquals( in.readStr(), "PEARS" );
    assertNull( in.readStr() );

  }

  @Test
  public void testReadStringExtremes()  throws IOException {

    String str = "";
    InputReader in = getReader(str);
    assertNull(in.readStr());

    str = "\n";
    in = getReader(str);
    assertNull(in.readStr());

    str = "   ";
    in = getReader(str);
    assertNull(in.readStr());

    str = "\n\n\n\n\n";
    in = getReader(str);
    assertNull(in.readStr());

    str = "\n\n\nabcdef\n\n\n";
    in = getReader(str);
    assertEquals("abcdef", in.readStr());
    assertEquals(null, in.readStr());

    str = "\n\n\n    abcdef    \n\n\n";
    in = getReader(str);
    assertEquals("abcdef", in.readStr());
    assertEquals(null, in.readStr());
    assertEquals(null, in.readStr());

  }

  @Test
  public void testReadLine() throws IOException {

    String s = "hello\nworld\n";
    InputReader in = getReader(s);
    assertEquals("hello", in.readLine());
    assertEquals("world", in.readLine());
    assertEquals(null, in.readLine());

    s = "a\nb\nc\n";
    in = getReader(s);
    assertEquals("a", in.readLine());
    assertEquals("b", in.readLine());
    assertEquals("c", in.readLine());
    assertEquals(null, in.readLine());

    s = "aaaa\nbbbb\ncccc\n";
    in = getReader(s);
    assertEquals("aaaa", in.readLine());
    assertEquals("bbbb", in.readLine());
    assertEquals("cccc", in.readLine());
    assertEquals(null, in.readLine());

    s = "Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   ";
    in = getReader(s);
    assertEquals(in.readLine(), "Apple banana orange");
    assertEquals(in.readLine(), "");
    assertEquals(in.readLine(), " KiWi dragonFRuIt ");
    assertEquals(in.readLine(), "   ");
    assertEquals(in.readLine(), "  WatERmeOn  ");
    assertEquals(in.readLine(), "");
    assertEquals(in.readLine(), "");
    assertEquals(in.readLine(), "PEARS");
    assertEquals(in.readLine(), "");
    assertEquals(in.readLine(), "   ");
    assertNull(in.readLine());

  }

  @Test
  public void testReadLineExtremes() throws IOException {

    String s = "";
    InputReader in = getReader(s);
    assertEquals(null, in.readLine());
    assertEquals(null, in.readLine());

    s = " ";
    in = getReader(s);
    assertEquals(" ", in.readLine());
    assertEquals(null, in.readLine());    
    assertEquals(null, in.readLine());    
    
    s = "\n";
    in = getReader(s);
    assertEquals("", in.readLine());  
    assertEquals(null, in.readLine());
    assertEquals(null, in.readLine());

    s = "\n\n\nA\n\nB\n\n";
    in = getReader(s);
    assertEquals("", in.readLine());  
    assertEquals("", in.readLine());  
    assertEquals("", in.readLine());  
    assertEquals("A", in.readLine());  
    assertEquals("", in.readLine());  
    assertEquals("B", in.readLine());  
    assertEquals("", in.readLine());  
    assertEquals(null, in.readLine());
    assertEquals(null, in.readLine());
    assertEquals(null, in.readLine());

    s = "\n  A\n B\n C \n  \nD";
    in = getReader(s);
    assertEquals(in.readLine(), "");
    assertEquals(in.readLine(), "  A");
    assertEquals(in.readLine(), " B");
    assertEquals(in.readLine(), " C ");
    assertEquals(in.readLine(), "  ");
    assertEquals(in.readLine(), "D");


  }

  @Test
  public void combinedStringTest1() throws IOException {

    String s = "  123 3.141592    abcdef    the quick brown fox\n jumps \nover\n\n the lazy dog";
    InputReader in = getReader(s);

    assertEquals( in.readInt(), 123);
    assertEquals( in.readDouble(), 3.141592, 0.0000001);
    assertEquals( in.readStr(), "abcdef");
    assertEquals( in.readStr(), "the");
    assertEquals( in.readLine(), "quick brown fox");
    assertEquals( in.readLine(), " jumps ");
    assertEquals( in.readStr(), "over");
    assertEquals( in.readLine(), "");
    assertEquals( in.readLine(), " the lazy dog");

    String s2 = "123  \n   \n3.45 163\n\n \n4";
    InputReader in2 = getReader(s2);
    
    assertEquals(in2.readInt(), 123);
    assertEquals(in2.readLine(), " ");
    assertEquals(in2.readLine(), "   ");
    assertEquals(in2.readDouble(),3.45, 0.00000001);
    assertEquals(in2.readInt(), 163);
    assertEquals(in2.readLine(), "");
    assertEquals(in2.readLine(), " ");
    assertEquals(in2.readInt(), 4);
    


  }


}

