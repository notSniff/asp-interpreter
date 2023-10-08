package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspReturnStmt extends AspSmallStmt {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspReturnStmt(int n) {
    super(n);
  }

  public static AspReturnStmt parse(Scanner s) {
    enterParser("return stmt");

    AspReturnStmt ars = new AspReturnStmt(s.curLineNum());
    skip(s, returnToken);
    ars.ael.add(AspExpr.parse(s));

    leaveParser("return stmt");
    return ars;
  }


  @Override
  public void prettyPrint() {
    for(AspExpr ae: ael) {
      prettyWrite("return ");
      ae.prettyPrint();
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue v = ael.get(0).eval(curScope);
    trace("return "+ v.showInfo());
    throw new RuntimeReturnValue(v, lineNum);
  }
}
