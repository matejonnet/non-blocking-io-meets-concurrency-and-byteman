package com.github.matejonnet.demo.nonblockingio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
* @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
*/
class OnReadAttachment {

    final ByteBuffer byteBuffer;
    final AsynchronousSocketChannel clientChannel;

    public OnReadAttachment(ByteBuffer readBuffer, AsynchronousSocketChannel clientChannel) {
        byteBuffer = readBuffer;
        this.clientChannel = clientChannel;
    }
}
