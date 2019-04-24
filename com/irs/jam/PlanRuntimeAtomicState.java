//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeAtomicState.java,v 1.1 1998/05/09 18:32:17 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeAtomicState.java,v $
//  
//  File              : PlanRuntimeAtomicState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:26 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:28 1999
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

/**
 *
 * Represents the runtime state of plan constructs
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeAtomicState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected int			_currentConstructNum;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanRuntimeAtomicState(PlanAtomicConstruct be)
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
  public int			execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    int		returnVal;

    // Continue to loop execute while the sequence isn't done.  If the
    // sequence signals that it failed or succeeded, then return this
    // value.
    while (true) {
      returnVal = _substate.execute(b, thisGoal, thisGoal.getPrevGoal());

      if (returnVal != PLAN_CONSTRUCT_INCOMP)
	return returnVal;
    }
  }

}
