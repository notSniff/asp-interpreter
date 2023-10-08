package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspListDisplay extends AspAtom {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspListDisplay(int n) {
    super(n);
  }

  public static AspListDisplay parse(Scanner s) {
    enterParser("list display");

    AspListDisplay ald = new AspListDisplay(s.curLineNum());
    skip(s, leftBracketToken);
    if(s.curToken().kind != rightBracketToken) {
      ald.ael.add(AspExpr.parse(s));
    }
    while(s.curToken().kind != rightBracketToken) {
      skip(s, commaToken);
      ald.ael.add(AspExpr.parse(s));
    }
    skip(s, rightBracketToken);

    leaveParser("list display");
    return ald;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("[");
    if(ael.size() > 0) {
      ael.get(0).prettyPrint();
    }
    for(int i = 1; i < ael.size(); i++){
      prettyWrite(", ");
      ael.get(i).prettyPrint();
    }
    prettyWrite("]");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    ArrayList<RuntimeValue> tempList = new ArrayList<RuntimeValue>();
    for(AspExpr x: ael) {
      tempList.add(x.eval(curScope));
    }
    RuntimeListValue temp = new RuntimeListValue(tempList);
    return temp;
  }
}
