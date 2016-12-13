import java.io.File;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;


public class Main {
    static String torrent_name = "default.torrent";
    static String m_check="M";
    static String tcp_port="1212";
    static String path;
    static String file_name;
    static String windowMachineName = System.getProperty("user.name").toUpperCase();
    static boolean found_status = false;

    static String get_master_ip(String substringg) {
        ConcurrentSkipListSet<String> temp = Broadcast_serv.get_IP();
        for (String x : temp) {
            if (x.startsWith(substringg)) {
                String masterIP = x.substring(substringg.length());
                return masterIP;
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception {

        Thread register = new Thread(new Register());
        Thread serv = new Thread(new Broadcast_serv(m_check));
        register.start();
        serv.start();

        System.out.println("If master enter the file name else type 'c' for client");
        Scanner user_input_scanner = new Scanner(System.in);
        file_name = user_input_scanner.nextLine();

        path = "C:/Users/" + windowMachineName + "/Desktop/";
        String type = ""; //either "client or master"
        while (found_status == false) {
            File f = new File(path + file_name);
            if (f.exists()) {
                System.out.println("File found");
                System.out.println("Master Machine");
                found_status = true;
                type = "master";
            }

            if (get_master_ip(m_check).equals("")) {
                continue;
            } else {
                System.out.println("Client Machine");
                type = "client";
                break;
            }
        }

        Thread reegister_m = new Thread(new Register_master(m_check));
        if (type.equals("master")) {
            register.stop();
            reegister_m.start();
            String IPP = getIP.getMyIP();
            Thread give_torrent = new Thread(new SimpleFileServer(tcp_port,path+torrent_name));
            T_torrent.makeTorrent(path+file_name,path,torrent_name,IPP,InetAddress.getByName(IPP));
            give_torrent.start();

            Seed.loadIt(path+torrent_name,path);

        } else if (type.equals("client")) {
            String torrentGetter_url = get_master_ip(m_check);
            Thread.sleep(5000);
            SimpleFileClient s = new SimpleFileClient(torrentGetter_url,tcp_port,path+torrent_name);
            s.request();
            Seed.loadIt(path+torrent_name,path);
        }
    }
}


