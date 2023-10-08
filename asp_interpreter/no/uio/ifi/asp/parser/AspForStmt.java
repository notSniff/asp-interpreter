package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspForStmt extends AspCompoundStmt {
  ArrayList<AspName> anl = new ArrayList<>();
  ArrayList<AspExpr> ael = new ArrayList<>();
  ArrayList<AspSuite> asl = new ArrayList<>();

  AspForStmt(int n) {
    super(n);
  }

  public static AspForStmt parse(Scanner s) {
    enterParser("for stmt");

    AspForStmt afs = new AspForStmt(s.curLineNum());
    skip(s, forToken);
    afs.anl.add(AspName.parse(s));
    skip(s, inToken);
    afs.ael.add(AspExpr.parse(s));
    skip(s, colonToken);
    afs.asl.add(AspSuite.parse(s));

    leaveParser("for stmt");
    return afs;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("for ");
    anl.get(0).prettyPrint();
    prettyWrite(" in ");
    ael.get(0).prettyPrint();
    prettyWrite(":");
    asl.get(0).prettyPrint();
  }

  //for
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    long i = 0;
    String n = anl.get(0).nt.get(0).name;
    RuntimeValue tempExpr = ael.get(0).eval(curScope);
    while(true) {
      //RuntimeValue tempExpr = ael.get(0).eval(curScope);
      if(tempExpr instanceof RuntimeListValue) {
        RuntimeIntValue ri = new RuntimeIntValue(i);
        if(ri.evalLess(tempExpr.evalLen(this), this).getBoolValue("for-loop", this)) {
          RuntimeValue tempListValue = tempExpr.evalSubscription(ri, this);
          trace("for #"+(i+1)+": "+n+" = "+tempListValue.showInfo());
          curScope.assign(n, tempListValue);
          asl.get(0).eval(curScope);
          i++;
        }
        else {
          return null;
        }
      }
      else {
        tempExpr.runtimeError("Type error for for-loop", this);
        return null;
      }
    }
  }
}
