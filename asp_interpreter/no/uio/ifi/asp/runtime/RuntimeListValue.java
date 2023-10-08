package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeListValue extends RuntimeValue {
  ArrayList<RuntimeValue> listValue = new ArrayList<RuntimeValue>();

  public RuntimeListValue(ArrayList<RuntimeValue> v) {
    for(RuntimeValue temp: v) {
      listValue.add(temp);
    }
  }

  @Override
  protected String typeName() {
    return "list";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    String temp = "[";
    if(listValue.size() > 0) {
      temp += listValue.get(0).showInfo();
      for(int i = 1; i < listValue.size(); i++) {
        temp += ", ";
        temp += listValue.get(i).showInfo();
      }
    }
    temp += "]";
    return temp;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(listValue.size() == 0) {
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<RuntimeValue> getListValue(String what, AspSyntax where) {
    return listValue;
  }


  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      ArrayList<RuntimeValue> temp = new ArrayList<RuntimeValue>();
      for(int i = 0; i < v.getIntValue("* operand", where); i++) {
        for(RuntimeValue x: listValue) {
          temp.add(x);
        }
      }
      return new RuntimeListValue(temp);
    }
    else {
      runtimeError("Type error for *", where);
      return null;
    }
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
    if(v instanceof RuntimeIntValue) {
      long tempInt = v.getIntValue("[...] Subscription", where);
      if(tempInt < 0 || tempInt >= listValue.size()) {
        runtimeError("Index out of bounds: " + tempInt, where);
        return null;
      }
      else {
        int i = (int) tempInt;
        return listValue.get(i);
      }
    }
    else {
      runtimeError("Type error for [...] Subscription", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    if(listValue.size() == 0) {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }

  @Override
  public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
    if(inx instanceof RuntimeIntValue) {
      long tempInt = inx.getIntValue("[...] AssignElem", where);
      if(tempInt < 0 || tempInt >= listValue.size()) {
        runtimeError("Index out of bounds: " + tempInt, where);
      }
      else {
        int i = (int) tempInt;
        listValue.set(i, val);
      }
    }
    else {
      runtimeError("Type error for [...] AssignElem", where);
    }
  }

  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(listValue.size());
  }

}
