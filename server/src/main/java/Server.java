import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private final List<Handler> clients;
    private final AuthService authService;

    public Server() {
        clients=new ArrayList<>();
        authService=new SimpleAuthService();
    try (ServerSocket serverSocket = new ServerSocket(8193);) {
            System.out.println("Server start");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket,this);
                System.out.println("New Handler");
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void subscribe(Handler handler){
        clients.add(handler);
    }

    public void unsubscribe(Handler handler){
        clients.remove(handler);
    }

    public AuthService getAuthService() {
        return authService;
    }

    public boolean isNicknameBusy(String nickname) {
        for (Handler client : clients) {
            if(client.getName().equals(nickname)){
                return true;
            }
        }
        return false;
    }
    public void broadcast(String msg){
        for (Handler client : clients) {
            client.sendMsg(msg);
        }
    }
}
