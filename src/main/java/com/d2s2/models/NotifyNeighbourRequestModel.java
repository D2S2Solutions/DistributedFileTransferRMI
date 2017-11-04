package com.d2s2.models;

import com.d2s2.Handler.Handler;
import com.d2s2.Handler.HandlerImpl;
import com.d2s2.overlay.route.NeighbourTableImpl;
import com.d2s2.overlay.route.PeerTableImpl;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Created by dimuth on 10/24/17.
 */
public class NotifyNeighbourRequestModel extends AbstractRequestModel {

    private static Handler handler = new HandlerImpl();

    public NotifyNeighbourRequestModel(String ip, int port) throws RemoteException {
        super(ip, port);

    }


    @Override
    public void handle() {
        NeighbourTableImpl neighbourTable = NeighbourTableImpl.getInstance();
        Node node = new Node(this.ip, this.port);
        neighbourTable.insert(node);
        PeerTableImpl peerTable = PeerTableImpl.getInstance();
        if (peerTable.getPeerNodeList().size() < 10) {
            if(!peerTable.getPeerNodeList().contains(node)){
                peerTable.insert(node);
                try {
                    handler.notifyNeighbours(node.getNodeIp(), node.getPort());
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
        System.out.println("Peer table");
        System.out.println(peerTable.getPeerNodeList());


    }
}
