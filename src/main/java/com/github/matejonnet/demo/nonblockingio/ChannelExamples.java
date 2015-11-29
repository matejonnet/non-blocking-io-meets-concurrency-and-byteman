package com.github.matejonnet.demo.nonblockingio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * https://docs.oracle.com/javase/tutorial/essential/io/file.html
 *
 * @author <a href="mailto:matejonnet@gmail.com">Matej Lazar</a>
 */
public class ChannelExamples {

    public void fileChannel() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer directBuffer = ByteBuffer.allocateDirect(1024);

        byte[] byteArray = new byte[2014];

        Path file = Paths.get("pathToFile");

        try (FileInputStream inputStream = new FileInputStream(file.toFile())) {

            //blocking
            //inputStream.read(byteArray);

            FileChannel fileChannel = inputStream.getChannel();
            //loop required and call-back recommended
            fileChannel.read(buffer); //TODO wrong ... it might not be able to fully read

            readFully(fileChannel, buffer);

            //get OS level buffer
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFully(FileChannel fileChannel, ByteBuffer byteBuffer) throws IOException {
        while (true) {
            int read = fileChannel.read(byteBuffer);
            if (read == -1) {
                return;
            } else if (read == 0) {
                //TODO multiplex
                //no Thread.sleep(5);
            }
        }
    }

    public void channelTransferTo() {
        Path file = Paths.get("pathToFile");

        try (FileInputStream inputStream = new FileInputStream(file.toFile())) {

            FileChannel fileChannel = inputStream.getChannel();

            URL url = new URL("http://mysocket");

            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();
            WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);

            fileChannel.transferTo(0, fileChannel.size(), writableByteChannel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
