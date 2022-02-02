package com.engine.multimodulelibraryinitial.utils;
import com.engine.multimodulelibraryinitial.logic.actor;

class ChannelLimitException  extends RuntimeException
{  
    public   ChannelLimitException(String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
}


public class channel{
    private int last;
    actor[] connections;

    public channel(int max_conn){
        last = 0;
        connections = new actor[max_conn];
    }
    
    public void append(actor conn) throws ChannelLimitException{
        if(last < this.connections.length){
            this.connections[last] = conn;
            this.last++;
        }else{
            throw new ChannelLimitException("hit channel limit");
        }

    }

    public void send(message m){
        for(int i = 0; i<this.last;i++){
            this.connections[i].addMessage(m);
        }
    }


}
