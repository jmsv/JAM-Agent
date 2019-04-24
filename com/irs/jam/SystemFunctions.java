//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: SystemFunctions.java,v 1.6 1998/11/04 17:25:05 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\SystemFunctions.java,v $
//  
//  File              : SystemFunctions.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:25 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Sun Oct 28 14:28:09 2001
//  Update Count      : 221
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997 Marcus J. Huber and Jaeho Lee.
//  Copyright (C) 1997-1999 Intelligent Reasoning Systems
//  
//  Permission is granted to copy and redistribute this software so long
//  as no fee is charged, and so long as the copyright notice above, this
//  grant of permission, and the disclaimer below appear in all copies
//  made.  JAM may not be bundled, or sold alone or as part of another
//  product, without permission.
//  
//  This software is provided as is, without representation as to its
//  fitness for any purpose, and without warranty of any kind, either
//  express or implied, including without limitation the implied
//  warranties of merchantability and fitness for a particular purpose.
//  Marcus J. Huber, Jaeho Lee and Intelligent Reasoning Systems shall
//  not be liable for any damages, including special, indirect,
//  incidental, or consequential damages, with respect to any claim
//  arising out of or in connection with the use of the software, even
//  if they have been or are hereafter advised of the possibility of
//  such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package com.irs.jam;

import java.io.*;
import java.util.*;
import java.net.*;
import java.lang.reflect.*;

