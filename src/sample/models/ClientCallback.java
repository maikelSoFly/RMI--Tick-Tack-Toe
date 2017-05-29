package sample.models;
//

//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    // Interfejsy do ustawiania callbacków na serwerze (metod z klienta uruchamianych przez serwer)

    void setOnClientCallback(ClientCallbackListener clientCallbackListener) throws RemoteException;
    ClientCallbackListener getOnClientCallback() throws RemoteException;
}