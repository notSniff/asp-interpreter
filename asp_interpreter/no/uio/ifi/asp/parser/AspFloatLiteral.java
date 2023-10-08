package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFloatLiteral extends AspAtom {
  ArrayList<Token> flToken = new ArrayList<>();

  AspFloatLiteral(int n) {
    super(n);
  }

  public static AspFloatLiteral parse(Scanner s) {
    enterParser("float literal");

    AspFloatLiteral afl = new AspFloatLiteral(s.curLineNum());
    afl.flToken.add(s.curToken());
    skip(s, floatToken);

    leaveParser("float literal");
    return afl;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(flToken.get(0).getValue());
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeFloatValue temp = new RuntimeFloatValue(flToken.get(0).floatLit);
    return temp;
  }
}
