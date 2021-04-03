package com.example.sharefinapp;

import java.util.List;

public class Group {
    private String groupName;
    private List<String> groupUsers;

    public Group(String groupName, List<String> groupUsers) {
        this.groupName = groupName;
        this.groupUsers = groupUsers;
    }
    // for firestore compatibility
    public Group() {}

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<String> groupUsers) {
        this.groupUsers = groupUsers;
    }
}
