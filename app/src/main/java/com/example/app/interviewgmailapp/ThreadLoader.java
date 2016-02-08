package com.example.app.interviewgmailapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ThreadLoader extends AsyncTaskLoader<List<Thread>> {

    private List<String> mThreadIds;
    private com.google.api.services.gmail.Gmail mService = null;
    private String mAccount;

    public ThreadLoader(Context context, GoogleAccountCredential credential, List<String> threadIds) {
        super(context);
        mThreadIds = threadIds;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mAccount = credential.getSelectedAccountName();
        mService = new com.google.api.services.gmail.Gmail.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("InterviewGmailApp")
                .build();
    }

    @Override
    public List<Thread> loadInBackground() {
        List<Thread> threads = new ArrayList<>();
        try {
            for (String id : mThreadIds) {
                Thread thread = mService.users().threads().get(mAccount, id).execute();
                threads.add(thread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return threads;
    }
}
