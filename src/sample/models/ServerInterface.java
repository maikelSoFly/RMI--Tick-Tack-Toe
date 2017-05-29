package sample.models; //

//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

import java.rmi.Remote;
import java.rmi.RemoteException;

interface ServerInterface extends Remote {
    void sendText(String text, int tileNum)  throws RemoteException;
}