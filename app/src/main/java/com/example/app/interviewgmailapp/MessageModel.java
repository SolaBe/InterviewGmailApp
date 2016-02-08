package com.example.app.interviewgmailapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Sola2Be on 08.02.2016.
 */
@Table(name = "Message")
public class MessageModel extends Model {

    @Column(name = "userAccount")
    private String userAccount;

    @Column(name = "message")
    private String message;

    @Column(name = "type")
    private int type;

    @Column(name = "thread")
    private ThreadModel threadModel;

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ThreadModel getThreadModel() {
        return threadModel;
    }

    public void setThreadModel(ThreadModel threadModel) {
        this.threadModel = threadModel;
    }
}
