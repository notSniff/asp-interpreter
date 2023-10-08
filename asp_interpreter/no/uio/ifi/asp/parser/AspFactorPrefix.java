package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorPrefix extends AspSyntax {
  ArrayList<Token> fpToken = new ArrayList<>();

  AspFactorPrefix(int n) {
    super(n);
  }

  public static AspFactorPrefix parse(Scanner s) {
    enterParser("factor prefix");

    AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum());
    afp.fpToken.add(s.curToken());
    switch(s.curToken().kind) {
      case plusToken:
        skip(s, plusToken);
        break;
      case minusToken:
        skip(s, minusToken);
        break;
      default:
        parserError("Expected factor prefix but found "+ s.curToken().toString()+"!", afp.lineNum);
    }

    leaveParser("factor prefix");
    return afp;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(" ");
    prettyWrite(fpToken.get(0).toString());
    prettyWrite(" ");
  }

  public Token getToken() {
    return fpToken.get(0);
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}
