package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAssignment extends AspSmallStmt {
  ArrayList<AspName> anl = new ArrayList<>();
  ArrayList<AspExpr> ael = new ArrayList<>();
  ArrayList<AspSubscription> asl = new ArrayList<>();

  AspAssignment(int n) {
    super(n);
  }

  public static AspAssignment parse(Scanner s) {
    enterParser("assignment");

    AspAssignment aa = new AspAssignment(s.curLineNum());
    aa.anl.add(AspName.parse(s));
    while(s.curToken().kind != equalToken) {
      aa.asl.add(AspSubscription.parse(s));
    }
    skip(s, equalToken);
    aa.ael.add(AspExpr.parse(s));

    leaveParser("assignment");
    return aa;
  }


  @Override
  public void prettyPrint() {
    anl.get(0).prettyPrint();
    for(AspSubscription as: asl) {
      as.prettyPrint();
      prettyWrite(" ");
    }
    prettyWrite(" = ");
    ael.get(0).prettyPrint();
  }

  //fester navn til variabler.
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    String n = anl.get(0).nt.get(0).name;
    String traceString = n;
    RuntimeValue temp = null;
    if(asl.size() == 0) {
      curScope.assign(n, ael.get(0).eval(curScope));
      trace(n+" = "+curScope.find(n, this).showInfo());
    }
    else {
      temp = curScope.find(n, this);
      for(int i = 0; i < asl.size(); i++) {
        if(i == asl.size()-1) {
          RuntimeValue tempExpr = ael.get(0).eval(curScope);
          temp.evalAssignElem(asl.get(i).eval(curScope) , tempExpr, this);
          trace(traceString+ "["+ asl.get(i).eval(curScope).showInfo() + "]" + " = " + tempExpr.showInfo());
        }
        else {
          traceString += "[";
          traceString += asl.get(i).eval(curScope).showInfo();
          traceString += "]";
          temp = temp.evalSubscription(asl.get(i).eval(curScope), this);
        }
      }
    }
    return null;
  }
}
