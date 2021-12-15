package com.zbw.crm.exception;

public class LoginException extends Exception{
    public LoginException(){};
    public  LoginException(String msg){
        super(msg);
    }
}
