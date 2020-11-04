package com.cmpt275.house.classDef;

import com.cmpt275.house.interfaceDef.house;

class House implements house{

    public houseInfo[] houses;
    public userInfo temp;

    @Override
    public boolean createHouse(houseInfo hInfo) {
        return false;
    }

    @Override
    public boolean joinHouse(int house_id, userInfo uInfo) {
        return false;
    }

    @Override
    public houseInfo[] viewYourHouses(userInfo uInfo) {
        return new houseInfo[0];
    }

    @Override
    public houseInfo viewHouse(int house_id) {
        return null;
    }

    @Override
    public houseInfo approveMember(int house_id, int user_id) {
        return null;
    }

    @Override
    public houseInfo addMember(String userEmail) {
        return null;
    }

    @Override
    public userInfo viewMember(int user_id) {
        return null;
    }

    @Override
    public boolean deleteMember(int user_id) {
        return false;
    }

    @Override
    public houseInfo makeMemberAdmin(userInfo uInfo) {
        return null;
    }

    @Override
    public votingInfo viewVoting(int voting_id) {
        return null;
    }

    @Override
    public votingInfo submitVote(int voting_id, int voteType, userInfo uInfo) {
        return null;
    }

    @Override
    public houseInfo viewSettings(int house_id) {
        return null;
    }

    @Override
    public houseInfo editSettings(houseInfo hInfo) {
        return null;
    }

    @Override
    public taskInfo viewTask(int task_id) {
        return null;
    }

    @Override
    public boolean createTask(taskInfo tInfo) {
        return false;
    }

    @Override
    public boolean assignTask(taskInfo tInfo) {
        return false;
    }

    @Override
    public taskInfo displayTask(int task_id) {
        return null;
    }

    @Override
    public boolean approveTask(int task_id) {
        return false;
    }

    @Override
    public taskInfo[] sortTasks(int sortType) {
        return new taskInfo[0];
    }
}
