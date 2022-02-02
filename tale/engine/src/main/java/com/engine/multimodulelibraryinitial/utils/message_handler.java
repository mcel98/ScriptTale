package com.engine.multimodulelibraryinitial.utils;
import java.util.*;
import com.engine.multimodulelibraryinitial.utils.channel;
import com.engine.multimodulelibraryinitial.logic.actor;

class TypeCreationException  extends Exception  
{  
    public  TypeCreationException(String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
}

class InvalidTypeException  extends RuntimeException
{  
    public  InvalidTypeException(String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
} 

class ChannelTypeException  extends RuntimeException
{  
    public  ChannelTypeException(String str)  
    {  
        // calling the constructor of parent Exception  
        super(str);  
    }  
}  

/* handler de los mensajes recibidos y enviados del actor
*
*  Sistema de mensajeria publish/subscribe por topic del mensaje
*  
*/
public class message_handler {

    private Queue<message> message_buffer;
    private int buffer_size;
    private Map<String, channel> channels;

    public message_handler(int max_buffer, int subs_limit){
        this.message_buffer = new ArrayDeque<message>();
        this.buffer_size = max_buffer;
        this.channels = new HashMap<String, channel>();
    }

    public void add_type(String type) throws ChannelTypeException {
        
        if(!this.channels.containsKey(type)){

            this.channels.put(type, new channel(100));

        }else{

            throw new ChannelTypeException("topic already exists");
        }
      
    }

    public void subscribe(String type, actor Subscriber) throws InvalidTypeException {

        if(this.channels.containsKey(type) ){

            try{
                this.channels.get(type).append(Subscriber);
            }catch ( ChannelLimitException e ) {
                //reemplazar por loggin
                e.printStackTrace();
            }
        

        }else{
            throw new InvalidTypeException("topic does not exist");
        }

    }

    public void delete_channel(String type){
        this.channels.remove(type);
    }
    

    public void addMessage(message m){
        this.message_buffer.offer(m);
    }
    
    public message retrieve(){
        message event = this.message_buffer.poll();
        return event;
    }

    public void notify(message m) throws ChannelTypeException {
        if(this.channels.containsKey(m.getType())){
            
            this.channels.get(m.getType()).send(m);
        }else{

            throw new ChannelTypeException("topic does not exist");

        }
        

    }

    
}
