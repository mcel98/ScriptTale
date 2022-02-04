package com.engine.multimodulelibraryinitial.interpreter;
import static com.engine.multimodulelibraryinitial.interpreter.TokenType.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private ErrorReporter reporter;
  private static final Map<String, TokenType> keywords;
  private int start = 0;
  private int current = 0;
  private int line = 1;
  private int scope_Indent = 0;
  private int current_indent_level = 0;
  private int casing_nest_level = 0;

  Scanner(String source, ErrorReporter reporter) {
    this.source = source;
    this.reporter = reporter;
  }

  static {
    keywords = new HashMap<>();
    keywords.put("and",    AND);
    keywords.put("else",   ELSE);
    keywords.put("false",  FALSE);
    keywords.put("fun",    FUN);
    keywords.put("if",     IF);
    keywords.put("nil",    NIL);
    keywords.put("or",     OR);
    keywords.put("print",  PRINT);
    keywords.put("return", RETURN);
    keywords.put("true",   TRUE);
    keywords.put("var",    VAR);
    keywords.put("new",    NEW);
    keywords.put("say",    SAY);
    keywords.put("send",   SEND);
    keywords.put("actor",  ACTOR);
    keywords.put("item",   ITEM);
    keywords.put("world",  WORLD);
    keywords.put("bag",    BAG);
  }

  private boolean isAtEnd() {
    return current >= source.length();
  }

  List<Token> scanTokens() {
    while (!isAtEnd()) {
      // We are at the beginning of the next lexeme.
      start = current;
      scanToken();
    }

    tokens.add(new Token(EOF, "", null, line));
    return tokens;
  }

  private char advance() {
    return source.charAt(current++);
  }

  private void addToken(TokenType type) {
    addToken(type, null);
  }

  private void addToken(TokenType type, Object literal) {
    String text = source.substring(start, current);
    tokens.add(new Token(type, text, literal, line));
  }

  private boolean match(char expected) {
    if (isAtEnd()) return false;
    if (source.charAt(current) != expected) return false;

    current++;
    return true;
  }

  private char peek() {
    if (isAtEnd()) return '\0';
    return source.charAt(current);
  }

   private void string() {
    while (peek() != '"' && !isAtEnd()) {
      if (peek() == '\n') line++;
      advance();
    }

    if (isAtEnd()) {
      // error(line, "Unterminated string.");
      return;
    }

    // The closing ".
    advance();

    // Trim the surrounding quotes.
    String value = source.substring(start + 1, current - 1);
    addToken(STRING, value);
  }

  private boolean isDigit(char c) {
    return c >= '0' && c <= '9';
  }

  private char peekNext() {
    if (current + 1 >= source.length()) return '\0';
    return source.charAt(current + 1);
  }

  private char peekLast() {
    if (current - 1 < 0) return '\0';
    return source.charAt(current + 1);
  }
  
  private boolean isTerminated(){
    if(peekLast() != '.' || isDigit(peekNext()) ) return false;
    return true;
  }

  private void number() {
    while (isDigit(peek())) advance();

    // Look for a fractional part.
    if (peek() == '.' && isDigit(peekNext())) {
      // Consume the "."
      advance();

      while (isDigit(peek())) advance();
    }

    addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
  }
  
  private void identifier() {
    while (isAlphaNumeric(peek())) advance();

    String text = source.substring(start, current);
    TokenType type = keywords.get(text);
    if (type == null) type = IDENTIFIER;
    addToken(type);
  }

  private boolean isAlpha(char c) {
    return (c >= 'a' && c <= 'z') ||
           (c >= 'A' && c <= 'Z') ||
            c == '_';
  }

  private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private boolean isBlank(char c){
    if(c != ' ') return false;
    return true;
  }

  private boolean isNestedCasing(){
    if(casing_nest_level > 0) return true;
    return false;

  }

  private boolean detectScopeChange(){
    boolean detected = !isNestedCasing() && (peek() == '\t' || isBlank(peek()));
    return detected;
  }

  private void set_scope(){
    while( detectScopeChange() ){
      if(!isBlank(peek())){
        current_indent_level++;
      }
      advance();
    }
    if(current_indent_level > scope_Indent){
      scope_Indent++;
      addToken(INDENT);
    }else{
      scope_Indent -= current_indent_level;
      for(int i = 0; i < current_indent_level; i++ ) addToken(DEDENT);
    }
    current_indent_level = 0;

  }


  private void scanToken() {
    char c = advance();
    switch (c) {
        case '(': addToken(LEFT_PAREN); casing_nest_level++; break;
        case ')': addToken(RIGHT_PAREN); casing_nest_level--; break;
        case '{': addToken(LEFT_BRACE); casing_nest_level++; break;
        case '}': addToken(RIGHT_BRACE); casing_nest_level--; break;
        case ',': addToken(COMMA); break;
        case ';': addToken(SEMICOLON); break;
        case '.': addToken(DOT); break;
        case '-': addToken(MINUS); break;
        case '+': addToken(PLUS); break;
        case '*': addToken(STAR); break;
        case '!': addToken(match('=') ? BANG_EQUAL : BANG); break;
        case '=': addToken(match('=') ? EQUAL_EQUAL : EQUAL); break;
        case '<': addToken(match('=') ? LESS_EQUAL : LESS); break;
        case '>': addToken(match('=') ? GREATER_EQUAL : GREATER); break;
        case '/':
            if (match('/')) {
            // A comment goes until the end of the line.
            while (peek() != '\n' && !isAtEnd()) advance();
            } else {
                addToken(SLASH);
            }
            break;
        case ' ':
        case '\r':
        case '\t':
            break;
        case '\n': if(isTerminated() && isDigit(peekNext())){

          }else{
            line++;
          } break;
        case '"': string(); break;
        default:
          if (isDigit(c)) {
            number();
          } else if (isAlpha(c)) {
            identifier();
          }else {
            error(line, "Unexpected character.");
          }
    }
  }

  private void error(int line, String message) {
    reporter.report("[line " + line + "] Error"  + ": " + message);
}

}
