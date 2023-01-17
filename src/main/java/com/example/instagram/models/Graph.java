package com.example.instagram.models;

import com.example.instagram.SqlManager;

import java.sql.Array;
import java.util.ArrayList;

public class Graph {
    private ArrayList<String> usernames;
    private ArrayList<ArrayList<Double>> adjMatrix;
    private static Graph graph = null;
    public static Graph getInstance() {
        if (graph == null) {
            return new Graph();
        }
        return graph;
    }
    // singleton design pattern is used to avoid more than one graph object for the entire system

    private Graph() {
        usernames = SqlManager.getInstance().getAllUsernames();
        adjMatrix = new ArrayList<>(usernames.size()+1);
        setAdjMatrix();
    }
    private void setAdjMatrix() {
        for (int i=0; i<usernames.size()+1; i++) {
            adjMatrix.add(new ArrayList<>(usernames.size() + 1));
            for (int x = 0; x < usernames.size() + 1; x++) {
                adjMatrix.get(i).add(0.0);
            }
        }
        for (int i=0; i<usernames.size(); i++) {
            String username = usernames.get(i);
            ArrayList<String> followers = SqlManager.getInstance().getFollowings(username);
            for (String follower: followers) {
                int j = usernames.indexOf(follower);
                adjMatrix.get(i+1).set(j+1, 1.0);
                adjMatrix.get(i+1).set(0, adjMatrix.get(i+1).get(0)+1.0);
                adjMatrix.get(0).set(j+1, adjMatrix.get(0).get(j+1)+1.0);
            }
        }
    }

    public ArrayList<String> recommend(String username, int n) {
        int index = usernames.indexOf(username);
        setProbabilities(username);
        ArrayList<String> recommendedUsers = new ArrayList<>();
        for (int i=0; i<Math.min(n, usernames.size()-1); i++) {
            int maxIndex = 0;
            double max = 0.0;
            for (int j=1; j<usernames.size()+1; j++) {
                if (adjMatrix.get(index).get(j) > max) {
                    max = adjMatrix.get(index).get(j);
                    maxIndex = j;
                }
            }
            recommendedUsers.add(usernames.get(maxIndex));
        }
        return recommendedUsers;
    }
//    private void setProbabilities(String username) {
//        setAdjMatrix();
//        for (int i=0; i< usernames.size(); i++) {
//            for (int j=0; j< i; j++) {
//                int IFollowerNum = 0, JFollowerNum = 0, bothFollowingNum = 0;
//                for (int k=0; k< usernames.size(); k++) {
//                    double ki = adjMatrix[k][i], kj = adjMatrix[k][j];
//                    if (ki == 1) {
//                        IFollowerNum += 1;
//                    }
//                    if (kj == 1) {
//                        JFollowerNum += 1;
//                    }
//                    if (ki == 1 && kj == 1) {
//                        bothFollowingNum += 1;
//                    }
//                }
//                if (adjMatrix[i][j] != 1) {
//                    adjMatrix[i][j] = bothFollowingNum/IFollowerNum;
//                }
//                if (adjMatrix[j][i] != 1) {
//                    adjMatrix[j][i] = bothFollowingNum/JFollowerNum;
//                }
//
//            }
//        }
//    }
    private void setProbabilities(String username) {
        int index = usernames.indexOf(username)+1;
        for (int i=1; i<usernames.size()+1; i++) {
            if (i == index) {
                continue;
            }
            double bothFollowingNum = 0.0;
            for (int k=1; k< usernames.size()+1; k++) {
                if (adjMatrix.get(index).get(k) == 1 && adjMatrix.get(i).get(k) == 1) {
                    bothFollowingNum += 1;
                }
            }
            double numberOfFollowers = adjMatrix.get(0).get(index);
            if (numberOfFollowers != 0) {
                adjMatrix.get(index).set(i, bothFollowingNum/numberOfFollowers);
            }

        }
        print();
    }

    public void addUser(String username) {
        usernames.add(username);
        adjMatrix.add(new ArrayList<>(usernames.size()));
        for (int i = 0; i<usernames.size()+1; i++) {
            adjMatrix.get(i).add(0.0);
        }
    }
    public void deleteUser(String username) {
        int index = usernames.indexOf(username);
        adjMatrix.remove(index+1);
        usernames.remove(username);
        for (int i=0; i<usernames.size()+1; i++) {
            adjMatrix.get(i).remove(index);
        }
    }
    public void follows(String username1, String username2) {
        int index1 = usernames.indexOf(username1);
        int index2 = usernames.indexOf(username2);
        adjMatrix.get(index1).set(index2, 1.0);
    }
    public void unfollows(String username1, String username2) {
        int index1 = usernames.indexOf(username1);
        int index2 = usernames.indexOf(username2);
        adjMatrix.get(index1).set(index2, 0.0);
    }
    public void print() {
        System.out.println(usernames);
        for (int i=0; i<adjMatrix.size(); i++) {
            System.out.println(adjMatrix.get(i));
        }
    }
}
