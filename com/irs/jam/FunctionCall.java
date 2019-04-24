//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: FunctionCall.java,v 1.4 1998/11/04 18:05:03 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\FunctionCall.java,v $
//  
//  File              : FunctionCall.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:02 1997
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Wed Jun 21 08:16:38 2000
//  Update Count      : 78
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
import java.lang.reflect.*;

/**
 *
 * Represents a function call
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class FunctionCall extends Expression implements Serializable
{

  //
  // Members
  //
  protected String		_name;
  protected String		_classname;
  protected int			_arity;
  protected ExpList		_args;
  protected Variable		_object;
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Constructor with name and argument list
   * 
   */
  FunctionCall(String name, ExpList args, Interpreter interpreter)
  {
    _name = name;
    if (args != null)
      _arity = args.getCount();
    else
      _arity = 0;
    _args = args;
    _interpreter = interpreter;
    _object = null;
  }

  /**
   * Constructor for class-based method invocation with class and
   * function names and argument list
   * 
   */
  FunctionCall(String className, String  functionName,
	       ExpList args, Interpreter interpreter)
  {
    _name = functionName;
    _classname = className;
    if (args != null)
      _arity = args.getCount();
    else
      _arity = 0;
    _args = args;
    _interpreter = interpreter;
    _object = null;
  }

  /**
   * Constructor for class-based method invocation with class and
   * function names and argument list
   * 
   */
  FunctionCall(String className, String  functionName,
	       Variable v, ExpList args, Interpreter interpreter)
  {
    _name = functionName;
    _classname = className;
    if (args != null)
      _arity = args.getCount();
    else
      _arity = 0;
    _args = args;
    _interpreter = interpreter;
    _object = v;
  }

  //
  // Member functions
  //

  public String		getName()	{ return _name; }
  public ExpList	getArgs()	{ return _args; }
  public int		getArity()	{ return _arity; }
  public int		getType()	{ return EXP_FUNCALL; }
  
  /**
   * Perform the function
   * 
   */
  public Value eval(Binding binding)
  {
    Value	returnValue;
    Class	c = null;
    Object	obj = null;
    Object	returnedObj = null;
    boolean	foundMethod = false;
    Goal	currentGoal = _interpreter.getIntentionStructure().getCurrentGoal();

    // If there's a class defined then try reflection
    if (_classname != null && _classname.length() != 0) {
      /*
      System.out.println("Executing class function " + _classname + "." +
			 _name);
			 */
      try {
	c = Class.forName(_classname);
      }
      catch (ClassNotFoundException e) {
	System.out.println("\nCould not find class \"" + _classname + "\"!\n");
	return Value.FALSE;
      }

      if (_object != null) {
	obj = (Object) _object.eval(binding).getObject();
	c = obj.getClass();
      }

      // Create a local instance of the specified class if there
      // isn't already one specified (i.e., if the method is a static
      // function or otherwise doesn't require an object reference)
      if (obj == null) {

	try {
	  obj = c.newInstance();
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
      }

      // Check for the case that the class is set up according to
      // our JAM-specified interface.
      if (obj instanceof com.irs.jam.PrimitiveAction) {
	  return ((com.irs.jam.PrimitiveAction) obj).execute(new String(_classname + "." + _name),
							     _arity, _args,
							     binding, currentGoal);
      }

      // Go through all the object's methods and find the method with a name
      // matching the plan's action
      Method[] methods = c.getDeclaredMethods();
    
      for (int i = 0; i < methods.length; i++) {
	if (methods[i] instanceof Method) {
	  Method m = (Method) methods[i];

	  // Perform name comparison
	  if (_name.equals(m.getName())) {

	    Class	returntype = null;
	    Class	parameters[];
	    Class	exceptions[];
	    Object	margs[];
	  
	    // Ignore return type and exceptions for now
	    returntype = m.getReturnType();
	    exceptions = m.getExceptionTypes();
	  
	    // Check to see if parameter list length and types match.
	    parameters = m.getParameterTypes();
	    if (parameters.length == _args.getCount()) {

	      boolean matched = true;
	      margs = new Object[_args.getCount()];

	      // Go through each parameter and check for matching types
	      for (int j = 0; j < parameters.length; j++) {

		// Check each parameter for a matching type and build an
		// Object array from the arguments

		//
		// JavaCC parser will be creating Value objects of the appropriate
		// parameter type though, so will need to restrict member functions
		// to parameters of type Long, String, Double, and Object.
		Value argument = null;
		argument = ((Expression)_args.nth(j+1)).eval(binding);

		/*
		  System.out.println("parameters[j].getName() = " +
		  parameters[j].getName());
		  System.out.println("argument = " + argument);
		  */
		if ((argument.type() == Value.VAL_LONG) &&
		    ((parameters[j].getName().equals("java.lang.Integer")) ||
		     (parameters[j].getName().equals("int")))) {
		  margs[j] = new Integer((int) argument.getLong());
		}
		else if ((argument.type() == Value.VAL_LONG) &&
			 ((parameters[j].getName().equals("java.lang.Long")) ||
			  (parameters[j].getName().equals("long")))) {
		  margs[j] = new Long(argument.getLong());
		}
		else if ((argument.type() == Value.VAL_REAL) &&
			 ((parameters[j].getName().equals("java.lang.Float")) ||
			  (parameters[j].getName().equals("java.lang.Double")) ||
			  (parameters[j].getName().equals("float")) ||
			  (parameters[j].getName().equals("double")))) {
		  margs[j] = new Double(argument.getReal());
		}
		else if ((argument.type() == Value.VAL_STRING) &&
			 (parameters[j].getName().equals("java.lang.String"))) {
		  margs[j] = new String(argument.getString());
		}
		else if (argument.type() == Value.VAL_OBJECT) {
		  // Need to see if the parameter is an object of some form
		  // (and not a String) 
		  if ((parameters[j].getName().indexOf("java.lang.String") == -1) ||
		      (!parameters[j].getName().equals("int")) ||
		      (!parameters[j].getName().equals("long")) ||
		      (!parameters[j].getName().equals("float")) ||
		      (!parameters[j].getName().equals("double"))) {
		    margs[j] = argument.getObject();
		  }
		}
		else {
		  matched = false;
		  break;
		}
	      }
	  	    
	      // if parameters match then invoke method
	      if (matched == true) {
		try {
		  Class returnedClass;
		  foundMethod = true;
		  returnedObj = m.invoke(obj, margs);
		  /*
		  System.out.println("HERE: Returned object is: " +
				     returnedObj);
				     */
		  returnedClass = returnedObj.getClass();
		  /*
		  System.out.println("returnedClass is: " +
				     returnedClass);
				     */
		  if (returnedObj instanceof Double)
		    return new Value(((Double) returnedObj).doubleValue());

		  if (returnedObj instanceof Float)
		    return new Value(((Float) returnedObj).doubleValue());

		  if (returnedObj instanceof Integer)
		    return new Value(((Integer) returnedObj).longValue());

		  if (returnedObj instanceof Long)
		    return new Value(((Long) returnedObj).longValue());

		  if (returnedObj instanceof String)
		    return new Value(returnedObj.toString());

		  return new Value(returnedObj);
		}
		catch(IllegalAccessException e) {
		  System.out.println(e);
		  return Value.FALSE;
		}
		catch(IllegalArgumentException e) {
		  System.out.println(e);
		  return Value.FALSE;
		}
		catch(InvocationTargetException e) {
		  System.out.println(e);
		  return Value.FALSE;
		}
	      }
	    }
	    else {
	      System.out.println("Argument count mis-match (plan action had " +
				 _args.getCount() + " and member function had " +
				 parameters.length + ")");
	    }
	  }
	  else {
	    //System.out.println(" not a match.");
	  }
	}
      }

      return new Value(returnedObj);

    }

    // Otherwise, see if it's a system-defined primitive
    //System.out.println("Attempting to execute a normal function.");
    returnValue = _interpreter.getSystemFunctions().execute(_name, _arity,
							    _args, binding,
							    currentGoal);

    if (returnValue.isDefined())
      return returnValue;
    /*
    else
      System.out.println("FunctionCall: Action \"" + _classname +
			 "\" not found in system functions, checking user functions.");
			 */

    // Last but not least, see if it's a user-defined primitive
    returnValue = _interpreter.getUserFunctions().execute(_name, _arity, _args,
							  binding, currentGoal);

    if (!returnValue.isDefined())
      System.out.println("FunctionCall: Action \"" + _name +
			 "\" not found in user-defined functions!\n");

    return returnValue;
  }
  
  /**
   * Display information without considering it being in-line
   * with other information
   * 
   */
  public void print(PrintStream s, Binding b)
  {
    eval(b).print(s, b);
  }

  /**
   * Display information considering that it will be in-line
   * with other information
   * 
   */
  public void format(PrintStream s, Binding b)
  {
    eval(b).format(s, b);
  }

}
