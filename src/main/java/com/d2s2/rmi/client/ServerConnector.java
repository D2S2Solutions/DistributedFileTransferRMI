package com.d2s2.rmi.client;

import com.d2s2.models.AbstractRequestResponseModel;
import com.d2s2.models.Node;
import com.d2s2.rmi.server.RemoteFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heshan Sandamal on 11/4/2017.
 */
public class ServerConnector {

    private static ServerConnector serverConnector;
    private  RemoteFactory remoteFactory;

    private String ip;
    private AbstractRequestResponseModel searchResponseModel;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public  static Set<ServerConnector> getServerConnectors() {
        return serverConnectors;
    }

    public static void setServerConnectors(HashSet<ServerConnector> serverConnectors) {
        ServerConnector.serverConnectors = serverConnectors;
    }

    private int port;

    private volatile static Set<ServerConnector> serverConnectors;

    static {
        serverConnectors = ConcurrentHashMap.newKeySet();
    }

    private AbstractRequestResponseModel searchRequestModel;

    private ServerConnector(String ip, int port) throws NotBoundException, MalformedURLException, RemoteException {
        remoteFactory = (RemoteFactory) Naming.lookup("rmi://" + ip + ":" + port + "/DBFileTranfer");
        this.ip=ip;
        this.port=port;
    }


    public static ServerConnector getServerConnector(String ip, int port) throws RemoteException, NotBoundException, MalformedURLException {

        synchronized (ServerConnector.class) {

            for (ServerConnector sc : serverConnectors) {
                if (sc.getPort() == port && sc.getIp().equals(ip)) {
                    return sc;
                }
            }

            ServerConnector serverConnector = new ServerConnector(ip, port);
            serverConnectors.add(serverConnector);
            return serverConnector;
        }


    }

    public void callRemoteSearchRequestHadle(String ip, int port, String fileName, int hops, ArrayList<Node> lastHops) throws RemoteException, NotBoundException {
        searchRequestModel = remoteFactory.getSearchRequestModel(ip, port, fileName, hops, lastHops);
        if(searchRequestModel!=null){
            this.searchRequestModel.handle();
            System.out.println("Invoking Search Request remote method");
        }else{
            System.out.println("remote object is null");
        }

    }


    public void callRemoteSearchResponseHadle(String ip, int port, int hops, int noOfFiles, HashSet<String> fileList) throws RemoteException, NotBoundException {
        searchResponseModel = remoteFactory.getSearchReeponseModel(ip, port, hops, noOfFiles, fileList);
        if(searchResponseModel!=null){
            this.searchResponseModel.handle();
            System.out.println("Invoking Search Response remote method");
        }else{
            System.out.println("remote object is null");
        }

    }

    @Override
    public int hashCode() {
        return (this.ip + ":" + String.valueOf(this.port)).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        ServerConnector sc = (ServerConnector) o;
        return (this.ip.equals(sc.getIp()) && this.port == sc.getPort()) ? true : false;
    }
}
