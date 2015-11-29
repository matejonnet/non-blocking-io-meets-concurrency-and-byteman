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

Visit [http://matejonnet.github.io/non-blocking-io-meets-concurrency-and-byteman/](http://matejonnet.github.io/non-blocking-io-meets-concurrency-and-byteman/) to see slides in presentation mode.

---

Agenda
======

### - Why non-blocking IO
- many long lived concurrent connections
    - web sockets
- 50k requests per second
- threads are considered resource-intensive

### - Non-blocking IO principles

- channels and buffers instead of stream (NIO)
    - do not block a thread waiting on IO
    - get notified by when data is available
    - buffer-oriented model
        - deal with data in large blocks
    - InputStream -> byte -> OutputStream _(Img 1)_
    - Channel -> buffer -> Channel _(Img 1)_
    - See Javadoc _(ChannelExamples#fileChannel)_

- buffers can represent system-level buffers _(Img 2)_
    - allocateDirect or MappedByteBuffer _(ChannelExamples#fileChannel)_
        - OS-level facilities to maximize throughput
    - fileChannel.transferTo _(ChannelExamples#channelTransferTo)_


### - How to handle everything with a single thread
- selectors
    - to deal with a large number of data sources simultaneously
    - notifies when any I/O activity happens
    - "event loop"

- IO threads and worker threads

- see the example _(Server)_

### - NIO2 Way
- https://docs.oracle.com/javase/tutorial/essential/io/file.html
- Path and File
- AsynchronousSocketChannel & AsynchronousServerSocketChannel
- AsynchronousFileChannel
- ThreadPool & ExecutorService
    - control in which thread is executed CompletionHandler callback
- see the example _(AsynchronousExamples & AsynchronousExamplesJ8)_

### - Higher level implementations
- Undertow http://undertow.io/ _(UndertowExample)_
    - lightweight Webserver
    - small dependency tree
- Netty http://netty.io/
    - easy to implement your own protocol
- Async Http Client


### - Why being limited to a single thread while you can use few of them

---

Links
=====

### References

[http://github.com/matejonnet/get-rid-of-boilerplate-with-j8/](http://github.com/matejonnet/get-rid-of-boilerplate-with-j8/)
http://www.javaworld.com/article/2073344/core-java/use-select-for-high-speed-networking.html
http://tutorials.jenkov.com/java-nio/nio-vs-io.html
http://stackoverflow.com/questions/8086930/non-blocking-socket-writes-in-java-versus-blocking-socket-writes
http://www.programmingopiethehokie.com/2014/03/asynchronous-non-blocking-io-java-echo.html
http://examples.javacodegeeks.com/core-java/nio/channels/asynchronoussocketchannel/java-nio-channels-asynchronoussocketchannel-example/
http://openjdk.java.net/projects/nio/presentations/TS-4222.pdf
http://docs.oracle.com/javase/7/docs/technotes/guides/io/

### Presentation tool used
https://github.com/gnab/remark/

*Matej Lazar (matejonnet@gmail.com, mlazar@redhat.com)*
