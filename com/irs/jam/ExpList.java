//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ExpList.java,v 1.2 1998/05/09 17:55:06 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ExpList.java,v $
//  
//  File              : ExpList.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:20 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Tue Aug 14 17:05:34 2001
//  Update Count      : 64
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

/**
 *
 * Expression List
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class ExpList extends DList implements Serializable
{
  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public ExpList()
  {
    super();
  }

  /**
   * 
   * 
   */
  public ExpList(Expression e)
  {
    super(e);
  }

  //
  // Member functions
  //

  /**
   * Convert all variables elements of the expression into constants
   * 
   */
  public ExpList explistEval(Binding binding)
  {
    ExpListEnumerator	expressions = new ExpListEnumerator(this);
    ExpList		newExpList = new ExpList();
    Expression		e;

    while (expressions.hasMoreElements()) {
      e = (Expression) expressions.nextElement();
      newExpList.append(new Value(e.eval(binding)));
    }

    return newExpList;
  }

  /**
   * Format the output and don't worry about being printed out
   * in-line with other information.
   * 
   */
  public void print(PrintStream s, Binding b)
  {
    ExpListEnumerator	expressions = new ExpListEnumerator(this);
    Expression		e;

    while (expressions.hasMoreElements()) {
      e = (Expression) expressions.nextElement();
      if (e.getType() == Expression.EXP_VALUE &&
	  e.eval(b).type() == Value.VAL_STRING) {
	  s.print("\"");
	  e.eval(b).print(s, b);
	  s.print("\" ");
      }
      else {
	  s.print(e.eval(b) + " ");
      }
    }
    s.println();
  }

  /**
   * Format the output so that it's conducive to being printed out
   * in-line with other information.
   *
   */
  public void format(PrintStream s, Binding b)
  {
    Expression		e;
    ExpListEnumerator	expressions = new ExpListEnumerator(this);

    while (expressions.hasMoreElements()) {
      e = (Expression) expressions.nextElement();
      // s.print(" " + e.getClass() + ": value ");
      e.eval(b).format(s, b);
      if (expressions.hasMoreElements())
	s.print(", ");
    }
  }

}

