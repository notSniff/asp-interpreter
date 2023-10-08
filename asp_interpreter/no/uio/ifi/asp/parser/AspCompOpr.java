package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspCompOpr extends AspSyntax {
  ArrayList<Token> coToken = new ArrayList<>();

  AspCompOpr(int n) {
    super(n);
  }

  public static AspCompOpr parse(Scanner s) {
    enterParser("comp opr");

    AspCompOpr aco = new AspCompOpr(s.curLineNum());
    aco.coToken.add(s.curToken());
    switch(s.curToken().kind) {
      case lessToken:
        skip(s, lessToken);
        break;
      case greaterToken:
        skip(s, greaterToken);
        break;
      case doubleEqualToken:
        skip(s, doubleEqualToken);
        break;
      case lessEqualToken:
        skip(s, lessEqualToken);
        break;
      case greaterEqualToken:
        skip(s, greaterEqualToken);
        break;
      case notEqualToken:
        skip(s, notEqualToken);
        break;
      default:
        parserError("Expected comp opr but found "+ s.curToken().toString()+"!", aco.lineNum);
    }

    leaveParser("comp opr");
    return aco;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(" ");
    prettyWrite(coToken.get(0).toString());
    prettyWrite(" ");
  }

  public Token getToken() {
    return coToken.get(0);
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}
