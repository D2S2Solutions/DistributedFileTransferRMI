package com.d2s2.message.tokenize;

import com.d2s2.constants.ApplicationConstants;
import com.d2s2.message.MessageConstants;
import com.d2s2.models.*;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class MessageTokenizerImpl implements MessageTokenizer {
    @Override
    public AbstractRequestResponseModel tokenizeMessage(String message) throws RemoteException {
        message = message.substring(0, Integer.parseInt(message.substring(0, 4)));
        StringTokenizer stringTokenizer = new StringTokenizer(message, " ");
        int length = Integer.parseInt(stringTokenizer.nextToken());
        String response = stringTokenizer.nextToken();
        System.out.println(message);

        switch (response) {
            case MessageConstants.REGOK_MESSAGE:
                return this.getRegisterResponseMessageOb(stringTokenizer);

            case MessageConstants.UNROK_MESSAGE:
                return this.getUnregisterResponseMessageOb(stringTokenizer);

            case MessageConstants.SER_MESSAGE:
                return this.getSearchMessageOb(stringTokenizer);

            case MessageConstants.SEROK_MESSAGE:
                return this.getSearchResponseOb(stringTokenizer);

            case MessageConstants.HEARTBEAT_MESSAGE:
                return this.getHeartBeatSignalOb(stringTokenizer);

            case MessageConstants.NEIGHBOUR_MESSAGE:
                return this.getNeighbourResponseMessageOb(stringTokenizer);

            case MessageConstants.LEAVE_MESSAGE:
                System.out.println("LEAVE MESSAGE RECEIVED");
                return null;
            //return this.getNeighbourResponseMessageOb(stringTokenizer);


        }
        return null;


    }

    private AbstractRequestResponseModel getHeartBeatSignalOb(StringTokenizer stringTokenizer) throws RemoteException {
        String ip = stringTokenizer.nextToken();
        int port = Integer.parseInt(stringTokenizer.nextToken());
        String username = stringTokenizer.nextToken();
        return new HeartBeatSignalModel(ip, port, username);
    }

    private AbstractRequestResponseModel getSearchResponseOb(StringTokenizer stringTokenizer) throws RemoteException {

        int noOfFiles = Integer.valueOf(stringTokenizer.nextToken());
        String ip = stringTokenizer.nextToken();
        int port = Integer.parseInt(stringTokenizer.nextToken());
        int hops = Integer.parseInt(stringTokenizer.nextToken());
        HashSet<String> fileSet = new HashSet<>();
        for (int i = 0; i < noOfFiles; i++) {
            fileSet.add(stringTokenizer.nextToken());
        }
        return new SearchResponseModel(ip, port, hops, noOfFiles, fileSet);

    }

    private AbstractRequestResponseModel getSearchMessageOb(StringTokenizer stringTokenizer) throws RemoteException {
        String ip = stringTokenizer.nextToken();
        int port = Integer.parseInt(stringTokenizer.nextToken());
        String fileName = stringTokenizer.nextToken();
        int hops = Integer.parseInt(stringTokenizer.nextToken());
        ArrayList<Node> nodes = new ArrayList<>();

        for (int x = 0; x < (ApplicationConstants.HOPS - hops); x++) {
            String hopIp = stringTokenizer.nextToken();
            int hopPort = Integer.parseInt(stringTokenizer.nextToken());
            nodes.add(new Node(hopIp, hopPort));
        }

        SearchRequestModel searchRequestModel = new SearchRequestModel(ip, port, fileName, hops, nodes);
        return searchRequestModel;
    }

    private AbstractRequestResponseModel getUnregisterResponseMessageOb(StringTokenizer stringTokenizer) throws RemoteException {
        String token = stringTokenizer.nextToken();
        if (token != null) {
            return new UnregistrationResponseModel(Integer.parseInt(token));
        } else {
            return null;
        }

    }

    private AbstractRequestResponseModel getRegisterResponseMessageOb(StringTokenizer stringTokenizer) throws RemoteException {
        int nodeCount = Integer.parseInt(stringTokenizer.nextToken());
        switch (nodeCount) {
            case 0:
                System.out.println(" Request is successful but, no other nodes in the system");
                return null;
            case 9996:
                System.out.println(" failed, can’t register. BS is full.");
                return null;
            case 9997:
                System.out.println(" failed, registered to another user, try a different IP and port");
                return null;
            case 9998:
                System.out.println(" failed, already registered to you, unregister first");
                return null;
            case 9999:
                System.out.println(" failed, there is some error in the command");
                return null;
            default:
                HashSet<Node> nodeset = new HashSet<>();
                for (int i = 0; i < nodeCount; i++) {
                    String ip = stringTokenizer.nextToken();
                    String port = stringTokenizer.nextToken();
                    nodeset.add(new Node(ip, Integer.parseInt(port)));
                }
                return new RegistrationResponseModel(nodeCount, nodeset);
        }

    }

    private AbstractRequestResponseModel getNeighbourResponseMessageOb(StringTokenizer stringTokenizer) throws RemoteException {
        String ip = stringTokenizer.nextToken();
        String portST = stringTokenizer.nextToken();
        int port = Integer.parseInt(portST.substring(0, portST.length() - 1));
        return new NotifyNeighbourRequestModel(ip, port);
    }
}
