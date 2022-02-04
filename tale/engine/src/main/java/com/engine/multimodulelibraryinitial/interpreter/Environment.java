package com.engine.multimodulelibraryinitial.interpreter;

import java.util.HashMap;
import java.util.Map;

class Environment {
  final Environment enclosing;
  private final Map<String, Object> values = new HashMap<>();
  private final Boolean global; 

  Environment() {
    this.enclosing = null;
    this.global = true;
  }
  
  Environment(Environment enclosing) {
    this.enclosing = enclosing;
    this.global = false;
  }

  void define(Token name, Object value) {
    if(!isGlobal()){ 
      values.put(name.lexeme, value);
    }else{
      throw new CompilingError(name, "Invalid global variable '" + name.lexeme + "'.");
    }
  }

  Object get(Token name) {
    if (values.containsKey(name.lexeme)) {
      return values.get(name.lexeme);
    }

    if (enclosing != null) return enclosing.get(name);

    throw new CompilingError(name, "Undefined variable '" + name.lexeme + "'.");
  }

  void assign(Token name, Object value) {
    if (values.containsKey(name.lexeme)) {
      values.put(name.lexeme, value);
      return;
    }
    
    if (enclosing != null) {
      enclosing.assign(name, value);
      return;
    }

    throw new CompilingError(name,"Undefined variable '" + name.lexeme + "'.");
  }

  private Boolean isGlobal(){
    return this.global;
  }
}
