import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

public class getIP {

    static String getMyIP() throws Exception{
        Enumeration<NetworkInterface> list = NetworkInterface.getNetworkInterfaces();
        NetworkInterface current;
        boolean found = false;
        while(!found) {
            if (!list.hasMoreElements()) {
                list = NetworkInterface.getNetworkInterfaces();
            }
            current = list.nextElement();
            List<InterfaceAddress> interfaceAddresses = current.getInterfaceAddresses();
            if (interfaceAddresses.size() < 1) {
                continue;
            }
            InterfaceAddress address = interfaceAddresses.get(0);
            String IP = address.getAddress().getHostAddress();
            if (IP.length() > 15) {
                continue;
            }
            if (IP.substring(0,3).contains("10.")){
                return IP;
            }
        }
        return "None";}
    public static void main(String[] args) {
        try {
            System.out.println(getMyIP());
        }catch (Exception e){
            System.out.println("Error");
        }

    }
}
