package com.d2s2.overlay;

import com.d2s2.models.Node;

/**
 * Created by Heshan Sandamal on 10/6/2017.
 */

public interface Table {

    void insert(Node node);

    void remove(Node node);

    void search(String query);

}
