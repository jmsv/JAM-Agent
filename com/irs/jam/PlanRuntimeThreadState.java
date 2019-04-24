//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeThreadState.java,v 1.5 2000/07/04 09:21:37 klesen Exp $
//  $Source: /project/imedia3/imediabackbone/framework/jam2000/src/com/irs/jam/PlanRuntimeThreadState.java,v $
//  
//  File              : PlanRuntimeThreadState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:02 1997
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Sat Jul 07 16:21:15 2001
//  Update Count      : 30
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
import java.lang.*;

/**
 *
 * Represents the runtime state of a threaded sequence of constructs
 * This class re-implements the PlanRuntimeState members simply
 * because it's easier.
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeThreadState extends Thread implements Serializable
{
  //
  // Members
  //
  protected PlanSequenceConstruct	_thisConstruct;
  protected PlanRuntimeSequenceState	_substate;
  protected Binding			_binding;
  protected Goal			_goal;
  protected int				_threadNumber;
  protected int				_threadState[];

  //:modified by klesen June Thu 29 2000
  // used to produce a thread that may be safely "stopped" or "suspended"
  // see also jdk1.2/docs/guide/misc/threadPrimitiveDeprecation.html
  private volatile boolean		_threadStopped = true;
  private volatile boolean		_threadSuspended = false;

  //
  // Constructors
  //
  public PlanRuntimeThreadState(PlanSequenceConstruct be, Binding b, Goal thisGoal, 
                                int threadNumber, int threadState[])
  {
    _thisConstruct = be;
    // Execute this construct directly (it's the only one)
    _substate = (PlanRuntimeSequenceState) be.newRuntimeState();
    _binding = b;			// To hold for subconstructs
    _goal = thisGoal;			// To hold for subconstructs
    _threadNumber = threadNumber;	// Index into state array
    _threadState = threadState;		// reference to array held by PlanRuntimeParallelState
    setDaemon(true);
  }
  
  /** 
  * Suspend the currently executing thread safely. 
  *
  */
  protected void mySuspend()
  {
    _threadSuspended = true;
  }             
  
  /** 
  * Returns <code>true</code> if this thread has been suspended. 
  * @see #mySuspend()
  *
  */
  protected boolean suspended()
  {
    return _threadSuspended;
  }

  /** 
  * Resume the currently executing thread safely. 
  *
  */
  protected synchronized void myResume()
  {
    _threadSuspended = false;
    notify(); 
  }
     
  /** 
  * Stop the currently executing thread safely. 
  *
  */
  protected synchronized void myStop() {
    _threadStopped = true;
    notify();
  }

  /** 
  * Returns <code>true</code> if this thread has been stopped.
  * @see #myStop()
  *
  */
  protected boolean stopped()
  {
    return _threadStopped;
  }
  
  /** 
  * Returns <code>true</code> if this thread has executed the next action/construct 
  * and suspended itself. If this thread is still in the middle of execution the 
  * main thread waits until it gets notified.
  * @see #suspended()
  *
  */
  protected synchronized boolean stepComplete()
  {
    while (!_threadSuspended) {
        try {
            wait();
        }
	catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
    return true;
  }
    
  //
  // Member functions
  //
  public void run()
  {
    int returnVal;
    if (_substate == null) {
      _substate = (PlanRuntimeSequenceState) _thisConstruct.newRuntimeState();
    }
    _threadStopped = false;
    //System.out.println("PlanRuntimeThreadState:thread \"" + getName() + "\" running...");
    while (!_threadStopped) { // Loop forever until sequence completes or fails
        returnVal = _substate.execute(_binding, _goal, _goal.getPrevGoal());

        if (returnVal == PlanRuntimeState.PLAN_CONSTRUCT_FAILED) {
            _threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_FAILED;
            //System.out.println("Subconstruct in \"" + getName() + "\" failed! (" + returnVal + ")");
	    _threadStopped = true;
        }
        else if (returnVal == PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE) {
	    _threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE;
	    //System.out.println("Subconstruct in \"" + getName() + "\" complete! (" + returnVal + ")");
	    _threadStopped = true;
        }
        else { // return_val == PLAN_CONSTRUCT_INCOMP
            //System.out.println("Subconstruct in \"" + getName() + "\" incomplete!");
            _threadState[_threadNumber] = PlanRuntimeState.PLAN_CONSTRUCT_INCOMP;

            synchronized (this) {
                //:modified by klesen August Wed 30 2000
                _threadSuspended = true;
                notify();
                try {
                    while (_threadSuspended && !_threadStopped) {
			//System.out.println("PRTS: Thread \"" + getName() + "\" waiting");
                        wait();
			//System.out.println("PRTS: Thread \"" + getName() + "\" done waiting");
                    }
                }
		catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
    //System.out.println("PlanRuntimeThreadState:thread \"" + getName() + "\" exiting...");
  }

}
