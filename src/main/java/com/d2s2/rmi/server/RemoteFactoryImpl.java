package com.d2s2.rmi.server;

import com.d2s2.models.AbstractRequestResponseModel;
import com.d2s2.models.Node;
import com.d2s2.models.SearchRequestModel;
import com.d2s2.models.SearchResponseModel;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Heshan Sandamal on 11/4/2017.
 */
public class RemoteFactoryImpl extends UnicastRemoteObject implements RemoteFactory {

    public RemoteFactoryImpl() throws RemoteException {
    }

    @Override
    public AbstractRequestResponseModel getSearchRequestModel(String ip, int port, String fileName, int hops, ArrayList<Node> lastHops) throws RemoteException {
        return new SearchRequestModel(ip,port,fileName,hops,lastHops);
    }

    @Override
    public AbstractRequestResponseModel getSearchReeponseModel(String ip, int port, int hops, int noOfFiles, HashSet<String> fileList) throws RemoteException {
        return new SearchResponseModel(ip,port,hops,noOfFiles,fileList);
    }

}
