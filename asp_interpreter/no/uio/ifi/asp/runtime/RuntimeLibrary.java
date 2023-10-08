package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {

      //exit
      assign("exit", new RuntimeFunc("exit") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "exit", where);
          if(actualParams.get(0) instanceof RuntimeIntValue) {
            System.exit((int) (actualParams.get(0).getIntValue("exit", where)));
            return null;
          }
          runtimeError("Type error for exit", where);
          return null;
        }
      });

      //float
      assign("float", new RuntimeFunc("float") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "float", where);
          if(actualParams.get(0) instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(actualParams.get(0).getFloatValue("float function", where));
          }
          else if(actualParams.get(0) instanceof RuntimeIntValue) {
            return new RuntimeFloatValue((float) (actualParams.get(0).getIntValue("float function", where)));
          }
          else if(actualParams.get(0) instanceof RuntimeStringValue) {
            String tempString = actualParams.get(0).getStringValue("float function", where);
            for(int i = 0; i < tempString.length(); i++) {
              char c = tempString.charAt(i);
              if(('0'>c || c>'9') && c!='.') {
                runtimeError("String in float function can only contain numbers and '.'", where);
              }
            }
            if(tempString.equals("")) {
              runtimeError("String in float function can only contain numbers and '.'", where);
            }
            float f = Float.parseFloat(tempString);
            return new RuntimeFloatValue(f);
          }
          else {
            runtimeError("Type error for float function", where);
            return null;
          }
        }
      });

      //input
      assign("input", new RuntimeFunc("input") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "input", where);
          if(actualParams.get(0) instanceof RuntimeStringValue) {
            System.out.print(actualParams.get(0).getStringValue("input", where));
            String userInput = keyboard.nextLine();
            if(userInput.isEmpty()) {
              return new RuntimeStringValue("");
            }
            return new RuntimeStringValue(userInput);
          }
          runtimeError("Type error for input", where);
          return null;
        }
      });

      //int
      assign("int", new RuntimeFunc("int") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "int", where);
          if(actualParams.get(0) instanceof RuntimeFloatValue) {
            return new RuntimeIntValue((long) (actualParams.get(0).getFloatValue("int function", where)));
          }
          else if(actualParams.get(0) instanceof RuntimeIntValue) {
            return new RuntimeIntValue(actualParams.get(0).getIntValue("int function", where));
          }
          else if(actualParams.get(0) instanceof RuntimeStringValue) {
            String tempString = actualParams.get(0).getStringValue("int function", where);
            for(int i = 0; i < tempString.length(); i++) {
              char c = tempString.charAt(i);
              if('0'>c || c>'9') {
                runtimeError("String in int function can only contain numbers", where);
              }
            }
            if(tempString.equals("")) {
              runtimeError("String in int function can only contain numbers", where);
            }
            long l = Long.parseLong(tempString);
            return new RuntimeIntValue(l);
          }
          else {
            runtimeError("Type error for int function", where);
            return null;
          }
        }
      });

      //len
      assign("len", new RuntimeFunc("len") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "len", where);
          return actualParams.get(0).evalLen(where);
        }
      });

      //print
      assign("print", new RuntimeFunc("print") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          for(RuntimeValue x: actualParams) {
            if(x instanceof RuntimeStringValue) {
              System.out.print(x.stringPrint("print", where));
              System.out.print(" ");
            }
            else{
              System.out.print(x.showInfo());
              System.out.print(" ");
            }
          }
          System.out.println();
          return new RuntimeNoneValue();
        }
      });

      //range
      assign("range", new RuntimeFunc("range") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 2, "range", where);
          if(actualParams.get(0) instanceof RuntimeIntValue && actualParams.get(1) instanceof RuntimeIntValue) {
            long i = actualParams.get(0).getIntValue("range", where);
            long j = actualParams.get(1).getIntValue("range", where);
            ArrayList<RuntimeValue> temp = new ArrayList<RuntimeValue>();
            if(i == j) {
              return new RuntimeListValue(temp);
            }
            if(i < j) {
              while(i < j) {
                temp.add(new RuntimeIntValue(i));
                i++;
              }
              return new RuntimeListValue(temp);
            }
            runtimeError("out of bounds for range", where);
            return null;
          }
          runtimeError("Type error for range", where);
          return null;
        }
      });

      //str
      assign("str", new RuntimeFunc("str") {
        @Override
        public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
          checkNumParams(actualParams, 1, "str", where);
          return new RuntimeStringValue(actualParams.get(0).showInfo());
        }
      });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs,
				int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
