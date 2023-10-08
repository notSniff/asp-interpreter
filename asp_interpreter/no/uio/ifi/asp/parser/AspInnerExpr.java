package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspInnerExpr extends AspAtom {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspInnerExpr(int n) {
    super(n);
  }

  public static AspInnerExpr parse(Scanner s) {
    enterParser("inner expr");

    AspInnerExpr aie = new AspInnerExpr(s.curLineNum());
    skip(s, leftParToken);
    aie.ael.add(AspExpr.parse(s));
    skip(s, rightParToken);

    leaveParser("inner expr");
    return aie;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("(");
    ael.get(0).prettyPrint();
    prettyWrite(")");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return ael.get(0).eval(curScope);
  }
}
