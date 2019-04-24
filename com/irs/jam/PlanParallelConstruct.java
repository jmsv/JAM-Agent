//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanParallelConstruct.java,v 1.1 1998/05/09 18:32:16 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanParallelConstruct.java,v $
//  
//  File              : PlanParallelConstruct.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:28 1997
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Thu Aug 24 20:03:30 2000
//  Update Count      : 17
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
 * Represents a parallel-execution construct within plans
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanParallelConstruct extends PlanConstruct implements Serializable
{

  //
  // Members
  //
  protected Vector		_threads;	// Vector of PlanSequenceConstructs

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public PlanParallelConstruct()
  {
    _threads = new Vector(1, 1);
  }

  /**
   * Create a number of sequences of constructs and actions that will
   * be managed as threads by this construct.
   * 
   */
  public PlanParallelConstruct(PlanConstruct be)
  {
    _threads = new Vector(1, 1);
    if (be != null) {
      if (be.getType() == PLAN_SEQUENCE) {
	_threads.addElement(be);
      }
      else {
	PlanSequenceConstruct	s;
	s = new PlanSequenceConstruct(be);
	_threads.addElement(s);
      }
    }
    _constructType = PLAN_PARALLEL;
  }

  //
  // Member functions
  //
  public Vector		getConstructs()		{ return _threads; }
  public int 		getNumConstructs()	{ return _threads.size(); }

  /**
   * 
   * 
   */
  public PlanRuntimeState newRuntimeState()
  {
    return new PlanRuntimeParallelState(this);
  }

  /**
   * Add an action/construct to this sequence of actions/constructs
   * 
   */
  public void insertConstruct(PlanConstruct be)
  {
    if (be != null) {
      if (be.getType() == PLAN_SEQUENCE) {
	_threads.addElement(be);
      }
      else {
	PlanSequenceConstruct	s;
	s = new PlanSequenceConstruct(be);
	_threads.addElement(s);
      }
    }
  }

  /**
   * Return the indicated construct in the construct/action sequence
   * 
   */
  public PlanConstruct getConstruct(int n)
  {
    try {
      return (PlanConstruct) _threads.elementAt(n);
    }
    catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

}
