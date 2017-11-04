package com.d2s2.overlay.route;

import com.d2s2.models.Node;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class PeerTableImpl {


    private static Set<Node> peerNodeList;
    private volatile static PeerTableImpl peerTable;

    static {
        peerNodeList = ConcurrentHashMap.newKeySet();
    }

    private PeerTableImpl() {
    }

    public static PeerTableImpl getInstance() {
        if (peerTable == null) {
            synchronized (PeerTableImpl.class) {
                if (peerTable == null) {
                    peerTable = new PeerTableImpl();
                }
            }
        }
        return peerTable;
    }


    public void insert(Node node) {
        peerNodeList.add(node); // todo limit the number of node to 2

    }

    public Set<Node> getPeerNodeList() {
        return peerNodeList;
    }

    public void remove() {

    }

    public void search() {

    }

}
