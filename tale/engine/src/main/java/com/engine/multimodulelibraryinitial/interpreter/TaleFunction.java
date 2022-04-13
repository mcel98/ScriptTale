package com.engine.multimodulelibraryinitial.interpreter;

public class TaleFunction implements TaleCallable {
    private final Stmt.Function declaration;
    TaleFunction(Stmt.Function declaration) {
        this.declaration = declaration;
    }


    public Object call(Interpreter interpreter, List<Object> arguments) {
      Environment environment = new Environment(interpreter.environment);
      for (int i = 0; i < declaration.params.size(); i++) {
        environment.define(declaration.params.get(i).lexeme,
            arguments.get(i));
      }
  
      interpreter.executeBlock(declaration.body, environment);
      return null;
    }

    public int arity() {
      return declaration.params.size();
    }

    public String toString() {
      return "<fn " + declaration.name.lexeme + ">";
    }
}
