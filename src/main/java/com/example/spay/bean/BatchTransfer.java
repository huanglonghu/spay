package com.example.spay.bean;

import java.util.List;

public class BatchTransfer {


    /**
     * userID : 0
     * toUserID : 0
     * mcProductIDList : [0]
     */

    private int userID;
    private int toUserID;
    private List<Integer> mcProductIDList;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public void setToUserID(int toUserID) {
        this.toUserID = toUserID;
    }

    public List<Integer> getMcProductIDList() {
        return mcProductIDList;
    }

    public void setMcProductIDList(List<Integer> mcProductIDList) {
        this.mcProductIDList = mcProductIDList;
    }
}
