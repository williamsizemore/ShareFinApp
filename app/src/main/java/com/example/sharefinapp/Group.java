package com.example.sharefinapp;

import java.util.Date;
import java.util.List;

public class Group {
    private String groupName;
    private List<String> groupUserIDs;
    private String groupID;
    private Date createDate;

    public Group(String groupName, List<String> groupUserIDs,String groupID, Date createDate) {
        this.groupName = groupName;
        this.groupUserIDs = groupUserIDs;
        this.groupID = groupID;
        this.createDate = createDate;
    }
    // for firestore compatibility
    public Group() {}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getGroupUserIDs() {
        return groupUserIDs;
    }

    public void setGroupUserIDs(List<String> groupUserIDs) {
        this.groupUserIDs = groupUserIDs;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
