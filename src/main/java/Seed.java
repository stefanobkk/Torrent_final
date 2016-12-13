import com.turn.ttorrent.client.Client;
import com.turn.ttorrent.client.SharedTorrent;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import static java.lang.System.out;

public class Seed {
    public static void loadIt(String torrent_file_path,String original_file_dir) throws InterruptedException {
        try {
            String MyIP =getIP.getMyIP();
            Client client = new Client(InetAddress.getByName(MyIP), SharedTorrent.fromFile(new File(torrent_file_path), new File(original_file_dir)));
            client.addObserver(new Observer(){
                @Override
                public void update(Observable observable, Object data) {
                    Client client = (Client) observable;
                    int progress = Math.round(client.getTorrent().getCompletion());

                    try {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                StringBuilder progress_bar = new StringBuilder(100);
                                for (int i=0; i<progress; i++){
                                    progress_bar.append("-");
                                }
                                out.printf("\rProgress |%s| - %d%s", progress_bar.toString(), progress,"%");;
                            }
                        });
                        java.lang.Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            });
            client.download();
            client.share(3600);
            client.waitForCompletion();
        } catch (UnknownHostException e) {
            out.println(e.getMessage());
        } catch (IOException e) {
            out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
