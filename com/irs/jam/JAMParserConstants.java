/* Generated By:JavaCC: Do not edit this line. JAMParserConstants.java */
package com.irs.jam;

public interface JAMParserConstants {

  int EOF = 0;
  int WS = 1;
  int SINGLE_LINE_COMMENT = 5;
  int FORMAL_COMMENT = 6;
  int MULTI_LINE_COMMENT = 7;
  int STRING = 10;
  int ws = 17;
  int delim = 18;
  int letter = 19;
  int digit = 20;
  int charac = 21;
  int EXP = 22;
  int SIGN = 23;
  int number0 = 24;
  int number1 = 25;
  int number2 = 26;
  int GOAL_DECL = 27;
  int FACT_DECL = 28;
  int OBSERVER_DECL = 29;
  int PLAN_START = 30;
  int PLAN_NAME = 31;
  int PLAN_DOC = 32;
  int PLAN_GOALSPEC = 33;
  int PLAN_CONCLUDESPEC = 34;
  int PLAN_CONTEXT = 35;
  int PLAN_PRECONDITION = 36;
  int PLAN_UTILITY = 37;
  int PLAN_EFFECTS = 38;
  int PLAN_FAILURE = 39;
  int PLAN_ATTRIBUTES = 40;
  int PLAN_BODY = 41;
  int PLAN_BODY_AND = 42;
  int PLAN_BODY_OR = 43;
  int PLAN_BODY_PARALLEL = 44;
  int PLAN_BODY_DO_ALL = 45;
  int PLAN_BODY_DO_ANY = 46;
  int PLAN_BODY_DO = 47;
  int PLAN_BODY_WHILE = 48;
  int PLAN_BODY_WHEN = 49;
  int PLAN_BODY_ATOMIC = 50;
  int PLAN_BODY_WAIT = 51;
  int IF = 52;
  int ELSEIF = 53;
  int ELSE = 54;
  int KEYWORD_TEST = 55;
  int KEYWORD_UTILITY = 56;
  int KEYWORD_BY = 57;
  int KEYWORD_NOT_BY = 58;
  int EXECUTE = 59;
  int LOAD = 60;
  int PARSE = 61;
  int ASSIGN = 62;
  int FACT = 63;
  int RETRIEVE = 64;
  int RETRIEVEALL = 65;
  int NEXTFACT = 66;
  int TEST = 67;
  int POST = 68;
  int UNPOST = 69;
  int ACHIEVE = 70;
  int PERFORM = 71;
  int MAINTAIN = 72;
  int QUERY = 73;
  int ASSERT = 74;
  int SUCCEED = 75;
  int FAIL = 76;
  int RETRACT = 77;
  int UPDATE = 78;
  int INTEGER = 79;
  int FLOAT = 80;
  int IDENTIFIER = 81;
  int CLASS_IDENTIFIER = 82;
  int VARIABLE = 83;
  int SPECIAL_FUNCTION_NAME = 84;

  int DEFAULT = 0;
  int IN_SINGLE_LINE_COMMENT = 1;
  int IN_FORMAL_COMMENT = 2;
  int IN_MULTI_LINE_COMMENT = 3;
  int IN_STRING = 4;

  String[] tokenImage = {
    "<EOF>",
    "<WS>",
    "\"//\"",
    "<token of kind 3>",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "\"*/\"",
    "<token of kind 8>",
    "\"\\\"\"",
    "\"\\\"\"",
    "\"\\\\\\\"\"",
    "\"\\\\t\"",
    "\"\\\\n\"",
    "\"\\\\r\"",
    "\"\\\\f\"",
    "<token of kind 16>",
    "<ws>",
    "<delim>",
    "<letter>",
    "<digit>",
    "<charac>",
    "<EXP>",
    "<SIGN>",
    "<number0>",
    "<number1>",
    "<number2>",
    "<GOAL_DECL>",
    "<FACT_DECL>",
    "<OBSERVER_DECL>",
    "<PLAN_START>",
    "<PLAN_NAME>",
    "<PLAN_DOC>",
    "<PLAN_GOALSPEC>",
    "<PLAN_CONCLUDESPEC>",
    "<PLAN_CONTEXT>",
    "<PLAN_PRECONDITION>",
    "<PLAN_UTILITY>",
    "<PLAN_EFFECTS>",
    "<PLAN_FAILURE>",
    "<PLAN_ATTRIBUTES>",
    "<PLAN_BODY>",
    "\"AND\"",
    "\"OR\"",
    "\"PARALLEL\"",
    "\"DO_ALL\"",
    "\"DO_ANY\"",
    "\"DO\"",
    "\"WHILE\"",
    "\"WHEN\"",
    "\"ATOMIC\"",
    "\"WAIT\"",
    "\"IF\"",
    "\"ELSEIF\"",
    "\"ELSE\"",
    "\":TEST\"",
    "\":UTILITY\"",
    "\":BY\"",
    "\":NOT-BY\"",
    "\"EXECUTE\"",
    "\"LOAD\"",
    "\"PARSE\"",
    "\"ASSIGN\"",
    "\"FACT\"",
    "\"RETRIEVE\"",
    "\"RETRIEVEALL\"",
    "\"NEXTFACT\"",
    "\"TEST\"",
    "\"POST\"",
    "\"UNPOST\"",
    "\"ACHIEVE\"",
    "\"PERFORM\"",
    "\"MAINTAIN\"",
    "\"QUERY\"",
    "\"ASSERT\"",
    "\"SUCCEED\"",
    "\"FAIL\"",
    "\"RETRACT\"",
    "\"UPDATE\"",
    "<INTEGER>",
    "<FLOAT>",
    "<IDENTIFIER>",
    "<CLASS_IDENTIFIER>",
    "<VARIABLE>",
    "<SPECIAL_FUNCTION_NAME>",
    "\"{\"",
    "\"}\"",
    "\";\"",
    "\":\"",
    "\"[\"",
    "\"]\"",
    "\"(\"",
    "\")\"",
  };

}