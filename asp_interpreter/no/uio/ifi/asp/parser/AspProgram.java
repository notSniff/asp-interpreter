package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspProgram extends AspSyntax {
  ArrayList<AspStmt> stmts = new ArrayList<>();

  AspProgram(int n) {
    super(n);
  }


  public static AspProgram parse(Scanner s) {
    enterParser("program");

    AspProgram ap = new AspProgram(s.curLineNum());
    while (s.curToken().kind != eofToken) {
      ap.stmts.add(AspStmt.parse(s));
    }
    skip(s, eofToken);

    leaveParser("program");
    return ap;
  }


  @Override
  public void prettyPrint() {
    for(AspStmt as: stmts) {
      as.prettyPrint();
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    try {
      for(AspStmt as: stmts) {
        as.eval(curScope);
      }
    } catch(RuntimeReturnValue rrv) {
      parserError("Return statment outside of function", rrv.lineNum);
    }
    return null;
  }
}
