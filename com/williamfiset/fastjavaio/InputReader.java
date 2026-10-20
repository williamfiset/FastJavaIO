/**
 * A very fast input reader to read data from any InputStream (System.in in particular).
 * I primarily use this input reader for competitive programming to get an edge on the
 * time required to read input. That being said, this input reader assumes you know what
 * data types you are reading in and will not do any validation of input whatsoever!
 *
 * @author William Fiset, william.alexandre.fiset@gmail.com
 **/
package com.williamfiset.fastjavaio;

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class InputReader {

  /**
   * The default size of the InputReader's buffer is 2<sup>16</sup>.
   */
  private static final int DEFAULT_BUFFER_SIZE = 1 << 16;

  /**
   * The default stream for the InputReader is standard input.
   */
  private static final InputStream DEFAULT_STREAM = System.in;

  // Mantissas are accumulated into a long while below this bound, so m*10+9 never overflows.
  private static final long MANTISSA_LIMIT = 100_000_000_000_000_000L;

  // Bytes map 1:1 to chars, and the JDK builds compact strings from it with a plain array copy.
  private static final Charset LATIN1 = StandardCharsets.ISO_8859_1;

  // End Of File (EOF) character
  private static final int EOF = -1;

  // New line character: '\n'
  private static final byte NEW_LINE = 10;

  // Space character: ' '
  private static final byte SPACE = 32;

  // Dash character: '-'
  private static final byte DASH = 45;

  // Exactly representable powers of ten, used by the fast path of the double parser.
  private static final double[] POW10 = {
    1e0, 1e1, 1e2, 1e3, 1e4, 1e5, 1e6, 1e7, 1e8, 1e9, 1e10, 1e11,
    1e12, 1e13, 1e14, 1e15, 1e16, 1e17, 1e18, 1e19, 1e20, 1e21, 1e22
  };

  // Variables associated with the byte buffer.
  private final byte[] buf;
  private final int bufferSize;
  private int bufIndex, numBytesRead;

  private final InputStream stream;

  // Reusable scratch space for tokens which straddle a buffer refill.
  private byte[] tok = new byte[64];
  private int tokLen;

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
   * Refills the buffer from the stream and resets bufIndex. Once the end of
   * the stream is reached the stream is never read from again.
   * @return The number of bytes read, or EOF.
   * @throws IOException If the underlying stream throws.
   */
  private int fill() throws IOException {
    bufIndex = 0;
    return numBytesRead = numBytesRead == EOF ? EOF : stream.read(buf, 0, bufferSize);
  }

  /**
   *  Read values from the input stream until you reach a character with a
   *  higher ASCII value than 'token'.
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
      numBytesRead = stream.read(buf, 0, bufferSize);
      if (numBytesRead == EOF) return EOF;
      bufIndex = 0;

    } while(true);

  }

  /**
   * Copies the partial token buf[start, numBytesRead) into the scratch buffer and keeps reading
   * until the token's delimiter (consumed) or EOF. The token ends up in tok[0, tokLen).
   * @param start  Index in buf where the token begins.
   * @param line   If true the delimiter is a new line, otherwise any byte <= ' '.
   */
  private void spill(int start, boolean line) throws IOException {
    final byte[] buf = this.buf;
    byte[] t = tok;
    int len = numBytesRead - start;
    if (len > t.length) t = Arrays.copyOf(t, Math.max(len, t.length << 1));
    System.arraycopy(buf, start, t, 0, len);
    for (int n; (n = fill()) != EOF; ) {
      int i = 0;
      if (line) while (i < n && buf[i] != NEW_LINE) i++;
      else while (i < n && buf[i] > SPACE) i++;
      if (len + i > t.length) t = Arrays.copyOf(t, Math.max(len + i, t.length << 1));
      System.arraycopy(buf, 0, t, len, i);
      len += i;
      if (i < n) { bufIndex = i + 1; break; }
    }
    tok = t;
    tokLen = len;
  }

  /**
   * Reads a single byte from the input stream.
   * @return The next byte in the input stream
   * @throws IOException Throws exception at end of stream.
   */
  public byte nextByte() throws IOException {
    return (byte) nextInt();
  }

  // Note: nextInt and nextLong deliberately keep the field based loop shape. Hoisting
  // bufIndex/numBytesRead into locals or using a sentinel terminated loop benchmarked
  // 15-50% slower on HotSpot C2, as did a SWAR (8 digits per long) parser.

  /**
   * Reads a 32 bit signed integer from input stream.
   * @return The next integer value in the stream.
   * @throws IOException Throws exception at end of stream.
   */
  public int nextInt() throws IOException {

    if (readJunk(DASH-1) == EOF) throw new IOException();
    int sgn = 1, res = 0;

    int c = buf[bufIndex];
    if (c == DASH) { sgn = -1; bufIndex++; }

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > SPACE) {
          res = res * 10 + buf[bufIndex++] - '0';
        } else {
          bufIndex++;
          return res*sgn;
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf, 0, bufferSize);
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
    int c = buf[bufIndex];
    if (c == DASH) { sgn = -1; bufIndex++; }

    do {

      while(bufIndex < numBytesRead) {
        if (buf[bufIndex] > SPACE) {
          res = res * 10 + buf[bufIndex++] - '0';
        } else {
          bufIndex++;
          return res*sgn;
        }
      }

      // Reload buffer
      numBytesRead = stream.read(buf, 0, bufferSize);
      if (numBytesRead == EOF) return res*sgn;
      bufIndex = 0;

    } while(true);

  }

  /**
   * Reads a line from the input stream.
   * @return Returns a line from the input stream in the form a String not
   * including the new line character. Returns <code>null</code> when there are
   * no more lines.
   * @throws IOException Throws IOException when something terrible happens.
   */
  public String nextLine() throws IOException {

    if (bufIndex >= numBytesRead && fill() == EOF) return null;

    final byte[] buf = this.buf;
    final int n = numBytesRead, start = bufIndex;
    int i = start;
    while (i < n && buf[i] != NEW_LINE) i++;
    if (i < n) {
      bufIndex = i + 1;
      return new String(buf, start, i - start, LATIN1);
    }

    spill(start, true);
    return new String(tok, 0, tokLen, LATIN1);

  }

  // Reads a string of characters from the input stream.
  // The delimiter separating a string of characters is set to be:
  // any ASCII value <= 32 meaning any spaces, new lines, EOF, tabs...
  public String nextString() throws IOException {

    if (readJunk(SPACE) == EOF) return null;

    final byte[] buf = this.buf;
    final int n = numBytesRead, start = bufIndex;
    int i = start;
    while (i < n && buf[i] > SPACE) i++;
    if (i < n) {
      bufIndex = i + 1;
      return new String(buf, start, i - start, LATIN1);
    }

    spill(start, false);
    return new String(tok, 0, tokLen, LATIN1);

  }

  // Returns an exact value a double value from the input stream.
  public double nextDouble() throws IOException {
    return nextDouble(true);
  }

  // Reads a double value from the input stream, trading up to 1 ulp of accuracy for speed on
  // high precision input. For values with at most ~15 significant digits (most input) this is
  // identical to nextDouble(): same exact result, same speed. For 16+ significant digits, such as
  // the full precision output of Double.toString, nextDouble() falls back to Double.parseDouble
  // to stay exact, while this method keeps the fast path and may be off by 1 ulp (~2x faster).
  public double nextDoubleFast() throws IOException {
    return nextDouble(false);
  }

  private double nextDouble(boolean exact) throws IOException {

    if (readJunk(SPACE) == EOF) throw new IOException();

    final byte[] buf = this.buf;
    final int n = numBytesRead, start = bufIndex;
    int i = start;
    while (i < n && buf[i] > SPACE) i++;
    if (i < n) {
      bufIndex = i + 1;
      return parseDouble(buf, start, i, exact);
    }

    spill(start, false);
    return parseDouble(tok, 0, tokLen, exact);

  }

  /**
   * Parses the double in a[p, end). Values whose decimal mantissa fits in 53 bits with a
   * power of ten exponent of at most 22 are computed with a single correctly rounded multiply
   * or divide (Clinger's fast path); anything else is handed to Double.parseDouble.
   * @param exact  If false, excess mantissa digits are truncated rather than falling back.
   */
  private static double parseDouble(byte[] a, int p, int end, boolean exact) {

    final int off = p;
    final boolean neg = a[p] == '-';
    if (neg || a[p] == '+') p++;

    long m = 0;
    int exp = 0, d;

    for (; p < end && (d = a[p] - '0') >= 0 && d < 10; p++) {
      if (m < MANTISSA_LIMIT) m = m * 10 + d;
      else if (exact) return slowParseDouble(a, off, end);
      else exp++;
    }

    if (p < end && a[p] == '.') {
      for (p++; p < end && (d = a[p] - '0') >= 0 && d < 10; p++) {
        if (m < MANTISSA_LIMIT) { m = m * 10 + d; exp--; }
        else if (exact) return slowParseDouble(a, off, end);
      }
    }

    if (p < end && (a[p] | 0x20) == 'e') {
      boolean eneg = false;
      if (++p < end && (a[p] == '-' || a[p] == '+')) eneg = a[p++] == '-';
      int e = 0;
      for (; p < end && (d = a[p] - '0') >= 0 && d < 10; p++)
        if (e < 100000) e = e * 10 + d;
      exp += eneg ? -e : e;
    }

    // Leftover characters (NaN, Infinity, hex, type suffixes...) or an exponent out of range
    if (p != end || exp < -22 || exp > 22) return slowParseDouble(a, off, end);

    double v;
    if (exp == 0) v = m;
    else if (m < (1L << 53) || !exact) v = exp < 0 ? m / POW10[-exp] : m * POW10[exp];
    else return slowParseDouble(a, off, end);

    return neg ? -v : v;

  }

  private static double slowParseDouble(byte[] a, int off, int end) {
    return Double.parseDouble(new String(a, off, end - off, LATIN1));
  }

  // Read an array of n byte values
  public byte[] nextByteArray(int n) throws IOException {
    byte[] ar = new byte[n];
    for (int i = 0; i < n; i++) ar[i] = nextByte();
    return ar;
  }

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
