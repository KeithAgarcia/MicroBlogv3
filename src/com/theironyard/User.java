package com.theironyard;


import java.util.ArrayList;

public class User {
    String name;
    ArrayList<Message> messages = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }
}

