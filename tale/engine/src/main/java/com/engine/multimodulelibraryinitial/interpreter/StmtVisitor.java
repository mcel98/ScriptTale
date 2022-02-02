package com.engine.multimodulelibraryinitial.interpreter;

import com.engine.multimodulelibraryinitial.interpreter.Stmt.*;


public interface StmtVisitor<R> {
    R visitExpression(Expression expression);
    R visitPrint(Print print);
    R visitVariable(Var var);
    
}