package com.example.app.interviewgmailapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.activeandroid.ActiveAndroid;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ChatsListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Thread>> {


    static final int THREADS_LIST_LOADER = 0;
    static final int THREAD_LOADER = 1;
    GoogleAccountCredential mCredential;
    private static final String[] SCOPES = { GmailScopes.GMAIL_READONLY };
    private List<ThreadModel> threadModels;
    private ChatListAdapter adapter;
    private ChatInterface chatInterface;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String accountName = getArguments().getString("accountName");
        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accountName);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListThreadsLoader();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list,null);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        return view;
    }

    public void initListThreadsLoader() {
        getActivity().getSupportLoaderManager().initLoader(THREADS_LIST_LOADER, null, this);
    }

    public void initThreadLoader(ArrayList<String> threadIds) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("threadId", threadIds);
        getActivity().getSupportLoaderManager().restartLoader(THREAD_LOADER, bundle, this);
    }

    public void saveThreads(List<Thread> data){
        for (Thread thread : data) {
            ActiveAndroid.beginTransaction();
            ThreadModel model = new ThreadModel();
            model.setThreadId(thread.getId());
            for (int i = 0; i < thread.getMessages().size(); i++) {
                List<MessagePartHeader> headers = thread.getMessages().get(i).getPayload().getHeaders();
                for (int j = 0; j < headers.size(); j++) {
                    if(headers.get(j).getName().equals("Subject"))
                        model.setSubject(headers.get(j).getValue());
                    if(headers.get(j).getName().equals("From")) {
                        String users = model.getUsers();
                        if(users == null)
                            model.setUsers(headers.get(j).getValue()+";");
                        else
                            model.setUsers(users+headers.get(j).getValue()+";");
                    }
                }
                List<String> labels = thread.getMessages().get(i).getLabelIds();
                for (int k = 0; k < labels.size(); k++) {
                    if(labels.get(k).equals("UNREAD"))
                        model.setUnread(true);
                }
            }
            model.save();

            if(adapter == null) {
                mProgressBar.setVisibility(View.GONE);
                threadModels = new ArrayList<>();
                threadModels.add(model);
                adapter = new ChatListAdapter(getActivity(), R.layout.thread_list_item, threadModels);
                setListAdapter(adapter);
            }
            else {
                threadModels.add(model);
                adapter.notifyDataSetChanged();
            }
        }
        ActiveAndroid.setTransactionSuccessful();
        ActiveAndroid.endTransaction();
    }

    @Override
    public Loader<List<Thread>> onCreateLoader(int id, Bundle args) {
        Loader loader = null;
        switch (id){
            case THREADS_LIST_LOADER :
                mProgressBar.setVisibility(View.VISIBLE);
                loader = new ThreadsListLoader(getActivity(),mCredential);
                break;
            case THREAD_LOADER :
                loader = new ThreadLoader(getActivity(),mCredential,args.getStringArrayList("threadId"));
                break;
        }
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Thread>> loader, final List<Thread> data) {
        if (loader.getId() == THREADS_LIST_LOADER) {
            ArrayList<String> list = new ArrayList<>();
            for( Thread th : data)
                list.add(th.getId());
                initThreadLoader(list);
        }
        if(loader.getId() == THREAD_LOADER) {
            if(threadModels != null)
                threadModels.clear();
            saveThreads(data);
            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    chatInterface.onSelectThread(data.get(position));
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Thread>> loader) {

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        chatInterface = (ChatInterface) context;
    }
}
