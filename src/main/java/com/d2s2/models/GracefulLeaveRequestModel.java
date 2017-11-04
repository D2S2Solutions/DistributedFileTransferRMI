package com.d2s2.models;

import java.rmi.RemoteException;

public class GracefulLeaveRequestModel extends AbstractRequestModel {

    private String userName;

    public GracefulLeaveRequestModel(String ip, int port, String userName) throws RemoteException {
        super(ip, port);
        this.userName = userName;
    }

    @Override
    public String toString() {
        int length = ip.length() + String.valueOf(port).length() + userName.length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " LEAVE " + ip + " " + port + " ";
    }

    @Override
    public void handle() {

    }
}
