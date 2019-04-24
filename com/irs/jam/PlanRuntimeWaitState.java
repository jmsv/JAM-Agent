//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeWaitState.java,v 1.4 1998/11/04 18:11:16 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeWaitState.java,v $
//  
//  File              : PlanRuntimeWaitState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:01 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Wed Oct 17 15:51:08 2001
//  Update Count      : 36
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998-1999 Marcus J. Huber and Intelligent Reasoning Systems
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
//  Marcus J. Huber and Intelligent Reasoning Systems shall not be
//  liable for any damages, including special, indirect, incidental, or
//  consequential damages, with respect to any claim arising out of or
//  in connection with the use of the software, even if they have been
//  or are hereafter advised of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package com.irs.jam;

import java.io.*;

/**
 *
 * Represents the runtime state of plan constructs
 *
 * @author Marc Huber
 *
 **/

public class PlanRuntimeWaitState extends PlanRuntimeState implements Serializable
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
  public PlanRuntimeWaitState(PlanWaitConstruct be)
  {
    _thisConstruct = be;
    _substate = null;
  }

  //
  // Member functions
  //

  /**
   * Check to see whether the action returns successfully or the goal has
   * been accomplished.
   * 
   */
  public int execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    // Check to see if the agent is waiting on action success
    if (((PlanWaitConstruct)_thisConstruct).getAction() != null) {

      int returnVal;
      returnVal = ((PlanWaitConstruct)_thisConstruct).getAction().execute(b, thisGoal);
      if (returnVal == Action.ACT_SUCCEEDED)
	return PLAN_CONSTRUCT_COMPLETE;
      else
	return PLAN_CONSTRUCT_INCOMP;
    }
    
    // Agent must be waiting on a goal
    else {
      // Check for match of goal relation on world model
	/*
	System.out.println("PRWS::Waiting on goal achievement of relation:");
	System.out.print("\n");
	(((PlanWaitConstruct)_thisConstruct).getRelation()).format(System.out, b);
	System.out.println();
	*/
	boolean matchFound;
	matchFound = thisGoal.getIntentionStructure().getInterpreter().getWorldModel().match(((PlanWaitConstruct)_thisConstruct).getRelation(), b);
	//System.out.println("  Match found: " + matchFound);
	return (matchFound) ? PLAN_CONSTRUCT_COMPLETE : PLAN_CONSTRUCT_INCOMP;
    }
  }

}

