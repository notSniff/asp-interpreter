package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSubscription extends AspPrimarySuffix {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspSubscription(int n) {
    super(n);
  }

  public static AspSubscription parse(Scanner s) {
    enterParser("subscription");

    AspSubscription as = new AspSubscription(s.curLineNum());
    skip(s, leftBracketToken);
    as.ael.add(AspExpr.parse(s));
    skip(s, rightBracketToken);


    leaveParser("subscription");
    return as;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("[");
    ael.get(0).prettyPrint();
    prettyWrite("]");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = ael.get(0).eval(curScope);
    return temp;
  }
}
