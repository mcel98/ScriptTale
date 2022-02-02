package com.engine.multimodulelibraryinitial.interpreter;

interface ErrorReporter {   

    void report(String message);
    boolean hasError();

}
