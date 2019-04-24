//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Expression.java,v 1.1 1998/05/09 18:32:03 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Expression.java,v $
//  
//  File              : Expression.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:14 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:34 1999
//  Update Count      : 15
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
 * Represents the basic data-types within JAM agents
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class Expression implements Serializable
{
  //
  // Members
  //
  public final static int EXP_UNDEFINED	= -1;
  public final static int EXP_VALUE 	= 1;
  public final static int EXP_VARIABLE 	= 2;
  public final static int EXP_FUNCALL 	= 3;
  public final static int EXP_PREDICATE = 4;

  //
  // Constructors
  //

  //
  // Abstract member functions
  // 
  public abstract String	getName();
  public abstract int		getType();
  public abstract void		print(PrintStream s, Binding b);
  public abstract void		format(PrintStream s, Binding b);

  /**
    * Evaluates the expression to a single resultant
    */
  public abstract Value		eval(Binding b);

  //
  // Member functions
  //

  public boolean		isVariable()	{ return false; }
  public Variable		getVariable()	{ return null; }

  /**
    * Evaluates the expression to a single resultant (whether the
    * two values are equivalent)
    */
  public boolean 		equals(Expression e, Binding b)
  {
    Value one = eval(b);
    Value two = e.eval(b);

    if (one.isDefined())
      return two.isDefined() ? one.eq(two) : false;
    else
      return two.isDefined() ? false : true;
  }

  /**
    * Evaluates the expression to a single resultant (whether the
    * first value is "less than" the second value)
    */
  public boolean 		lessthan(Expression e, Binding b)
  {
    Value one = eval(b);
    Value two = e.eval(b);

    if (one.isDefined())
      return two.isDefined() ? one.lt(two) : false;
    else
      return two.isDefined() ? false : true;
  }

}
