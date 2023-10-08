package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeStringValue extends RuntimeValue {
  String stringValue;

  public RuntimeStringValue(String v) {
    stringValue = v;
  }

  @Override
  protected String typeName() {
    return "string";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    String temp = "'";
    temp += stringValue;
    temp += "'";
    return temp;
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(stringValue == "") {
      return false;
    }
    return true;
  }

  @Override
  public String getStringValue(String what, AspSyntax where) {
    return stringValue;
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      String temp = stringValue + v.getStringValue("+ operand", where);
      return new RuntimeStringValue(temp);
    }
    else {
      runtimeError("Type error for +", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      String temp = "";
      for(int i = 0; i < v.getIntValue("* operand", where); i++) {
        temp += stringValue;
      }
      return new RuntimeStringValue(temp);
    }
    else {
      runtimeError("Type error for *", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      if(stringValue.equals(v.getStringValue("== operand", where))) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for ==", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      if(stringValue.equals(v.getStringValue("!= operand", where))) {
        return new RuntimeBoolValue(false);
      }
      return new RuntimeBoolValue(true);
    }
    else if(v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(false);
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
      if(tempInt < 0 || tempInt >= stringValue.length()) {
        runtimeError("Index out of bounds: " + tempInt, where);
        return null;
      }
      else {
        int i = (int) tempInt;
        String s = String.valueOf(stringValue.charAt(i));
        return new RuntimeStringValue(s);
      }
    }
    else {
      runtimeError("Type error for [...] Subscription", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      int temp = stringValue.compareTo(v.getStringValue("< operand", where));
      if(temp < 0) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for <", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      int temp = stringValue.compareTo(v.getStringValue("<= operand", where));
      if(temp <= 0) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for <=", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      int temp = stringValue.compareTo(v.getStringValue("> operand", where));
      if(temp > 0) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for >", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeStringValue) {
      int temp = stringValue.compareTo(v.getStringValue(">= operand", where));
      if(temp >= 0) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else {
      runtimeError("Type error for >=", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalNot(AspSyntax where) {
    if(stringValue.equals("")) {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }

  @Override
  public RuntimeValue evalLen(AspSyntax where) {
    return new RuntimeIntValue(stringValue.length());
  }

  //for aa printe strings uten ''
  public String stringPrint(String what, AspSyntax where) {
    return stringValue;
  }
}
