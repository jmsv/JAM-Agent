//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanContext.java,v 1.1 1998/05/09 18:32:13 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanContext.java,v $
//  
//  File              : PlanContext.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:37 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:29 1999
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
import java.util.*;

/**
 *
 * Represents the conditions under which a plan is applicable
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanContext implements Serializable
{

  //
  // Members
  //
  ConditionList		_conditions;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanContext()
  {
    _conditions = null;
  }

  /**
   * 
   * 
   */
  public PlanContext(ConditionList cList)
  {
    _conditions = cList;
  }

  //
  // Member functions
  //

  /**
   * 
   * 
   */
  public ConditionList addConditions(ConditionList cList)
  {
    ConditionListEnumerator	cle;
    Condition			cond;

    cle = new ConditionListEnumerator(cList);
    while (cle.hasMoreElements())
      _conditions.insert(cle.nextElement());

    return _conditions;
  }

  /**
   * Establish contexts (generate the binding list)
   * 
   */
  public boolean check(BindingList bindingList)
  {
    if (_conditions == null)
      return true;

    ConditionListEnumerator	cle;
    Condition			cond;

    cle = new ConditionListEnumerator(_conditions);
    while(cle.hasMoreElements()) {
      cond = (Condition) cle.nextElement();
      if (!cond.check(bindingList)) // modifies bindingList
	break;
    }

    return (bindingList.getCount() == 0) ? false : true;
  }

  /**
   * Confirm the validity of the current context with the current binding
   * 
   */
  public boolean confirm(Binding b)
  {
    if (_conditions == null)
      return true;

    ConditionListEnumerator	cle;
    Condition			cond;

    cle = new ConditionListEnumerator(_conditions);
    while(cle.hasMoreElements()) {
      cond = (Condition) cle.nextElement();
      if (cond.confirm(b) == false)
	return false;
    }
    return true;
  }

}
