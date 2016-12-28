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

// Reading from Web Socket example coming soon
```

## InputReader methods

**ALL methods in the InputReader class must be caught or thrown** because they throw an java.io.IOException when something bad happens such as trying to read a byte value from an empty stream. 

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
Reads a double value ~2.5x times faster from the input stream than .readDouble() but at the cost of accuracy. The value returned may not be exact due to finite floating point number arithmetic (adding, multiplication) used to quickly compute the next double's value.
``` java
InputReader in = new InputReader();
double doublevalue = in.readDoubleFast();
```
### .readStr()
Reads a string of characters from the input stream. The delimiter separating a string of characters is set to be
any ASCII value <= 32 meaning any spaces, new lines, EOF characters, tabs... do not count as being part of the string. If the input stream is empty null is returned.
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

#### General case

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

#### .readByte() examples
``` java
// Suppose standard input stream contains the following byte values we want to read:
"-128 127 -1 -0 0 1 3454"
//                     ^ NOTE: This does NOT fit in a signed byte!

InputReader in = new InputReader();
byte b1 = in.readByte(); // -128
byte b2 = in.readByte(); // 127
byte b3 = in.readByte(); // -1
byte b4 = in.readByte(); // 0
byte b5 = in.readByte(); // 0
byte b6 = in.readByte(); // 1
byte b7 = in.readByte(); // 126, this byte value overflowed! No safety check
                         // gets done for this. It is assumed the user knows
                         // the range of the values they're reading from the stream.
byte b8 = in.readByte(); // Nothing left in stream so an error is thrown
```

#### .readInt() examples
``` java
// Suppose standard input stream contains the following byte values we want to read:
"2147483647 -2147483648 34545 -1 -0 0 1 999999999999"
//                                          ^ NOTE: This does NOT fit in a signed int!

InputReader in = new InputReader();
int integer1 = in.readInt(); // 2147483647
int integer2 = in.readInt(); // -2147483648
int integer3 = in.readInt(); // 34545
int integer4 = in.readInt(); // -1
int integer5 = in.readInt(); // 0
int integer6 = in.readInt(); // 0
int integer7 = in.readInt(); // 1
int integer8 = in.readInt(); // -727379969, this int value overflowed! No safety check   
                             // gets done for this.It is assumed the user knows the 
                             // range of the values they're reading from the stream
int integer9 = in.readInt(); // Nothing left in stream so an error is thrown
```

#### .readStr() example

``` java
// Suppose standard input stream contains the following string and we want
// to read the individual fruits names contained inside this sentence. We
// Can easily do this by using the .readStr()
"Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   "

InputReader in = new InputReader();
String s1 = in.readStr(); // "Apple"
String s2 = in.readStr(); // "banana"
String s3 = in.readStr(); // "orange"
String s4 = in.readStr(); // "KiWi"
String s5 = in.readStr(); // "dragonFRuIt"
String s6 = in.readStr(); // "WatERmeOn"
String s7 = in.readStr(); // "PEARS"
String s8 = in.readStr(); // null - Returns null when no more strings are found in the input stream


```

#### .readLine() example

``` java
// Suppose standard input stream contains the following string and we want
// to read it line by line. We can do this using the .readLine() method
"Apple banana orange\n\n KiWi dragonFRuIt \n   \n  WatERmeOn  \n\n\nPEARS\n\n   "

InputReader in = new InputReader();
String s1  = in.readLine(); // "Apple banana orange"
String s2  = in.readLine(); // ""
String s3  = in.readLine(); // " KiWi dragonFRuIt "
String s4  = in.readLine(); // "   "
String s5  = in.readLine(); // "  WatERmeOn  "
String s6  = in.readLine(); // ""
String s7  = in.readLine(); // ""
String s8  = in.readLine(); // "PEARS"
String s9  = in.readLine(); // ""
String s10 = in.readLine(); // "   "
String s11 = in.readLine(); // null - No more lines left so null is returned

```

#### .readAll() example

``` java
// Suppose standard input stream contains the following string and we want
// to read it all, then we can do this using the .readAll() method
"Self-education is, I firmly believe, the only kind of education there is. - Isaac Asimov"

InputReader in = new InputReader();
String quote = in.readAll(); // read the entire input stream

```


