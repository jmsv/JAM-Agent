//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeDoAllState.java,v 1.2 1998/05/09 18:32:16 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeDoAllState.java,v $
//  
//  File              : PlanRuntimeDoAllState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:23 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:28 1999
//  Update Count      : 32
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
 * Represents the runtime state of DoAll constructs
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeDoAllState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected int		_activeBranchNum;	// index (0 based) to current branch
  protected Vector	_branchesLeft;		// Vector holding indices of branches that
						// have not yet completed (succeeded OR failed)

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanRuntimeDoAllState(PlanDoAllConstruct be)
  {
    _thisConstruct = be;
    _branchesLeft = new Vector(be.getNumBranches());
    for (int i=0; i < be.getNumBranches(); i++) {
      _branchesLeft.addElement(new Integer(i));
    }

    setActiveBranchNum(selectRandomBranch());
    _substate = be.getBranch(getActiveBranchNum()).newRuntimeState();
  }

  //
  // Member functions
  //
  public int		getActiveBranchNum()	{ return _activeBranchNum; }

  /**
   * Determine if any branches are incomplete (i.e., not successful or failed)
   * 
   */
  private boolean	allBranchesTried()
  {
    return (_branchesLeft.size() == 0);
  }

  /**
   * Select a branch randomly from those still not completed or failed.
   * 
   */
  private int		selectRandomBranch()
  {
    Random ran = new Random();
    return (((Integer) _branchesLeft.elementAt(Math.abs(ran.nextInt() % _branchesLeft.size()))).intValue());
  }

  /**
   * Execute something in one of the branches
   * 
   */
  public int execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    PlanConstruct	currentConstruct;
    int			returnVal;

    currentConstruct = ((PlanDoAllConstruct) _thisConstruct).getBranch(getActiveBranchNum());
  
    if (_substate == null)
      _substate = currentConstruct.newRuntimeState();
  
    returnVal = _substate.execute(b, thisGoal, prevGoal);
  
    //
    // FAILED!
    //
    // Something in the branch failed so this construct fails
    if (returnVal == PLAN_CONSTRUCT_FAILED) {
   
      return PLAN_CONSTRUCT_FAILED;
    }
    
    //
    // INCOMPLETE
    //
    // Nothing's been determined at this point
    else if (returnVal == PLAN_CONSTRUCT_INCOMP) {

      return PLAN_CONSTRUCT_INCOMP;
    }

    //
    // COMPLETE!
    //
    else {
      
      // If the agent's just completed the last branch then return success.
      if (_branchesLeft.size() == 1) 
	return PLAN_CONSTRUCT_COMPLETE;

      else {
	// If not, then the agent goes on to the next, random, branch
	// First, find and remove the current branch from further consideration
	for (int i = 0; i < _branchesLeft.size(); i++) {
	  if (((Integer) _branchesLeft.elementAt(i)).intValue() == getActiveBranchNum()) {
	    _branchesLeft.removeElementAt(i);
	  }
	}

	// And then select the new branch.
	setActiveBranchNum(selectRandomBranch());
	_substate = ((PlanDoAllConstruct) _thisConstruct).getBranch(getActiveBranchNum()).newRuntimeState();
	return PLAN_CONSTRUCT_INCOMP;
      }
    }
  }

  /**
   * Set which branch is being executed and keep within bounds.
   * 
   */
  public void setActiveBranchNum(int n)
  {
    if (n >= 0 && n < ((PlanDoAllConstruct) _thisConstruct).getNumBranches()) 
      _activeBranchNum = n;
    else
      _activeBranchNum = -1;
  }

}
