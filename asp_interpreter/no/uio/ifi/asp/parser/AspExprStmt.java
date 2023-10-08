package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExprStmt extends AspSmallStmt {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspExprStmt(int n) {
    super(n);
  }

  public static AspExprStmt parse(Scanner s) {
    enterParser("expr stmt");

    AspExprStmt aes = new AspExprStmt(s.curLineNum());
    aes.ael.add(AspExpr.parse(s));

    leaveParser("expr stmt");
    return aes;
  }


  @Override
  public void prettyPrint() {
    ael.get(0).prettyPrint();
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue x = ael.get(0).eval(curScope);
    trace(x.showInfo());
    return null;
  }
}
