import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    private Connection connection;

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
                textArea.setText(msg+"\n"+textArea.getText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}