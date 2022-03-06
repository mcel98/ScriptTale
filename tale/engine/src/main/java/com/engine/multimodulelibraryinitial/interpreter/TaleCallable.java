package com.engine.multimodulelibraryinitial.interpreter;
import java.util.List;

interface TaleCallable {
    int arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
