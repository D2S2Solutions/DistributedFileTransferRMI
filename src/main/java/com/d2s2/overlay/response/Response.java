package com.d2s2.overlay.response;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface Response {

    void handleRegisterResponse();

    void handleUnregisterResponse();

    void handleJoinResponse();

    void handleLeaveResponse();

    void receiveFileList();

}
