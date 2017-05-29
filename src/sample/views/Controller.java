package sample.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import sample.Main;
import sample.models.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    private Label lbl1;
    @FXML
    private Label lbl2;

    private ArrayList<Tile> tilesList;
    private int tilesInARow = 3;
    private String sign;
    private boolean isServer = false;
    private Main mainApp;
    private boolean myTurn = false;

    public Controller() {

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tilesList = new ArrayList<>();

        double tileWidth = 400 / tilesInARow;
        double tileHeight = 400 / tilesInARow;

        for(int i = 0; i < tilesInARow; ++i) {
            for(int j = 0; j < tilesInARow; ++j) {
                Tile tile = new Tile(tileWidth, tileHeight, i *tilesInARow+ j);
                tile.setLayoutX(j * tile.getWidth() + (tile.getStrokeWidth() / (tilesInARow+1))/2);
                tile.setLayoutY(i * tile.getHeight());
                tilesList.add(tile);
            }
        }
        pane.getChildren().addAll(tilesList);

        ButtonType btnServer = new ButtonType("Server", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnClient = new ButtonType("Client", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Choose mode:",
                btnServer,
                btnClient);

        alert.setTitle("Mode");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == btnServer) {
            isServer = true;
        }

        if(isServer) {
            sign = "x";
            lbl1.setText(sign.toUpperCase());
            changeTurn();

            try {
                System.out.println("Im server");
                Server server = new Server();
                server.setGUIListener(new GUIListener() {
                    @Override
                    public void writeText(GUIEvent evt) {
                        tilesList.get(evt.getTileNum()).setTxtSign(evt.getText(), pane);
                        changeTurn();
                    }
                });

                for(Tile tile : tilesList) {
                    tile.setOnMouseClicked(event -> {
                        if(myTurn) {
                            tile.setSign(sign);
                            tile.setTxtSign(sign, pane);
                            server.sendToClient(sign, tile.getIid());
                            changeTurn();
                        }
                    });
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            sign = "o";
            lbl1.setText(sign.toUpperCase());
            lbl2.setText("WAIT FOR YOUR TURN");
            try {
                System.out.println("Im client");
                Client client = new Client();
                client.setGUIListener(new GUIListener() {
                    @Override
                    public void writeText(GUIEvent evt) {
                        tilesList.get(evt.getTileNum()).setTxtSign(evt.getText(), pane);
                        changeTurn();

                    }
                });

                for(Tile tile : tilesList) {
                    tile.setOnMouseClicked(event -> {
                        if(myTurn) {
                            tile.setSign(sign);
                            tile.setTxtSign(sign, pane);
                            client.sendToServer(sign, tile.getIid());
                            changeTurn();
                        }
                    });
                }


            } catch(IOException e) {
                e. printStackTrace();
            }
        }

    }

    private void changeTurn() {
        myTurn = !myTurn;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(myTurn)
                    lbl2.setText("YOUR TURN");
                else lbl2.setText("WAIT FOR YOUR TURN");
            }
        });
    }
}
