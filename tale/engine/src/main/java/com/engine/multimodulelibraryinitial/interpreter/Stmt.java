package com.engine.multimodulelibraryinitial.interpreter;
import java.util.List;

abstract class Stmt {

  abstract <R> R accept(StmtVisitor<R> visitor);
  static class Block extends Stmt {
    Block(List<Stmt> statements) {
      this.statements = statements;
    }

    @Override
    <R> R accept(StmtVisitor<R> visitor) {
      return visitor.visitBlock(this);
    }
    final List<Stmt> statements;
  }
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
