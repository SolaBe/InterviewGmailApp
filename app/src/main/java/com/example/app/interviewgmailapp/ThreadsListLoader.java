package com.example.app.interviewgmailapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ThreadsListLoader extends AsyncTaskLoader<List<Thread>> {

    private GoogleAccountCredential mCredential;
    private com.google.api.services.gmail.Gmail mService = null;
    private String mAccount;

    public ThreadsListLoader(Context context, GoogleAccountCredential credential) {
        super(context);
        mCredential = credential;
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
            ListThreadsResponse response = mService.users().threads().list(mAccount)
                    .setIncludeSpamTrash(false).setMaxResults(10l).execute();
            if (response.getThreads() != null){
                threads.addAll(response.getThreads());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return threads;
    }
}
