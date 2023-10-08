package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspAndTest extends AspSyntax {
  ArrayList<AspNotTest> antl = new ArrayList<>();

  AspAndTest(int n) {
    super(n);
  }

  public static AspAndTest parse(Scanner s) {
    enterParser("and test");

    AspAndTest aat = new AspAndTest(s.curLineNum());
    while(true) {
      aat.antl.add(AspNotTest.parse(s));
      if(s.curToken().kind != andToken) {
        break;
      }
      skip(s, andToken);
    }

    leaveParser("and test");
    return aat;
  }


  @Override
  public void prettyPrint() {
    int i = 0;
    for(AspNotTest ant: antl) {
      if(i > 0) {
        prettyWrite(" and ");
      }
      ant.prettyPrint();
      i++;
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = null;
    for(AspNotTest ant: antl) {
      temp = ant.eval(curScope);
      if(!temp.getBoolValue(temp.showInfo(), this)) {
        return temp;
      }
    }
    return temp;
  }
}
