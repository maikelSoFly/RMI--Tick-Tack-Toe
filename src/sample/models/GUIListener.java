package sample.models; //

import java.util.EventListener;

//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//
public interface GUIListener extends EventListener {
    // zwykly listener do polaczenia sieci z GUI
    void writeText(GUIEvent evt);
    void reset();
}
