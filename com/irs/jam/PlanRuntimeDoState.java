//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeDoState.java,v 1.2 1998/05/09 18:32:18 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeDoState.java,v $
//  
//  File              : PlanRuntimeDoState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:19 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:28 1999
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

public class PlanRuntimeDoState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanRuntimeDoState(PlanDoConstruct be)
  {
    _thisConstruct = be;
    _substate = be.getSequence().newRuntimeState();
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

    // Execute the current PLAN constructs in the loop.  If execution
    // reaches the end of the iteration body then check the test condition.
    returnVal = _substate.execute(b, thisGoal, prevGoal);

    if (returnVal == PLAN_CONSTRUCT_FAILED) {
      return PLAN_CONSTRUCT_FAILED;
    }
    else if (returnVal == PLAN_CONSTRUCT_COMPLETE) {

      testReturnVal = ((PlanDoConstruct)_thisConstruct).getTest().execute(b, thisGoal);
      if (testReturnVal != Action.ACT_SUCCEEDED) {
	return PLAN_CONSTRUCT_COMPLETE;
      }
      else {
	_substate = ((PlanDoConstruct)_thisConstruct).getSequence().newRuntimeState();
	return PLAN_CONSTRUCT_INCOMP;
      }
    }
    else { // return_val == PLAN_CONSTRUCT_INCOMP) {
      return PLAN_CONSTRUCT_INCOMP;
    }
  }

}
