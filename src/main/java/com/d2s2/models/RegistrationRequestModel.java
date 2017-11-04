package com.d2s2.models;

import java.rmi.RemoteException;

public class RegistrationRequestModel extends AbstractRequestModel {

    String userName;

    public RegistrationRequestModel(String ip, int port, String userName) throws RemoteException {
        super(ip, port);
        this.userName = userName;
    }

    @Override
    public String toString() {
        int length = ip.length() + String.valueOf(port).length() + userName.length() + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " REG " + ip + " " + port + " " + userName;
    }

    @Override
    public void handle() {


    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
