package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspSuite extends AspSyntax {
  ArrayList<AspStmt> asl = new ArrayList<>();
  ArrayList<AspSmallStmtList> assll = new ArrayList<>();

  AspSuite(int n) {
    super(n);
  }

  public static AspSuite parse(Scanner s) {
    enterParser("suite");

    AspSuite as = new AspSuite(s.curLineNum());
    if(s.curToken().kind == newLineToken) {
      skip(s, newLineToken);
      skip(s, indentToken);
      while(s.curToken().kind != dedentToken) {
        as.asl.add(AspStmt.parse(s));
      }
      skip(s, dedentToken);
    }
    else {
      as.assll.add(AspSmallStmtList.parse(s));
    }

    leaveParser("suite");
    return as;
  }


  @Override
  public void prettyPrint() {
    if(asl.size() > 0) {
      prettyWriteLn();
      prettyIndent();
      for(AspStmt as: asl) {
        as.prettyPrint();
      }
      prettyDedent();
    }
    else{
      assll.get(0).prettyPrint();
    }
  }

  //Starter innmaten paa linjer etter innrykk
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    if(asl.size() > 0) {
      for(AspStmt s: asl) {
        s.eval(curScope);
      }
    }
    else {
      assll.get(0).eval(curScope);
    }
    return null;
  }
}
