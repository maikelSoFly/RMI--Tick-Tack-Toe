package sample.models;

import java.util.EventObject;

//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

public class GUIEvent extends EventObject {
    // Klasa bedaca parametrem listenera laczacego siec z GUI
    private String text;
    private int tileNum;

    public GUIEvent(Object source, String text, int tileNum) {
        super(source);
        this.text = text;
        this.tileNum = tileNum;
    }

    public String getText() {
        return text;
    }

    public int getTileNum() {
        return tileNum;
    }
}
