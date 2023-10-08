package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFactor extends AspSyntax {
  ArrayList<AspFactorPrefix> afpl = new ArrayList<>();
  ArrayList<AspFactorOpr> afol = new ArrayList<>();
  ArrayList<AspPrimary> apl = new ArrayList<>();
  //prefixPos sier hvilken prefix som hoorer til hvilken primary
  ArrayList<Integer> prefixPos = new ArrayList<>();

  AspFactor(int n) {
    super(n);
  }

  public static AspFactor parse(Scanner s) {
    enterParser("factor");

    AspFactor af = new AspFactor(s.curLineNum());
    int posNr = 0;
    while(true) {
      if(s.isFactorPrefix()) {
        af.afpl.add(AspFactorPrefix.parse(s));
        af.prefixPos.add(posNr);
      }
      af.apl.add(AspPrimary.parse(s));
      posNr++;
      if(!s.isFactorOpr()) {
        break;
      }
      af.afol.add(AspFactorOpr.parse(s));
    }

    leaveParser("factor");
    return af;
  }


  @Override
  public void prettyPrint() {
    int posNr = 0;
    int prefixNr = 0;
    for(AspPrimary ap: apl) {
      if(afpl.size() >= prefixNr+1) {
        if(prefixPos.get(prefixNr) == posNr) {
          afpl.get(prefixNr).prettyPrint();
          prefixNr++;
        }
      }
      ap.prettyPrint();
      if(afol.size() >= posNr+1) {
        afol.get(posNr).prettyPrint();
      }
      posNr++;
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    //binder prefix til primary fosrt saa binder den med operatorene.
    ArrayList<RuntimeValue> primerysAndPrefix = new ArrayList<>();
    RuntimeValue temp = null;

    int posNr = 0;
    int prefixNr = 0;
    for(AspPrimary ap: apl) {
      temp = ap.eval(curScope);
      if(afpl.size() >= prefixNr+1) {
        if(prefixPos.get(prefixNr) == posNr) {
          TokenKind fPrefix = afpl.get(prefixNr).getToken().kind;
          switch(fPrefix) {
            case minusToken:
              temp = temp.evalNegate(this);
              break;
            case plusToken:
              temp = temp.evalPositive(this);
              break;
            default:
              Main.panic("Illegal factor prefix: " + fPrefix + "!");
          }
          prefixNr++;
        }
      }
      primerysAndPrefix.add(temp);
      posNr++;
    }

    temp = primerysAndPrefix.get(0);
    for(int i = 1; i < primerysAndPrefix.size(); i++) {
      TokenKind fOpr = afol.get(i-1).getToken().kind;
      switch(fOpr) {
        case astToken:
          temp = temp.evalMultiply(primerysAndPrefix.get(i), this);
          break;
        case slashToken:
          temp = temp.evalDivide(primerysAndPrefix.get(i), this);
          break;
        case percentToken:
          temp = temp.evalModulo(primerysAndPrefix.get(i), this);
          break;
        case doubleSlashToken:
          temp = temp.evalIntDivide(primerysAndPrefix.get(i), this);
          break;
        default:
          Main.panic("Illegal factor operator: " + fOpr + "!");
      }
    }
    return temp;
  }
}
