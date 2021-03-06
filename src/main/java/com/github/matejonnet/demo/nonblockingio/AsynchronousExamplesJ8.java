package com.github.matejonnet.demo.nonblockingio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.function.BiConsumer;

/**
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class AsynchronousExamplesJ8 {

    public void asynchronousExample() throws IOException, ExecutionException, InterruptedException {

        //Open Channel
        AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open();

        //bind to address
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8181);
        listener.bind(hostAddress);

        resumeAccept(listener);
    }

    private void resumeAccept(AsynchronousServerSocketChannel listener) {
        listener.accept(listener, handlerFrom(
                (clientChannel, ss) -> onConnect(clientChannel, listener),
                (e, ss) -> onConnectionError(e, listener)));
    }

    void onConnect(AsynchronousSocketChannel clientChannel, AsynchronousServerSocketChannel serverSocket) {
        System.out.println("New connection opened.");
        resumeReads(clientChannel);
        resumeAccept(serverSocket); // get ready for next connection
    }

    void onConnectionError(Throwable e, AsynchronousServerSocketChannel serverSocket) {
        e.printStackTrace();
        try {
            serverSocket.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    };

    private void resumeReads(AsynchronousSocketChannel clientChannel) {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        OnReadAttachment onReadAttachment = new OnReadAttachment(readBuffer, clientChannel);
        clientChannel.read(readBuffer, onReadAttachment, handlerFrom(
                        (written, attachment) -> onMessage(written, attachment),
                        (e, attachment) -> onMessageError(e, attachment))
        );
    }

    void onMessage (Integer written, OnReadAttachment attachment) {
        ByteBuffer receivedBuffer = attachment.byteBuffer;
        receivedBuffer.flip();
        System.out.println("New message received.");
        attachment.clientChannel.write(receivedBuffer); //TODO missing complete handler
        resumeReads(attachment.clientChannel);
    }

    void onMessageError(Throwable e, OnReadAttachment attachment) {
        e.printStackTrace();
        try {
            attachment.clientChannel.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    };

    private static <V, A> CompletionHandler<V, A> handlerFrom(
            BiConsumer<V, A> completed,
            BiConsumer<Throwable, A> failed) {

        return new CompletionHandler<V, A>(){
            @Override
            public void completed(V result, A attachment) {
                completed.accept(result, attachment);
            }

            @Override
            public void failed(Throwable exc, A attachment) {
                failed.accept(exc, attachment);
            }
        };
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        new AsynchronousExamplesJ8().asynchronousExample();

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
