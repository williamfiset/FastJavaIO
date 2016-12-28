# InputReader

The InputReader provides a way to read data from an input stream (much like java.util.Scanner) but many orders of magnitude faster. To get started using the InputReader look at the examples below on how to read various types of data from an input stream. 

### Reading from an input stream

Input data can come in a variety of ways. Sometimes it comes from standard input in for form of [System.in](https://docs.oracle.com/javase/7/docs/api/java/lang/System.html#in), other times it may be from a file we are trying to read as a [FileInputStream](https://docs.oracle.com/javase/7/docs/api/java/io/FileInputStream.html) object or even in the form of a [Web Socket](https://docs.oracle.com/javase/7/docs/api/java/net/Socket.html#getInputStream()). For this reason anythings that extends [InputStream](https://docs.oracle.com/javase/7/docs/api/java/io/InputStream.html) can be read with the InputReader. The default stream is set to be the standard input stream, but this can be changed when constructing an InputReader object:

``` java

// Reading from standard input
InputReader stdinReader = new InputReader(); // Defaults to reading from System.in
String line = stdinReader.readLine();

// Reading from a file
FileInputStream fileStream = new FileInputStream("/path/to/the/file");
InputReader fileReader = new InputReader(fileStream);
String entireFileContents = fileReader.readAll();

// Reading from Web Socket
// Provide examples...
```

## InputReader methods

### .byteInt()
Reads a signed 8 bit integer from the input stream.
``` java
InputReader in = new InputReader();
byte bytevalue = in.readByte();
```

### .readInt()
Reads a signed 32 bit integer from the input stream.
``` java
InputReader in = new InputReader();
int intvalue = in.readInt();
```

### .readLong()
Reads a signed 64 bit integer from the input stream.
``` java
InputReader in = new InputReader();
long longvalue = in.readLong();
```

### .readDouble()
Reads a signed double from the input stream.
``` java
InputReader in = new InputReader();
double doublevalue = in.readDouble();
```

### .readFastDouble()
Reads a double value faster (~2.5x) from the input stream than .readDouble() but at the cost of accuracy. The value returned may not be exact because finite floating point number arithmetic (adding, multiplication) being used to quickly compute the next double's value.
``` java
InputReader in = new InputReader();
double doublevalue = in.readDoubleFast();
```
### .readStr()
Reads a string of characters from the input stream. The delimiter separating a string of characters is set to be
any ASCII value <= 32 meaning any spaces, new lines, EOF characters, tabs... If the input stream is empty null is returned.
``` java
InputReader in = new InputReader();
String str = in.readStr();
```

### .readLine()
Reads a line of characters from the input stream until a new line character is reached. The .readLine() method includes spaces found in the input stream. If the input stream is empty a null value is returned to indicate so.
``` java
InputReader in = new InputReader();
String line = in.readLine();
```

### .readAll()
Reads all remeaning characters found in the input stream and returns it as a String. 
``` java
InputReader in = new InputReader();
String everything = in.readAll();
```

## Examples

``` java
// Suppose standard input stream contains the following string we want to read:
"  123 3.141592    abcdef    the quick brown fox\n jumps \nover\n\n the lazy dog"

InputReader in = new InputReader();
int intvalue = in.readInt();       // '123'
double dblvalue = in.readDouble(); // '3.141592'
String str = in.readStr();         // 'abcdef'
String str2 = in.readStr();        // 'the'
String line = in.readLine();       // 'quick brown fox'
String rest = in.readAll();        // ' jumps \nover\n\n the lazy dog'
```
