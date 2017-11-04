package com.d2s2.overlay.request;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface Request {

    void registerRequestToBS();

    void unregisterToBS();

    void join();

    void leave();

    void searchFile();


}
