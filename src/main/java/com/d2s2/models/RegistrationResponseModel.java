package com.d2s2.models;

import com.d2s2.Handler.Handler;
import com.d2s2.Handler.HandlerImpl;
import com.d2s2.overlay.route.PeerTableImpl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class RegistrationResponseModel implements AbstractRequestResponseModel {
    HashSet<Node> nodeset;
    int nodeCount;
    Handler handler;

    public RegistrationResponseModel(int nodeCount, HashSet<Node> nodeset) throws RemoteException {
        this.nodeCount = nodeCount;
        this.nodeset = nodeset;
        handler = new HandlerImpl();
    }

    @Override
    public void handle() {
        nodeset.forEach((node) -> {
            PeerTableImpl instance = PeerTableImpl.getInstance();
            instance.insert(node);
            try {
                handler.notifyNeighbours(node.getNodeIp(), node.getPort());
            } catch (IOException io) {
                io.printStackTrace();
            }
        });
    }
}
