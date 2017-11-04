package com.d2s2.heartbeater;

import com.d2s2.constants.ApplicationConstants;
import com.d2s2.models.Node;
import com.d2s2.overlay.route.NeighbourTableImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tharindu Diluksha on 10/24/2017.
 */
public class HeartBeaterImpl {

    private static HeartBeaterImpl heartBeater;
    private static volatile Set<Node> beatedNodes;
    private static volatile int count;

    static {
        count = 0;
        beatedNodes = ConcurrentHashMap.newKeySet();
    }

    private HeartBeaterImpl() {
        // ConcurrentHashMap<String, ConcurrentLinkedQueue<Node>> statTable = StatTableImpl.getInstance().
    }

    public static HeartBeaterImpl getInstance() {
        if (heartBeater == null) {
            synchronized (HeartBeaterImpl.class) {
                if (heartBeater == null) {
                    heartBeater = new HeartBeaterImpl();
                }
            }
        }
        return heartBeater;
    }

    public void handleBeat(Node beatedNode) {
        //todo if there are no consecutive two beats with in the time period (10beats) remove node from neighbour(down) list
        if (ApplicationConstants.HEART_BEAT_THRESHOLD == count) {
            for (Node neighbourNode : NeighbourTableImpl.getInstance().getNeighbourNodeList()) {
                if (!beatedNodes.contains(neighbourNode)) {
                    System.out.println("Removing node in heartbeating failure " + String.valueOf(count));
                    NeighbourTableImpl.getInstance().remove(neighbourNode);
                    //todo remove from stat table
                }
            }
            count = 0;
            beatedNodes.clear();
        } else {
            System.out.println("Adding beating node " + String.valueOf(count));
            beatedNodes.add(beatedNode);
            count++;
        }
    }
}
