package sample.views;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import sample.Main;
import sample.models.*;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
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
    @FXML
    private Button btnReset;

    private ArrayList<Tile> tilesList;
    private int tilesInARow = 3;
    private String sign;
    private boolean isServer = false;
    private Main mainApp;
    private boolean isMyTurn = false;
    private boolean isWin = false;
    private Line winLine;
    private Server server = null;
    private Client client = null;

    private int[][] winSpots = new int[][] {
            {0,1,2},
            {3,4,5},
            {6,7,8},
            {0,3,6},
            {1,4,7},
            {2,5,8},
            {0,4,8},
            {2,4,6}
    };

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
                this.server = server;
                server.setGUIListener(new GUIListener() {
                    @Override
                    public void writeText(GUIEvent evt) {
                        tilesList.get(evt.getTileNum()).setSign(evt.getText());
                        tilesList.get(evt.getTileNum()).setTxtSign(evt.getText(), pane);
                        drawWinLine(checkWin(evt.getText()));
                        changeTurn();
                    }
                    @Override
                    public void reset() {
                        resetMe();
                    }
                });

                for(Tile tile : tilesList) {
                    tile.setOnMouseClicked(event -> {
                        if(isMyTurn && !isWin) {
                            tile.setSign(sign);
                            tile.setTxtSign(sign, pane);
                            server.sendToClient(sign, tile.getIid());
                            drawWinLine(checkWin(sign));
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
                this.client = client;
                client.setGUIListener(new GUIListener() {
                    @Override
                    public void writeText(GUIEvent evt) {
                        tilesList.get(evt.getTileNum()).setSign(evt.getText());
                        tilesList.get(evt.getTileNum()).setTxtSign(evt.getText(), pane);
                        drawWinLine(checkWin(evt.getText()));
                        changeTurn();
                    }
                    @Override
                    public void reset() {
                        resetMe();
                    }
                });

                for(Tile tile : tilesList) {
                    tile.setOnMouseClicked(event -> {
                        if(isMyTurn && !isWin) {
                            tile.setSign(sign);
                            tile.setTxtSign(sign, pane);
                            client.sendToServer(sign, tile.getIid());
                            drawWinLine(checkWin(sign));
                            changeTurn();
                        }
                    });
                }


            } catch(IOException e) {
                e. printStackTrace();
            }
        }
    }

    private Object checkWin(String sign) {
        for(int i = 0; i < 8; ++i) {
            boolean win = true;
            for(int j = 0; j < 3; ++j) {
                if(!tilesList.get(winSpots[i][j]).getSign().equals(sign)) {
                    win = false;
                }
            }
            if(win) return i;
        }
        return null;
    }

    private void drawWinLine(Object ob) {
        if(ob != null) {
            isWin = true;
            Tile tileStart = tilesList.get(winSpots[(int)ob][0]);
            Tile tileEnd = tilesList.get(winSpots[(int)ob][2]);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Line line = new Line(tileStart.getLayoutX() + tileStart.getWidth()/2,
                            tileStart.getLayoutY() + tileStart.getHeight()/2,
                            tileEnd.getLayoutX() + tileEnd.getWidth()/2,
                            tileEnd.getLayoutY() + tileEnd.getHeight()/2
                    );
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    line.setOpacity(0.8);
                    line.setStroke(Color.color(0.61,1,0.3));
                    line.setStrokeWidth(10);
                    winLine = line;
                    line.toFront();
                    pane.getChildren().add(line);
                }
            });

        }
    }

    @FXML
    private void handleReset() {
        resetMe();

        if(server != null) {
            server.sendResetToClient();
        }

        else if(client != null) {
            client.sendResetToServer();
        }


    }

    private void resetMe() {
        System.out.println("Reset");
        for(Tile tile : tilesList) {
            tile.setSign("");
            tile.removeTxtSign(pane);
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(isWin) {
                    pane.getChildren().remove(winLine);
                    isWin = false;
                }
            }
        });
    }

    private void changeTurn() {
        isMyTurn = !isMyTurn;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(isMyTurn)
                    lbl2.setText("YOUR TURN");
                else lbl2.setText("WAIT FOR YOUR TURN");
            }
        });
    }
}
