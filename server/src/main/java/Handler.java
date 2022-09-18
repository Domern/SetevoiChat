import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Handler {
    private final Server server;
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public String getName() {
        return name;
    }

    private String name;

    public Handler(Socket socket, Server server) throws IOException {
        this.name = "";
        this.socket = socket;
        this.server = server;
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            authenticate();
            readMsg();
        }).start();
    }

    private void authenticate() {
        boolean boo=true;
        while (boo) {
            try {
                String str = in.readUTF();
                System.out.println(str);
                if (str.startsWith("#auth")) {
                    String[] split = str.split("\\s");
                    String login = split[1];
                    String password = split[2];
                    String nickname = server.getAuthService().getNicknameByLoginAndPassword(login, password);
                    if (nickname != null) {
                        if (!server.isNicknameBusy(nickname)) {
                            sendMsg("#authok " + nickname);
                            this.name = nickname;
                            server.broadcast("Пользователь " + nickname + " зашел в чат");
                            System.out.println("Пользователь " + nickname + " зашел в чат");
                            server.subscribe(this);
                            boo=false;
                        } else {
                            sendMsg("Уже произведен вход в учетную запись");
                        }
                    } else {
                        sendMsg("Не верный логин/пароль");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readMsg() {
        while (true) {
            String msgFromClient = null;
            try {
                msgFromClient = in.readUTF();
            } catch (IOException e) {
                closeConnection();
                e.printStackTrace();
            }finally {

            }
            if (msgFromClient.equals("#end")) {
                closeConnection();
            } else {
                System.out.println(msgFromClient);
                server.broadcast(msgFromClient);
            }
        }
    }

    private void closeConnection() {
        try {
            socket.close();
            in.close();
            out.close();
            server.unsubscribe(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
