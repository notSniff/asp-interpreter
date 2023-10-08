package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspPrimary extends AspSyntax {
  ArrayList<AspAtom> aal = new ArrayList<>();
  ArrayList<AspPrimarySuffix> apsl = new ArrayList<>();

  AspPrimary(int n) {
    super(n);
  }

  public static AspPrimary parse(Scanner s) {
    enterParser("primary");

    AspPrimary ap = new AspPrimary(s.curLineNum());
    ap.aal.add(AspAtom.parse(s));
    while(true) {
      if(s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
        ap.apsl.add(AspPrimarySuffix.parse(s));
      }
      else {
        break;
      }
    }

    leaveParser("primary");
    return ap;
  }


  @Override
  public void prettyPrint() {
    aal.get(0).prettyPrint();
    for(AspPrimarySuffix aps: apsl) {
      aps.prettyPrint();
    }
  }

  //Hvis atom peker paa en funsjon kalles evalFuncCall ellers evalSubscription
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeValue temp = aal.get(0).eval(curScope);
    if(temp instanceof RuntimeFunc) {
      RuntimeValue tempReturn = null;
      ArrayList<RuntimeValue> aParams = new ArrayList<RuntimeValue>();
      RuntimeValue tempListValue = apsl.get(0).eval(curScope);
      aParams = tempListValue.getListValue("Run function", this);
      trace("Call function " + temp.showInfo() + " with params "+ tempListValue.showInfo());
      tempReturn = temp.evalFuncCall(aParams, this);
      return tempReturn;
    }
    else {
      for(int i = 0; i < apsl.size(); i++) {
        temp = temp.evalSubscription(apsl.get(i).eval(curScope), this);
      }
      return temp;
    }
  }
}
