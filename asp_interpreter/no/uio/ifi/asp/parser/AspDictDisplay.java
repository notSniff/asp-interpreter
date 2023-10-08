package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspDictDisplay extends AspAtom {
  ArrayList<AspStringLiteral> asll = new ArrayList<>();
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspDictDisplay(int n) {
    super(n);
  }

  public static AspDictDisplay parse(Scanner s) {
    enterParser("dict display");

    AspDictDisplay aspdd = new AspDictDisplay(s.curLineNum());
    skip(s, leftBraceToken);
    while(s.curToken().kind != rightBraceToken) {
      aspdd.asll.add(AspStringLiteral.parse(s));
      skip(s, colonToken);
      aspdd.ael.add(AspExpr.parse(s));
      if(s.curToken().kind == commaToken) {
        skip(s, commaToken);
      }
    }
    skip(s, rightBraceToken);

    leaveParser("dict display");
    return aspdd;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("{");
    if(asll.size() > 0) {
      asll.get(0).prettyPrint();
      prettyWrite(": ");
      ael.get(0).prettyPrint();
    }
    for(int i = 1; i < asll.size(); i++) {
      prettyWrite(", ");
      asll.get(i).prettyPrint();
      prettyWrite(": ");
      ael.get(i).prettyPrint();
    }
    prettyWrite("}");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    ArrayList<RuntimeValue> k = new ArrayList<RuntimeValue>();
    ArrayList<RuntimeValue> v = new ArrayList<RuntimeValue>();
    for(int i = 0; i < asll.size(); i++) {
      k.add(asll.get(i).eval(curScope));
      v.add(ael.get(i).eval(curScope));
    }
    return new RuntimeDictValue(k, v);
  }
}
