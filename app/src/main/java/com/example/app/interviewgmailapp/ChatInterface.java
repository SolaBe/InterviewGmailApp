package com.example.app.interviewgmailapp;

import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.model.Thread;

/**
 * Created by Sola2Be on 08.02.2016.
 */
public interface ChatInterface {

    void onSelectThread(com.google.api.services.gmail.model.Thread thread);

    Thread getSelectedThread();
}
