package client.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Sender extends Thread {

    private String hostname;
    private int port;

    public Sender(String hostName, int port) {
        this.hostname = hostName;
        this.port = port;
    }
   

    @Override
    public void run() {
        TargetDataLine microphone;
        SourceDataLine speakers;
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
//            microphone = AudioSystem.getTargetDataLine(format);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();

            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
//            speakers.open(format);
//            speakers.start();

            // Configure the ip and port
//            String hostname = "192.168.1.31";
//            int port = 5555;

            InetAddress address = InetAddress.getByName(hostname);
            DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[1024];
            for (;;) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                //  bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead);
                // write mic data to stream for immediate playback
//                speakers.write(data, 0, numBytesRead);
                DatagramPacket request = new DatagramPacket(data, numBytesRead, address, port);
                socket.send(request);

            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}