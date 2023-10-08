package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeIntValue extends RuntimeValue {
  long intValue;

  public RuntimeIntValue(long v) {
    intValue = v;
  }

  @Override
  protected String typeName() {
    return "int";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    return String.valueOf(intValue);
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(intValue == 0) {
      return false;
    }
    return true;
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    double temp = intValue;
    return temp;
  }

  @Override
  public long getIntValue(String what, AspSyntax where) {
    return intValue;
  }



  @Override
  public RuntimeValue evalPositive(AspSyntax where) {
    return new RuntimeIntValue(intValue);
  }

  @Override
  public RuntimeValue evalNegate(AspSyntax where) {
    return new RuntimeIntValue(intValue*(-1));
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      long temp = intValue + v.getIntValue("+ operand", where);
      return new RuntimeIntValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = intValue + v.getFloatValue("+ operand", where);
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for +", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      long temp = intValue - v.getIntValue("- operand", where);
      return new RuntimeIntValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = intValue - v.getFloatValue("- operand", where);
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for -", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      long temp = intValue * v.getIntValue("* operand", where);
      return new RuntimeIntValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = intValue * v.getFloatValue("* operand", where);
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for *", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      double t = intValue;
      double temp = t / v.getIntValue("/ operand", where);
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = intValue / v.getFloatValue("/ operand", where);
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for /", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      long temp = Math.floorDiv(intValue, v.getIntValue("// operand", where));
      return new RuntimeIntValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = Math.floor(intValue / v.getFloatValue("// operand", where));
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for //", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      long v2 = v.getIntValue("% operand", where);
      long temp = Math.floorMod(intValue, v2);
      return new RuntimeIntValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double v2 = v.getFloatValue("% operand", where);
      double temp = (intValue - v2 * Math.floor(intValue / v2));
      return new RuntimeFloatValue(temp);
    }
    else {
      runtimeError("Type error for %", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue == v.getIntValue("== operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t == v.getFloatValue("== operand", where)) {
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
    if(v instanceof RuntimeIntValue) {
      if(intValue != v.getIntValue("!= operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t != v.getFloatValue("!= operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeNoneValue) {
      return new RuntimeBoolValue(true);
    }
    else {
      runtimeError("Type error for !=", where);
      return null;
    }
  }

  @Override
  public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      if(intValue < v.getIntValue("< operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t < v.getFloatValue("< operand", where)) {
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
    if(v instanceof RuntimeIntValue) {
      if(intValue <= v.getIntValue("<= operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t <= v.getFloatValue("<= operand", where)) {
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
    if(v instanceof RuntimeIntValue) {
      if(intValue > v.getIntValue("> operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t > v.getFloatValue("> operand", where)) {
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
    if(v instanceof RuntimeIntValue) {
      if(intValue >= v.getIntValue(">= operand", where)) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      double t = intValue;
      if(t >= v.getFloatValue(">= operand", where)) {
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
    if(intValue == 0) {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }
}
