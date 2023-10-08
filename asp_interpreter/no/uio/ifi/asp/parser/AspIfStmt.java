package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspIfStmt extends AspCompoundStmt {
  ArrayList<AspExpr> ael = new ArrayList<>();
  ArrayList<AspSuite> asl = new ArrayList<>();

  AspIfStmt(int n) {
    super(n);
  }

  public static AspIfStmt parse(Scanner s) {
    enterParser("if stmt");

    AspIfStmt ais = new AspIfStmt(s.curLineNum());
    skip(s, ifToken);
    while(true) {
      ais.ael.add(AspExpr.parse(s));
      skip(s, colonToken);
      ais.asl.add(AspSuite.parse(s));
      if(s.curToken().kind != elifToken) {
        break;
      }
      skip(s, elifToken);
    }
    if(s.curToken().kind == elseToken) {
      skip(s, elseToken);
      skip(s, colonToken);
      ais.asl.add(AspSuite.parse(s));
    }

    leaveParser("if stmt");
    return ais;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("if ");
    int j = ael.size();
    for(int i = 0; i < j; i++) {
      if(i == 0) {}
      else {
        prettyWrite("elif ");
      }
      ael.get(i).prettyPrint();
      prettyWrite(":");
      asl.get(i).prettyPrint();
    }
    if(asl.size() > j) {
      prettyWrite("else:");
      asl.get(j).prettyPrint();
    }
  }

  //if
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = null;
    for(int i = 0; i < ael.size(); i++) {
      temp = ael.get(i).eval(curScope);
      if(temp.getBoolValue(temp.showInfo(), this)) {
        trace("if True alt #" + (i+1));
        return asl.get(i).eval(curScope);
      }
    }
    if(ael.size() != asl.size()) {
      trace("else");
      return asl.get(asl.size()-1).eval(curScope);
    }
    return null;
  }
}
