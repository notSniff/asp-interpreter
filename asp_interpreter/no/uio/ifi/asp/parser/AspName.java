package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspName extends AspAtom {
  //Legger nametoken i AspName
  //om name er lovlig blir sjekket i scanner
  ArrayList<Token> nt = new ArrayList<>();

  AspName(int n) {
    super(n);
  }

  public static AspName parse(Scanner s) {
    enterParser("name");

    AspName an = new AspName(s.curLineNum());
    an.nt.add(s.curToken());
    skip(s, nameToken);

    leaveParser("name");
    return an;
  }


  @Override
  public void prettyPrint() {
    prettyWrite(nt.get(0).getValue());
  }


  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    String id = nt.get(0).name;
    return curScope.find(id, this);
  }
}
