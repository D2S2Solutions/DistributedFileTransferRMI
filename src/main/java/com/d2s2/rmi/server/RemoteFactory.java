package com.d2s2.rmi.server;

import com.d2s2.models.AbstractRequestResponseModel;
import com.d2s2.models.Node;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Heshan Sandamal on 11/4/2017.
 */
public interface RemoteFactory extends Remote{

    AbstractRequestResponseModel getSearchRequestModel(String ip, int port, String fileName, int hops, ArrayList<Node> lastHops) throws RemoteException;
    AbstractRequestResponseModel getSearchReeponseModel(String ip, int port, int hops, int noOfFiles, HashSet<String> fileList) throws RemoteException;
}
