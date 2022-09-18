import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Socket socket;


    public Connection() throws IOException {
        socket = new Socket("localhost",8193);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public DataOutputStream outputStream(){
        return out;
    }

    public DataInputStream inputStream()  {
        return in;
    }
}
