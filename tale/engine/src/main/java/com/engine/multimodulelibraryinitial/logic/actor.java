package com.engine.multimodulelibraryinitial.logic;

import com.engine.multimodulelibraryinitial.utils.message_handler;
import com.engine.multimodulelibraryinitial.utils.message;
import java.util.ArrayList;

public class actor {

    private script Script;
    private int id;
    private message_handler MessageHandler;
    
    public actor (script _script, int _id, message_handler _MessageHandler){
        this.Script = _script;
        this.id = _id;
        this.MessageHandler = _MessageHandler;
    }

    public int getID(){
        return this.id;
    }

    private void send(message m){

        this.MessageHandler.notify(m);
            
    }

    public void addMessage(message m){
        this.MessageHandler.addMessage(m);
    }

    public void onEvent(){
        message event = this.MessageHandler.retrieve();
        ArrayList<message> response = this.Script.respond(event);

        for(int i = 0; i < response.size(); i++){

            send(response.get(i));

        }
        
    }
}
