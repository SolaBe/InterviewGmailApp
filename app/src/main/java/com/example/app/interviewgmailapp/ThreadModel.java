package com.example.app.interviewgmailapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Sola2Be on 08.02.2016.
 */
@Table(name = "Thread")
public class ThreadModel extends Model {

    @Column(name = "threadId")
    private String threadId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "users")
    private String users;

    @Column(name = "unread")
    private boolean unread;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }
}
