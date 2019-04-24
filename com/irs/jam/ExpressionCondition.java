//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ExpressionCondition.java,v 1.1 1998/05/09 18:32:04 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ExpressionCondition.java,v $
//  
//  File              : ExpressionCondition.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:13 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:33 1999
//  Update Count      : 14
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
 * A boolean-evaluable expression condition
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class ExpressionCondition extends Condition implements Serializable
{

  //
  // Members
  //
  protected Expression		_expression;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public ExpressionCondition(Expression e)
  {
    super();
    _expression = e;
  }

  //
  // Member functions
  //

  /**
   * 
   * 
   */
  public String getName()
  {
    return _expression.getName();
  }
  
  /**
   * 
   * 
   */
  public int getType()
  {
    return COND_EXP;
  }

  /**
   * Remove from the given binding list the ones not satisfying the
   * expression.
   * 
   */
  public boolean check(BindingList bl)
  {
    BindingListEnumerator bli = new BindingListEnumerator(bl);
    Binding b;

    while ((b = (Binding) bli.nextElement()) != null) {

      if (!_expression.eval(b).isTrue()) {
	bli.removeThis();
      }
    }
  
    return (bl.getCount() > 0) ? true : false;
  }

  /**
   * Confirm whether the binding is still valid against the current
   * World Model.
   * 
   */
  public boolean confirm(Binding b)
  {
    return _expression.eval(b).isTrue();
  }

}
