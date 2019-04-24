//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeSimpleState.java,v 1.2 1998/05/09 18:32:21 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeSimpleState.java,v $
//  
//  File              : PlanRuntimeSimpleState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:11 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Fri Oct 26 13:51:50 2001
//  Update Count      : 34
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
 * Represents the runtime state of plan constructs
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeSimpleState extends PlanRuntimeState implements Serializable
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
  public PlanRuntimeSimpleState(PlanSimpleConstruct be)
  {
    _thisConstruct = be;
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
      int		actionReturnVal;

      try {
	  actionReturnVal = ((PlanSimpleConstruct)_thisConstruct).getAction().execute(b, thisGoal);
      }
      catch (RuntimeException e) {
	  Action a = ((PlanSimpleConstruct)_thisConstruct).getAction();
	  System.err.println("**** Action in \"" + a.getTraceFile() +
			     "\" at line#" + a.getTraceLine() + " threw an exception!****");
	  e.printStackTrace();
	  throw new RuntimeException("Plan action failed");
      }
    
      if (actionReturnVal == Action.ACT_FAILED) {
	  Action a = ((PlanSimpleConstruct)_thisConstruct).getAction();
	  if ((thisGoal == null) || thisGoal.getIntentionStructure().getInterpreter().getShowActionFailure()) {
	      System.out.println("**** Action failed in \"" + a.getTraceFile() + "\", line#" + a.getTraceLine() + " ****");
	  }
	  return PLAN_CONSTRUCT_FAILED;
      }
      else {
	  return PLAN_CONSTRUCT_COMPLETE;
      }
  }
}
