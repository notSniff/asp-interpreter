package no.uio.ifi.asp.runtime;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeFloatValue extends RuntimeValue {
  double floatValue;

  public RuntimeFloatValue(double v) {
    floatValue = v;
  }

  @Override
  protected String typeName() {
    return "float";
  }

  @Override
  protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
    return String.valueOf(floatValue);
  }

  @Override
  public boolean getBoolValue(String what, AspSyntax where) {
    if(floatValue == 0.0) {
      return false;
    }
    return true;
  }

  @Override
  public double getFloatValue(String what, AspSyntax where) {
    return floatValue;
  }

  @Override
  public RuntimeValue evalPositive(AspSyntax where) {
    return new RuntimeFloatValue(floatValue);
  }

  @Override
  public RuntimeValue evalNegate(AspSyntax where) {
    return new RuntimeFloatValue(floatValue*(-1));
  }

  @Override
  public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
    if(v instanceof RuntimeIntValue) {
      double temp = floatValue + v.getIntValue("+ operand", where);
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = floatValue + v.getFloatValue("+ operand", where);
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
      double temp = floatValue - v.getIntValue("- operand", where);
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = floatValue - v.getFloatValue("- operand", where);
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
      double temp = floatValue * v.getIntValue("* operand", where);
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = floatValue * v.getFloatValue("* operand", where);
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
      double temp = floatValue / v.getIntValue("/ operand", where);
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = floatValue / v.getFloatValue("/ operand", where);
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
      double temp = Math.floor(floatValue / v.getIntValue("// operand", where));
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double temp = Math.floor(floatValue / v.getFloatValue("// operand", where));
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
      double temp = (floatValue - v2 * Math.floor(floatValue / v2));
      return new RuntimeFloatValue(temp);
    }
    else if(v instanceof RuntimeFloatValue) {
      double v2 = v.getFloatValue("% operand", where);
      double temp = (floatValue - v2 * Math.floor(floatValue / v2));
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
      double t = v.getIntValue("== operand", where);
      if(floatValue == t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue == v.getFloatValue("== operand", where)) {
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
      double t = v.getIntValue("!= operand", where);
      if(floatValue != t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue != v.getFloatValue("!= operand", where)) {
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
      double t = v.getIntValue("< operand", where);
      if(floatValue < t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue < v.getFloatValue("< operand", where)) {
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
      double t = v.getIntValue("<= operand", where);
      if(floatValue <= t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue <= v.getFloatValue("<= operand", where)) {
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
      double t = v.getIntValue("> operand", where);
      if(floatValue > t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue > v.getFloatValue("> operand", where)) {
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
      double t = v.getIntValue(">= operand", where);
      if(floatValue >= t) {
        return new RuntimeBoolValue(true);
      }
      return new RuntimeBoolValue(false);
    }
    else if(v instanceof RuntimeFloatValue) {
      if(floatValue >= v.getFloatValue(">= operand", where)) {
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
    if(floatValue == 0.0) {
      return new RuntimeBoolValue(true);
    }
    return new RuntimeBoolValue(false);
  }
}
