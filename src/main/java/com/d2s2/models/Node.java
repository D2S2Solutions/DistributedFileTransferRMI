package com.d2s2.models;

import java.io.Serializable;

public class Node implements Serializable {
    private String nodeIp;
    private int port;
    private String id;


    public Node(String nodeIp, int port) {
        this.nodeIp = nodeIp;
        this.port = port;
        this.id = nodeIp + " " + port; // Todo Find a proper Id schema
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return String.valueOf(this.nodeIp).concat(":").concat(String.valueOf(this.port)).hashCode();
    }


    @Override
    public boolean equals(Object object) {
        Node node = (Node) object;
        return (this.nodeIp.equals(node.getNodeIp()) && this.port == node.getPort()) ? true : false;
    }

    @Override
    public String toString() {
        return String.valueOf(this.nodeIp).concat(":").concat(String.valueOf(this.port));
    }
}
