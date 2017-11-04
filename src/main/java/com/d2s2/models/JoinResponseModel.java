package com.d2s2.models;

import java.rmi.RemoteException;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class JoinResponseModel implements AbstractRequestResponseModel {

    int status;

    public JoinResponseModel(int status) throws RemoteException {
        this.status = status;
    }

    @Override
    public void handle() {
        System.out.println("Join Response Received");

    }
}
