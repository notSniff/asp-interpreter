package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTermOpr extends AspSyntax {
  ArrayList<Token> toToken = new ArrayList<>();

  AspTermOpr(int n) {
    super(n);
  }

  public static AspTermOpr parse(Scanner s) {
    enterParser("term opr");

    AspTermOpr ato = new AspTermOpr(s.curLineNum());
    ato.toToken.add(s.curToken());
    switch(s.curToken().kind) {
      case plusToken:
        skip(s, plusToken);
        break;
      case minusToken:
        skip(s, minusToken);
        break;
      default:
        parserError("Expected term opr but found "+ s.curToken().toString()+"!", ato.lineNum);
    }

    leaveParser("term opr");
    return ato;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(" ");
    prettyWrite(toToken.get(0).toString());
    prettyWrite(" ");
  }

  public Token getToken() {
    return toToken.get(0);
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}
