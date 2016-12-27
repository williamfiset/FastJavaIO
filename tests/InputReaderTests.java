
import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;
import java.io.*;

public class InputReaderTests {

  public static InputReader getReader(String s) {
    if (s == null) return null;
    InputStream is = new ByteArrayInputStream(s.getBytes());
    return new InputReader(is);
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

}

