import java.util.InputMismatchException;
import java.io.InputStream;
import java.io.IOException;

public class InputReader {
  
  InputStream stream;
  byte[] buf = new byte[65536]; // 2^16
  int charIndex, numChars;
  
  double[][] doubles = {
    { 0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d,0d},
    { 0.1d,0.01d,0.001d,0.0001d,0.00001d,0.000001d,0.0000001d,0.00000001d,0.000000001d,0.0000000001d,0.00000000001d,0.000000000001d,0.0000000000001d,0.00000000000001d,0.000000000000001d},        
    { 0.2d,0.02d,0.002d,0.0002d,0.00002d,0.000002d,0.0000002d,0.00000002d,0.000000002d,0.0000000002d,0.00000000002d,0.000000000002d,0.0000000000002d,0.00000000000002d,0.000000000000002d},        
    { 0.3d,0.03d,0.003d,0.0003d,0.00003d,0.000003d,0.0000003d,0.00000003d,0.000000003d,0.0000000003d,0.00000000003d,0.000000000003d,0.0000000000003d,0.00000000000003d,0.000000000000003d},        
    { 0.4d,0.04d,0.004d,0.0004d,0.00004d,0.000004d,0.0000004d,0.00000004d,0.000000004d,0.0000000004d,0.00000000004d,0.000000000004d,0.0000000000004d,0.00000000000004d,0.000000000000004d},        
    { 0.5d,0.05d,0.005d,0.0005d,0.00005d,0.000005d,0.0000005d,0.00000005d,0.000000005d,0.0000000005d,0.00000000005d,0.000000000005d,0.0000000000005d,0.00000000000005d,0.000000000000005d},        
    { 0.6d,0.06d,0.006d,0.0006d,0.00006d,0.000006d,0.0000006d,0.00000006d,0.000000006d,0.0000000006d,0.00000000006d,0.000000000006d,0.0000000000006d,0.00000000000006d,0.000000000000006d},        
    { 0.7d,0.07d,0.007d,0.0007d,0.00007d,0.000007d,0.0000007d,0.00000007d,0.000000007d,0.0000000007d,0.00000000007d,0.000000000007d,0.0000000000007d,0.00000000000007d,0.000000000000007d},        
    { 0.8d,0.08d,0.008d,0.0008d,0.00008d,0.000008d,0.0000008d,0.00000008d,0.000000008d,0.0000000008d,0.00000000008d,0.000000000008d,0.0000000000008d,0.00000000000008d,0.000000000000008d},        
    { 0.9d,0.09d,0.009d,0.0009d,0.00009d,0.000009d,0.0000009d,0.00000009d,0.000000009d,0.0000000009d,0.00000000009d,0.000000000009d,0.0000000000009d,0.00000000000009d,0.000000000000009d}
  };

  public InputReader () { stream = System.in; }
  public InputReader (InputStream stream) { this.stream = stream; }

  // Reads a single character from input and returns -1 if there is no more data to read
  public int read() {
    if (numChars == -1) throw new InputMismatchException();
    if (charIndex >= numChars) {
      charIndex = 0;
      try { numChars = stream.read(buf); } 
      catch (IOException e) { throw new InputMismatchException(); }
      if (numChars <= 0) return -1;
    }
    return buf[charIndex++];
  }

  // Reads a 32bit signed integer from input stream
  public int readInt() throws IOException {
    int c = read(), sgn = 1, res = 0;
    while (c <= 32) c = read(); // while c is either: ' ', '\n', -1
    if (c == '-') { sgn = -1; c = read(); }
    do { res = (res<<3)+(res<<1); res += c - '0'; c = read(); }
    while (c > 32); // Still has digits
    return res * sgn;
  }

  // Reads a 64bit signed integer from input stream
  public long readLong() throws IOException {
    int c = read();
    while (c <= 32) c = read(); // while c is either: ' ', '\n', -1
    int sgn = 1;
    if (c == '-') { sgn = -1; c = read(); }
    long res = 0;
    do { res = (res<<3)+(res<<1); res += c - '0'; c = read(); }
    while (c > 32); // Still has digits
    return res * sgn; 
  }

  // Reads a line from input stream.
  // Returns null if there are no more lines
  public String readLine() throws IOException {
    int c = read();
    if (c == '\n') return ""; // Empty line
    if (c == -1) return null; // EOF
    StringBuilder res = new StringBuilder();
    do { res.appendCodePoint(c); c = read(); } 
    while (c != '\n' && c != -1); // Spaces & tabs are ok but not newlines or EOF characters
    return res.toString();    
  }

  // Reads a string of characters from the input stream. 
  // The delimiter separating a string of characters is set to be:
  // any ASCII value <= 32 meaning any spaces, new lines, EOF, tabs ...
  public String readStr() throws IOException {
    int c = read();
    while (c <= 32) c = read(); // while c is either: ' ', '\n', -1
    StringBuilder res = new StringBuilder();
    do { res.appendCodePoint(c); c = read(); } 
    while (c > 32); // Still non-space characters
    return res.toString();
  }

  // Very quickly reads a double value from standard input. However, this method only 
  // returns an approximate double value from input stream. The value is not
  // exact because we're doing arithmetic (adding, multiplication) on finite floating point numbers.
  @Deprecated public double readDoubleFast() throws IOException {
    int c = read(), sgn = 1;
    while (c <= 32) c = read(); // while c is either: ' ', '\n', -1
    if (c == '-') { sgn = -1; c = read(); }
    double res = 0.0;
    // while c is not: ' ', '\n', '.' or -1
    while (c > 46) {res *= 10.0; res += c - '0'; c = read(); }
    if (c == '.') {
      int i = 0; c = read();
      // while c is digit and there are < 15 digits after dot
      while (c > 32 && i < 15)
      { res += doubles[(c - '0')][i++]; c = read(); }
    }
    return res * sgn;
  }

  // About 2.5x slower than readDouble, but returns an exact value a double
  // It is recommended that you use this method instead of readDouble
  public double readDouble() throws IOException {
    return Double.valueOf(readStr());
  }

}
