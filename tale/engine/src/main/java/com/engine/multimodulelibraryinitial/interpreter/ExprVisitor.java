package com.engine.multimodulelibraryinitial.interpreter;

import com.engine.multimodulelibraryinitial.interpreter.Expr.*;

public interface ExprVisitor<R> {
    R visitBinary(Binary binary);
    R visitUnary(Unary unary);
    R visitLiteral(Literal literal);
    R visitGrouping(Grouping grouping);
    R visitVariable(Variable variable);
    R visitAssign(Assign assign);
}
