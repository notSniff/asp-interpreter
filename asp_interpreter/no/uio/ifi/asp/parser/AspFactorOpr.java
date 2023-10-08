package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactorOpr extends AspSyntax {
  ArrayList<Token> foToken = new ArrayList<>();

  AspFactorOpr(int n) {
    super(n);
  }

  static AspFactorOpr parse(Scanner s) {
    enterParser("factor opr");

    AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
    afo.foToken.add(s.curToken());
    switch(s.curToken().kind) {
      case astToken:
        skip(s, astToken);
        break;
      case slashToken:
        skip(s, slashToken);
        break;
      case percentToken:
        skip(s, percentToken);
        break;
      case doubleSlashToken:
        skip(s, doubleSlashToken);
        break;
      default:
        parserError("Expected factor opr but found "+ s.curToken().toString()+"!", s.curLineNum());
    }

    leaveParser("factor opr");
    return afo;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(" ");
    prettyWrite(foToken.get(0).toString());
    prettyWrite(" ");
  }

  public Token getToken() {
    return foToken.get(0);
  }

  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    return null;
  }
}
