package com.pathofthefood.flyingburger;

/**
 * Created by co_mmsalinas on 13/11/2014.
 */
public class NotAuthException extends Exception {

    private boolean isAuth;

    public NotAuthException(String message, boolean isAuth){
        super(message);
        this.isAuth = isAuth;
    }

    private boolean getIsAuth(){
        return this.isAuth;
    };


}
