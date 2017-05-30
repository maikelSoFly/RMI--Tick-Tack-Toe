package sample.models;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

//
//     Project name: RMI--Tick-Tack-Toe
//
//     Created by maikel on 28.05.2017.
//     Copyright © 2017 Mikołaj Stępniewski. All rights reserved.
//

public class Client extends UnicastRemoteObject implements ClientCallbackListener {
    private GUIListener GUIListener;

    String serverAddr = "rmi://localhost:1099/TestServer";
    Remote remoteService = null;
    private ClientCallbackListener clientCallbackListener;

    public Client() throws RemoteException {
        super();
        try {
            remoteService = Naming.lookup ( serverAddr );

            // Sending client's own method to server (callback), to allow the server to use it

            ClientCallback callback = (ClientCallback)remoteService;
            callback.setOnClientCallback(this);

        } catch (NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void sendToServer(String text, int tileNum){
        // Sending message to Server
        try {
            ServerInterface server = (ServerInterface)remoteService;
            server.sendText(text, tileNum);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void sendResetToServer(){
        // Sending message to Server
        try {
            ServerInterface server = (ServerInterface)remoteService;
            server.sendReset();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendToClient(String text, int tileNum) throws RemoteException {
        // Method which be used by server
        if(GUIListener != null){
            GUIListener.writeText(new GUIEvent(this, text, tileNum));
        }
    }

    @Override
    public void sendResetToClient() {
        if(GUIListener != null) {
            GUIListener.reset();
        }
    }

    public GUIListener getGUIListener() {
        return GUIListener;
    }

    public void setGUIListener(GUIListener guiTextListener) {
        this.GUIListener = guiTextListener;
    }

}
