package com.engine.multimodulelibraryinitial.interpreter;

public class ConsoleErrorReporter implements ErrorReporter {
    
    private boolean hadError;

    ConsoleErrorReporter(){
        hadError = false;
    }

    public boolean hasError(){
        return hadError;
    }
    
    public void report(String message) {
        System.err.println(message);
        hadError = true;
    }

    public void reset(){
        hadError = false;
    }


}
