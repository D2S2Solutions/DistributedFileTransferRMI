package com.d2s2.files;

import java.util.List;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */
public interface FileHandler {

    void initializeFileStorage(String documentName);

    List<String> searchLocalFileList(String query);

}
