package com.d2s2.models;

import java.rmi.RemoteException;

public class UnregistrationRequestModel extends AbstractRequestModel {

    String userName;

    public UnregistrationRequestModel(String ip, int port, String userName) throws RemoteException {
        super(ip, port);
        this.userName = userName;
    }

    @Override
    public String toString() {
        int length = ip.length() + String.valueOf(port).length() + userName.length() + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " UNREG " + ip + " " + port + " " + userName;
    }

    @Override
    public void handle() {

    }
}
