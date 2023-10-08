package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspBooleanLiteral extends AspAtom {
  ArrayList<Token> blToken = new ArrayList<>();

  AspBooleanLiteral(int n) {
    super(n);
  }

  public static AspBooleanLiteral parse(Scanner s) {
    enterParser("boolean literal");

    AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
    abl.blToken.add(s.curToken());
    if(s.curToken().kind == trueToken) {
      skip(s, trueToken);
    }
    else{
      skip(s, falseToken);
    }

    leaveParser("boolean literal");
    return abl;
  }


  @Override
  public void prettyPrint() {
    switch(blToken.get(0).kind) {
      case trueToken:
        prettyWrite("True");
        break;
      case falseToken:
        prettyWrite("False");
        break;
    }
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    RuntimeBoolValue temp = null;
    switch(blToken.get(0).kind){
      case trueToken:
        temp = new RuntimeBoolValue(true);
        break;
      case falseToken:
        temp = new RuntimeBoolValue(false);
        break;
      default:
        Main.panic("Illegal boolean value: " + blToken.get(0) + "!");
    }
    return temp;
  }
}
