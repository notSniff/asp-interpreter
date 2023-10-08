package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspArguments extends AspPrimarySuffix {
  ArrayList<AspExpr> ael = new ArrayList<>();

  AspArguments(int n) {
    super(n);
  }

  public static AspArguments parse(Scanner s) {
    enterParser("arguments");

    AspArguments aa = new AspArguments(s.curLineNum());
    skip(s, leftParToken);
    if(s.curToken().kind != rightParToken) {
      aa.ael.add(AspExpr.parse(s));
    }
    while(s.curToken().kind != rightParToken) {
      skip(s, commaToken);
      aa.ael.add(AspExpr.parse(s));
    }
    skip(s, rightParToken);

    leaveParser("arguments");
    return aa;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("(");
    if(ael.size() > 0) {
      ael.get(0).prettyPrint();
    }
    for(int i = 1; i < ael.size(); i++){
      prettyWrite(", ");
      ael.get(i).prettyPrint();
    }
    prettyWrite(")");
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    ArrayList<RuntimeValue> paramList = new ArrayList<RuntimeValue>();
    for(AspExpr ae: ael) {
      RuntimeValue temp = ae.eval(curScope);
      paramList.add(temp);
    }
    RuntimeListValue tempRList = new RuntimeListValue(paramList);
    return tempRList;
  }
}
