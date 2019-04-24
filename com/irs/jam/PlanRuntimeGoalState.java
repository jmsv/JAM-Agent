//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanRuntimeGoalState.java,v 1.7 1998/11/04 18:12:05 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanRuntimeGoalState.java,v $
//  
//  File              : PlanRuntimeGoalState.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:17 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Tue Oct 23 19:29:34 2001
//  Update Count      : 68
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
 * Represents a subgoaling construct
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanRuntimeGoalState extends PlanRuntimeState implements Serializable
{

  //
  // Members
  //
  protected Goal		_subgoal;
  
  //
  // Constructors
  //

  /**
   * Constructor w/ goal specifier as argument
   * 
   */
  public PlanRuntimeGoalState(PlanSimpleConstruct se)
  {
    _thisConstruct = se;
    _substate = null;		// It's a primitive action
    _subgoal = null;
  }

  //
  // Member functions
  //
  public Goal		getSubgoal()		{ return _subgoal; }
  public Goal		setSubgoal(Goal g)	{ return _subgoal = g; }


  // !!!!!NEED TO SIMPLIFY THIS!!!!!
  /**
   * Find an applicable plan if necessary and execute the plan if it exists.
   * Also, deal with plan/subgoal failure and success appropriately.
   *
   */
  public int execute(Binding b, Goal thisGoal, Goal prevGoal)
  {
    Goal		newGoal;
    int			returnValue;
    PlanRuntimeState	activeState;
    PlanAtomicConstruct	failureSection;

    if (thisGoal == null) {
      System.out.println("\nJAM: Warning!  Detected goal action within execution of Observer body.  Ignoring.\n");
      return PLAN_CONSTRUCT_COMPLETE;
    }

    // If this is an ACHIEVEment goal then check to see if the goal's already been
    // achieved.
    if (((PlanSimpleConstruct)_thisConstruct).getAction().getType() == Action.ACT_ACHIEVE) {
      
      Relation rel = ((GoalAction) ((PlanSimpleConstruct)_thisConstruct).getAction()).getGoal();
      
      if (thisGoal.getIntentionStructure().getInterpreter().getWorldModel().match(rel, b)) {
	if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
	    thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
	  System.out.println("JAM:Already completed ACHIEVE goal.");
	}
	return PLAN_CONSTRUCT_COMPLETE;
      }
    }

    // Check to see if we've already posted a subgoal.  If there is,
    // we then need to see if an intention has been found for it.  If
    // so, we can execute something in the intention.
    if (_subgoal != null) {

      if (_subgoal.getIntention() != null) {
	activeState = _subgoal.getRuntimeState();

	if (activeState != null) {
	
	  // At this point we'll be executing an action from a PLAN so we have
	  // to check the subgoal to see if it's still valid.
	  if (!_subgoal.getIntention().getPlan().confirmContext(_subgoal.getIntentionBinding()) ||
	      (_subgoal.getStatus() == IntentionStructure.IS_ABANDONED)) {
	  
	    // OOPS!  subgoal invalid!
	    // Clean up the stack. Replace some of these with something like
	    // IS.force_goal_failure()???
	    _subgoal.removeIntention(true);
	    thisGoal.getIntentionStructure().removeGoal(_subgoal);
	    _subgoal.setStatus(IntentionStructure.IS_FAILURE);
	    return PLAN_CONSTRUCT_FAILED;
	  }
	
	  // NOW we can run something!
	  returnValue = activeState.execute(_subgoal.getIntentionBinding(),
					    _subgoal, _subgoal.getPrevGoal());
	
	  if (returnValue == PLAN_CONSTRUCT_FAILED) {
	  
	    if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
	      System.out.println("JAM:Subgoal \"" + _subgoal.getName() + "\" failed!\n");
	    }

	    // Clean up the stack.  This might be the exact same as the context
	    // failure case above so I might want to create something like
	    // IS.force_goal_failure() and call it here and elsewhere.
	    _subgoal.removeIntention(true);
	    _subgoal.setStatus(IntentionStructure.IS_FAILURE);
	    thisGoal.getIntentionStructure().removeGoal(_subgoal);
	    thisGoal.setSubgoal(null);
	    _subgoal = null;
	    return PLAN_CONSTRUCT_FAILED;
	  }
	  else if (returnValue == PLAN_CONSTRUCT_INCOMP) {
	    return PLAN_CONSTRUCT_INCOMP;
	  }
	  else { // if (returnValue == PLAN_CONSTRUCT_COMPLETE)

	    if (_subgoal.getIntention().getPlan().getEffects() != null) {
	      _subgoal.executeEffects();
	    }

	    // Switch according to the _PLAN'S_ goal specification rather than
	    // the goal's specification
	    switch (_subgoal.getIntention().getPlan().getGoalSpecification().getType()) {

	    case Action.ACT_ACHIEVE:

	      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
		System.out.println("JAM:Just completed ACHIEVE goal.");
	      }

	      // Assert achieved goal state onto World Model
	      Relation rel = ((GoalAction) ((PlanSimpleConstruct)_thisConstruct).getAction()).getGoal();
	      thisGoal.getIntentionStructure().getInterpreter().getWorldModel().assert(rel, b);

	      if (thisGoal.getIntentionStructure().getInterpreter().getShowWorldModel()) {
		thisGoal.getIntentionStructure().getInterpreter().getWorldModel().print(System.out);
	      }

	      break;

	    case Action.ACT_PERFORM:
	      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
		System.out.println("JAM:Just completed PERFORM goal.");
	      }
	      break;

	    case Action.ACT_WAIT:
	      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
		System.out.println("JAM:Just completed WAIT goal.");
	      }
	      break;

	    case Action.ACT_MAINTAIN:
	      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
		System.out.println("JAM:Just completed MAINTAIN goal.");
	      }
	      break;

	    case Action.ACT_QUERY:
	      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
		  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
		System.out.println("JAM:Just completed QUERY goal.");
	      }
	      break;

	    default:
	      System.out.println("JAM:Just completed UNKNOWN type of goal:" +
				 ((PlanSimpleConstruct)_thisConstruct).getAction().getType());
	      break;
	    }

	    _subgoal = null;
	    thisGoal.setSubgoal(null);

	    return PLAN_CONSTRUCT_COMPLETE;
	  }
	}
      
	else { // goal must be done
	  System.out.println("JAM::PlanRuntimeGoalState: ERROR! activeState == null!");
	}
      }

      else { // no intention for subgoal yet

	thisGoal.setStatus(IntentionStructure.IS_BLOCKED);
	if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
	    thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
	  System.out.println("JAM::PlanRuntimeGoalState: Setting goal status to BLOCKED for goal:");
	  thisGoal.print(System.out);
	}
	thisGoal.getIntentionStructure().renewLeafGoals();

	return PLAN_CONSTRUCT_INCOMP;
      }
    }

    // Otherwise we need to post the subgoal to the system.
    else {

      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
	  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
	System.out.print("JAM:PlanRuntimeGoalState: Adding subgoal:\n  ");
	((PlanSimpleConstruct)getThisConstruct()).getAction().format(System.out, b);
	System.out.println();
      }

      newGoal = thisGoal.getIntentionStructure().addUnique((GoalAction)((PlanSimpleConstruct)getThisConstruct()).getAction(),
							   null,
							   thisGoal,
							   b);

      // Set state's subgoal field
      _subgoal = newGoal;
      _subgoal.setStatus(IntentionStructure.IS_ACTIVE);
    
      if (thisGoal.getIntentionStructure().getInterpreter().getShowIntentionStructure() ||
	  thisGoal.getIntentionStructure().getInterpreter().getShowGoalList()) {
	System.out.println("JAM:Just posted subgoal " + newGoal.getName() + " to system.\n");
	thisGoal.getIntentionStructure().print(System.out);
      }
    
      return PLAN_CONSTRUCT_INCOMP;
    }

    System.out.println("JAM::PlanRuntimeGoalState: Reached end of logic without return!\n");
    return PLAN_CONSTRUCT_INCOMP;
  }

  /**
   * Add an intention to the agent's Intention Structure
   *
   */
  public void intend(APLElement ae)
  {
    APLElement	newAPL = new APLElement();

    // If new goal, then set pointers
    if (_subgoal == null)
      _subgoal = ae.getFromGoal();
  
    // Need to copy APLElement
    newAPL.copy(ae);
  
    // Add instantiated intention to goal
    _subgoal.setIntention(newAPL);
    _subgoal.setStatus(IntentionStructure.IS_ACTIVE);

    _subgoal.setRuntimeState(newAPL.getPlan().getBody().newRuntimeState());

    if (newAPL.getFromGoal().getPrevGoal() == null)
      _subgoal.setPrevGoal(newAPL.getFromGoal().getPrevGoal());
  }

}
