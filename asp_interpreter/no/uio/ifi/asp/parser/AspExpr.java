package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspExpr extends AspSyntax {
    ArrayList<AspAndTest> andTests = new ArrayList<>();

    AspExpr(int n) {
	super(n);
    }


    public static AspExpr parse(Scanner s) {
	enterParser("expr");

  AspExpr ae = new AspExpr(s.curLineNum());
  while(true) {
    ae.andTests.add(AspAndTest.parse(s));
    if(s.curToken().kind != orToken){
      break;
    }
    skip(s, orToken);
  }

	leaveParser("expr");
	return ae;
    }


    @Override
    public void prettyPrint() {
      int i = 1;
      for(AspAndTest aat: andTests) {
        aat.prettyPrint();
        if(i < andTests.size()) {
          prettyWrite(" or ");
        }
        i++;
      }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
      RuntimeValue temp = null;
      for(AspAndTest aat: andTests) {
        temp = aat.eval(curScope);
        if(temp.getBoolValue(temp.showInfo(), this)) {
          return temp;
        }
      }
      return temp;
    }
}
