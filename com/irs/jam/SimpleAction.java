//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: SimpleAction.java,v 1.5 1998/11/04 17:43:21 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\SimpleAction.java,v $
//  
//  File              : SimpleAction.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:32 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:21 1999
//  Update Count      : 32
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

/**
 *
 * A simple (non-decomposable) action within a plan
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class SimpleAction extends Action implements Serializable
{

  //
  // Members
  //
  protected int			_arity;
  protected ExpList		_args;
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Constructor with name and argument list
   * 
   */
  SimpleAction(String name, ExpList el, Interpreter interpreter)
  {
    super(name);
    _args = el;
    _arity = (el != null) ? el.getCount() : 0;
    _interpreter = interpreter;
    _actType = ACT_PRIMITIVE;
  }

  //
  // Member functions
  //
  public boolean	isExecutableAction()	{ return true; }
  public int		getArity()		{ return _arity; }
  public ExpList	getArgs()		{ return _args; }

  /**
   * Execute a non-decomposable action
   * 
   */
  public int execute(Binding b, Goal currentGoal)
  {
    Value	returnValue;

    returnValue = _interpreter.getSystemFunctions().execute(_name, _arity, _args,
							   b, currentGoal);

    if (returnValue.isDefined()) {
      return (returnValue.eval(b).isTrue()) ? ACT_SUCCEEDED : ACT_FAILED;
    }

    returnValue = _interpreter.getUserFunctions().execute(_name, _arity, _args, b,
							 currentGoal);

    if (returnValue.isDefined())
      return (returnValue.eval(b).isTrue()) ? ACT_SUCCEEDED : ACT_FAILED;
    else {
      /*
      System.out.println("SimpleAction: Action \"" + _name +
			 "\" not found in user-defined functions in UserFunctions.java!\n");
			 */
      return ACT_FAILED;
    }
  }

  /**
   * Print out the action information in-line with other information.
   * 
   */
  public void format(PrintStream s, Binding b)
  {
    s.print("PRIMITIVE: " + _name + " ");
    _args.format(s, b);
    s.print(" ");
  }

}
