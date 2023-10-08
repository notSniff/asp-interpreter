package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspComparison extends AspSyntax {
  ArrayList<AspTerm> atl = new ArrayList<>();
  ArrayList<AspCompOpr> acol = new ArrayList<>();

  AspComparison(int n) {
    super(n);
  }

  public static AspComparison parse(Scanner s) {
    enterParser("comparison");

    AspComparison ac = new AspComparison(s.curLineNum());
    while(true) {
      ac.atl.add(AspTerm.parse(s));
      if(!s.isCompOpr()) {
        break;
      }
      ac.acol.add(AspCompOpr.parse(s));
    }

    leaveParser("comparison");
    return ac;
  }


  @Override
  public void prettyPrint() {
    int i =1;
    for(AspTerm at: atl) {
      at.prettyPrint();
      if(i < atl.size()){
        acol.get(i-1).prettyPrint();
      }
      i++;
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = atl.get(0).eval(curScope);
    RuntimeValue boolTemp = new RuntimeBoolValue(true);
    boolean didComparison = false;
    for(int i = 1; i < atl.size(); i++) {
      didComparison = true;
      TokenKind tempToken = acol.get(i-1).getToken().kind;
      switch(tempToken) {
        case lessToken:
          boolTemp = temp.evalLess(atl.get(i).eval(curScope), this);
          break;
        case greaterToken:
          boolTemp = temp.evalGreater(atl.get(i).eval(curScope), this);
          break;
        case doubleEqualToken:
          boolTemp = temp.evalEqual(atl.get(i).eval(curScope), this);
          break;
        case greaterEqualToken:
          boolTemp = temp.evalGreaterEqual(atl.get(i).eval(curScope), this);
          break;
        case lessEqualToken:
          boolTemp = temp.evalLessEqual(atl.get(i).eval(curScope), this);
          break;
        case notEqualToken:
          boolTemp = temp.evalNotEqual(atl.get(i).eval(curScope), this);
          break;
        default:
          Main.panic("Illegal comparison operator: " + tempToken + "!");
      }
      if(!boolTemp.getBoolValue(boolTemp.showInfo(), this)) {
        return boolTemp;
      }
      temp = atl.get(i).eval(curScope);
    }
    if(didComparison) {
      return boolTemp;
    }
    return temp;
  }
}
