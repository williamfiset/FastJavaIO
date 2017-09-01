/**
 * A very fast input reader to read data from any InputStream (System.in in particular).
 * I primarily use this input reader for competitive programming to get an edge on the
 * time required to read input. That being said, this input reader assumes you know what
 * data types you are reading in and will not do any validation of input whatsoever!
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/

package fastjavaio;

import java.io.InputStream;
import java.io.IOException;

public class InputReader {
  
  /**
   * The default size of the InputReader's buffer is 2<sup>16</sup>.
   */
  public static final int DEFAULT_BUFFER_SIZE = 1 << 16;
  
  /**
   * The default stream for the InputReader is standard input.
   */  
  public static final InputStream DEFAULT_STREAM = System.in;
  
  /**
   * The maximum number of accurate decimal digits the method {@link #nextDoubleFast() nextDoubleFast()} can read.
   * Currently this value is set to 21 because it is the maximum number of digits a double precision float can have at the moment.
   */
  public static final int MAX_DECIMAL_PRECISION = 21;
  
  // 'c' is used to refer to the current character in the stream
  private int c;
  
  // Variables associated with the byte buffer. 
  private final byte[] buf;
  private int bufferSize, bufIndex, numBytesRead;
  
  private final InputStream stream;

  private static final byte EOF   = -1; // End Of File (EOF) character
  private static final byte NL    = 10; // '\n' - New Line (NL)
  private static final byte SP    = 32; // ' '  - Space character (SP)
  private static final byte DASH  = 45; // '-'  - Dash character (DASH)
  private static final byte DOT   = 46; // '.'  - Dot character (DOT)

  // A character buffer I reuse when reading string data.
  private static char[] charBuffer = new char[128];

  // Primitive data type lookup tables used for optimizations
  private static byte[] bytes = new byte[58];
  private static  int[] ints  = new int[58];
  private static char[] chars = new char[128];

  static {
    char ch = ' '; int value = 0; byte _byte = 0;
    for (int i = 48; i <  58; i++ ) bytes[i] = _byte++;
    for (int i = 48; i <  58; i++ )  ints[i] = value++;
    for (int i = 32; i < 128; i++ ) chars[i] = ch++;
  }

  // Primitive double lookup table used for optimizations.
  private static final double[][] doubles = {
    { 0.0d,0.00d,0.000d,0.0000d,0.00000d,0.000000d,0.0000000d,0.00000000d,0.000000000d,0.0000000000d,0.00000000000d,0.000000000000d,0.0000000000000d,0.00000000000000d,0.000000000000000d,0.0000000000000000d,0.00000000000000000d,0.000000000000000000d,0.0000000000000000000d,0.00000000000000000000d,0.000000000000000000000d },
    { 0.1d,0.01d,0.001d,0.0001d,0.00001d,0.000001d,0.0000001d,0.00000001d,0.000000001d,0.0000000001d,0.00000000001d,0.000000000001d,0.0000000000001d,0.00000000000001d,0.000000000000001d,0.0000000000000001d,0.00000000000000001d,0.000000000000000001d,0.0000000000000000001d,0.00000000000000000001d,0.000000000000000000001d },
    { 0.2d,0.02d,0.002d,0.0002d,0.00002d,0.000002d,0.0000002d,0.00000002d,0.000000002d,0.0000000002d,0.00000000002d,0.000000000002d,0.0000000000002d,0.00000000000002d,0.000000000000002d,0.0000000000000002d,0.00000000000000002d,0.000000000000000002d,0.0000000000000000002d,0.00000000000000000002d,0.000000000000000000002d },
    { 0.3d,0.03d,0.003d,0.0003d,0.00003d,0.000003d,0.0000003d,0.00000003d,0.000000003d,0.0000000003d,0.00000000003d,0.000000000003d,0.0000000000003d,0.00000000000003d,0.000000000000003d,0.0000000000000003d,0.00000000000000003d,0.000000000000000003d,0.0000000000000000003d,0.00000000000000000003d,0.000000000000000000003d },
    { 0.4d,0.04d,0.004d,0.0004d,0.00004d,0.000004d,0.0000004d,0.00000004d,0.000000004d,0.0000000004d,0.00000000004d,0.000000000004d,0.0000000000004d,0.00000000000004d,0.000000000000004d,0.0000000000000004d,0.00000000000000004d,0.000000000000000004d,0.0000000000000000004d,0.00000000000000000004d,0.000000000000000000004d },
    { 0.5d,0.05d,0.005d,0.0005d,0.00005d,0.000005d,0.0000005d,0.00000005d,0.000000005d,0.0000000005d,0.00000000005d,0.000000000005d,0.0000000000005d,0.00000000000005d,0.000000000000005d,0.0000000000000005d,0.00000000000000005d,0.000000000000000005d,0.0000000000000000005d,0.00000000000000000005d,0.000000000000000000005d },
    { 0.6d,0.06d,0.006d,0.0006d,0.00006d,0.000006d,0.0000006d,0.00000006d,0.000000006d,0.0000000006d,0.00000000006d,0.000000000006d,0.0000000000006d,0.00000000000006d,0.000000000000006d,0.0000000000000006d,0.00000000000000006d,0.000000000000000006d,0.0000000000000000006d,0.00000000000000000006d,0.000000000000000000006d },
    { 0.7d,0.07d,0.007d,0.0007d,0.00007d,0.000007d,0.0000007d,0.00000007d,0.000000007d,0.0000000007d,0.00000000007d,0.000000000007d,0.0000000000007d,0.00000000000007d,0.000000000000007d,0.0000000000000007d,0.00000000000000007d,0.000000000000000007d,0.0000000000000000007d,0.00000000000000000007d,0.000000000000000000007d },
    { 0.8d,0.08d,0.008d,0.0008d,0.00008d,0.000008d,0.0000008d,0.00000008d,0.000000008d,0.0000000008d,0.00000000008d,0.000000000008d,0.0000000000008d,0.00000000000008d,0.000000000000008d,0.0000000000000008d,0.00000000000000008d,0.000000000000000008d,0.0000000000000000008d,0.00000000000000000008d,0.000000000000000000008d },
    { 0.9d,0.09d,0.009d,0.0009d,0.00009d,0.000009d,0.0000009d,0.00000009d,0.000000009d,0.0000000009d,0.00000000009d,0.000000000009d,0.0000000000009d,0.00000000000009d,0.000000000000009d,0.0000000000000009d,0.00000000000000009d,0.000000000000000009d,0.0000000000000000009d,0.00000000000000000009d,0.000000000000000000009d }
  };
  
  /**
   * Create an InputReader that reads from standard input.
   */
  public InputReader () {
    this(DEFAULT_STREAM, DEFAULT_BUFFER_SIZE);
  }
  
  /**
   * Create an InputReader that reads from standard input.
   * @param bufferSize    The buffer size for this input reader.
   */
  public InputReader(int bufferSize) {
    this(DEFAULT_STREAM, bufferSize);
  }
  
  /**
   * Create an InputReader that reads from standard input.
   * @param stream  Takes an InputStream as a parameter to read from.
   */
  public InputReader(InputStream stream) {
    this(stream, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Create an InputReader that reads from standard input.
   * @param  stream        Takes an {@link java.io.InputStream#InputStream() InputStream} as a parameter to read from.
   * @param  bufferSize    The size of the buffer to use.
   */
  public InputReader (InputStream stream, int bufferSize) {
    if (stream == null || bufferSize <= 0)
      throw new IllegalArgumentException();
    buf = new byte[bufferSize];
    this.bufferSize = bufferSize;
    this.stream = stream;
  }
  
  /**
   * Reads a single character from the input stream.
   * @return Returns the byte value of the next character in the buffer and EOF 
   * at the end of the stream.
   * @throws IOException throws exception if there is no more data to read
   */
  private byte read() throws IOException {

    if (numBytesRead == EOF) throw new IOException();

    if (bufIndex >= numBytesRead) {
      bufIndex = 0;
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF)
        return EOF;
    }

    return buf[bufIndex++];
  }
  
  /**
   *  Read values from the input stream until you reach a character with a 
   *  higher ASCII value than 'token'
   * @param token The token is a value which we use to stop reading junk out of
   * the stream.
   * @return Returns 0 if a value greater than the token was reached or -1 if
   * the end of the stream was reached.
   * @throws IOException Throws exception at end of stream.
   */
  private int readJunk(int token) throws IOException { 
    
    if (numBytesRead == EOF) return EOF;

    // Seek to the first valid position index
    do {
      
      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > token) return 0;
        bufIndex++;
      }

      // reload buffer
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF) return EOF;
      bufIndex = 0;

    } while(true);

  }
  
  /**
   * Reads a single byte from the input stream.
   * @return The next byte in the input stream
   * @throws IOException Throws exception at end of stream.
   */
  public byte nextByte() throws IOException {
    return (byte) nextInt();
  }
  
  /**
   * Reads a 32 bit signed integer from input stream.
   * @return The next integer value in the stream.
   * @throws IOException Throws exception at end of stream.
   */
  public int nextInt() throws IOException {
    
    if (readJunk(DASH-1) == EOF) throw new IOException();
    int sgn = 1, res = 0;

    c = buf[bufIndex];
    if (c == DASH) { sgn = -1; bufIndex++; }

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > SP) {
          res = (res<<3)+(res<<1);
          res += ints[buf[bufIndex++]];
        } else {
          bufIndex++;
          return res*sgn;
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF) return res*sgn;
      bufIndex = 0;

    } while(true);

  }

  /**
   * Reads a 64 bit signed long from input stream.
   * @return The next long value in the stream.
   * @throws IOException Throws exception at end of stream.
   */
  public long nextLong() throws IOException {
    
    if (readJunk(DASH-1) == EOF) throw new IOException();
    int sgn = 1;
    long res = 0L;
    c = buf[bufIndex];
    if (c == DASH) { sgn = -1; bufIndex++; }

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > SP) {
          res = (res<<3)+(res<<1);
          res += ints[buf[bufIndex++]];
        } else {
          bufIndex++;
          return res*sgn;
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF) return res*sgn;
      bufIndex = 0;

    } while(true);

  }
  
  /**
   * Doubles the size of the internal char buffer for strings
   */
  private void doubleCharBufferSize() {
    char[] newBuffer = new char[charBuffer.length << 1];
    for(int i = 0; i < charBuffer.length; i++) newBuffer[i] = charBuffer[i];
    charBuffer = newBuffer;
  }

  /**
   * Reads a line from the input stream.
   * @return Returns a line from the input stream in the form a String not 
   * including the new line character. Returns <code>null</code> when there are 
   * no more lines. 
   * @throws IOException Throws IOException when something terrible happens.
   */
  public String nextLine() throws IOException {

    try { c=read(); } catch (IOException e) { return null; }
    if (c == NL) return ""; // Empty line
    if (c == EOF) return null; // EOF
    
    int i = 0;
    charBuffer[i++] = (char)c;

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] != NL) {
          if (i == charBuffer.length) doubleCharBufferSize();
          charBuffer[i++] = (char) buf[bufIndex++];
        } else {
          bufIndex++;
          return new String(charBuffer, 0, i);
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF)
        return new String(charBuffer, 0, i);
      bufIndex = 0;

    } while(true);

  }

  // Reads a string of characters from the input stream. 
  // The delimiter separating a string of characters is set to be:
  // any ASCII value <= 32 meaning any spaces, new lines, EOF, tabs ...
  public String nextString() throws IOException {

    if (numBytesRead == EOF) return null;
    if (readJunk(SP) == EOF) return null;
    int i = 0;

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > SP) {
          if (i == charBuffer.length) doubleCharBufferSize();
          charBuffer[i++] = (char) buf[bufIndex++];
        } else {
          bufIndex++;
          return new String(charBuffer, 0, i);
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf);
      if (numBytesRead == EOF) return new String(charBuffer, 0, i);
      bufIndex = 0;

    } while(true);

  }

  // Returns an exact value a double value from the input stream.
  public double nextDouble() throws IOException {
    String doubleVal = nextString();
    if (doubleVal == null) throw new IOException();
    return Double.valueOf(doubleVal);
  }

  // Very quickly reads a double value from the input stream (~3x faster than nextDouble()). However,
  // this method may provide a slightly less accurate reading than .nextDouble() if there are a lot
  // of digits (~16+). In particular, it will only read double values with at most 21 digits after
  // the decimal point and the reading my be as inaccurate as ~5*10^16 from the true value.
  public double nextDoubleFast() throws IOException {
    c = read(); int sgn = 1;
    while (c <= SP) c = read(); // while c is either: ' ', '\n', EOF
    if (c == DASH) { sgn = -1; c = read(); }
    double res = 0.0;
    // while c is not: ' ', '\n', '.' or -1
    while (c > DOT) {res *= 10.0; res += ints[c]; c = read(); }
    if (c == DOT) {
      int i = 0; c = read();
      // while c is digit and we are less than the maximum decimal precision
      while (c > SP && i < MAX_DECIMAL_PRECISION) {
        res += doubles[ints[c]][i++]; c = read();
      }
    }
    return res * sgn;
  }

  // Read an array of n byte values
  public byte[] nextByteArray(int n) throws IOException {
    byte[] ar = new byte[n];
    for (int i = 0; i < n; i++) ar[i] = nextByte();
    return ar;
  }

  // Read an array of n char values
  // Buggy method must patch
  // public char[] nextCharArray(int n) throws IOException {
  //   String str = nextString();
  //   if (str == null) return null;
  //   return str.toCharArray();
  // }

  // Read an integer array of size n
  public int[] nextIntArray(int n) throws IOException {
    int[] ar = new int[n];
    for (int i = 0; i < n; i++) ar[i] = nextInt();
    return ar;
  }

  // Read a long array of size n
  public long[] nextLongArray(int n) throws IOException {
    long[] ar = new long[n];
    for (int i = 0; i < n; i++) ar[i] = nextLong();
    return ar;
  }

  // read an of doubles of size n 
  public double[] nextDoubleArray(int n) throws IOException {
    double[] ar = new double[n];
    for (int i = 0; i < n; i++) ar[i] = nextDouble();
    return ar;
  }

  // Quickly read an array of doubles
  public double[] nextDoubleArrayFast(int n) throws IOException {
    double[] ar = new double[n];
    for (int i = 0; i < n; i++) ar[i] = nextDoubleFast();
    return ar;
  }
  
  // Read a string array of size n
  public String[] nextStringArray(int n) throws IOException {
    String[] ar = new String[n];
    for (int i = 0; i < n; i++) {
      String str = nextString();
      if (str == null) throw new IOException();
      ar[i] = str;
    }
    return ar;
  }

  // Read a 1-based byte array of size n+1
  public byte[] nextByteArray1(int n) throws IOException {
    byte[] ar = new byte[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextByte();
    return ar;
  }

  // Read a 1-based integer array of size n+1
  public int[] nextIntArray1(int n) throws IOException {
    int[] ar = new int[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextInt();
    return ar;
  }

  // Read a 1-based long array of size n+1
  public long[] nextLongArray1(int n) throws IOException {
    long[] ar = new long[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextLong();
    return ar;
  }

  // Read a 1-based double array of size n+1
  public double[] nextDoubleArray1(int n) throws IOException {
    double[] ar = new double[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextDouble();
    return ar;
  }

  // Quickly read a 1-based double array of size n+1
  public double[] nextDoubleArrayFast1(int n) throws IOException {
    double[] ar = new double[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextDoubleFast();
    return ar;
  }

  // Read a 1-based string array of size n+1
  public String[] nextStringArray1(int n) throws IOException {
    String[] ar = new String[n+1];
    for (int i = 1; i <= n; i++) ar[i] = nextString();
    return ar;
  }

  // Read a two dimensional matrix of bytes of size rows x cols
  public byte[][] nextByteMatrix(int rows, int cols) throws IOException {
    byte[][] matrix = new byte[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextByte();
    return matrix;
  }

  // Read a two dimensional matrix of ints of size rows x cols
  public int[][] nextIntMatrix(int rows, int cols) throws IOException {
    int[][] matrix = new int[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextInt();
    return matrix;
  }

  // Read a two dimensional matrix of longs of size rows x cols
  public long[][] nextLongMatrix(int rows, int cols) throws IOException {
    long[][] matrix = new long[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextLong();
    return matrix;
  }

  // Read a two dimensional matrix of doubles of size rows x cols
  public double[][] nextDoubleMatrix(int rows, int cols) throws IOException {
    double[][] matrix = new double[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextDouble();
    return matrix;
  }

  // Quickly read a two dimensional matrix of doubles of size rows x cols
  public double[][] nextDoubleMatrixFast(int rows, int cols) throws IOException {
    double[][] matrix = new double[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextDoubleFast();
    return matrix;
  }
  
  // Read a two dimensional matrix of Strings of size rows x cols
  public String[][] nextStringMatrix(int rows, int cols) throws IOException {
    String[][] matrix = new String[rows][cols];
    for(int i = 0; i < rows; i++)
      for (int j = 0; j < cols; j++)
        matrix[i][j] = nextString();
    return matrix;    
  }

  // Read a 1-based two dimensional matrix of bytes of size rows x cols
  public byte[][] nextByteMatrix1(int rows, int cols) throws IOException {
    byte[][] matrix = new byte[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextByte();
    return matrix;
  }

  // Read a 1-based two dimensional matrix of ints of size rows x cols
  public int[][] nextIntMatrix1(int rows, int cols) throws IOException {
    int[][] matrix = new int[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextInt();
    return matrix;
  }

  // Read a 1-based two dimensional matrix of longs of size rows x cols
  public long[][] nextLongMatrix1(int rows, int cols) throws IOException {
    long[][] matrix = new long[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextLong();
    return matrix;
  }

  // Read a 1-based two dimensional matrix of doubles of size rows x cols
  public double[][] nextDoubleMatrix1(int rows, int cols) throws IOException {
    double[][] matrix = new double[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextDouble();
    return matrix;
  }

  // Quickly read a 1-based two dimensional matrix of doubles of size rows x cols
  public double[][] nextDoubleMatrixFast1(int rows, int cols) throws IOException {
    double[][] matrix = new double[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextDoubleFast();
    return matrix;
  }
  
  // Read a 1-based two dimensional matrix of Strings of size rows x cols
  public String[][] nextStringMatrix1(int rows, int cols) throws IOException {
    String[][] matrix = new String[rows+1][cols+1];
    for(int i = 1; i <= rows; i++)
      for (int j = 1; j <= cols; j++)
        matrix[i][j] = nextString();
    return matrix;    
  }

  // Closes the input stream
  public void close() throws IOException {
    stream.close();
  }

}
