/**
 * Created by Don on 11/23/2016 AD.
 */
// source: http://michieldemey.be/blog/network-discovery-using-udp-broadcast/
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentSkipListSet;

public class Broadcast_serv implements Runnable {
    DatagramSocket socket;

    private String zos;

    public Broadcast_serv(String zos) {
        this.zos = zos;
    }
    //Construct a new, empty set that order its element according to their natural ordering
    static ConcurrentSkipListSet<String> IP = new ConcurrentSkipListSet<>();

    public static ConcurrentSkipListSet get_IP(){
        return IP;
    }

    @Override
    public void run() {
        try {
            //Keep a socket open to listen to all the UDP traffic that is destined for this port
            socket = new DatagramSocket(8686, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                //Receive a packet
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                String message = new String(packet.getData()).trim();

                //See if the packet holds the right command (message)
                if (message.equals("REQUEST")) {
                    byte[] sendData = "RESPONSE".getBytes();
                    IP.add(packet.getAddress().getHostAddress());
                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                }
                if (message.equals(zos)){
                    byte[] sendData = zos.getBytes();
                    IP.add( message+ packet.getAddress().getHostAddress());
                    //Send a response
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}