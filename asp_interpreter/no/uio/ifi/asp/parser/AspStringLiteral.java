package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspStringLiteral extends AspAtom {
  ArrayList<Token> slToken = new ArrayList<>();

  AspStringLiteral(int n) {
    super(n);
  }

  public static AspStringLiteral parse(Scanner s) {
    enterParser("string literal");

    AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
    asl.slToken.add(s.curToken());
    skip(s, stringToken);

    leaveParser("string literal");
    return asl;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("\"");
    prettyWrite(slToken.get(0).getValue());
    prettyWrite("\"");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeStringValue temp = new RuntimeStringValue(slToken.get(0).stringLit);
    return temp;
  }
}
