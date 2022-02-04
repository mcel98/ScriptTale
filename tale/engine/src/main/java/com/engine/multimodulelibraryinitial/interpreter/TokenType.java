package com.engine.multimodulelibraryinitial.interpreter;

enum TokenType {
  // Single-character tokens.
  LEFT_PAREN, RIGHT_PAREN,
  COMMA, DOT, MINUS, PLUS, SLASH, STAR, COLON, INDENT, DEDENT, SEMICOLON, LEFT_BRACE, RIGHT_BRACE,

  // One or two character tokens.
  BANG, BANG_EQUAL,
  EQUAL, EQUAL_EQUAL,
  GREATER, GREATER_EQUAL,
  LESS, LESS_EQUAL,

  // Literals.
  IDENTIFIER, STRING, NUMBER,

  // Keywords.
  AND, ELSE, FALSE, FUN, IF, NIL, OR,
  PRINT, TRUE, VAR, SEND, SAY, NEW, RETURN, ACTOR, WORLD, ITEM, BAG,

  EOF
}
