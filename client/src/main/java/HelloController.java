import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Connection connection;


    public TextField loginField;
    public PasswordField passField;
    public TextField serviceField;
    public Button sendAuthButton;
    public Button sendMsgButtom;
    public TextField textField;
    public TextArea textArea;
    public ListView listView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            connection=new Connection();
            new Thread(()->readMsg()).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMsg() {
        try {
            while (true) {
                String msg = connection.inputStream().readUTF();
                if(msg.startsWith("#authok")){
                    authOk(msg);
                }else {
                    serviceField.setText(msg);
                    textArea.setText(msg + "\n" + textArea.getText());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void authOk(String msg){
        loginField.setVisible(false);
        passField.setVisible(false);
        serviceField.setVisible(false);
        sendAuthButton.setVisible(false);

        textArea.setVisible(true);
        textField.setVisible(true);
        sendMsgButtom.setVisible(true);
        listView.setVisible(true);

        String[] split = msg.split("\\s");
    }

    public void sendMsg(ActionEvent actionEvent) {
        try {
            String text = textField.getText();
            connection.outputStream().writeUTF(text);
            textField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAuth(ActionEvent actionEvent) {
        String autf="#auth "+loginField.getText()+" "+passField.getText();
        try {
            connection.outputStream().writeUTF(autf);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}