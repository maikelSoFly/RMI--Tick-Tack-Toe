package sample.models;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

//
//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

public class Server extends UnicastRemoteObject implements ServerInterface, ClientCallback {

    private ClientCallbackListener clientCallbackListener;
    private GUIListener GUIListener;

    public Server() throws RemoteException {
        // RMI Server registration
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("TestServer", this);
    }

    @Override
    public void setOnClientCallback(ClientCallbackListener clientCallbackListener) throws RemoteException {
        // Setting method from client to be used in the future by server
        this.clientCallbackListener = clientCallbackListener;
    }

    @Override
    public ClientCallbackListener getOnClientCallback() throws RemoteException {
        return clientCallbackListener;
    }

    @Override
    public void sendText(String text, int tileNum) throws RemoteException {
        // Server's method calling from client
        if(GUIListener != null){
            GUIListener.writeText(new GUIEvent(this, text, tileNum));
        }
    }

    public void sendToClient(String text, int tileNum){
        // Calling method from client
        if(clientCallbackListener != null) {
            try {
                clientCallbackListener.sendToClient(text, tileNum);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    //GETTERS & SETTERS
    public ClientCallbackListener getClientCallbackListener() {
        return clientCallbackListener;
    }

    public void setClientCallbackListener(ClientCallbackListener clientCallbackListener) {
        this.clientCallbackListener = clientCallbackListener;
    }

    public GUIListener getGUIListener() {
        return GUIListener;
    }

    public void setGUIListener(GUIListener guiTextListener) {
        this.GUIListener = guiTextListener;
    }
}
