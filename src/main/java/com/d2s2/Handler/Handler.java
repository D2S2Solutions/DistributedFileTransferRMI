package com.d2s2.Handler;

import com.d2s2.models.Node;
import com.d2s2.models.SearchRequestModel;
import com.d2s2.models.SearchResponseModel;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface Handler {
    public void handleResponse(String message) throws RemoteException, NotBoundException;

    public void registerInBS() throws IOException;

    public void searchFile(String file) throws RemoteException, NotBoundException;

    public void notifyNeighbours(String ip, int port) throws IOException;

    public void sendSearchRequest(SearchRequestModel model, ConcurrentLinkedQueue<Node> concurrentLinkedQueue) throws IOException, NotBoundException;

    public void sendLocalSearchToSource(SearchResponseModel searchResponseModel, List<String> list) throws IOException, NotBoundException;

    public void sendHeartBeatSignal() throws RemoteException;

    public void gracefulLeaveRequest() throws RemoteException;
}
