/**
 * Created by Don on 11/23/2016 AD.
 */
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

public class Register implements Runnable {

    DatagramSocket c = null;

    public void run(){
        try{
            c = new DatagramSocket();
            c.setBroadcast(true);
            byte[] send_data_message = "REQUEST".getBytes();
            while (true){
                try{
                    InetAddress ip = InetAddress.getByName("255.255.255.255");
                    DatagramPacket packet = new DatagramPacket(send_data_message,send_data_message.length,ip,8686);
                    c.send(packet);
                }catch(IOException e){
                    e.printStackTrace();
                }

                // Broadcast the message over all the network interfaces
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                        continue; // Don't want to broadcast to the loopback interface
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                            continue;
                        }
                        // Send the broadcast package!
                        DatagramPacket sendPacket = new DatagramPacket(send_data_message, send_data_message.length, broadcast, 8686);
                        c.send(sendPacket);
                    }
                }
                //Wait for a response
                byte[] recvBuf = new byte[15000];
                DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
                c.receive(receivePacket);

                //Check if the message is correct
                String message = new String(receivePacket.getData()).trim();
                if (message.equals("RESPONSE")) {
                    continue;
                }

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