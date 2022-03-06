package com.engine.multimodulelibraryinitial.interpreter;

import java.util.List;
import com.engine.multimodulelibraryinitial.logic.script;
import java.util.ArrayList;

class CompilingError extends RuntimeException {
    final Token token;


    CompilingError(Token token, String message) {
        super(message);
        this.token = token;
    }
}

class Interpreter implements ExprVisitor<Object>, StmtVisitor<Void>{

    private ErrorReporter reporter;
    private Environment environment = new Environment();

    Interpreter(ErrorReporter reporter){
        this.reporter = reporter;
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new CompilingError(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;
    
        throw new CompilingError(operator, "Operands must be numbers.");
    }

    private String stringify(Object object) {
        if (object == null) return "nil";
    
        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }
    
        return object.toString();
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        }finally {
            this.environment = previous;
        }
    }

    @Override
    public Void visitExpression(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    
    @Override
    public Void visitPrint(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitVariable(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name, value);
        return null;
    }

    @Override
    public Void visitBlock(Stmt.Block stmt) {
      executeBlock(stmt.statements, new Environment(environment));
      return null;
    }

    @Override
    public Void visitIf(Stmt.If stmt) {
      if (isTruthy(evaluate(stmt.condition))) {
        execute(stmt.thenBranch);
      } else if (stmt.elseBranch != null) {
        execute(stmt.elseBranch);
      }
      return null;
    }

    @Override
    public Void visitFunction(Stmt.Function stmt) {
      TaleFunction function = new TaleFunction(stmt);
      environment.define(stmt.name, function);
      return null;
    }

    @Override
    public Object visitVariable(Expr.Variable expr) {
      return environment.get(expr.name);
    }

    @Override
    public Object visitAssign(Expr.Assign expr) {
      Object value = evaluate(expr.value);
      environment.assign(expr.name, value);
      return value;
    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
      return expr.value;
    }

    @Override
    public Object visitGrouping(Expr.Grouping expr) {
      return evaluate(expr.expression);
    }

    @Override
    public Object visitUnary(Expr.Unary expr) {
      Object right = evaluate(expr.right);
  
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double)right;
            case BANG:
                return !isTruthy(right);
      }
  
      // Unreachable.
      return null;
    }

    @Override
    public Object visitBinary(Expr.Binary expr) {
    Object left = evaluate(expr.left);
    Object right = evaluate(expr.right); 
        switch (expr.operator.type) {
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                } 

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                throw new CompilingError(expr.operator, "Operands must be two numbers or two strings.");    
            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double)(left) - (double)right;
            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            case BANG_EQUAL: return !left.equals(right);
            case EQUAL_EQUAL: return left.equals(right);
        }

    // Unreachable.
    return null;
  }

  @Override
  public Object visitLogical(Expr.Logical expr) {
    Object left = evaluate(expr.left);

    if (expr.operator.type == TokenType.OR) {
      if (isTruthy(left)) return left;
    } else {
      if (!isTruthy(left)) return left;
    }

    return evaluate(expr.right);
  }

  @Override
  public Object visitCall(Expr.Call expr) {
    Object callee = evaluate(expr.callee);

    List<Object> arguments = new ArrayList<>();
    for (Expr argument : expr.arguments) { 
      arguments.add(evaluate(argument));
    }

    if (!(callee instanceof TaleCallable)) {
        throw new CompilingError(expr.paren,
            "Can only call functions and classes.");
    }

    TaleCallable function = (TaleCallable)callee;
    if (arguments.size() != function.arity()) {
        throw new CompilingError(expr.paren, "Expected " +
            function.arity() + " arguments but got " +
            arguments.size() + ".");
    }
    return function.call(this, arguments);
  }

    void interpret(List<Stmt> statements) { 
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        }catch (CompilingError error) {
            error(error);
        }
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
      }

    private void error(CompilingError error){
        reporter.report(error.getMessage() +
        "\n[line " + error.token.line + "]");
    }

}
