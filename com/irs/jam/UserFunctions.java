//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: UserFunctions.java,v 1.1 1998/11/04 18:32:26 marcush Exp $
//  $Source: c:/com/irs/jam/RCS/UserFunctions.java,v $
//  
//  File              : UserFunctions.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:10 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Thu Aug 09 09:04:25 2001
//  Update Count      : 86
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

import com.irs.jam.examples.*;

/**
 *
 * Base class for defining primitive functionality
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class UserFunctions extends Functions {

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
  public UserFunctions(Interpreter interpreter)
  {
    _interpreter = interpreter;
  }

  //
  // Member functions
  //
  public Value execute(String name, int arity, ExpList args,
		       Binding binding, Goal currentGoal)
  {
    //
    // Create a TestClass object instance and return it
    //
    // Out:TestClass object
    //
    if (name.equals("createNewTestClassObject")) {

      if (arity != 1) {
	System.out.println("Invalid number of arguments: " + arity +
			   " to function \"" + name + "\"\n");
	return Value.FALSE;
      }

      ExpListEnumerator	ele = new ExpListEnumerator(args);
      Expression        exp = (Expression) ele.nextElement();

      TestClass obj = new TestClass();

      binding.setValue(exp, new Value(obj));
      return Value.TRUE;
    }

    //
    // Nothing matches
    //
    else {
      return Value.UNDEFINED;
    }
  }

}

