package com.engine.multimodulelibraryinitial.interpreter;
import com.engine.multimodulelibraryinitial.logic.script;
import java.util.List;

abstract class Expr {

  abstract <R> R accept(ExprVisitor<R> visitor);
    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
          this.left = left;
          this.operator = operator;
          this.right = right;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
          return visitor.visitBinary(this);
        }
        final Expr left;
        final Token operator;
        final Expr right;
    }

    static class Unary extends Expr {
        Unary(Expr right, Token operator) {
          this.right = right;
          this.operator = operator;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
          return visitor.visitUnary(this);
        } 

        final Expr right;
        final Token operator;
      }

    static class Literal extends Expr {
        Literal(Object value) {
          this.value = value;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
          return visitor.visitLiteral(this);
        }

        final Object value;
      }

    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(ExprVisitor<R> visitor) {
          return visitor.visitGrouping(this);
        }

        final Expr expression;
      }

    static class Variable extends Expr {
      Variable(Token name) {
        this.name = name;
      }

      @Override
      <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitVariable(this);
      }

      final Token name;
    }

    static class Assign extends Expr {
      Assign(Token name, Expr value) {
        this.name = name;
        this.value = value;

      }

      @Override
      <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitAssign(this);
      }

      final Token name;
      final Expr value;
    }

    static class Logical extends Expr {
      Logical(Expr left, Token operator, Expr right) {
        this.operator = operator;
        this.left = left;
        this.right = right ;

      }

      @Override
      <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitLogical(this);
      }

      final Token operator;
      final Expr left;
      final Expr right;
    }

    static class Call extends Expr {
      Call(Expr callee, Token paren, List<Expr> arguments) {
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments ;

      }

      @Override
      <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitCall(this);
      }

      final Expr callee;
      final Token paren;
      final List<Expr> arguments;
    }
    
}
