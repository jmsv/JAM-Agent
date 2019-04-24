//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeParallelState.java,v 1.4 2000/07/03 13:10:18 klesen Exp $
//  $Source: /project/imedia3/imediabackbone/framework/jam2000/src/com/irs/jam/PlanRuntimeParallelState.java,v $
//  
//  File              : PlanRuntimeParallelState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:15 1997
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Sat Jul 07 16:25:22 2001
//  Update Count      : 71
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
import java.lang.*;

/**
 *
 * Represents the runtime state of parallel execution of sequences
 * within plans.
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeParallelState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected boolean			_threadsStarted;
  protected PlanRuntimeThreadState	_threads[];
  public int				_threadState[];
  public static final boolean		_output1 = true;
  public static final boolean		_output2 = false;

  //
  // Constructors
  //
  public PlanRuntimeParallelState(PlanParallelConstruct be)
  {
    _thisConstruct = be;
    _substate = be.getConstruct(0).newRuntimeState();
    // Initialize the array to all constructs incomplete.
    _threadsStarted = false;
    int threadNum;
    _threadState = new int[be.getNumConstructs()];
    _threads = new PlanRuntimeThreadState[be.getNumConstructs()];
    for (threadNum = 0; threadNum < be.getNumConstructs(); threadNum++) {
      _threadState[threadNum] = PLAN_CONSTRUCT_INCOMP;
    }
  }

  //
  // Member functions
  //
  public int execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    // Spawn off each thread and then, every execution cycle, wake them up so
    // they'll execute the next action/construct.
    int threadNum;
    boolean anyFailed = false;
    boolean allComplete = true;
    int numConstructs = ((PlanParallelConstruct)_thisConstruct).getNumConstructs();

    if (_output2) System.out.println("PRPS: entering execute method");

    // If threads have not been started then created them and start them.
    if (_threadsStarted == false) {
      for (threadNum = 0; threadNum < numConstructs; threadNum++) {
        _threads[threadNum] =
	    new PlanRuntimeThreadState(((PlanSequenceConstruct)
					((PlanParallelConstruct)_thisConstruct).getConstruct(threadNum)),
				       b, thisGoal, threadNum, _threadState);
        if (_output2) System.out.println("PRPS: Starting thread# " + threadNum);
	_threads[threadNum].start();
      }
      _threadsStarted = true;
    }

    // Comment: Without the sleep statement the interpreter is blocked.
    // Checked with jdk1.2.2 under Windows NT by klesen on August 30 2000.
    try {
        synchronized (this) { Thread.sleep(0,1); }
    } catch (InterruptedException e) {
        if (_output2) System.out.println("PRPS: Interrupted while in wait(): " + e + ".");
        e.printStackTrace();
        return PLAN_CONSTRUCT_FAILED;
    }

    // Go through the threads and check if any have failed or if all
    // have completed.
    for (threadNum = 0; threadNum < numConstructs; threadNum++) {
        if (_threadState[threadNum] == PLAN_CONSTRUCT_FAILED) {
	    if (_output2) System.out.println("PRPS: Found that thread# " + threadNum + " has failed.");
            anyFailed = true;
	    allComplete = false;
        }
        else if (_threadState[threadNum] == PLAN_CONSTRUCT_INCOMP) {
	    if (_output1) System.out.println("PRPS: Found that thread# " + threadNum + " is incomplete.");
            allComplete = false;
        }
	else {
	    if (_output1) System.out.println("PRPS: Found that thread# " + threadNum + " has status:" + _threadState[threadNum]);
	}
    }

    // Make sure all the threads are done before we signal that 
    // the construct completed.
    if (allComplete == true) {
	if (_output2) System.out.println("PRPS: All threads complete.");
        for (threadNum = 0; threadNum < numConstructs; threadNum++) {
            try {
		if (_output1) System.out.println("PRPS: 1.Telling thread#" + threadNum + " to join.");
                _threads[threadNum].join();
            }
	    catch (InterruptedException e) {
                if (_output2) System.out.println("Interrupted while in waiting for join()s " + e + ".");
		e.printStackTrace();
		return PLAN_CONSTRUCT_FAILED;
	    }
        }
        return PLAN_CONSTRUCT_COMPLETE;
    }
    else {
	if (_output2) System.out.println("PRPS: All threads were NOT complete.");
    }

    // At least one thread failed, so stop the threads and wait for them to die
    if (anyFailed == true) {
	if (_output2) System.out.println("PRPS: At least one thread failed.");

	for (threadNum = 0; threadNum < numConstructs; threadNum++) {
	    //:modified by klesen June Thu 29 2000
	    if (_threads[threadNum].isAlive()) {
		if (_output2) System.out.println("PRPS: Telling thread# " + threadNum + " to myStop.");
		_threads[threadNum].myStop();
	    }
	    else {
		if (_output2) System.out.println("PRPS: Found thread# " + threadNum + " was dead.");
	    }
	}

	for (threadNum = 0; threadNum < numConstructs; threadNum++) {
	    try {
		if (_output2) System.out.println("PRPS: 2.Telling thread#" + threadNum + " to join.");
		_threads[threadNum].join();
	    }
	    catch (InterruptedException e) {
	        if (_output2) System.out.println("Interrupted while in waiting for join()s " + e + ".");
	        e.printStackTrace();
	        return PLAN_CONSTRUCT_FAILED;
	    }
	}
	return PLAN_CONSTRUCT_FAILED;
    }
    else {
	if (_output2) System.out.println("PRPS: No threads failed.");
    }

    // If they're all complete, then kick them to go on to the next step.
    // Note that the stepComplete method will wait for a thread that hasn't
    // yet had its construct completed.
    for (threadNum = 0; threadNum < numConstructs; threadNum++) {
	if (_threadState[threadNum] == PLAN_CONSTRUCT_INCOMP) {
	    //:modified by klesen August Wed 30 2000
	    if (_threads[threadNum].stepComplete()) {
		if (_output2) System.out.println("PRPS: 2. Plan construct incomplete and stepComplete true. Telling thread#" + threadNum + " to myResume.");
		_threads[threadNum].myResume();
	    }
	}
	else {
	    if (_output1) System.out.println("PRPS: 2. Plan construct had threadstate of " + _threadState[threadNum]);
	}
    }

    return PLAN_CONSTRUCT_INCOMP;
  }

}
