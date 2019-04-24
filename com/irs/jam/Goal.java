//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Goal.java,v 1.8 1998/11/04 18:08:34 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Goal.java,v $
//  
//  File              : Goal.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:58 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Thu Oct 25 21:15:40 2001
//  Update Count      : 124
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
 * Represents an agent's goals
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Goal implements Serializable
{

  //
  // Members
  //
  protected GoalAction		_goalAction;	// The expression for the goal to be
  						// completed - points to action from parent
  						// Plan body
  protected Relation		_concludeRelation; // The Relation (if any) responsible for the
						   // goal being created for data-driven behavior
  protected Goal		_subgoal;
  protected Goal		_prevGoal;	// Goal for which this intention was created
  protected int			_newGoal;	// Flag of whether goal is new or not.
  protected int			_status;	// Run state of the intention (ACTIVE,
  						// SUSPENDED, SUCCESS, FAILURE, etc.)
  protected APLElement		_intention;	// Intended (instantiated with bindings) Plan
  protected PlanRuntimeState	_runtimeState;	// Plan information related to execution
  protected IntentionStructure	_intentionStructure; // The IS from whence this came


  //
  // Constructors
  //

  /**
   * Constructor with the goal specification, conclude relation,
 * parent goal, and intention structure as parameters
   * 
   */
  public Goal(GoalAction ga, Relation concludeRel, Goal prev, IntentionStructure is)
  {
    _goalAction = ga;
    _concludeRelation = concludeRel;
    _subgoal = null;
    _prevGoal = prev;
    if (prev != null)
      prev.setSubgoal(this);

    _newGoal = 1;

    _status = IntentionStructure.IS_UNTRIED;
    _intention = null;
    _runtimeState = null;
    _intentionStructure = is;
  }

  //
  // Member functions
  //

  public boolean	isNew()			{ return _newGoal == 1; }
  public boolean	isToplevelGoal()	{ return _prevGoal == null; }
  public boolean	isLeafGoal()		{ return _subgoal == null; }
  public GoalAction	getGoalAction()		{ return _goalAction; }
  public Relation	getConcludeRelation()	{ return _concludeRelation; }
  public int		setNew()		{ return _newGoal = 1; }
  public int		clearNew()		{ return _newGoal = 0; }
  public Goal		getSubgoal()		{ return _subgoal; }
  public Goal		setSubgoal(Goal g)	{ return _subgoal = g; }
  public Goal		getPrevGoal()		{ return _prevGoal; }
  public Goal		setPrevGoal(Goal g)	{ return _prevGoal = g; }
  public APLElement	getIntention()		{ return _intention; }
  public APLElement	setIntention(APLElement se) { return _intention = se; }
  public int		getStatus()		{ return _status; }
  public int		setStatus(int st)	{ return _status = st; }
  public PlanRuntimeState getRuntimeState()	{ return _runtimeState; }
  public PlanRuntimeState setRuntimeState(PlanRuntimeState r) { return _runtimeState = r; }


  /**
   * Verify that the plan's context is valid.
   * 
   */
  public boolean confirmContext()
  {
    return 
      (_intention != null &&
       !_intention.getPlan().confirmContext(getIntentionBinding()));
  }

  /**
   * Execute the procedure associated with a plan's BODY specification.
   * 
   */
  public int execute()
  {
    return getRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
  }

  /**
   * Execute the procedure associated with a plan's FAILURE specification.
   * 
   */
  public int executeFailure()
  {
    return getIntention().getPlan().getFailure().newRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
  }

  /**
   * Execute the procedure associated with a plan's EFFECTS specification.
   * 
   */
  public int executeEffects()
  {
    return getIntention().getPlan().getEffects().newRuntimeState().execute(getIntentionBinding(), this, getPrevGoal());
  }

  /**
   * Return the goal's relation label
   * 
   */
  public String getName()
  {
    return (_goalAction != null) ? _goalAction.getName() : null;
  }
  
  /**
   * Return the goal specification
   * 
   */
  public Relation getRelation()
  {
    return (_goalAction != null) ? _goalAction.getRelation() : null;
  }

  /**
   * Check whether the goal should have an Applicable Plan List created
   * for it.
   *
   */
  public boolean generateAPL()
  {
    /*
    System.out.println("generateAPL: _status = " + _status);
    System.out.println("generateAPL: _subgoal = " + _subgoal);
    System.out.println("generateAPL: _newGoal = " + _newGoal);
    System.out.println("generateAPL: _intention = " + _intention);
    System.out.println("generateAPL: WM.anyNew() = " +
		       _intentionStructure.getInterpreter().getWorldModel().anyNew());
		       */

    if (
	// If the goal hasn't been completed yet and hasn't been UNPOSTed
	(_status != IntentionStructure.IS_SUCCESS &&
	 _status != IntentionStructure.IS_ABANDONED) &&
	
	// and the goal is a "leaf" goal.
	(_subgoal == null)) {
      
	// If the goal already has an intended plan, then definitely do
	// not generate an APL
	if (_intention != null)
	    return false;

	// If it is "new" (meaning that it has just been POSTed, subgoaled,
	// or has been refreshed by the intention structure) then definitely
	// generate an APL
	if (_newGoal == 1)
	    return true;
      
	// If anything in the world model has changed, then we need to
	// consider the goal for APL generation
	if (_intentionStructure.getInterpreter().getWorldModel().anyNew())
	    return true;
    }
    return false;
  }

  /**
   * Get the binding of the goal arguments based upon the parent goal
   * (if it exists).
   * 
   */
  public Binding getGoalBinding()
  {
    return (_prevGoal != null) ? _prevGoal.getIntentionBinding() : null;
  }

  /**
   * Get the binding of the goal arguments based upon the plan's goal
   * (if it exists).
   * 
   */
  public Binding getIntentionBinding()
  {
    return (_intention != null) ? _intention.getBinding() : null;
  }

  /**
   * Return whether the goal is still worth considering.
   * 
   */
  public boolean isValid()
  {
    return ((_status != IntentionStructure.IS_SUCCESS) &&
	    (_status != IntentionStructure.IS_ABANDONED));
  }

  /**
   * Check to see if the stack in which this goal is part is blocked.
   * 
   */
  public boolean isStackBlocked()
  {
    Goal gl;

    gl = _prevGoal;

    // Check this goal
    if (_status == IntentionStructure.IS_BLOCKED)
      return true;
    
    // First move up to the top of the stack for this goal (if any), checking
    // on the way up.
    while (gl != null) {
      if (gl.getStatus() == IntentionStructure.IS_BLOCKED)
	return true;
      gl = gl.getPrevGoal();
    }

    // Then check from the goal down
    gl = _subgoal;
    while (gl != null) {
      if (gl.getStatus() == IntentionStructure.IS_BLOCKED)
	return true;
      gl = gl.getSubgoal();
    }
    return false;
  }

  /**
   * This function should be defined in this goal class
   * because it must use the binding of the subgoaling plan,
   * not the candidate plans for this goal.
   *
   */
  public double evalUtility()
  {
    Expression goalUtility = ((_goalAction != null) ? _goalAction.getUtility() : null);
    return (goalUtility != null) ? goalUtility.eval(getGoalBinding()).getReal() : 0.0;
  }

  /**
   * Find matches between bound and unbound variables 
   * 
   */
  public boolean matchRelation(Relation dstRelation, Binding dstBinding)
  {
    // pattRelation and pattBinding are from the goal to be POSTed
    // "this" is a goal that's already on the goal list
    return dstRelation.unify(dstRelation, dstBinding,
			     getRelation(), getGoalBinding()); // srcRelation
  }
  
  /**
   * Check whether the goal's specification compares to the parameters
   * 
   */
  public boolean matchGoal(GoalAction goalAction, Binding goalActionBinding)
  {
    if (goalAction != null) {
      boolean b;

      /*
      System.out.print("Goal:srcRelation before matchRelation is: ");
      goalAction.getRelation().print(System.out, goalActionBinding);
      */

      b = matchRelation(goalAction.getRelation(), goalActionBinding);
      if (b == true) {
	  //if (goalAction.getUtility() != null ||
	  //    evalUtility() == goalAction.evalUtility(goalActionBinding)) {
	  //    return true;
	  //}

	  // Following fix from Alexander Staller (alexs@ai.univie.ac.at)
	  // Austrian Research Institute for Artificial Intelligence
	  if (goalAction.getUtility() == null) {
	      //System.out.println("  no utility specified -> TRUE");
	      return true;
	  } else {
	      if(evalUtility() == goalAction.evalUtility(goalActionBinding)) {
		  //System.out.println("  utility specified and matches -> TRUE");
		  return true;
	      } else {
		  //System.out.println("  utility specified and NOT matching -> FALSE");
	      }
	  }
      }
    }

    return false;
  }

  /**
   * Return the agent's intention structure
   * 
   */
  public IntentionStructure getIntentionStructure()
  {
    return _intentionStructure;
  }

  /**
   * Remove the goal's intention and all subgoal intentions
   * 
   */
  public void removeIntention(boolean failed)
  {
    Goal		aGoal;
    PlanAtomicConstruct	failureSection;

    aGoal = this;

    // Move down to bottom of subgoal string
    while (aGoal != null && aGoal.getSubgoal() != null) {
      aGoal = aGoal.getSubgoal();
    }

    // Now backtrack, cleaning things up on the way
    while (aGoal != null && (aGoal != this)) {

      if (failed == true && (aGoal.getIntention() != null)) {

	// Execute FAILURE section
	if ((failureSection =
	     aGoal.getIntention().getPlan().getFailure()) != null) {
	  aGoal.setRuntimeState(failureSection.newRuntimeState());
	  aGoal.getRuntimeState().execute(aGoal.getIntentionBinding(),
					  aGoal, aGoal.getPrevGoal());
	}
      }

      // clear up memory
      aGoal.setIntention(null);

      // Remove the goal from the Intention Structure
      _intentionStructure.getInterpreter().getIntentionStructure().removeGoal(aGoal);

      aGoal = aGoal.getPrevGoal();
    }

    // Execute FAILURE section for this plan too
    if (failed == true) {
      if (_intention != null &&
	  (failureSection = _intention.getPlan().getFailure()) != null) {
	_runtimeState = failureSection.newRuntimeState();
	_runtimeState.execute(aGoal.getIntentionBinding(), this, this.getPrevGoal());
      }
    }

    setIntention(null);
    setSubgoal(null);
    setRuntimeState(null);
  }

  /**
   * Format output to the given stream without considering having the output
   * in-line with other output.
   * 
   */
  public void		print(PrintStream s)
  {
    s.print("  Goal   \t: ");
    if (_goalAction != null)
      _goalAction.print(s, getGoalBinding(), "", "");
    else
      s.println("null (data-driven)");

    if (_concludeRelation != null) {
	s.print("  concludeRel\t: ");
	_concludeRelation.print(s, getGoalBinding());
    }

    if (_intention != null)
      s.println("  Utility\t: " + _intention.evalUtility());
    else
      s.println("  Utility\t: " + evalUtility());
    s.println("  New ?    \t: " + ( (_newGoal == 1) ? "True" : "False"));
    s.print("  Status   \t: ");
    switch (_status) {
    case IntentionStructure.IS_UNTRIED:
      s.println("IS_UNTRIED");
      break;
    case IntentionStructure.IS_FAILURE:
      s.println("IS_FAILURE");
      break;
    case IntentionStructure.IS_SUCCESS:
      s.println("IS_SUCCESS");
      break;
    case IntentionStructure.IS_ACTIVE:
      s.println("IS_ACTIVE");
      break;
    case IntentionStructure.IS_BLOCKED:
      s.println("IS_BLOCKED");
      break;
    case IntentionStructure.IS_ABANDONED:
      s.println("IS_ABANDONED");
      break;
    }

    s.print("  Subgoal  \t: " + _subgoal);
    s.println(" => " + ( (_subgoal != null) ? _subgoal.getName() : "NONE"));
    s.print("  Prev_Goal\t: " + _prevGoal);
    s.println(" => " + ( (_prevGoal != null) ? _prevGoal.getName() : "NONE"));

    s.println("  Intention\t: " + _intention);
    s.println("  RuntimeState\t: " + _runtimeState);
  }

  /**
   * Format output to the given stream so that it can be in-line
   * with other output.
   * 
   */
  public void		format(PrintStream s)
  {
    s.print("Goal: ");
    if (_goalAction != null)
      _goalAction.formatArgs(s, getGoalBinding(), "", "");
    else
      s.println("null");
    if (_intention != null)
      s.print(", Utility: " + _intention.evalUtility());
    else
      s.println(", Utility: " + evalUtility());
    s.print(", New?: " + ( (_newGoal == 1) ? "True" : "False"));
    s.print(", Status: ");
    switch (_status) {
    case IntentionStructure.IS_UNTRIED:
      s.print("IS_UNTRIED");
      break;
    case IntentionStructure.IS_FAILURE:
      s.print("IS_FAILURE");
      break;
    case IntentionStructure.IS_SUCCESS:
      s.print("IS_SUCCESS");
      break;
    case IntentionStructure.IS_ACTIVE:
      s.print("IS_ACTIVE");
      break;
    case IntentionStructure.IS_BLOCKED:
      s.print("IS_BLOCKED");
      break;
    case IntentionStructure.IS_ABANDONED:
      s.print("IS_ABANDONED");
      break;
    }

    s.print(", Subgoal: " + _subgoal);
    s.print(" => " + ( (_subgoal != null) ? _subgoal.getName() : "NONE"));
    s.print(", Prev_Goal: " + _prevGoal);
    s.print(" => " + ( (_prevGoal != null) ? _prevGoal.getName() : "NONE"));

    s.println(", Intention: " + _intention);
    s.println(", RuntimeState: " + _runtimeState);
  }

}
