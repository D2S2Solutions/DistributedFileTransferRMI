package com.d2s2.models;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public interface AbstractRequestResponseModel extends Remote{


    void handle() throws RemoteException, NotBoundException;

}
