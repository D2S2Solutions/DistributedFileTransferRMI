package com.d2s2.Handler;

import com.d2s2.constants.ApplicationConstants;
import com.d2s2.message.build.MessageBuilder;
import com.d2s2.message.build.MessageBuilderImpl;
import com.d2s2.message.tokenize.MessageTokenizer;
import com.d2s2.message.tokenize.MessageTokenizerImpl;
import com.d2s2.models.*;
import com.d2s2.overlay.NeighbourTableImpl;
import com.d2s2.overlay.PeerTableImpl;
import com.d2s2.rmi.client.ServerConnector;
import com.d2s2.socket.UDPConnectorImpl;
import com.d2s2.socket.UdpConnector;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Heshan Sandamal on 10/24/2017.
 */
public class HandlerImpl implements Handler {

    MessageTokenizer messageTokenizer;
    MessageBuilder messageBuilder;
    UdpConnector udpConnector;

    public HandlerImpl() {
        this.udpConnector = new UDPConnectorImpl();
        this.messageTokenizer = new MessageTokenizerImpl();
        this.messageBuilder = new MessageBuilderImpl();
    }

    @Override
    public void handleResponse(String message) throws RemoteException, NotBoundException {
        AbstractRequestResponseModel abstractRequestResponseModel = messageTokenizer.tokenizeMessage(message);
        if (abstractRequestResponseModel != null) {
            abstractRequestResponseModel.handle();
        }

    }

    @Override
    public void registerInBS() throws IOException {
        RegistrationRequestModel registrationRequestModel = new RegistrationRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, ApplicationConstants.USER_NAME);
        String message = messageBuilder.buildRegisterRequestMessage(registrationRequestModel);
        udpConnector.send(message, null, 55555);
    }

    @Override
    public void sendHeartBeatSignal() throws RemoteException {
        Set<Node> peerNodes = PeerTableImpl.getInstance().getPeerNodeList();
        if (!peerNodes.isEmpty()) {
            HeartBeatSignalModel heartBeatSignalModel = new HeartBeatSignalModel(ApplicationConstants.IP, ApplicationConstants.PORT, ApplicationConstants.USER_NAME);
            for (Node peer : peerNodes) {
                String heartBeatMessage = messageBuilder.buildHeartBeatSignalMessage(heartBeatSignalModel);
                try {
                    System.out.println("Sending HBEAT by" + ApplicationConstants.IP + " " + String.valueOf(ApplicationConstants.PORT) + " " + ApplicationConstants.USER_NAME);
                    udpConnector.send(heartBeatMessage, InetAddress.getByName(peer.getNodeIp()), peer.getPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void gracefulLeaveRequest() throws RemoteException {
        NeighbourTableImpl neighbourTable = NeighbourTableImpl.getInstance();
        Set<Node> neighbourNodeList = neighbourTable.getNeighbourNodeList();

        GracefulLeaveRequestModel gracefulLeaveRequestModel = new GracefulLeaveRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, "username");
        String message = gracefulLeaveRequestModel.toString();
        for (Node node : neighbourNodeList) {
            try {
                udpConnector.send(message, null, gracefulLeaveRequestModel.getPort());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("Unregister the node from the BS");
                            udpConnector.send(message, null, 55555);// Unregister from the BS
                            System.exit(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                15000
        );

    }

    @Override
    public void searchFile(String file) throws RemoteException, NotBoundException {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(ApplicationConstants.IP, ApplicationConstants.PORT));
        SearchRequestModel searchRequestModel = new SearchRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT, file, ApplicationConstants.HOPS, nodes);
        searchRequestModel.handle();

        //forward to two picked nodes
    }

    @Override
    public void sendSearchRequest(SearchRequestModel model, ConcurrentLinkedQueue<Node> statTablePeers) throws IOException, NotBoundException {
        String searchRequestMessage = messageBuilder.buildSearchRequestMessage(model);

        System.out.println("Found stat table entries");
        System.out.println(statTablePeers);

        Iterator<Node> nodeIterator = statTablePeers.iterator();
        while (nodeIterator.hasNext()){
            Node node = nodeIterator.next();
            if(!model.getLastHops().contains(node)){
//                udpConnector.send(searchRequestMessage,null, node.getPort());
                ServerConnector.getServerConnector(node.getNodeIp(),node.getPort())
                        .callRemoteSearchRequestHadle(model.getIp(),model.getPort(),model.getFileName(),model.getHops(),model.getLastHops());

            }
            System.out.println("send to stat table entries "+ node.getPort());
        }

        final Set<Node> peerNodeList = PeerTableImpl.getInstance().getPeerNodeList();

        final ArrayList<Node> peerNodeListToSend = new ArrayList<>();

        peerNodeList.forEach((node) -> {
            if (!model.getLastHops().contains(node)  && !statTablePeers.contains(node)) {
                peerNodeListToSend.add(node);
            }
        });

        System.out.println("peer nodes to send list "+peerNodeListToSend );
        Random random = new Random();
        int size = peerNodeListToSend.size();
        if (size > 0) {
            final int item1 = random.nextInt(size);
            ServerConnector serverConnector = ServerConnector.getServerConnector(peerNodeListToSend.get(item1).getNodeIp(), peerNodeListToSend.get(item1).getPort());

            if(serverConnector!=null){
                serverConnector.callRemoteSearchRequestHadle(model.getIp(),model.getPort(),model.getFileName(),model.getHops(),model.getLastHops());
                System.out.println("Sending to peer node "+peerNodeListToSend.get(item1).getPort());
                peerNodeListToSend.remove(item1);
            }else{
                System.out.println("server connector is null");
            }

        }
        size = peerNodeListToSend.size();
        if (size > 0) {
            final int item2 = random.nextInt(size);
            ServerConnector.getServerConnector(peerNodeListToSend.get(item2).getNodeIp(),peerNodeListToSend.get(item2).getPort())
                    .callRemoteSearchRequestHadle(model.getIp(),model.getPort(),model.getFileName(),model.getHops(),model.getLastHops());
            System.out.println("Sending to peer node "+peerNodeListToSend.get(item2).getPort());
        }
    }

    @Override
    public void sendLocalSearchToSource(SearchResponseModel searchResponseModel, List<String> list) throws IOException, NotBoundException {
        final ServerConnector serverConnector = ServerConnector.getServerConnector(searchResponseModel.getIp(), searchResponseModel.getPort());
        serverConnector.callRemoteSearchResponseHadle(
                ApplicationConstants.IP,ApplicationConstants.PORT,
                searchResponseModel.getHops(),searchResponseModel.getNoOfFiles(),new HashSet<>(list));
    }


    public void notifyNeighbours(String ip, int port) throws IOException {
        NotifyNeighbourRequestModel notifyNeighbourRequestModel = new NotifyNeighbourRequestModel(ApplicationConstants.IP, ApplicationConstants.PORT);
        String message = messageBuilder.buildNeighbourJoinMessage(notifyNeighbourRequestModel);
        udpConnector.send(message, null, port);

    }


}
