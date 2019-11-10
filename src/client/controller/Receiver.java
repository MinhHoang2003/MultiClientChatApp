package client.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Receiver extends Thread {

    private String hostname;
    private int port;

    public Receiver(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
   

    @Override
    public void run() {
        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
            TargetDataLine microphone;
            SourceDataLine speakers;
//            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
//            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
//            microphone.start();

            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

//            String hostname = "localhost";
//            int port = 6666;

            try {
                InetAddress address = InetAddress.getByName(hostname);
                DatagramSocket socket = new DatagramSocket();

                DatagramSocket serverSocket = new DatagramSocket(port);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];

                while (true) {

                    byte[] buffer = new byte[1024];
                    DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    serverSocket.receive(response);

                    out.write(response.getData(), 0, response.getData().length);
                    speakers.write(response.getData(), 0, response.getData().length);
                    String quote = new String(buffer, 0, response.getLength());

                    System.out.println(quote);
                    System.out.println();

                    //Thread.sleep(10000);
                }

            } catch (SocketTimeoutException ex) {
                System.out.println("Timeout error: " + ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("Client error: " + ex.getMessage());
                ex.printStackTrace();
            }/* catch (InterruptedException ex) {
            ex.printStackTrace();
            }*/
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }/* catch (InterruptedException ex) {
        ex.printStackTrace();
    }*/
    }
}
