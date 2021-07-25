package com.byted.chapter5;

public class NewUser {

    private String username;

    private String password;

    private String repassword;

    public NewUser(String username, String password, String repassword){
        this.username = username;
        this.password = password;
        this.repassword = repassword;
    }

    @Override
    public String toString() {

        return "{" +

                "username='" + username + '\'' +

                ", usertoken='" + password + '\'' +

                ", password='" + repassword + '\'' +

                '}';

    }

}
