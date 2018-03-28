package SampleApp;

import auth.Node;
import auth.NodeFileRepository;
import auth.NodeRepository;
import auth.security.AES;
import auth.security.KeyRSA;
import auth.security.RSA;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import kotlin.Pair;

import java.io.File;
import java.util.*;

public class Controller {

    public Button btn_create;
    public Button btn_get;
    public Button btn_connect;
    public Button btn_send;
    public TextArea txt_main;
    public Label lbl_messages;
    public ComboBox box_secondNode;
    public ComboBox box_firstNode;
    public Button btn_reset;
    public GridPane grid;
    public TextField txt_id;
    public ListView list_ids;

    private HashMap nodeList = new HashMap<String, Node>();
    private NodeRepository repository = new NodeFileRepository();
    private RSA rsa = new RSA();
    private AES aes = new AES();

    @FXML
    public void initialize() {
        updateNodeList();
        updateBoxes();
    }

    private void updateNodeList() {
        File folder = new File("secret_data\\");
        File[] listOfFiles = folder.listFiles();
        ArrayList<String> ids = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                ids.add(listOfFiles[i].getName().split("\\.")[0]);
            }
        }
        ArrayList<String> visited = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            if (!visited.contains(ids.get(i))) {
                nodeList.put(ids.get(i), repository.readNode(ids.get(i)));
            } else {
                visited.add(ids.get(i));
            }
        }
    }

    private void appendText(String text) {
        txt_main.setText(txt_main.getText() + text + "\n");
    }

    private void updateBoxes() {
        box_firstNode.setItems(FXCollections.observableArrayList(
                nodeList.keySet().toArray()));
        box_secondNode.setItems(FXCollections.observableArrayList(
                nodeList.keySet().toArray()));
    }


    public void createNodeAction(ActionEvent actionEvent) {
        if (txt_id.getText().isEmpty()) {
            appendText("\"Node Id\" field can not be empty");
        } else {
            Pair keys = rsa.generateKeyPair();
            Node node = new Node(txt_id.getText(), (KeyRSA) keys.getFirst(), (KeyRSA) keys.getSecond(), new HashMap<String, String>());
            if (!repository.addNode(node)) {
                appendText("Node with Id=" + txt_id.getText() + " already exists.");
            } else {
                nodeList.put(node.getId(), node);
                updateBoxes();
                appendText("Node with Id=" + txt_id.getText() + " was created.");
            }
        }
    }

    public void getNodeAction(ActionEvent actionEvent) {
        updateNodeList();
        updateBoxes();
        appendText("Nodes were updated according to file settings");

    }

    public void connectNodesAction(ActionEvent actionEvent) {
        if (box_firstNode.getValue() != null && box_secondNode.getValue() != null) {
            String firstId = box_firstNode.getValue().toString();
            String secondId = box_secondNode.getValue().toString();
            if (firstId.isEmpty() || secondId.isEmpty()) {
                appendText("Please select two nodes in \"First Node\" and \"Second Node\" to perform mutual authentication");
            } else {

                Node first = (Node) nodeList.get(firstId);
                Node second = (Node) nodeList.get(secondId);

                if (first.connectToNode(second, true)) {
                    appendText("Nodes " + firstId + " and " + secondId + " were mutually authenticated.");
                } else {
                    appendText("Nodes " + firstId + " and " + secondId + " were not authenticated. Someone's cheating.");
                }
            }
        } else {
            appendText("Please select two nodes to perform mutual authentication");
        }
    }

    public void hardResetAction(ActionEvent actionEvent) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Request");
        alert.setHeaderText("This will delete all nodes and saved files");
        alert.setContentText("Continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            nodeList.clear();
            repository.clear();
            updateBoxes();
            appendText("\n\nLoaded Nodes and save Files were cleared.\n\n");
        }

    }

    public void SendMsgAction(ActionEvent actionEvent) {
        if (box_firstNode.getValue() != null && box_secondNode.getValue() != null) {
            String firstId = box_firstNode.getValue().toString();
            String secondId = box_secondNode.getValue().toString();
            if (firstId.isEmpty() || secondId.isEmpty()) {
                appendText("Please select two nodes in \"First Node\" and \"Second Node\" to send a message");
            } else {

                Node first = (Node) nodeList.get(firstId);
                Node second = (Node) nodeList.get(secondId);
                String msg = "A cool message! #" + new Random().nextInt();
                if (first.sendMessage(second, msg)) {
                    byte[] key1 = first.getCommonSecretList().get(secondId);
                    byte[] key2 = second.getCommonSecretList().get(firstId);

                    if (Arrays.equals(key1, key2)) {
                        byte[] encoded = aes.encrypt(msg, key1);
                        String decoded = aes.decrypt(encoded, key2);
                        appendText("Both secret keys are equal: " + new String(key1));
                        appendText("Initial msg is: " + msg);
                        appendText("Encoded msg is: " + new String(encoded));
                        appendText("Received msg is: " + decoded);
                    }

                } else {
                    appendText("Nodes " + firstId + " and " + secondId + " were not authenticated. Someone's cheating.");
                }
            }
        } else {
            appendText("Please select two nodes to send messages");
        }
    }
}
