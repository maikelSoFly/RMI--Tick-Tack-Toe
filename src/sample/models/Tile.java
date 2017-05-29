package sample.models;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//
//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 27.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

public class Tile extends Rectangle {
    String sign;
    int iid;
    Text txtSign;

    public Tile (double w, double h, int iid) {
        super(w, h);
        this.setFill(Color.LIGHTGRAY);
        this.sign = "";
        this.iid = iid;
        this.setStroke(Color.DARKGRAY);
        this.setStrokeWidth(3);
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getIid() {
        return iid;
    }

    public void setIid(int iid) {
        this.iid = iid;
    }

    public void setTxtSign(String sign, Pane pane) {
        double lX = this.getLayoutX();
        double lY = this.getLayoutY();
        double w = this.getWidth();
        double h = this.getHeight();

        txtSign = new Text(sign);
        txtSign.setFont(new Font(100));
        txtSign.setFill(Color.WHITE);
        txtSign.setStroke(Color.DARKGREY);
        double textWidth = txtSign.layoutBoundsProperty().getValue().getWidth();
        double textHeight = txtSign.layoutBoundsProperty().getValue().getHeight();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtSign.setLayoutX(lX + w/2 - textWidth/2);
                txtSign.setLayoutY(lY + h/2 + textHeight/4);
                txtSign.toFront();

                pane.getChildren().add(txtSign);
            }
        });
    }
}
