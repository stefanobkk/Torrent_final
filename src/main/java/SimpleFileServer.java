import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleFileServer implements Runnable {
    static Integer PORT;
    static String FILE_TO_SEND;

    public SimpleFileServer(String port,String FTS){
        Integer pp = Integer.valueOf(port);
        this.PORT = pp;
        this.FILE_TO_SEND=FTS;
    }
    public void run() {
        FileInputStream file_input_steam;
        BufferedInputStream buffer_input_stream = null;
        OutputStream output_stream = null;
        ServerSocket servsock;
        Socket sock = null;
        try {
            servsock = new ServerSocket(PORT);
            while (true) {

                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);

                    // send file
                    File myFile = new File (FILE_TO_SEND);
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    file_input_steam = new FileInputStream(myFile);
                    buffer_input_stream = new BufferedInputStream(file_input_steam);
                    buffer_input_stream.read(mybytearray,0,mybytearray.length);

                    System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                    output_stream = sock.getOutputStream();
                    output_stream.write(mybytearray,0,mybytearray.length);
                    output_stream.flush();
                    System.out.println("Done");
                }
                finally {
                    if (buffer_input_stream != null) {
                        buffer_input_stream.close();
                    }
                    if (output_stream != null) {
                        output_stream.close();
                    }
                    if (sock!=null) {
                        sock.close();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
