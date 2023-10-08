package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspTerm extends AspSyntax {
  ArrayList<AspFactor> afl = new ArrayList<>();
  ArrayList<AspTermOpr> atol = new ArrayList<>();

  AspTerm(int n) {
    super(n);
  }

  public static AspTerm parse(Scanner s) {
    enterParser("term");

    AspTerm at = new AspTerm(s.curLineNum());
    while(true) {
      at.afl.add(AspFactor.parse(s));
      if(!s.isTermOpr()) {
        break;
      }
      at.atol.add(AspTermOpr.parse(s));
    }

    leaveParser("term");
    return at;
  }


  @Override
  public void prettyPrint() {
    int i =1;
    for(AspFactor af: afl) {
      af.prettyPrint();
      if(i < afl.size()){
        atol.get(i-1).prettyPrint();
      }
      i++;
    }
  }

  //ser etter + eller - og kaller paa riktig eval.
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = afl.get(0).eval(curScope);
    for(int i = 1; i < afl.size(); i++) {
      TokenKind tempToken = atol.get(i-1).getToken().kind;
      switch(tempToken) {
        case minusToken:
          temp = temp.evalSubtract(afl.get(i).eval(curScope), this);
          break;
        case plusToken:
          temp = temp.evalAdd(afl.get(i).eval(curScope), this);
          break;
        default:
          Main.panic("Illegal term operator: " + tempToken + "!");
      }
    }
    return temp;
  }
}
