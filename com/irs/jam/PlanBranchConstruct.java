//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanBranchConstruct.java,v 1.1 1998/05/09 18:32:12 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanBranchConstruct.java,v $
//  
//  File              : PlanBranchConstruct.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:40 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:30 1999
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
 * Represents conditional branching plan components
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanBranchConstruct extends PlanConstruct implements Serializable
{

  public final static int	PLAN_NO_BRANCH	= 0;
  public final static int	PLAN_AND_BRANCH	= 1;
  public final static int	PLAN_OR_BRANCH	= 2;

  //
  // Members
  //
  protected int			_branchType;
  protected Vector		_branches;	// Vector of PlanSequenceConstructs

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanBranchConstruct()
  {
    _branchType = PLAN_NO_BRANCH;
    _branches = new Vector(1, 1);

    _constructType = PLAN_BRANCH;
  }

  /**
   * 
   * 
   */
  public PlanBranchConstruct(PlanSequenceConstruct s, int branchType)
  {
    _branchType = branchType;
    _branches = new Vector(1, 1);
    addBranch(s);

    _constructType = PLAN_BRANCH;
  }

  //
  // Member functions
  //
  public int			getNumBranches()	{ return _branches.size(); }
  public int			getBranchType()		{ return _branchType; }

  /**
   * 
   * 
   */
  public PlanRuntimeState newRuntimeState()
  {
    return new PlanRuntimeBranchState(this);
  }

  /**
   * 
   * 
   */
  public void setBranchType(int bt)
  {
    if (bt >= PLAN_NO_BRANCH && bt <= PLAN_OR_BRANCH) {
      _branchType = bt;
    }
    else {
      // Throw an UndefinedType error or something like that.
    }
  }

  /**
   * 
   * 
   */
  public PlanSequenceConstruct getBranch(int branchnum)
  {
    return (branchnum >= 0 && branchnum < _branches.size()) ?
      (PlanSequenceConstruct)_branches.elementAt(branchnum) :
      null;
  }

  /**
   * 
   * 
   */
  public void addBranch(PlanConstruct be)
  {
    if (be != null) {
      if (be.getType() == PLAN_SEQUENCE) {
	_branches.addElement(be);
      }
      else {
	PlanSequenceConstruct	ns;
	ns = new PlanSequenceConstruct(be);
	_branches.addElement(ns);
      }
    }
  }

}
