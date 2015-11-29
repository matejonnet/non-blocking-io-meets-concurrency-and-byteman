Non blocking OI meets concurrency and Byteman
=============================================
<br />
<br />
<br />
<br />
<br />
<br />
<br />
Matej Lazar [matejonnet@gmail.com](matejonnet@gmail.com)

???

Visit [http://matejonnet.github.io/non-blocking-io-meets-concurrency-and-byteman/] (http://matejonnet.github.io/non-blocking-io-meets-concurrency-and-byteman/) to see slides in presentation mode.

---

Agenda
======

### - Async and non-blocking IO
- why non-blocking
    - 50k requests per second
    - threads are considered resource-intensive

### - Java 8 - Lambda expressions / Streams / CompletableFuture
 - more detailed in [Get rid of boilerplate code with Java 8](http://github.com/matejonnet/get-rid-of-boilerplate-with-j8/)

### - Reactive programming
- example from get rid of boilerplate

### - Non-blocking IO principles
- Channel vs Steam
    - Javadoc
. NIO.2

### - Non-blocking IO example
- reading / writing file

### - How to handle 10k requests with a single thread
- NIO (java 1.4)
    - buffer-oriented model
    - deals with data large blocks
    - OS-level facilities to maximize throughput
    - channels and buffers
        - all data is read and written via buffers
        - buffers can represent system-level buffers
        - InputStream -> byte -> OutputStream
        - Channel -> buffer -> Channel

- selectors
    - to deal with a large number of data sources simultaneously
    - tells you When I/O activity happens on any of the streams
    - "event loop"

- selectors example
      // Create a new Selector for selecting
      Selector selector = Selector.open();


- context switching
- IO threads and worker threads

### - NIO2 Files and Paths
https://docs.oracle.com/javase/tutorial/essential/io/file.html


### - Why being limited to a single thread while you can use few of them

---

Links
=====

### References

[http://github.com/matejonnet/get-rid-of-boilerplate-with-j8/] (http://github.com/matejonnet/get-rid-of-boilerplate-with-j8/)
http://www.javaworld.com/article/2073344/core-java/use-select-for-high-speed-networking.html
http://tutorials.jenkov.com/java-nio/nio-vs-io.html
http://stackoverflow.com/questions/8086930/non-blocking-socket-writes-in-java-versus-blocking-socket-writes


### Presentation tool used
https://github.com/gnab/remark/

*Matej Lazar (matejonnet@gmail.com, mlazar@redhat.com)*
