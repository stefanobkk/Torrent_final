import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SimpleFileClient{
    static String IP ;
    static Integer PORT;
    static String PATHFILE;
    public SimpleFileClient(String IP,String port,String PF){
        Integer p = Integer.valueOf(port);
        this.PORT = p;
        this.IP = IP;
        this.PATHFILE = PF;
    }

    public static void request() throws IOException {
        int filesize = 15022386;
        int bytesRead;
        int currentTot;

        Socket socket = new Socket(IP,PORT);
        byte [] bytearray = new byte [filesize];
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(PATHFILE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray,0,bytearray.length);
        currentTot = bytesRead;

        do {
            bytesRead = is.read(bytearray, currentTot, (bytearray.length-currentTot));
            if(bytesRead >= 0) currentTot += bytesRead;
        }
        while(bytesRead > -1);{
            bos.write(bytearray, 0, currentTot);
            bos.flush();
            bos.close();
            socket.close();
        }
    }
}


