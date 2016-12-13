
import com.turn.ttorrent.common.Torrent;
import com.turn.ttorrent.tracker.TrackedTorrent;
import com.turn.ttorrent.tracker.Tracker;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;


public class T_torrent {

    public static void makeTorrent(String filepath,String outfiledir,String file_name,String master,InetAddress mas) {
        String sharedFile = filepath;
        try {
            String url = "http://"+master+":3434"+"/"+"announce";
            URI uri = new URI(url);
            Tracker tracker = new Tracker(new InetSocketAddress(mas,3434));
            tracker.start();
            Torrent torrent = Torrent.create(new File(sharedFile),uri , "createdByAuthor");
            FileOutputStream fos = new FileOutputStream(outfiledir+file_name);
            torrent.save( fos );
            fos.close();
            System.out.println(".torrent has been created");
            tracker.announce((TrackedTorrent.load(new File(outfiledir+file_name))));

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
