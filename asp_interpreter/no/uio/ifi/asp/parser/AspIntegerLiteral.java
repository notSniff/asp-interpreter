package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIntegerLiteral extends AspAtom {
  ArrayList<Token> ilToken = new ArrayList<>();

  AspIntegerLiteral(int n) {
    super(n);
  }

  public static AspIntegerLiteral parse(Scanner s) {
    enterParser("integer literal");

    AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());
    ail.ilToken.add(s.curToken());
    skip(s, integerToken);

    leaveParser("integer literal");
    return ail;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(ilToken.get(0).getValue());
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeIntValue temp = new RuntimeIntValue(ilToken.get(0).integerLit);
    return temp;
  }
}
