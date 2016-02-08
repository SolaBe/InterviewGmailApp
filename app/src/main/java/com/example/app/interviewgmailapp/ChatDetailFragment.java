package com.example.app.interviewgmailapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;
import com.google.api.services.gmail.model.Thread;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sola2Be on 08.02.2016.
 */
public class ChatDetailFragment extends ListFragment {

    private ChatInterface chatInterface;
    private List<MessageModel> messages;
    private ChatAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = chatInterface.getSelectedThread();
        List<Message> msgs = thread.getMessages();
        messages = new ArrayList<>();
        setList(msgs);
        adapter = new ChatAdapter(getActivity(),R.layout.chat_detail_list,messages);
        setListAdapter(adapter);
    }


    private void setList(List<Message> msgs){
        for (int i = 0; i < msgs.size(); i++) {
            MessageModel model = new MessageModel();
            for (MessagePartHeader mPartHeader : msgs.get(i).getPayload().getHeaders())
                if(mPartHeader.getName().equals("From"))
                    model.setUserAccount(mPartHeader.getValue());
            if(msgs.get(i).getLabelIds().contains("INBOX"))
                model.setType(0);
            if(msgs.get(i).getLabelIds().contains("SENT"))
                model.setType(1);
            if(msgs.get(i).getPayload().getParts() != null) {
                List<MessagePart> msgParts = msgs.get(i).getPayload().getParts();
                StringBuilder text = new StringBuilder();
                for (MessagePart part :  msgParts) {
                    byte[] array = part.getBody().getData().getBytes();
                    String tmpText = new String(Base64.decode(array, Base64.URL_SAFE));
                    text.append(Html.fromHtml(tmpText));
                }
                int index = text.toString().indexOf(">");
                String msg = text.toString();
                if(index != -1)
                    msg = text.toString().substring(0, index);
                model.setMessage(msg);
                Log.d("TAG", "text - " + text.toString());
                Log.d("TAG", "model - "+ model.getUserAccount());
                model.save();
                messages.add(model);
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_detail_list, null);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        chatInterface = (ChatInterface) context;
    }
}
