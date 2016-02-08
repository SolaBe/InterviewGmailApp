package com.example.app.interviewgmailapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ChatListAdapter extends ArrayAdapter<ThreadModel> {

    private List<ThreadModel> mThreads;
    private ViewHolder vHolder;
    private LayoutInflater inflater;
    public ChatListAdapter(Context context, int resource, List<ThreadModel> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        mThreads = objects;
    }

    class ViewHolder {
        TextView textViewSubject;
        TextView textViewUsers;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mThreads.size();
    }

    @Override
    public ThreadModel getItem(int position) {
        return mThreads.get(position);
    }

    @Override
    public int getPosition(ThreadModel item) {
        return mThreads.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ThreadModel thread  = getItem(position);
        if (v == null){
            v = inflater.inflate(R.layout.thread_list_item,null);
            vHolder = new ViewHolder();
            vHolder.textViewSubject = (TextView) v.findViewById(R.id.textViewTopic);
            vHolder.textViewUsers = (TextView) v.findViewById(R.id.textViewUsers);
            v.setTag(vHolder);
        }
        else
            vHolder = (ViewHolder) v.getTag();
        vHolder.textViewSubject.setText(thread.getSubject());
        if (thread.isUnread()) {
            vHolder.textViewSubject.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            vHolder.textViewUsers.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        String[] users = thread.getUsers().split(";");
        for (int i = 0; i < users.length; i++)
            vHolder.textViewUsers.setText(users[i]+"\n");
        return v;
    }
}
