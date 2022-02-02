package com.engine.multimodulelibraryinitial.interpreter;


abstract class Stmt {

  abstract <R> R accept(StmtVisitor<R> visitor);
    static class Expression extends Stmt {
        Expression(Expr expression) {
          this.expression = expression;
        }

        @Override
        <R> R accept(StmtVisitor<R> visitor) {
          return visitor.visitExpression(this);
        }
        final Expr expression;
    }

    static class Print extends Stmt {
        Print(Expr expression) {
          this.expression = expression;
        }

        @Override
        <R> R accept(StmtVisitor<R> visitor) {
          return visitor.visitPrint(this);
        } 

        final Expr expression;
    }

    static class Var extends Stmt {
      Var(Token name, Expr initializer) {
        this.initializer = initializer;
        this.name = name;

      }

      @Override
      <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitVariable(this);
      } 

      final Expr initializer;
      final Token name;

    }
}
