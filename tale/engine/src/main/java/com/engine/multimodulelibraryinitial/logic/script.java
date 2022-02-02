package com.engine.multimodulelibraryinitial.logic;

import org.json.simple.JSONObject;
import java.util.HashMap;
import java.util.ArrayList;
import com.engine.multimodulelibraryinitial.utils.*;



public class script {

    private HashMap<String, command> lines;
    private HashMap<String, Object> state;

    public ArrayList<message> respond(message m){

        String option = m.getOption();
        ArrayList<Object> parameters = m.getParam();

        ArrayList<message> res = lines.get(option).run(parameters);

        return res;
    }

    public void set(String name, Object value){
        state.put(name, value);
    }

    public Object get(String name) {
        if (state.containsKey(name)) {
            return state.get(name);
        }

        return null;
    }
}
