package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspWhileStmt extends AspCompoundStmt {
  ArrayList<AspExpr> ael = new ArrayList<>();
  ArrayList<AspSuite> asl = new ArrayList<>();

  AspWhileStmt(int n) {
    super(n);
  }

  public static AspWhileStmt parse(Scanner s) {
    enterParser("while stmt");

    AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
    skip(s, whileToken);
    aws.ael.add(AspExpr.parse(s));
    skip(s, colonToken);
    aws.asl.add(AspSuite.parse(s));

    leaveParser("while stmt");
    return aws;
  }


  @Override
  public void prettyPrint() {
    prettyWrite("while ");
    ael.get(0).prettyPrint();
    prettyWrite(":");
    asl.get(0).prettyPrint();
  }

  //while
  @Override
  public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
    while(true) {
      RuntimeValue temp = ael.get(0).eval(curScope);
      if(!temp.getBoolValue("while loop test", this)) {
        break;
      }
      trace("while True:");
      asl.get(0).eval(curScope);
    }
    trace("while False:");
    return null;
  }
}
