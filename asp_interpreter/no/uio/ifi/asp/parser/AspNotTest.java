package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNotTest extends AspSyntax {
  ArrayList<AspComparison> acl = new ArrayList<>();
  boolean isNot = false;

  AspNotTest(int n) {
    super(n);
  }

  public static AspNotTest parse(Scanner s) {
    enterParser("not test");

    AspNotTest ant = new AspNotTest(s.curLineNum());
    if(s.curToken().kind == notToken) {
      skip(s, notToken);
      ant.isNot = true;
    }
    ant.acl.add(AspComparison.parse(s));

    leaveParser("not test");
    return ant;
  }


  @Override
  public void prettyPrint() {
    if(isNot) {
      prettyWrite(" not ");
    }
    acl.get(0).prettyPrint();
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = acl.get(0).eval(curScope);
    if(isNot) {
      return temp.evalNot(this);
    }
    return temp;
  }
}
