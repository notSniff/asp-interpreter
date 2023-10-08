package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;
import no.uio.ifi.asp.parser.AspFuncDef;

public class RuntimeFunc extends RuntimeValue {
  String name;
  AspFuncDef def;
  RuntimeScope defScope;
  ArrayList<String> fParams;

  public RuntimeFunc(String s, AspFuncDef d, RuntimeScope ds, ArrayList<String> fp) {
    name = s;
    def = d;
    defScope = ds;
    fParams = fp;
  }

  public RuntimeFunc(String s) {
    name = s;
  }

  @Override
  protected String typeName() {
    return "Function";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    return name;
  }

  //Lager nytt scope og caller paa suite for aa kjoere kodene i en funsjon.
  public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
    if(fParams.size() == actualParams.size()) {
      RuntimeScope newScope = new RuntimeScope(defScope);
      for(int i = 0; i < fParams.size(); i++) {
        newScope.assign(fParams.get(i), actualParams.get(i));
      }
      try {
        def.asl.get(0).eval(newScope);
      } catch(RuntimeReturnValue rrv) {
        return rrv.value;
      }
      return new RuntimeNoneValue();
    }
    runtimeError("Expected "+fParams.size()+"but found "+actualParams.size(), where);
    return null;
  }
}
