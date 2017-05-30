package sample.models; //

import java.rmi.Remote;
import java.rmi.RemoteException;

//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//
public interface ClientCallbackListener extends Remote {
    // Metoda implementowana w kliencie ale uruchamiana na serwerze
    void sendToClient(String text, int tileNum) throws RemoteException;
    void sendResetToClient() throws RemoteException;
}
