//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeWhileState.java,v 1.2 1998/05/09 18:32:23 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeWhileState.java,v $
//  
//  File              : PlanRuntimeWhileState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:20:59 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:26 1999
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
 * Represents the runtime state of plan constructs
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeWhileState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected boolean		_checkLoopCondition;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanRuntimeWhileState(PlanWhileConstruct be)
  {
    _thisConstruct = be;
    _substate = be.getSequence().newRuntimeState();

    _checkLoopCondition = true;
  }

  //
  // Member functions
  //

  /**
   * 
   * 
   */
  public int execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    int		testReturnVal;
    int		returnVal;

    // If we're at the top of the loop we evaluate the test and execute the
    // first step if it succeeds.  Otherwise the loop is done.
    if (_checkLoopCondition == true) {

      testReturnVal = ((PlanWhileConstruct)_thisConstruct).getTest().execute(b, thisGoal);
      if (testReturnVal != Action.ACT_SUCCEEDED) {
	return PLAN_CONSTRUCT_COMPLETE;
      }
      _checkLoopCondition = false;
    }

    returnVal = _substate.execute(b, thisGoal, prevGoal);
    
    if (returnVal == PLAN_CONSTRUCT_FAILED) {
      return PLAN_CONSTRUCT_FAILED;
    }
    else if (returnVal == PLAN_CONSTRUCT_COMPLETE) {
      _substate = ((PlanWhileConstruct)_thisConstruct).getSequence().newRuntimeState();
      _checkLoopCondition = true;
      return PLAN_CONSTRUCT_INCOMP;
    }
    else { // return_val == PLAN_CONSTRUCT_INCOMP
      return PLAN_CONSTRUCT_INCOMP;
    }
  }

}
