/**
 * Created by Don on 11/23/2016 AD.
 */
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Register_master implements Runnable {

    DatagramSocket datagram_socket = null;

    private String client_or_master;

    public Register_master(String msg) {
        this.client_or_master = msg;
    }

    public void run(){
        try{
            datagram_socket = new DatagramSocket();
            datagram_socket.setBroadcast(true);
            byte[] sendData = client_or_master.getBytes();
            while (true){
                try{
                    InetAddress ip = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(sendData,sendData.length,ip,8686);
                    datagram_socket.send(packet);

                }catch(IOException e){
                    e.printStackTrace();
                }

                // Broadcast the message over all the network interfaces
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    // Don't want to broadcast to the loopback interface
                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                        continue;
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                            continue;
                        }
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8686);
                        datagram_socket.send(sendPacket);
                    }
                }

                //Wait for a response
                byte[] recvBuf = new byte[16000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                datagram_socket.receive(receivePacket);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}