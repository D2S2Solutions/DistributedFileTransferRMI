package com.d2s2.message.build;

import com.d2s2.constants.ApplicationConstants;
import com.d2s2.message.MessageConstants;
import com.d2s2.models.*;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public class MessageBuilderImpl implements MessageBuilder {

    private static final String IP_ADDRESS = "";
    private static final String PORT = "";


    @Override
    public String buildRegisterRequestMessage(RegistrationRequestModel model) {
        int length = MessageConstants.REG_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length() + model.getUserName().length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.REG_MESSAGE + " " + model.getIp() + " " + model.getPort() + " " + model.getUserName();
    }

    @Override
    public String buildUnregisterRequestMessage() {
        return null;
    }

    @Override
    public String buildJoinMessage() {
        return null;
    }

    @Override
    public String buildLeaveMessage() {
        return null;
    }

    @Override
    public String buildSearchRequestMessage(SearchRequestModel model) {
        int length = MessageConstants.SER_MESSAGE.length() + model.getIp().length()
                + String.valueOf(model.getPort()).length() + model.getFileName().length()
                + String.valueOf(model.getHops()).length() + 5 + 4;
        for (Node node : model.getLastHops()) {
            length += node.getNodeIp().length();
            length += String.valueOf(node.getPort()).length();
            length += 2;
        }
        String requestFinalLength = String.format("%04d", length);
        requestFinalLength = requestFinalLength + " " + MessageConstants.SER_MESSAGE + " "
                + model.getIp() + " " + model.getPort() + " " + model.getFileName() + " " + model.getHops();
        for (Node node : model.getLastHops()) {
            requestFinalLength += (" " + node.getNodeIp() + " " + node.getPort());
        }
        return requestFinalLength;


    }

    @Override
    public String buildSearchResponseToSourceMessage(SearchResponseModel model) {
        StringBuilder stringBuilder = new StringBuilder();
        if (model.getFileList() != null) {
            for (String s : model.getFileList()) {
                stringBuilder.append(s);
                stringBuilder.append(" ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1); // to remove the trailing space.
        }
        int length = MessageConstants.SEROK_MESSAGE.length() + String.valueOf(model.getNoOfFiles()).length()
                + String.valueOf(ApplicationConstants.IP).length() + String.valueOf(ApplicationConstants.PORT).length()
                + String.valueOf(model.getHops()).length() + stringBuilder.length() + 6 + 4 + model.getNoOfFiles() - 1;

        final String requestFinalLength = String.format("%04d", length);
        String s = requestFinalLength + " " + MessageConstants.SEROK_MESSAGE + " " + model.getNoOfFiles() + " "
                + ApplicationConstants.IP + " " + ApplicationConstants.PORT + " " + model.getHops();
        if (model.getFileList() != null) {
            s += " " + stringBuilder;
        }
        return s;
    }

    @Override
    public String buildHeartBeatSignalMessage(HeartBeatSignalModel model) {
        //todo build heart beat msg
        // length HBEAT ip port username
        int length = MessageConstants.HEARTBEAT_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length() + model.getUserName().length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.HEARTBEAT_MESSAGE + " " + model.getIp() + " " + model.getPort() + " " + model.getUserName();
    }

    @Override
    public String buildNeighbourJoinMessage(NotifyNeighbourRequestModel model) {
        int length = MessageConstants.NEIGHBOUR_MESSAGE.length() + model.getIp().length() + String.valueOf(model.getPort()).length() + 4 + 4;
        final String requestFinalLength = String.format("%04d", length);
        return requestFinalLength + " " + MessageConstants.NEIGHBOUR_MESSAGE + " " + model.getIp() + " " + model.getPort();
    }


}
