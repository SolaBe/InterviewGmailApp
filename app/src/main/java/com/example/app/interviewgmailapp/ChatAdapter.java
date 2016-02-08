package com.example.app.interviewgmailapp;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ChatAdapter extends ArrayAdapter<MessageModel> {

    private List<MessageModel> messages;
    private LayoutInflater inflater;
    private ViewHolder vHolder;
    public ChatAdapter(Context context, int resource, List<MessageModel> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        messages = objects;
    }

    class ViewHolder {
        TextView textViewUser;
        TextView textViewMessage;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public int getPosition(MessageModel item) {
        return messages.indexOf(item);
    }

    @Override
    public MessageModel getItem(int position) {
        return messages.get(position);
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
           if (getItemViewType(position) == 0)
               v = inflater.inflate(R.layout.list_item_left,null);
           if (getItemViewType(position) == 1)
               v = inflater.inflate(R.layout.list_item_right, null);
            vHolder = new ViewHolder();
            vHolder.textViewUser = (TextView) v.findViewById(R.id.textViewUser);
            vHolder.textViewMessage = (TextView) v.findViewById(R.id.textViewMessage);
            v.setTag(vHolder);
        }
        else
            vHolder = (ViewHolder) v.getTag();
        vHolder.textViewUser.setText(getItem(position).getUserAccount());
        vHolder.textViewMessage.setText(getItem(position).getMessage());
        v.setTag(R.id.textViewMessage, vHolder.textViewMessage);
        v.setTag(R.id.textViewUser,position);
        v.setOnClickListener(listener);
        return v;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("TAG","onClick");
            TextView tv = (TextView) v.getTag(R.id.textViewMessage);
            int maxLength = getItem((Integer) v.getTag(R.id.textViewUser)).getMessage().length();
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            tv.setFilters(fArray);
            tv.setText(getItem((Integer) v.getTag(R.id.textViewUser)).getMessage());
        }
    };
}
