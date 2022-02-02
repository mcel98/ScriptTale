package com.engine.multimodulelibraryinitial.interpreter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TaleScript {

  private static final ErrorReporter reporter = new ConsoleErrorReporter();
  private static final Interpreter interpreter = new Interpreter(reporter);

    public static void main(String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: talescript [script]");
      System.exit(64); 
    } else if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

 

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // limitar directorio a donde puede leer
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) { 
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
        }
    }

    private static void run(String source) {
      Scanner scanner = new Scanner(source, reporter);
      List<Token> tokens = scanner.scanTokens();
 
      Parser parser = new Parser(tokens, reporter);
      List<Stmt> statements = parser.parse();

      
    
      // Stop if there was a syntax error.
      if (reporter.hasError()) return;
    
      interpreter.interpret(statements);
    }

}