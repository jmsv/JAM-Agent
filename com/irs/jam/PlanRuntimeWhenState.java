//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeWhenState.java,v 1.2 1998/05/09 18:32:22 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeWhenState.java,v $
//  
//  File              : PlanRuntimeWhenState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:01 1997
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

public class PlanRuntimeWhenState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected boolean	_testEvaluated;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanRuntimeWhenState(PlanWhenConstruct be)
  {
    _thisConstruct = be;
    _substate = be.getSequence().newRuntimeState();

    _testEvaluated = false;
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

    // If we're at the beginning of the construct, then we need to evaluate
    // the test.  If the test fails, we're simply done.  If it succeeds, we
    // go on and execute the sequence of constructs.
    if (_testEvaluated == false) {

      testReturnVal = ((PlanWhenConstruct)_thisConstruct).getTest().execute(b, thisGoal);
   
      if (testReturnVal != Action.ACT_SUCCEEDED) {
	return PLAN_CONSTRUCT_COMPLETE;
      }
      _testEvaluated = true;
    }

    returnVal = _substate.execute(b, thisGoal, prevGoal);
    return returnVal;
  }

}
