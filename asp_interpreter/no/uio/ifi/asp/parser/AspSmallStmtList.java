package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSmallStmtList extends AspStmt {
  ArrayList<AspSmallStmt> smallS = new ArrayList<>();

  AspSmallStmtList(int n) {
    super(n);
  }

  public static AspSmallStmtList parse(Scanner s) {
    enterParser("small stmt list");

    AspSmallStmtList assl = new AspSmallStmtList(s.curLineNum());
    while(s.curToken().kind != newLineToken) {
      assl.smallS.add(AspSmallStmt.parse(s));
      if(s.curToken().kind == semicolonToken) {
        skip(s, semicolonToken);
      }
    }
    skip(s, newLineToken);

    leaveParser("small stmt list");
    return assl;
  }

  @Override
  public void prettyPrint() {
    int i = 1;
    for(AspSmallStmt as: smallS) {
      as.prettyPrint();
      if(i < smallS.size()) {
        prettyWrite("; ");
      }
      i++;
    }
    prettyWriteLn();
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue x = null;
    for(AspSmallStmt s: smallS) {
      x = s.eval(curScope);
    }
    return x;
  }
}
