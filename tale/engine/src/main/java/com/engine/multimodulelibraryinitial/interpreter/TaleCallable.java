package com.engine.multimodulelibraryinitial.interpreter;
import java.util.List;

interface TaleCallable {
    Object call(Interpreter interpreter, List<Object> arguments);
}
