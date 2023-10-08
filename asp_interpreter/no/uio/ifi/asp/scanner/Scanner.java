package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;


    public Scanner(String fileName) {
	curFileName = fileName;
	indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
	String m = "Asp scanner error";
	if (curLineNum() > 0)
	    m += " on line " + curLineNum();
	m += ": " + message;

	Main.error(m);
    }


    public Token curToken() {
	while (curLineTokens.isEmpty()) {
	    readNextLine();
	}
	return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }


    private void readNextLine() {
      curLineTokens.clear();

      // Read the next line:
      String line = null;
      try {
        line = sourceFile.readLine();
        if (line == null) {
          sourceFile.close();
          sourceFile = null;
        } else {
          Main.log.noteSourceLine(curLineNum(), line);
        }
      } catch (IOException e) {
        sourceFile = null;
        scannerError("Unspecified I/O error!");
      }

      /**
      * Deler en linje fra en fil opp i tokens.
      */
      if(sourceFile == null){
        while(indents.peek() > 0){
          indents.pop();
          curLineTokens.add(new Token(dedentToken, curLineNum()));
        }
        curLineTokens.add(new Token(eofToken));
      } else {
        String lineV2 = expandLeadingTabs(line);
        int tempIndents = findIndent(lineV2);
        if(tempIndents == lineV2.length() || lineV2.charAt(tempIndents) == '#'){
          return;
        }

        if(tempIndents > indents.peek()){
          indents.push(tempIndents);
          curLineTokens.add(new Token(indentToken, curLineNum()));
        }
        while(tempIndents < indents.peek()){
          indents.pop();
          curLineTokens.add(new Token(dedentToken, curLineNum()));
        }

        if(tempIndents != indents.peek()){
          scannerError("Indentation error");
        }

        //Test for alle typer tokens.
        String s = "'";
        String tempToken = "";
        boolean isFloat = false;
        for(int i = indents.peek(); i < lineV2.length(); i++){
          tempToken += lineV2.charAt(i);

          if(tempToken.equals(";")){
            curLineTokens.add(new Token(semicolonToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("=")){
            if(i != lineV2.length()-1){
              if(lineV2.charAt(i+1) == '='){
                curLineTokens.add(new Token(doubleEqualToken, curLineNum()));
                tempToken = "";
                i++;
              }
              else{
                curLineTokens.add(new Token(equalToken, curLineNum()));
                tempToken = "";
              }
            }
            else{
              curLineTokens.add(new Token(equalToken, curLineNum()));
              tempToken = "";
            }
          }
          else if(tempToken.equals(":")){
            curLineTokens.add(new Token(colonToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("*")){
            curLineTokens.add(new Token(astToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("/")){
            if(i != lineV2.length()-1){
              if(lineV2.charAt(i+1) == '/'){
                curLineTokens.add(new Token(doubleSlashToken, curLineNum()));
                tempToken = "";
                i++;
              }
              else{
                curLineTokens.add(new Token(slashToken, curLineNum()));
                tempToken = "";
              }
            }
            else{
              curLineTokens.add(new Token(slashToken, curLineNum()));
              tempToken = "";
            }
          }
          else if(tempToken.equals(">")){
            if(i != lineV2.length()-1){
              if(lineV2.charAt(i+1) == '='){
                curLineTokens.add(new Token(greaterEqualToken, curLineNum()));
                tempToken = "";
                i++;
              }
              else{
                curLineTokens.add(new Token(greaterToken, curLineNum()));
                tempToken = "";
              }
            }
            else{
              curLineTokens.add(new Token(greaterToken, curLineNum()));
              tempToken = "";
            }
          }
          else if(tempToken.equals("<")){
            if(i != lineV2.length()-1){
              if(lineV2.charAt(i+1) == '='){
                curLineTokens.add(new Token(lessEqualToken, curLineNum()));
                tempToken = "";
                i++;
              }
              else{
                curLineTokens.add(new Token(lessToken, curLineNum()));
                tempToken = "";
              }
            }
            else{
              curLineTokens.add(new Token(lessToken, curLineNum()));
              tempToken = "";
            }
          }
          else if(tempToken.equals("-")){
            curLineTokens.add(new Token(minusToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("!")){
            if(i != lineV2.length()-1){
              if(lineV2.charAt(i+1) == '='){
                curLineTokens.add(new Token(notEqualToken, curLineNum()));
                tempToken = "";
                i++;
              }
            }
          }
          else if(tempToken.equals("%")){
            curLineTokens.add(new Token(percentToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("+")){
            curLineTokens.add(new Token(plusToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals(",")){
            curLineTokens.add(new Token(commaToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("{")){
            curLineTokens.add(new Token(leftBraceToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("}")){
            curLineTokens.add(new Token(rightBraceToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("(")){
            curLineTokens.add(new Token(leftParToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals(")")){
            curLineTokens.add(new Token(rightParToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("[")){
            curLineTokens.add(new Token(leftBracketToken, curLineNum()));
            tempToken = "";
          }
          else if(tempToken.equals("]")){
            curLineTokens.add(new Token(rightBracketToken, curLineNum()));
            tempToken = "";
          }
          //integer and float
          else if(isDigit(lineV2.charAt(i))){
            if(tempToken.equals("0")){
              if(i == lineV2.length()-1){
                long l = Long.parseLong(tempToken);
                curLineTokens.add(new Token(integerToken, curLineNum(), l));
                tempToken = "";
              }
              else if(lineV2.charAt(i+1) != '.'){
                long l = Long.parseLong(tempToken);
                curLineTokens.add(new Token(integerToken, curLineNum(), l));
                tempToken = "";
              }
            }
            else if(i != lineV2.length()-1){
              if(!isDigit(lineV2.charAt(i+1)) && lineV2.charAt(i+1) != '.'){
                if(isFloat){
                  double d = Double.parseDouble(tempToken);
                  curLineTokens.add(new Token(floatToken, curLineNum(), d));
                  tempToken = "";
                  isFloat = false;
                }
                else{
                  long l = Long.parseLong(tempToken);
                  curLineTokens.add(new Token(integerToken, curLineNum(), l));
                  tempToken = "";
                }
              }
            }
            else{
              if(isFloat){
                double d = Double.parseDouble(tempToken);
                curLineTokens.add(new Token(floatToken, curLineNum(), d));
                tempToken = "";
                isFloat = false;
              }
              else{
                long l = Long.parseLong(tempToken);
                curLineTokens.add(new Token(integerToken, curLineNum(), l));
                tempToken = "";
              }
            }
          }
          else if(lineV2.charAt(i) == '.'){
            if(isFloat){
              scannerError("Illegal float | " + tempToken);
            }
            else{
              if(i == indents.peek()){
                scannerError("Illegal float | " + tempToken);
              }
              else if(i != lineV2.length()-1){
                if(isDigit(lineV2.charAt(i-1)) && isDigit(lineV2.charAt(i+1))){
                  isFloat = true;
                }
                else{
                  scannerError("Illegal float | " + tempToken);
                }
              }
              else{
                scannerError("Illegal float | " + tempToken);
              }
            }
          }
          //name tokens
          else if(isLetterAZ(lineV2.charAt(i))){
            i++;
            while(i < lineV2.length()){
              if(isLetterAZ(lineV2.charAt(i)) || isDigit(lineV2.charAt(i))){
                tempToken += lineV2.charAt(i);
                i++;
              }
              else{
                break;
              }
            }
            i--;

            if(tempToken.equals("and")){
              curLineTokens.add(new Token(andToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("def")){
              curLineTokens.add(new Token(defToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("elif")){
              curLineTokens.add(new Token(elifToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("else")){
              curLineTokens.add(new Token(elseToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("False")){
              curLineTokens.add(new Token(falseToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("for")){
              curLineTokens.add(new Token(forToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("if")){
              curLineTokens.add(new Token(ifToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("in")){
              curLineTokens.add(new Token(inToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("None")){
              curLineTokens.add(new Token(noneToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("not")){
              curLineTokens.add(new Token(notToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("or")){
              curLineTokens.add(new Token(orToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("pass")){
              curLineTokens.add(new Token(passToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("return")){
              curLineTokens.add(new Token(returnToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("True")){
              curLineTokens.add(new Token(trueToken, curLineNum()));
              tempToken = "";
            }
            else if(tempToken.equals("while")){
              curLineTokens.add(new Token(whileToken, curLineNum()));
              tempToken = "";
            }
            else{
              curLineTokens.add(new Token(nameToken, curLineNum(), tempToken));
              tempToken = "";
            }
          }
          //String token with ""
          else if(lineV2.charAt(i) == '"'){
            i++;
            while(i < lineV2.length()){
              if(lineV2.charAt(i) != '"'){
                tempToken += lineV2.charAt(i);
                i++;
              }
              else{
                tempToken += lineV2.charAt(i);
                i++;
                break;
              }
            }
            i--;
            if(lineV2.charAt(i) != '"'){
              scannerError("Missing String end | " + tempToken);
            }
            String temp2 = tempToken.substring(1,tempToken.length()-1);
            curLineTokens.add(new Token(stringToken, temp2, curLineNum()));
            tempToken = "";
          }
          //String token with ''
          else if(lineV2.charAt(i) == s.charAt(0)){
            i++;
            while(i < lineV2.length()){
              if(lineV2.charAt(i) != s.charAt(0)){
                tempToken += lineV2.charAt(i);
                i++;
              }
              else{
                tempToken += lineV2.charAt(i);
                i++;
                break;
              }
            }
            i--;
            if(lineV2.charAt(i) != s.charAt(0)){
              scannerError("Missing String end | " + tempToken);
            }
            String temp2 = tempToken.substring(1,tempToken.length()-1);
            curLineTokens.add(new Token(stringToken, temp2, curLineNum()));
            tempToken = "";
          }
          //Ignore space
          else if(tempToken.equals(" ")){
            tempToken = "";
          }
          else if(tempToken.equals("#")){
            i = lineV2.length();
          }
          else{
            scannerError("Illegal character: " + tempToken);
          }
        }
        // Terminate line:
        curLineTokens.add(new Token(newLineToken,curLineNum()));
      }

      for (Token t: curLineTokens)
        Main.log.noteToken(t);
    }

    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

    private String expandLeadingTabs(String s) {
	String newS = "";
	for (int i = 0;  i < s.length();  i++) {
	    char c = s.charAt(i);
	    if (c == '\t') {
		do {
		    newS += " ";
		} while (newS.length()%TABDIST > 0);
	    } else if (c == ' ') {
		newS += " ";
	    } else {
		newS += s.substring(i);
		break;
	    }
	}
	return newS;
    }


    private boolean isLetterAZ(char c) {
	return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
	return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
	TokenKind k = curToken().kind;
  String kString = k.toString();
  if(kString.equals("<") || kString.equals(">") || kString.equals("==") || kString.equals("<=") || kString.equals(">=") || kString.equals("!=")){
    return true;
  }
	return false;
    }


    public boolean isFactorPrefix() {
	TokenKind k = curToken().kind;
  String kString = k.toString();
  if(kString.equals("+") || kString.equals("-")){
    return true;
  }
	return false;
    }


    public boolean isFactorOpr() {
	TokenKind k = curToken().kind;
  String kString = k.toString();
  if(kString.equals("*") || kString.equals("/") || kString.equals("%") || kString.equals("//")){
    return true;
  }
	return false;
    }


    public boolean isTermOpr() {
	TokenKind k = curToken().kind;
  String kString = k.toString();
  if(kString.equals("+") || kString.equals("-")){
    return true;
  }
	return false;
    }


    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
