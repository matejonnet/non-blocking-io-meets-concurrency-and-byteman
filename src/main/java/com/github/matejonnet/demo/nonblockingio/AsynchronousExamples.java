package com.github.matejonnet.demo.nonblockingio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class AsynchronousExamples {

    void asynchronousServerSocketChannelExample() throws IOException, ExecutionException, InterruptedException {

        //Open Channel
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open();

        //bind to address
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8181);
        listener.bind(hostAddress);

        //accept the connection
//        Future<AsynchronousSocketChannel> acceptResult = serverChannel.accept();
//        AsynchronousSocketChannel clientChannel = acceptResult.get(); //blocking operation
//        sendMessage(clientChannel);

        resumeAccept(listener);
    }

    //on new connection
    private CompletionHandler onConnect() {
        return new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
            @Override
            public void completed(AsynchronousSocketChannel clientChannel, AsynchronousServerSocketChannel listener) {
                System.out.println("New connection opened.");
                resumeReads(clientChannel);
                resumeAccept(listener);
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                exc.printStackTrace();
            }
        };
    }

    private void resumeAccept(AsynchronousServerSocketChannel listener) {
        listener.accept(listener, onConnect());
    }

    private void resumeReads(AsynchronousSocketChannel clientChannel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        OnReadAttachment onReadAttachment = new OnReadAttachment(readBuffer, clientChannel);
        clientChannel.read(readBuffer, onReadAttachment, onMessage());
    }

    private CompletionHandler onMessage() {
        return new CompletionHandler<Integer, OnReadAttachment>() {
            @Override
            public void completed(Integer written, OnReadAttachment attachment) {
                ByteBuffer receivedBuffer = attachment.byteBuffer;
                receivedBuffer.flip();
                System.out.println("New message received.");
                sendMessage(attachment.clientChannel, receivedBuffer);
            }

            @Override
            public void failed(Throwable exc, OnReadAttachment attachment) {
                exc.printStackTrace();
            }
        };
    }

    private void sendMessage(AsynchronousSocketChannel clientChannel, ByteBuffer buffer) {
        clientChannel.write(buffer);//TODO missing complete handler
        resumeReads(clientChannel);
    }

    private void asyncFileChanel(Path file) throws IOException {
        AsynchronousFileChannel afc = AsynchronousFileChannel.open(file, StandardOpenOption.READ);
        ByteBuffer buffer = ByteBuffer.wrap("My String".getBytes());
        afc.write(buffer, 0L);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        new AsynchronousExamples().asynchronousServerSocketChannelExample();

        Semaphore mutex = new Semaphore(1);
        mutex.acquire();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Shutting down.");
                mutex.release();
            }
        });

        mutex.acquire();
    }
}