/**
 *
 * Base class for defining primitive functionality
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class SystemFunctions extends Functions implements Serializable
{
  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * Primary constructor
   *
   */
  public SystemFunctions(Interpreter interpreter)
  {
    _interpreter = interpreter;
  }


  //
  // Member functions
  //

  /**
   *
   *
   */
  public Value execute(String name, int arity, ExpList args,
		       Binding binding, Goal currentGoal)
  {
    //
    // NUMERIC ACTIONS
    //

    //
    // Addition
    //
    if (name.equals("+")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      if (arity == 1) 
      	return result;

      while ((exp = (Expression) ele.nextElement()) != null)
	result = result.add(exp.eval(binding));

      return result;
    }

    //
    // Subtraction
    //
    else if (name.equals("-")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      //      if (arity == 1) 
      //	return -result;

      while ((exp = (Expression) ele.nextElement()) != null)
	result = result.sub(exp.eval(binding));

      return result;
    }

    //
    // Multiplication
    //
    if (name.equals("*")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      if (arity == 1) 
      	return result;

      while ((exp = (Expression) ele.nextElement()) != null)
	result = result.mul(exp.eval(binding));

      return result;
    }

    //
    // Division
    //
    else if (name.equals("/")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      while ((exp = (Expression) ele.nextElement()) != null)
	result = result.div(exp.eval(binding));

      return result;
    }

    //
    // Modulo
    //
    if (name.equals("%")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      if (arity == 1) 
      	return result;

      while ((exp = (Expression) ele.nextElement()) != null)
	result = result.mod(exp.eval(binding));

      return result;
    }

    //
    // Absolute value (of first argument only)
    //
    if (name.equals("abs")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      Value result = exp.eval(binding);

      if ((result.type() == Value.VAL_LONG) ||
	  (result.type() == Value.VAL_REAL)) {
	return (((result.getLong() - 0) > 0) ? result : result.mul(new Value(-1)));
      }
      else {
	return result;
      }
    }

    //
    // RELATION ACTIONS
    //

    //
    // Equality
    //
    else if (name.equals("==")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (exp1.equals(exp2, binding)) ? Value.TRUE : Value.FALSE;
    }

    //
    // Inequality
    //
    else if (name.equals("!=")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (!exp1.equals(exp2, binding)) ? Value.TRUE : Value.FALSE;
    }

    //
    // All these comparisons could be converted to arbitrary arity
    //

    //
    // Smaller cardinality
    //
    else if (name.equals("<")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (exp1.lessthan(exp2, binding)) ? Value.TRUE : Value.FALSE;
    }

    //
    // Smaller or equal cardinality
    //
    else if (name.equals("<=")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (exp1.equals(exp2, binding) ||
	      exp1.lessthan(exp2, binding))
	? Value.TRUE : Value.FALSE;
    }

    //
    // Greater cardinality
    //
    else if (name.equals(">")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (!exp1.equals(exp2, binding) &&
	      !exp1.lessthan(exp2, binding))
	? Value.TRUE : Value.FALSE;
    }

    //
    // Greater or equal cardinality
    //
    else if (name.equals(">=")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression) ele.nextElement();
      exp2 = (Expression) ele.nextElement();

      return (exp1.equals(exp2, binding) ||
	      !exp1.lessthan(exp2, binding))
	? Value.TRUE : Value.FALSE;
    }

    //
    // Boolean AND
    //
    else if (name.equals("&&")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp;

      while ((exp = (Expression) ele.nextElement()) != null)
	if (!(exp.eval(binding).isTrue()))
	  return Value.FALSE;

      return Value.TRUE;
    }

    //
    // Boolean OR
    //
    else if (name.equals("||")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }
      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp;

      while ((exp = (Expression) ele.nextElement()) != null)
	if ((exp.eval(binding).isTrue()))
	  return Value.TRUE;

      return Value.FALSE;
    }

    //
    // Boolean Negation
    //
    else if (name.equals("!")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      return ((exp.eval(binding).isTrue()) ? Value.FALSE : Value.TRUE);
    }

    //
    // Bitwise AND
    //
    else if (name.equals("and")) {

      if (arity != 2) {
	  System.out.println("Invalid number of arguments: " + arity + 
			  " to function \"" + name + "\"\n");
	  return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression)ele.nextElement();
      exp2 = (Expression)ele.nextElement();
              
      Value val1 = exp1.eval(binding);
      Value val2 = exp2.eval(binding);
              
      if ((val1.type() != Value.VAL_LONG) || (val2.type() != Value.VAL_LONG)) {
	  System.out.println("Invalid type of arguments to function \"" + name + "\"\n");
	  return Value.FALSE;
      }
              
      long result = val1.getLong() & val2.getLong();            
      return new Value(result);
    }

    //
    // Bitwise OR
    //
    else if (name.equals("or")) {
      if (arity != 2) {
	  System.out.println("Invalid number of arguments: " + arity + 
			  " to function \"" + name + "\"\n");
	  return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression)ele.nextElement();
      exp2 = (Expression)ele.nextElement();
              
      Value val1 = exp1.eval(binding);
      Value val2 = exp2.eval(binding);
              
      if ((val1.type() != Value.VAL_LONG) || (val2.type() != Value.VAL_LONG)) {
	  System.out.println("Invalid type of arguments to function \"" + name + "\"\n");
	  return Value.FALSE;
      }
              
      long result = val1.getLong() | val2.getLong();            
      return new Value(result);
    }

    //
    // Bitwise XOR
    //
    else if (name.equals("xor")) {
      if (arity != 2) {
	  System.out.println("Invalid number of arguments: " + arity + 
			  " to function \"" + name + "\"\n");
	  return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1, exp2;

      exp1 = (Expression)ele.nextElement();
      exp2 = (Expression)ele.nextElement();
              
      Value val1 = exp1.eval(binding);
      Value val2 = exp2.eval(binding);
              
      if ((val1.type() != Value.VAL_LONG) || (val2.type() != Value.VAL_LONG)) {
	  System.out.println("Invalid type of arguments to function \"" + name + "\"\n");
	  return Value.FALSE;
      }
              
      long result = val1.getLong() ^ val2.getLong();            
      return new Value(result);
    }

    //
    // Bitwise complement
    //
    else if (name.equals("not")) {
      if (arity != 1) {
	  System.out.println("Invalid number of arguments: " + arity + 
			  " to function \"" + name + "\"\n");
	  return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp1;

      exp1 = (Expression)ele.nextElement();
              
      Value val1 = exp1.eval(binding);
              
      if (val1.type() != Value.VAL_LONG) {
	  System.out.println("Invalid type of argument to function \"" + name + "\"\n");
	  return Value.FALSE;
      }
              
      long result = ~val1.getLong();            
      return new Value(result);
    }

    //
    // MISCELLANEOUS UTILITY ACTIONS
    //

    //
    // Create a new Java object using Java reflection
    //
    else if (name.equals("new")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	exp = (Expression) ele.nextElement();
      String 		className = exp.eval(binding).getString();
      Class		argArray1[] = new Class[args.getCount()-1];
      Object		argArray2[] = new Object[args.getCount()-1];
      Class		c = null;
      Constructor	constructor;
      Object		obj = null;

      System.out.println("In NEW function, className=." + className);

      if (className != null && className.length() != 0) {
	try {
	  c = Class.forName(className);
	}
	catch (ClassNotFoundException e) {
	  System.out.println("\nCould not find class \"" + className + "\"!\n");
	  return Value.FALSE;
	}
      }

      // Build an array of argument class types so we can find
      // the right constructor to call.
      int argNum = 0;
      while ((exp = (Expression) ele.nextElement()) != null) {

	Value v = exp.eval(binding);

	if (v.type() == Value.VAL_LONG) {
	  //argArray1[argNum] = new Long(v.getLong()).getClass();
	  //argArray2[argNum] = new Long(v.getLong());
	  argArray1[argNum] = int.class;
	  argArray2[argNum] = new Integer((int)v.getLong());
	}
	else if (v.type() == Value.VAL_REAL) {
	  //argArray1[argNum] = new Double(v.getReal()).getClass();
	  //argArray2[argNum] = new Double(v.getReal());
	  argArray1[argNum] = double.class;
	  argArray2[argNum] = new Double(v.getReal());
	}
	else if (v.type() == Value.VAL_STRING) {
	  argArray1[argNum] = new String(v.getString()).getClass();
	  argArray2[argNum] = new String(v.getString());
	}
	else if (v.type() == Value.VAL_OBJECT) {
	  argArray1[argNum] = v.getObject().getClass();
	  argArray2[argNum] = v.getObject();
	}
	argNum++;
      }

      /*
      for (argNum = 0; argNum < args.getCount()-1; argNum++) {
	System.out.println("arg[ " + argNum + "]=" + argArray1[argNum]);
      }
      */

      try {
	constructor = c.getConstructor(argArray1);
      }
      catch (NoSuchMethodException e) {
	System.out.println("NoSuchMethodException");
	e.printStackTrace();
	return Value.FALSE;
      }

      // Create the object using the matching constructor
      try {
	obj = constructor.newInstance(argArray2);
      }
      catch (InstantiationException e) {
	System.out.println("InstantiationException");
	e.printStackTrace();
	return Value.FALSE;
      }
      catch (IllegalAccessException e) {
	System.out.println("IllegalAccessException");
	e.printStackTrace();
	return Value.FALSE;
      }
      catch (IllegalArgumentException e) {
	System.out.println("IllegalArgumentException");
	e.printStackTrace();
	return Value.FALSE;
      }
      catch (InvocationTargetException e) {
	System.out.println("InvocationTargetException");
	e.printStackTrace();
	return Value.FALSE;
      }

      if (obj != null)
	return new Value(obj);
      else {
	System.out.println("ERROR: Public class constructor not found with matching argument list!");
	return Value.FALSE;
      }
    }


    //
    // Output information to standard output
    //
    else if (name.equals("print")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp;

      while ((exp = (Expression) ele.nextElement()) != null)
	exp.eval(binding).print(System.out, binding);

      return Value.TRUE;
    }

    //
    // Output information to standard output with a newline at the end
    //
    else if (name.equals("println")) {

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp;

      while ((exp = (Expression) ele.nextElement()) != null) {
	exp.eval(binding).print(System.out, binding);
      }
      System.out.println();

      return Value.TRUE;
    }

    //
    // Delay in milliseconds
    //
    else if (name.equals("sleep")) {

      if (arity <= 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	exp;

      while ((exp = (Expression) ele.nextElement()) != null) {
	if (exp.eval(binding).type() == Value.VAL_LONG) {
	  try {
	      //Thread.sleep(exp.eval(binding).getLong());
	      synchronized (Thread.currentThread()) {
		  Thread.currentThread().wait(exp.eval(binding).getLong());
	      }
	  }
	  catch (InterruptedException e) {
	    System.out.println("Interrupted sleep primitive!");
	  }
	}
      }
      return Value.TRUE;
    }

    //
    // Get the current time
    // [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
    //
    if (name.equals("getTime")) {

      if (arity == 0) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
     
      Date date = new Date();
      binding.setValue(exp, new Value(date.getTime()));

      return Value.TRUE;
    }

    //
    // Do nothing
    //
    else if (name.equals("noop")) {
	//System.out.println("No-operation.");
      return Value.TRUE;
    }

    //
    // Always fail
    //
    else if (name.equals("fail")) {
      return Value.FALSE;
    }

    //
    // Takes a variable as its only parameter and changes its binding 
    // to Value.UNDEFINED, i.e. the variable is unbound afterwards.
    //
    if (name.equals("unassign")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	uExp = (Expression) ele.nextElement();

      binding.unbindVariable(uExp);
      return Value.TRUE;
    }


    //
    // STRING AND TOKENIZING ACTIONS
    //

    //
    // String Length
    // [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
    //
    if (name.equals("strlen")) {

      if ((arity == 0) || (arity > 2)) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }    

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
     
      String bigString = exp.eval(binding).getString();

      if (arity == 1) {
	return new Value(bigString.length());
      }
      else { // (arity == 2)
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, new Value(bigString.length()));
      }

      return Value.TRUE;     
    }
   
    //
    // Remaining string tokens
    // [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
    //
    if (name.equals("rest")) {

      if ((arity == 0) || (arity > 2)) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	exp = (Expression) ele.nextElement();
     
      String		bigString = exp.eval(binding).getString();
      StringTokenizer	st = new StringTokenizer(bigString);
      String		first;
      Value		result;
      
      // Modified by Martin Klesen (DFKI) Mar Wed 24 1999
      if (st.hasMoreTokens()) {
	first = st.nextToken();
        result = new Value(bigString.substring(bigString.indexOf(first) + first.length()).trim());
      }
      else return Value.FALSE;

      if (arity == 1) {
	return result;
      }
      else { // (arity == 2)
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, result);
      }
      return Value.TRUE;
    }

    //
    // Last token in string
    // [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
    //
    if (name.equals("last")) {

      if ((arity == 0) || (arity > 2)) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
     
      String bigString = exp.eval(binding).getString();
     
      StringTokenizer st = new StringTokenizer(bigString);
      Value result = null;

      while (st.hasMoreTokens())
	result = new Value(st.nextToken());

      if (result == null)
	return Value.FALSE;
     
      if (arity == 1) {
	return result;
      }
      else { // (arity == 2)
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, result);
      }

      return Value.TRUE;
    }

    //
    // First token in string
    // [Modified from Chip McVey at Johns Hopkins University's Applied Physics Lab]
    //
    if (name.equals("first")) {

      if ((arity == 0) || (arity > 2)) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
     
      String bigString = exp.eval(binding).getString();

      StringTokenizer st = new StringTokenizer(bigString);
      Value result;

      if (st.hasMoreTokens())
	result = new Value(st.nextToken());
      else
	return Value.FALSE;

      if (arity == 1) {
	return result;
      }
      else { // (arity == 2)
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, result);
      }

      return Value.TRUE;
    }

    //
    // Parse a string containing JAM agent definitions (i.e., World Model,
    // Goals, Plans)
    //
    if (name.equals("parseString")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      String pString = exp.eval(binding).getString();

      // Modified by Martin Klesen (DFKI) Apr Mon 19 1999
      // Catch a parse error rather than dying on it.
      //try {
        _interpreter.parseString(_interpreter, pString);
      //}
      /*
      catch(ParseException pe) {
	System.err.println("JAM: Parser returned error!\n");
	pe.printStackTrace();
	return Value.FALSE;
      }
      */
      return Value.TRUE;
    }

    //
    // Takes a variable as its only parameter and changes its binding 
    // to ValueLong, if its value is a signed decimal integer
    // [From Martin Klesen: DFKI]
    //
    if (name.equals("parseInt")) {

	if (arity != 1) {
	    System.out.println("Invalid number of arguments: " + arity + " to function \"" + name + "\"\n");
	    return Value.FALSE;
	}

	ExpListEnumerator ele = new ExpListEnumerator(args);
	Expression exp = (Expression) ele.nextElement();

	Value val = exp.eval(binding);
	if (val.type() == Value.VAL_STRING) {
	    int i = Integer.parseInt(val.getString());
	    binding.setValue(exp, new Value(i));
	    return Value.TRUE;
	} 
	else {
	    System.err.println("ERROR in parseInt: " + val.toString() + " must be a String!");
	    return Value.FALSE;
	}
    }

    //
    // DEBUGGING ACTIONS
    //

    //
    // Display current world model state
    //
    else if (name.equals("printWorldModel")) {
      _interpreter.getWorldModel().print(System.out);
      return Value.TRUE;
    }

    //
    // Display current intention structure state
    //
    else if (name.equals("printIntentionStructure")) {
      _interpreter.getIntentionStructure().print(System.out);
      return Value.TRUE;
    }

    //
    // Turn debug information on/off
    //
    else if (name.equals("setShowWorldModel")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      _interpreter.setShowWorldModel(exp.eval(binding).isTrue());
      /*
      System.out.println("Showing World Model changes: " +
			 _interpreter.getShowWorldModel());
      */
      return Value.TRUE;
    }

    //
    // Turn debug information on/off
    //
    else if (name.equals("setShowGoalList")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      _interpreter.setShowGoalList(exp.eval(binding).isTrue());
      /*
      System.out.println("Showing Goal List changes: " +
			 _interpreter.getShowGoalList());
      */
      return Value.TRUE;
    }

    //
    // Turn debug information on/off
    //
    else if (name.equals("setShowAPL")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      _interpreter.setShowAPL(exp.eval(binding).isTrue());
      /*
      System.out.println("Showing APL reasoning: " + _interpreter.getShowAPL());
      */
      return Value.TRUE;
    }

    //
    // Turn debug information on/off
    //
    else if (name.equals("setShowIntentionStructure")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      _interpreter.setShowIntentionStructure(exp.eval(binding).isTrue());
      /*
      System.out.println("Showing Intention Structure changes: " +
			 _interpreter.getShowIntentionStructure());
      */
      return Value.TRUE;
    }

    //
    // Turn debug information on/off
    //
    else if (name.equals("setShowActionFailure")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      _interpreter.setShowActionFailure(exp.eval(binding).isTrue());
      /*
      System.out.println("Showing action failure: " + _interpreter.getShowAPL());
      */
      return Value.TRUE;
    }

    //
    // META-LEVEL ACTIONS
    //

    //
    // Get the currently executing Goal
    //
    else if (name.equals("getCurrentGoal")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      binding.setValue(exp, new Value(currentGoal));
      return Value.TRUE;
    }

    //
    // Print info about the currently executing Goal
    //
    else if (name.equals("printGoal")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      Goal g = (Goal) exp.eval(binding).getObject();
      g.print(System.out);

      return Value.TRUE;
    }

    //
    // Mark the plans with the given name such that they will not be
    // considered for APL generation
    //
    else if (name.equals("disablePlan")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      String planName = ((Expression) ele.nextElement()).eval(binding).getString();
      _interpreter.getPlanLibrary().disable(planName);
      return Value.TRUE;
    }

    //
    // Get the currently executing Plan
    //
    else if (name.equals("getCurrentPlan")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();

      if (currentGoal != null &&
	  currentGoal.getIntention() != null &&
	  currentGoal.getIntention().getPlan() != null) {
	System.out.print("PlanRuntimeSimpleState: current plan is - \"");
	System.out.println(currentGoal.getIntention().getPlan().getName() + "\"");
	binding.setValue(exp, new Value(currentGoal.getIntention().getPlan()));
	return Value.TRUE;
      }

      binding.setValue(exp, new Value((Object) null));
      return Value.TRUE;
    }

    //
    // Print the currently executing Goal
    //
    else if (name.equals("printPlan")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      Plan p = (Plan) exp.eval(binding).getObject();
      p.print(System.out);

      return Value.TRUE;
    }

    //
    // Get the attributes for the Plan
    //
    else if (name.equals("getPlanAttributes")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      Plan p = (Plan) exp.eval(binding).getObject();

      exp = (Expression) ele.nextElement();
      binding.setValue(exp, new Value(p.getAttributes()));
      return Value.TRUE;
    }

    //
    // Get the value for a specific attribute for an APL Element
    //
    // NOTE: Assumes values are numeric and returns -1 as a special
    // return code to indicate that the attribute was not found.
    //
    // Needs more protection against improper parameters!!!!
    //
    else if (name.equals("getAttributeValue")) {

      if (arity != 3) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APLElement a = (APLElement) exp.eval(binding).getObject();
      exp = (Expression) ele.nextElement();
      String attribute = exp.eval(binding).getString();
      exp = (Expression) ele.nextElement();

      // Find the specified Attribute
      String attributes = a.getPlan().getAttributes();
      int index = attributes.indexOf(attribute);

      if (index == -1) {
	binding.setValue(exp, new Value(-1));
	return Value.TRUE;
      }

      // Extract the corresponding value
      String tmp1 = attributes.substring(index);
      StringTokenizer st = new StringTokenizer(tmp1);

      // Advance to the token just after the attribute
      String tmp2 = st.nextToken();
      tmp2 = st.nextToken();
      System.out.println("getAttributeValue: value returned is " + tmp2);
      binding.setValue(exp, new Value(Double.valueOf(tmp2).doubleValue()));

      return Value.TRUE;
    }

    //
    // Select a random APL element
    //
    else if (name.equals("getAPLElement")) {

      if (arity != 3) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APL a = (APL) exp.eval(binding).getObject();
      exp = (Expression) ele.nextElement();
      int num = (int) exp.eval(binding).getLong();

      APLElement selectedElement = a.nth(num);

      exp = (Expression) ele.nextElement();
      binding.setValue(exp, new Value(selectedElement));

      return Value.TRUE;
    }

    //
    // Print the passed-in APL
    //
    else if (name.equals("printAPL")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APL a = (APL) exp.eval(binding).getObject();
      a.print(System.out);

      return Value.TRUE;
    }

    //
    // Print the passed-in APL Element
    //
    else if (name.equals("printAPLElement")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APLElement a = (APLElement) exp.eval(binding).getObject();
      a.print(System.out);

      return Value.TRUE;
    }

    //
    // Select a random APL element
    //
    else if (name.equals("selectRandomAPLElement")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APL a = (APL) exp.eval(binding).getObject();

      APLElement selectedElement = a.getUtilityRandom();
      exp = (Expression) ele.nextElement();
      binding.setValue(exp, new Value(selectedElement));

      return Value.TRUE;
    }

    //
    // Intend the APL element
    //
    else if (name.equals("intendAPLElement")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      APLElement a = (APLElement) exp.eval(binding).getObject();

      _interpreter.getIntentionStructure().intend(a, true);
      
      return Value.TRUE;
    }

    //
    // Communication actions
    //

    //
    // Make a connection to another agent as a client
    // IN:int port
    // IN:String hostName
    // OUT:BufferedReader inputStream
    // OUT:PrintWriter outputStream
    //
    else if (name.equals("connectToAgentAsClient")) {

      if (arity != 4) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      int port = (int) exp.eval(binding).getLong();
      exp = (Expression) ele.nextElement();
      String host = exp.eval(binding).getString();

      BufferedReader	in = null;
      PrintWriter	out;
      Socket		socket;

      try {
	socket = new Socket(host, port);

	in	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out	= new PrintWriter(socket.getOutputStream());

	exp = (Expression) ele.nextElement();
	binding.setValue(exp, new Value(in));
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, new Value(out));
	return Value.TRUE;
      }
      catch (IOException e) {
        System.out.println("JAM::ConnectToAgentAsClient:IOException : " + e);
	return Value.FALSE;
      }
    }

    //
    // Make a connection to another agent as a server
    // IN:int port
    // OUT:BufferedReader inputStream
    // OUT:PrintWriter outputStream
    //
    else if (name.equals("connectToAgentAsServer")) {

      if (arity != 3) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      int port = (int) exp.eval(binding).getLong();

      BufferedReader	in = null;
      PrintWriter	out;
      Socket		socket;
      ServerSocket	server;

      try {
	server = new ServerSocket(port);

	socket	= server.accept();
	in	= new BufferedReader(new InputStreamReader(socket.getInputStream()));
	out	= new PrintWriter(socket.getOutputStream());
	
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, new Value(in));
	exp = (Expression) ele.nextElement();
	binding.setValue(exp, new Value(out));
	
	return Value.TRUE;
      }
      catch (IOException e) {
        System.out.println("JAM::ConnectToAgentAsServer:IOException : " + e);
	return Value.FALSE;
      }
	
    }

    //
    // Closes the connection to another agent
    // IN:BufferedReader inputStream
    // IN:PrintWriter outputStream
    //
    // Note that it closes both streams and changes the bindings to
    // Value.UNDEFINED
    //
    else if (name.equals("closeConnection")) {

        if (arity != 2) {
            System.out.println("Invalid number of arguments: " + arity +
			    " to function \"" + name + "\"\n");
	    return Value.FALSE;
        }
          
        try {
	    ExpListEnumerator	ele = new ExpListEnumerator(args);
	    // close the inputStream
	    Expression exp = (Expression) ele.nextElement();
	    BufferedReader sIn = (BufferedReader) exp.eval(binding).getObject();
	    sIn.close();
	    binding.setValue(exp, Value.UNDEFINED);
	    // close the outputStream
	    exp = (Expression) ele.nextElement();
	    PrintWriter sOut = (PrintWriter) exp.eval(binding).getObject();
	    sOut.flush();
	    sOut.close();
	    binding.setValue(exp, Value.UNDEFINED);
	}
        catch (IOException e) {
            System.out.println("JAM::closeConnection:IOException : " + e);
	    return Value.FALSE;
        }
          
        return Value.TRUE;
    }
     
    //
    // Send a string-encoded message to another agent
    // IN:PrintWriter outputStream
    // IN:String message
    //
    else if (name.equals("sendMessage")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	exp = (Expression) ele.nextElement();
      PrintWriter	sOut = (PrintWriter) exp.eval(binding).getObject();
      exp = (Expression) ele.nextElement();
      String		message = exp.eval(binding).getString();

      sOut.println(message);

      return Value.TRUE;
    }

    //
    // Receive a string-encoded message from another agent (note; blocking)
    // IN:BufferedReader inputStream
    // OUT:String message
    //
    else if (name.equals("recvMessage")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression	exp = (Expression) ele.nextElement();
      BufferedReader	sIn = (BufferedReader) exp.eval(binding).getObject();
      String		message;

      try {
	exp = (Expression) ele.nextElement();
	message = sIn.readLine();

	// Modified by Martin Klesen (DFKI) Mar Wed 17 1999
	if (message == null) {
	  binding.setValue(exp, Value.UNDEFINED);
	} else {
	  binding.setValue(exp, new Value(message));
	}
	return Value.TRUE;
      }
      catch (IOException e) {
        System.out.println("JAM::ConnectToAgentAsServer:IOException : " + e);
	return Value.FALSE;
      }
    }

    //
    // Save a "checkpoint" of this Jam agent's run-time state to
    // a file. This has a number of possible uses, including to be
    // used later to recover from an agent failure, moved to 
    // another platform to implement migration, or instantiated
    // on the local machine to create a "clone".
    //
    // In:String filename - The file in which to store the serialized
    //                      agent
    //
    if (name.equals("checkpointAgentToFile")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      String filename = ((Expression) ele.nextElement()).eval(binding).getString();

      FileOutputStream		fos;
      ObjectOutputStream	out;

      try {
	fos = new FileOutputStream(filename);
	out = new ObjectOutputStream(fos);
	out.writeObject(_interpreter);
	return Value.TRUE;
      }
      catch (IOException e) {
	System.out.println("I/O Error *" + e + "* writing agent to " +
			   "\"" + filename + "\"!");
	return Value.FALSE;
      }

    }

    //
    // Save a "checkpoint" of this Jam agent's run-time state to
    // an array of bytes.
    //
    // Out:ByteArrayOutputStream outStream - Store the serialized agent
    //
    if (name.equals("checkpointAgent")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression outArray = ((Expression) ele.nextElement());

      ByteArrayOutputStream	baos = new ByteArrayOutputStream();
      ObjectOutputStream	out;

      try {
	out = new ObjectOutputStream(baos);
	out.writeObject(_interpreter);

	binding.setValue(outArray, new Value(baos.toByteArray()));

	return Value.TRUE;
      }
      catch (IOException e) {
	System.out.println("I/O Error *" + e + "* checkpointing agent!");
	return Value.FALSE;
      }
    }

    //
    // Contact the mobile agent server on the destination platform and send
    // checkpoint to that agent.
    // IN:String hostName
    // IN:int port
    //
    if (name.equals("agentGo")) {

      if (arity != 2) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression exp = (Expression) ele.nextElement();
      String host = exp.eval(binding).getString();
      exp = (Expression) ele.nextElement();
      int port = (int) exp.eval(binding).getLong();
      exp = (Expression) ele.nextElement();

      Socket			socket;
      ObjectOutputStream	out;


      // Connect to the restoration agent as a client and write out
      // the agent's internals
      //
      try {
	socket	= new Socket(host, port);
	out	= new ObjectOutputStream(socket.getOutputStream());
	out.writeObject(getInterpreter());
	out.close();
      }
      catch (IOException e) {
	System.out.println("JAM::GoAgent:IOException : " + e);
	e.printStackTrace();
	return Value.FALSE;
      }

      if (getInterpreter().getAgentHasMoved() == false) {
	System.exit(0);
      }
      getInterpreter().setAgentHasMoved(false);

      return Value.TRUE;
    }


    //
    // Execute the program and args passed in as a string
    //
    // In:String execString - The program and arguments
    //
    if (name.equals("exec")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      String execString = ((Expression) ele.nextElement()).eval(binding).getString();

      Runtime r = Runtime.getRuntime();
      try {
	r.exec(execString);
      }
      catch (java.io.IOException ie) {
	System.out.println(ie);
	return Value.FALSE;
      }

      return Value.TRUE;
    }

    //
    // Cause the agent to exit immediately.
    //
    //
    if (name.equals("exit")) {
      System.exit(0);
      return Value.TRUE;
    }

    //
    // An action that always fails
    //
    if (name.equals("fail")) {
      return Value.FALSE;
    }

    //
    // Nothing matches
    //
    else {
      return Value.UNDEFINED;
    }
  }

}
