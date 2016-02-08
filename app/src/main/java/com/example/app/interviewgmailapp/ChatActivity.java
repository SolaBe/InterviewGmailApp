package com.example.app.interviewgmailapp;

import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;

import java.util.Arrays;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements ChatInterface{


    private String mAccountName;
    private FragmentManager mFragmentManager;
    private ChatsListFragment mChatListFragment;
    private ChatDetailFragment mChatDetailFragment;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mFragmentManager = getSupportFragmentManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            mAccountName = getIntent().getStringExtra("accountName");
            showChatsList();
        }
        else
            mAccountName = savedInstanceState.getString("accountName");

    }


    private void showChatsList(){
        mChatListFragment = new ChatsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("accountName",mAccountName);
        mChatListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction().replace(R.id.frame, mChatListFragment).commit();
    }


    private void showChatDetail(){
        mChatDetailFragment = new ChatDetailFragment();
        mFragmentManager.beginTransaction().replace(R.id.frame,mChatDetailFragment)
                .addToBackStack("ChatDetailLst").commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState = new Bundle();
        outState.putString("accountName",mAccountName);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onSelectThread(Thread thread) {
        showChatDetail();
        this.thread = thread;
    }

    @Override
    public Thread getSelectedThread() {
        return this.thread;
    }

}
