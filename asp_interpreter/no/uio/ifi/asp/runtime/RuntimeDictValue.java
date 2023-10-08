package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeDictValue extends RuntimeValue {
  ArrayList<RuntimeValue> dictKey = new ArrayList<RuntimeValue>();
  ArrayList<RuntimeValue> dictValue = new ArrayList<RuntimeValue>();

  public RuntimeDictValue(ArrayList<RuntimeValue> k, ArrayList<RuntimeValue> v) {
    dictKey = k;
    dictValue = v;
  }

  @Override
  protected String typeName() {
    return "dict";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    String temp = "{";
    if(dictKey.size() > 0) {
      temp += dictKey.get(0).showInfo();
      temp += ": ";
      temp += dictValue.get(0).showInfo();
      for(int i = 1; i < dictKey.size(); i++) {
        temp += ", ";
        temp += dictKey.get(i).showInfo();
        temp += ": ";
        temp += dictValue.get(i).showInfo();
      }
    }
    temp += "}";
    return temp;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(dictKey.size() == 0) {
      return false;
    }
    return true;
  }

  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for ==", where);
      return null;
    }
  }

  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(true);
    }
    else {
      runtimeError("Type error for !=", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      String tempString = v.getStringValue("{...} Subscription", where);
      for(int i = 0; i < dictKey.size(); i++) {
        if(dictKey.get(i).getStringValue("", where).equals(v.getStringValue("", where))) {
          return dictValue.get(i);
        }
      }
      runtimeError("Key: "+v.showInfo()+" not found in Dict Display", where);
      return null;
    }
    else {
      runtimeError("Type error for {...} Subscription", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    if(dictKey.size() == 0) {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
    if(inx instanceof RuntimeStringValue) {
      String tempString = inx.getStringValue("{...} AssignElem", where);
      for(int i = 0; i < dictKey.size(); i++) {
        if(dictKey.get(i).getStringValue("", where).equals(inx.getStringValue("", where))) {
          dictValue.set(i, val);
          return;
        }
      }
      dictKey.add(inx);
      dictValue.add(val);
    }
    else {
      runtimeError("Type error for {...} AssignElem", where);
    }
  }

  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(dictKey.size());
  }
}
