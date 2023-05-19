package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username,email,password;
    private static final Boolean isAdmin = false;  //CHECK maybe remove

    public User(String email,String pw){
        this.email = email;
        this.password=pw;
    }
    public User(String name,String email,String pw){
        this.username=name;
        this.email=email;
        this.password=pw;
    }
    
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    
}
