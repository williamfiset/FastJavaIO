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
Read a signed 8 bit integer from the input stream.
``` java
InputReader in = new InputReader();
byte bytevalue = in.readByte();
```

### .readInt()
Read a signed 32 bit integer from the input stream.
``` java
InputReader in = new InputReader();
int intvalue = in.readInt();
```

### .readLong()
Read a signed 64 bit integer from the input stream.
``` java
InputReader in = new InputReader();
long longvalue = in.readLong();
```


