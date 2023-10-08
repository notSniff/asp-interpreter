package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspNoneLiteral extends AspAtom {
  ArrayList<Token> nlToken = new ArrayList<>();

  AspNoneLiteral(int n) {
    super(n);
  }

  public static AspNoneLiteral parse(Scanner s) {
    enterParser("none literal");

    AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());
    anl.nlToken.add(s.curToken());
    skip(s, noneToken);

    leaveParser("none literal");
    return anl;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("None");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeNoneValue temp = new RuntimeNoneValue();
    return temp;
  }
}
