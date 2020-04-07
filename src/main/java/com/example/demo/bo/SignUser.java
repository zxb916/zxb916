package com.example.demo.bo;

import com.example.demo.model.SignUp;
import com.example.demo.model.User;

public class SignUser {

    private User user;


    private SignUp signUp;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SignUp getSignUp() {
        return signUp;
    }

    public void setSignUp(SignUp signUp) {
        this.signUp = signUp;
    }
}
