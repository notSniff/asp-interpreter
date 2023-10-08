package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspFuncDef extends AspCompoundStmt {
  ArrayList<AspName> anl = new ArrayList<>();
  public ArrayList<AspSuite> asl = new ArrayList<>();

  public AspFuncDef(int n) {
    super(n);
  }

  public static AspFuncDef parse(Scanner s) {
    enterParser("func def");

    AspFuncDef afd = new AspFuncDef(s.curLineNum());
    skip(s, defToken);
    afd.anl.add(AspName.parse(s));
    skip(s, leftParToken);
    while(s.curToken().kind != rightParToken) {
      afd.anl.add(AspName.parse(s));
      if(s.curToken().kind == commaToken) {
        skip(s, commaToken);
      }
    }
    skip(s, rightParToken);
    skip(s, colonToken);
    afd.asl.add(AspSuite.parse(s));

    leaveParser("func def");
    return afd;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("def ");
    anl.get(0).prettyPrint();
    prettyWrite("(");
    if(anl.size() > 1) {
      anl.get(1).prettyPrint();
    }
    for(int i = 2; i < anl.size(); i++) {
      prettyWrite(", ");
      anl.get(i).prettyPrint();
    }
    prettyWrite("):");
    asl.get(0).prettyPrint();
    prettyWriteLn();
  }

  //Setter navn til en RuntimeFunc i scope.
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    String n = anl.get(0).nt.get(0).name;
    ArrayList<String> temp = new ArrayList<String>();
    for(int i = 1; i < anl.size(); i++) {
      temp.add(anl.get(i).nt.get(0).name);
    }
    trace("def "+n);
    RuntimeFunc tempFunk = new RuntimeFunc(n, this, curScope, temp);
    curScope.assign(n, tempFunk);

    return tempFunk;
  }
}
