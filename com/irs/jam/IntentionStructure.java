//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: IntentionStructure.java,v 1.10 1998/11/04 17:53:17 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\IntentionStructure.java,v $
//  
//  File              : IntentionStructure.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:55 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Fri Oct 26 13:03:28 2001
//  Update Count      : 283
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
 * Represents the agent's intentions
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class IntentionStructure implements Serializable
{
  //
  // Members
  //
  public final static int	IS_UNTRIED 	= -1;
  public final static int	IS_FAILURE 	= 0;
  public final static int	IS_SUCCESS 	= 1;
  public final static int	IS_ACTIVE 	= 2;
  public final static int	IS_BLOCKED 	= 3;
  public final static int	IS_ABANDONED 	= 4;

  // The list of (Goal, Intention)s being pursued.
  protected DList		_stacks;

  // Store the currently executing goal for each of access from metalevel
  // actions.
  protected Goal		_currentGoal;

  // The Interpreter overwatching this intention structure
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Default constructor w/ parent interpreter
   * 
   */
  public IntentionStructure(Interpreter interpreter)
  {
    _stacks = new DList();
    _interpreter = interpreter;
  }

  //
  // Member functions
  //
  public DList		getStacks()		{ return _stacks; }
  public DList		getToplevelGoals()	{ return _stacks; }
  public Goal		getCurrentGoal()	{ return _currentGoal; }
  public Interpreter	getInterpreter()	{ return _interpreter; }

  /**
   * Arrange the intention stacks according to their evaluated utilities.
   * 
   * [This seems like it doesn't use the leaf-level utilities, which
   *  would be incorrect.  Need to verify this.]
   */
  public void sortStacksByUtility()
  {
    Goal	currentGoal;
    Goal	stackGoal;
    int		stackLoop1;
    int		stackLoop2;
    double	currentUtility;
    double	highestUtility;
    int		indexOfHighestUtilityGoal;

    /*
    System.out.println("IntentionStructure before sorting:");
    this.print(System.out);
    */

    // Make sure all variable bindings are up to date
    refreshUtilities();

    for (stackLoop1 = 1; stackLoop1 < _stacks.getCount(); stackLoop1++) {
      
      currentGoal = (Goal) _stacks.nth(stackLoop1);

      // Get utility value to start comparisons with
      if (currentGoal.getIntention() != null) {
	//currentGoal.getIntention().getPlan().confirmContext(currentGoal.getIntentionBinding());
	highestUtility = currentGoal.getIntention().evalUtility();
      }
      else {
	highestUtility = currentGoal.evalUtility();
      }

      indexOfHighestUtilityGoal = stackLoop1;

      // Go through the rest of the stacks and see if any have higher utility
      for (stackLoop2 = stackLoop1; stackLoop2 <= _stacks.getCount(); stackLoop2++) {
	stackGoal = (Goal) _stacks.nth(stackLoop2);

	if (stackGoal.getIntention() != null)
	  currentUtility = stackGoal.getIntention().evalUtility();
	else
	  currentUtility = stackGoal.evalUtility();

	if (currentUtility > highestUtility) {
	  highestUtility = currentUtility;
	  indexOfHighestUtilityGoal = stackLoop2;
	}
      }

      // If the highest utility goal isn't the current goal, then switch
      if (indexOfHighestUtilityGoal != stackLoop1) {
	_stacks.replaceNth(_stacks.nth(indexOfHighestUtilityGoal),
			   stackLoop1);
	_stacks.replaceNth(currentGoal, indexOfHighestUtilityGoal);
      }
    }

    /*
    System.out.println("IntentionStructure after sorting:");
    this.print(System.out);
    */
  }

  /**
   * Add an intention to the agent's list of intentions
   * @param intention The instantiated plan to be intended
   * @param force Whether to consider utilities or just put onto intention structure
   */
  public APLElement intend(APLElement intention, boolean force)
  {
    if (intention.getFromGoal() != null &&
	intention.getFromGoal().getIntention() == null) {

	/*
	System.out.println("IntentionStructure::intend: fromGoal = " +
			   intention.getFromGoal() + " [" +
			   intention.getFromGoal().getName() + "]");

	if (intention.getFromGoal() != null) {
	    System.out.print("IntentionStructure::intend: fromGoal.prevGoal = " +
			       intention.getFromGoal().getPrevGoal());
	    if (intention.getFromGoal().getPrevGoal() != null) {
		System.out.println(" [" + intention.getFromGoal().getPrevGoal().getName() + "]");
	    }
	    else {
		System.out.println();
	    }
	}
	*/

	// If the intention is for a top-level goal then do not intend the
	// APL element if it is not of higher utility than the current
	// highest-utility intention stack
	if (!force && intention.getFromGoal().getPrevGoal() == null) {
	    sortStacksByUtility();

	    /*
	    System.out.println("\nJAM::IntentionStructure: after sorting by utility and before intending anything: ");
	    print(System.out);
	    */

	    if ((((Goal) _stacks.first()).getIntention() == null) ||
		(intention.evalUtility() >
		 ((Goal) _stacks.first()).getIntention().evalUtility())) {

		if (_interpreter.getShowAPL()) {
		    System.out.println("\n\nJAM::IntentionStructure: Intending plan: ");
		    intention.print(System.out);
		    System.out.println(" to goal: " + intention.getFromGoal());
		    intention.getFromGoal().print(System.out);
		}

		intention.getFromGoal().setIntention(intention);
		intention.getFromGoal().setStatus(IS_ACTIVE);
		intention.getFromGoal().setRuntimeState(intention.getPlan().getBody().newRuntimeState());
	    }
	    else {

		/*
		System.out.println("JAM::IntentionStructure: plan was not of sufficient utility to intend: ");
		intention.print(System.out);
		System.out.println(" to goal: " + intention.getFromGoal());
		intention.getFromGoal().print(System.out);
		*/

		// If intention is based on a data-driven (i.e,. temp) goal and it isn't
		// being intended then remove that goal from the intention structure
		if (_interpreter.getShowAPL()) {
		    System.out.println("IS::intend: Intention (" + intention.getPlan().getName() + ") not being intended...");

		    // This following commented-out section is only good for one
		    // data-driven plan at a time.  If there are multiple goals
		    // created in a single APL generation then this will not clean
		    // up things completely.
		    //

		    // Maybe all the "hanging" data-driven goals should be removed
		    // at the bottom of this method
    
		    /*
		    if (intention.getFromGoal().getGoalAction() == null) {
			System.out.println("IS::intend:Data-driven goal was not intended, removing spontaneous goal from intention structure...");
			DListEnumerator dle = new DListEnumerator(_stacks);
			Goal tGoal = (Goal) dle.getNext(intention.getFromGoal());
			System.out.println("IS::intend: goal found was : " + tGoal);
			dle.removeThis();
		    }
		    */
		}
	    }
	}
	else {
	    if (_interpreter.getShowAPL()) {
		System.out.println("\n\nJAM::IntentionStructure: Intending plan: ");
		intention.print(System.out);
		System.out.println(" to goal: " + intention.getFromGoal());
		intention.getFromGoal().print(System.out);
	    }

	    intention.getFromGoal().setIntention(intention);
	    intention.getFromGoal().setStatus(IS_ACTIVE);
	    intention.getFromGoal().setRuntimeState(intention.getPlan().getBody().newRuntimeState());
	}
    }

    if (_interpreter.getShowAPL()) {
      System.out.println("JAM::IntentionStructure:Intention Structure now:\n");
      print(System.out);
    }

    return intention;
  }

  /**
   * Execute the highest-utility intention
   * 
   */
  public int think()
  {
    DListEnumerator	dle = null;
    Goal		currentGoal;
    int			returnValue;

    // Sort stacks according to utility
    sortStacksByUtility();
    dle = new DListEnumerator(_stacks);

    // Go through stacks in sorted order and try to run something.
    while ((currentGoal = (Goal) dle.nextElement()) != null) {

      if (currentGoal.getStatus() == IS_ABANDONED) {
	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	  System.out.println("JAM: Plan abandoned!  Removing intention!\n");
	}
	currentGoal.removeIntention(true);
	dle.removeThis();
      }

      if (currentGoal.getRuntimeState() == null) {

	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	  System.out.println("JAM: Goal \"" + currentGoal.getName() +
			     "\" has no runtime state, skipping.\n");
	}

	continue;
      }

      // If this is an achievement goal (currently ACHIEVE or MAINTAIN) then
      // check to see if the goal's already been achieved.
      if (currentGoal.getGoalAction() != null &&
	  (currentGoal.getGoalAction().getType() == Action.ACT_ACHIEVE ||
	   currentGoal.getGoalAction().getType() == Action.ACT_MAINTAIN)) {
	
	Relation rel = currentGoal.getRelation();

	// Binding argument is null in the match() function below because this is
	// a top-level goal and therefore has no variable bindings
	if (_interpreter.getWorldModel().match(rel, null)) {

	  if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	    System.out.println("JAM: Goal \"" + currentGoal.getName() +
			       " already achieved!.\n");
	  }
			       
	  currentGoal.setRuntimeState(null);

	  // MAINTAIN goals should stick around on the Intention Structure
	  // (until, at least, it is explicitly removed by the programmer
	  // using an UNPOST)
	  if (currentGoal.getGoalAction() != null &&
	      currentGoal.getGoalAction().getType() != Action.ACT_MAINTAIN) {
	    dle.removeThis();
	  }

	  renewLeafGoals();
	  return IS_SUCCESS;
	}
      }

      // Check to see if the plan's context is still valid
      if (currentGoal.confirmContext()) {
	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	  System.out.println("\nJAM: Plan context failed!  Removing intention!\n");
	}
	currentGoal.removeIntention(true);

	currentGoal.setStatus(IS_FAILURE);
	currentGoal.setSubgoal(null);

	renewLeafGoals();
	return IS_FAILURE;
      }

      // As soon as something executes (successfully or not) then return.
      if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	System.out.println("JAM: Executing top-level goal \"" +
			   currentGoal.getName() + "\" (plan named \"" + currentGoal.getIntention().getPlan().getName() + "\".");
      }

      _currentGoal = currentGoal;
      switch (returnValue = currentGoal.execute()) {

      case PlanRuntimeState.PLAN_CONSTRUCT_FAILED:

	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	  System.out.println("JAM: Top-level goal \"" + currentGoal.getName() +
			     "\" failed!");
	}
	
	if (currentGoal.getIntention().getPlan().getFailure() != null) {
	  currentGoal.executeFailure();
	}

	currentGoal.removeIntention(false);
	currentGoal.setStatus(IS_FAILURE);
	currentGoal.setSubgoal(null);
	renewLeafGoals();
	return IS_FAILURE;
	
      case  PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE:

	if (_interpreter.getShowGoalList()) {
	  System.out.println("JAM: Just completed top-level goal " + currentGoal.getName());
	}

	currentGoal.setRuntimeState(null);

	// Execute the plan's "postcondition" (i.e., after-effects)
	if (currentGoal.getIntention().getPlan().getEffects() != null) {
	  currentGoal.executeEffects();
	}

	// If a MAINTAIN goal, then leave on the intention structure to
	// continue monitoring for the required state but assert that the
	// goal has been achieved. Otherwise, remove the goal from the
	// intention structure and remove it's particular intention.
	if (currentGoal.getGoalAction() == null ||
	    (currentGoal.getGoalAction() != null &&
	     currentGoal.getGoalAction().getType() != Action.ACT_MAINTAIN)) {
	  dle.removeThis();
	}

	if (currentGoal.getGoalAction() != null &&
	    (currentGoal.getGoalAction().getType() == Action.ACT_ACHIEVE ||
	     currentGoal.getGoalAction().getType() == Action.ACT_MAINTAIN)) {

	  // Assert achieved goal state onto World Model
	  Relation rel = (currentGoal.getGoalAction().getRelation());
	  _interpreter.getWorldModel().assert(rel, null);

	  currentGoal.setIntention(null);
	}

	renewLeafGoals();
	return IS_SUCCESS;
	
      case PlanRuntimeState.PLAN_CONSTRUCT_INCOMP:
	return IS_ACTIVE;

      default:
	System.out.println("JAM: Execution returned invalid value: " + returnValue);
      }
    }

    return IS_ACTIVE;
  }

  /**
   * Perform an agent's plan
   * 
   */
  public int executePlan(Plan plan)
  {
    if (plan == null)
      return IS_SUCCESS;

    int			returnVal;
    Binding		b = new Binding(plan.getSymbolTable());
    PlanRuntimeState	toplevelState = plan.getBody().newRuntimeState();

    while (toplevelState != null) {

      switch (returnVal = toplevelState.execute(b, null, null)) {

      case PlanRuntimeState.PLAN_CONSTRUCT_FAILED:
	System.out.println("JAM::IntentionStructure:executePlan - Plan failed!\n");
	return IS_FAILURE;

      case PlanRuntimeState.PLAN_CONSTRUCT_COMPLETE:
	return IS_SUCCESS;

      case PlanRuntimeState.PLAN_CONSTRUCT_INCOMP:
	break;

      default:
	System.out.print("JAM::IntentionStructure:executePlan - Plan returned ");
	System.out.println("unknown state: " + returnVal + "!\n");
	break;
      }
    }

    return IS_SUCCESS;
  }

  /**
   * Go through all of the stacks and recompute their utility values
   * 
   */
  public void refreshUtilities()
  {
    DListEnumerator	dle = new DListEnumerator(_stacks);
    Goal		stackGoal;
    Goal		subGoal;
    APLElement		intent;

    while ((stackGoal = (Goal) dle.nextElement()) != null) {

      subGoal = stackGoal;
      while (subGoal != null) {
	intent = subGoal.getIntention();
	if (intent != null)
	  intent.getPlan().confirmContext(subGoal.getIntentionBinding());
	subGoal = subGoal.getSubgoal();
      }
    }
  }

  /**
   * Go through all of the stacks and mark all inactive goals as
   * being "new" in order to trigger APL generation.
   * 
   */
  public void renewLeafGoals()
  {
    DListEnumerator	dle = new DListEnumerator(_stacks);
    Goal		stackGoal;
    Goal		subGoal;
    APLElement		intent;

    if (_interpreter.getShowGoalList()) {
      System.out.println("JAM: renewing leaf goals.");
    }
			       
    while ((stackGoal = (Goal) dle.nextElement()) != null) {

      subGoal = stackGoal;
      while (subGoal != null) {
	intent = subGoal.getIntention();
	if (subGoal.getSubgoal() == null &&
	    subGoal.getIntention() == null)
	  subGoal.setNew();
	subGoal = subGoal.getSubgoal();
      }
    }
  }

  /**
   * Old GoalList functionality
   * 
   */
  public boolean allGoalsDone()
  {
    return _stacks.getCount() == 0;
  }

  /**
   * Find and remove a goal from the Intention Structure
   * 
   */
  public void drop(GoalAction goalAction, Binding b)
  {
    // Find the goal and remove it
    Goal	aGoal = null;
    int		stackNum;
    boolean	foundGoal = false;

    if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	System.out.print("\nIS::drop:Dropping goal: ");
    }
    goalAction.print(System.out, b, "", "");

  stackLoop:
    // Go through all the stacks
    for (stackNum = 1; stackNum <= _stacks.getCount(); stackNum++) {
      aGoal = (Goal) _stacks.nth(stackNum);

      // Go through all the subgoals
      while (aGoal != null && aGoal.matchGoal(goalAction, b) == false) {
	//System.out.print("Matching against goal: ");
	//aGoal.getGoalAction().print(System.out, aGoal.getGoalBinding(), "", "");
	aGoal = aGoal.getSubgoal();
      }

      if (aGoal != null) {
	//System.out.print("Matching against goal: ");
	//aGoal.getGoalAction().print(System.out, aGoal.getGoalBinding(), "", "");
      }
      // See if we found a match
      if (aGoal != null && aGoal.matchGoal(goalAction, b) == true) {

	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	    System.out.print("Goals matched, ");
	}
	if (aGoal.getIntention() != null) {
	  if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	    System.out.println("marking as abandoned.");
	  }
	  aGoal.setStatus(IntentionStructure.IS_ABANDONED);
	  return;
	}

	if (_interpreter.getShowIntentionStructure() || _interpreter.getShowGoalList()) {
	  System.out.println("removing stack " + stackNum);
	}

	// Goal not being executed, so take it off the intention structure
	_stacks.removeNth(stackNum);
	stackNum--;
      }
    }
  }

  /**
   * Add the specified goal to the intention structure only if it doesn't
   * already exist.
   * 
   */
  public Goal addUnique(GoalAction goalAction, Relation concludeRel, Goal prevGoal, Binding b)
  {
      Goal aGoal = null;

      _interpreter.setNumGoalsStat(_interpreter.getNumGoalsStat()+1);

      // Go through all the stacks and check all goals for matches
      DListEnumerator	dle = new DListEnumerator(_stacks);

      while (dle.nextElement() != null) {
	  aGoal = (Goal) dle.getThis();

	  while (aGoal != null) {
	      if (aGoal.matchGoal(goalAction, b) == true) {
		  /*
		    System.out.println("\n\nGoal matches an already existing goal!\n\n");
		    aGoal.format(System.out);
		    System.out.println();
		    goalAction.formatArgs(System.out, b, "", "");
		    System.out.println();
		  */
		  return aGoal;
	      }
	      aGoal = aGoal.getSubgoal();
	  }
      }


      /*
	    System.out.println("\nGoal doesn't match any existing goals!");
	    goalAction.formatArgs(System.out, b, "", "");
	    System.out.println();
      */
      // ************* Need to clean this up into other classes *******
      GoalAction	newAction = null;
      Relation		newRelation = null;

      if (concludeRel != null) {

	  newRelation = concludeRel;
	  ExpListEnumerator	expressions = new ExpListEnumerator(concludeRel.getArgs());
	  ExpList		newExpList = new ExpList();
	  Expression	e;

	  while (expressions.hasMoreElements()) {
	      e = (Expression) expressions.nextElement();
	      newExpList.append(new Value(e.eval(b)));
	  }

	  newRelation = new Relation(concludeRel.getName(), newExpList, _interpreter);
      
	  //aGoal = new Goal(null, concludeRel, null, this);
	  aGoal = new Goal(null, newRelation, null, this);
    
	  if (_interpreter.getShowGoalList()) {
	      System.out.println("JAM::IntentionStructure:addUnique(): Adding new data-driven top-level goal " +
				 aGoal.getConcludeRelation().getName());
	  }
	  _stacks.append(aGoal);

	  return aGoal;
      }

      if (prevGoal == null && goalAction != null) {

	  // Create a new goal action where the goal arguments are grounded
	  // (i.e., everything is converted to simple values)
	  newRelation = goalAction.getRelation();
	  ExpListEnumerator	expressions = new ExpListEnumerator(goalAction.getRelation().getArgs());
	  ExpList		newExpList = new ExpList();
	  Expression	e;

	  while (expressions.hasMoreElements()) {
	      e = (Expression) expressions.nextElement();
	      newExpList.append(new Value(e.eval(b)));
	  }

	  newRelation = new Relation(goalAction.getRelation().getName(),
				     newExpList, _interpreter);
      
	  switch (goalAction.getType()) {
	  case Action.ACT_ACHIEVE:
	      newAction = new AchieveGoalAction(goalAction.getName(),
						newRelation,
						goalAction.getUtility(),
						goalAction.getBy(),
						goalAction.getNotBy(),
						_interpreter);
	      break;

	  case Action.ACT_PERFORM:
	      newAction = new PerformGoalAction(goalAction.getName(),
						newRelation,
						goalAction.getUtility(),
						goalAction.getBy(),
						goalAction.getNotBy(),
						_interpreter);
	      break;

	  case Action.ACT_MAINTAIN:
	      newAction = new MaintainGoalAction(goalAction.getName(),
						 newRelation,
						 goalAction.getUtility(),
						 _interpreter);
	      break;

	  case Action.ACT_QUERY:
	      newAction = new QueryGoalAction(goalAction.getName(),
					      newRelation,
					      goalAction.getUtility(),
					      _interpreter);
	      break;

	  }

	  newAction.getRelation().evalArgs(b);

	  // Not a duplicate goal, so add at top-level if no previous goal, otherwise
	  // its already been added to current intention stack (through the Goal constructor).
	  aGoal = new Goal(newAction, concludeRel, prevGoal, this);
    
	  if (prevGoal == null) {
	      if (_interpreter.getShowGoalList()) {
		  System.out.println("JAM::IntentionStructure:addUnique(): Adding new top-level goal " +
				     aGoal.getName());
	      }
	      _stacks.append(aGoal);
	  }
	  else {
	      if (_interpreter.getShowGoalList()) {
		  System.out.println("JAM::IntentionStructure:addUnique(): Adding subgoal " +
				     aGoal.getName() + " to goal " + prevGoal.getName());
	      }
	  }
	  return aGoal;
      }
      else {
	  newAction = goalAction;
      }

      // Not a duplicate goal, so add at top-level if no previous goal, otherwise
      // its already been added to current intention stack (through the Goal constructor).
      aGoal = new Goal(newAction, null, prevGoal, this);
    
      if (prevGoal == null) {
	  if (_interpreter.getShowGoalList()) {
	      System.out.println("JAM::IntentionStructure:addUnique(): Adding new top-level goal " +
				 aGoal.getName());
	  }
	  _stacks.append(aGoal);
      }
      else {
	  if (_interpreter.getShowGoalList()) {
	      System.out.println("JAM::IntentionStructure:addUnique(): Adding subgoal " +
				 aGoal.getName() + " to goal " + prevGoal.getName());
	  }
      }

      return aGoal;

  }

  /**
   * Remove the indicated goal by searching through each intention
   * stack and going through each from top to bottom.
   * 
   */
  public void removeGoal(Goal goal)
  {
    DListEnumerator	stacks = new DListEnumerator(_stacks);
    Goal		stackGoal;

    while ((stackGoal = (Goal) stacks.nextElement()) != null) {

      // Go through stack from top to bottom
      while (stackGoal != null) {

	// If we found the goal then either castrate the subgoal chain
	// at the parent or, if a top-level goal, remove the entire
	// intention stack.
	if (stackGoal == goal) {
	  if (stackGoal.getPrevGoal() != null)
	    stackGoal.getPrevGoal().setSubgoal(null);
	  else
	    stacks.removeThis();
	  return;
	}
	stackGoal = stackGoal.getSubgoal();
      }
    }
  }

  /**
   * Output information about the Intention Structure in a
   * readable format.
   * 
   */
  public void print(PrintStream s)
  {
    DListEnumerator	dle = new DListEnumerator(_stacks);
    Goal		stackGoal;
    Goal		subGoal;
    APLElement		intent;
    int			stackNum = 0;

    s.println("IntentionStructure:");
    s.println("  Stacks: " + _stacks.getCount());

    while ((stackGoal = (Goal) dle.nextElement()) != null) {

      s.println("\nStack#" + stackNum);
      subGoal = stackGoal;
      // Go through all the subgoals
      while (subGoal != null) {
	subGoal.print(s);
	if (subGoal.getIntention() != null)
	  subGoal.getIntention().print(s);
	subGoal = subGoal.getSubgoal();
      }
      stackNum++;
    }

    s.println();
  }
}

