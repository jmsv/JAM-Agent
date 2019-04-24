//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: APL.java,v 1.6 1998/10/11 15:19:41 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\APL.java,v $
//  
//  File              : APL.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:23:13 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Thu Oct 25 21:18:30 2001
//  Update Count      : 204
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997 Marcus J. Huber and Jaeho Lee.
//  Copyright (C) 1997-1999 Marcus J. Huber and Intelligent Reasoning Systems
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
 * A JAM agent's Applicable Plans List (APL)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class APL implements Serializable
{

  //
  // Members
  //
  protected DList		_intentions;

  private Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Generate an Applicable Plan List (APL) from the plans, current
   * state of the world model, and the goals on the intention
   * structure.
   * 
   */
  public APL(PlanTable pt, WorldModelTable wm,
	     IntentionStructure intentionStructure,
	     int metaLevel)
  {
    _intentions = new DList();
    _interpreter = intentionStructure.getInterpreter();

    if (_interpreter.getShowAPL()) {
      System.out.println("JAM::APL: Intention Structure is:\n");
      intentionStructure.print(System.out);
      System.out.println();
    }

    // Make sure to do the WM function first as the Goal function clears
    // "new" field in the world model entries.
    genWMBasedAPL(pt, wm, intentionStructure, metaLevel);
    genGoalBasedAPL(pt, wm, intentionStructure, metaLevel);
  }

  //
  // Member functions
  //

  /**
   * Find the APL elements based on goal matches
   * 
   */
  private void genGoalBasedAPL(PlanTable pt, WorldModelTable wm,
			       IntentionStructure intentionStructure,
			       int metaLevel)
  {
    int			goalNum;
    DListEnumerator	intentionStacks;
    Goal		toplevelGoal;
    Goal		goal;
    DListEnumerator	plans;
    Plan		plan;
    Binding		planBinding;

    _interpreter = intentionStructure.getInterpreter();

    intentionStacks = new DListEnumerator(intentionStructure.getToplevelGoals());

    // Go through each possible stack
    while (intentionStacks.hasMoreElements()) {

      toplevelGoal = (Goal) intentionStacks.nextElement();

      if (_interpreter.getShowAPL()) {
	System.out.println("APL::genGoalBasedAPL: checking stack w/ top-level goal: " +
			   toplevelGoal.getName());
	//	toplevelGoal.print(System.out);
      }

      // Need to move down to the leaf goal of the intention stack
      goal = toplevelGoal;
      while (goal.getSubgoal() != null) {
	if (_interpreter.getShowAPL()) {
	  System.out.println("                                            w/ subgoal: " +
			     goal.getSubgoal().getName());
	}
	goal = goal.getSubgoal();
      }

      if (_interpreter.getShowAPL()) {
	System.out.println("APL::genGoalBasedAPL: Leaf goal of stack is: " + goal.getName());
      }

      if (!goal.generateAPL()) {
	if (_interpreter.getShowAPL()) {
	  System.out.println("APL::genGoalBasedAPL: Skipping goal because generateAPL said no.\n");
	}
	continue;
      }

      // Don't waste time generating an APL if it's a "holder" goal
      // for a CONCLUDE-based intention
      if (goal.getGoalAction() == null) {
	if (_interpreter.getShowAPL()) {
	  System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a CONCLUDE-driven intention.\n");
	}
	continue;
      }

      // Don't waste time generating an APL if it's a satisfied MAINTAIN goal
      // or if it's a MAINTAIN goal and agent is performing metalevel reasoning
      if (goal.getGoalAction().getType() == Action.ACT_MAINTAIN) {
	if (wm.match(goal.getRelation(), null)) {
	  if (_interpreter.getShowAPL()) {
	    System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a satisfied MAINTAIN goal\n");
	  }
	  continue;
	}
	if (metaLevel >= 1) {
	  if (_interpreter.getShowAPL()) {
	    System.out.println("APL::genGoalBasedAPL: Skipping goal because it is a MAINTAIN goal and agent doing meta-reasoning\n");
	  }
	  continue;
	}
      }

      plans = new DListEnumerator(pt.getPlans(goal.getName()));

      // Go through each possible plan
      while (plans.hasMoreElements()) {
	plan = (Plan) plans.nextElement();

	// Only consider those plans that have goals (i.e. not
	// CONCLUDE-driven plans)
	if (plan.getGoalSpecification() != null) {
	  planBinding = new Binding(plan.getSymbolTable());

	  // Filter according to goal type and by ":BY" and ":NOT-BY" lists
	  if (goal.getGoalAction() == null ||
	      (goal.getGoalAction() != null &&
	       !goal.getGoalAction().isEligible(plan, planBinding))) {

	    if (_interpreter.getShowAPL()) {
		System.out.println("APL::genGoalBasedAPL: plan " +
				   plan + "(" + plan.getName() + ")" +
				   " with planBinding " + planBinding +
				   ", is not eligible for goal " +
				   goal);
	    }
	    continue;
	  }

	  // Now go through each possible plan variable binding and
	  // unify goal and plan relations
	  Relation planRelation =  plan.getGoalSpecification().getRelation();
	  if (goal.matchRelation(planRelation, planBinding)) {

	    if (_interpreter.getShowAPL()) {
	      System.out.print("APL::genGoalBasedAPL: instantiating plan " + plan +
			       " with planBinding ");
	      planBinding.format(System.out);
	      System.out.println(", for goal " + goal.getGoalAction().getName() + "\n\n");
	    }
	    instantiate(plan, planBinding, goal);
	  }
	}
      }	

      // Maintain goals should always be considered for APL generation.
      // Once a Maintain goal gets intended, then further APL generation
      // will be passed over by the generateAPL() member function above
      // because of the existance of the intention.
      if (goal.getGoalAction() != null &&
	  goal.getGoalAction().getType() != Action.ACT_MAINTAIN) {
	goal.clearNew();
      }
    }
    wm.clearNewAll();
  }

  /**
   * Find the APL elements based on world model matches
   * 
   */
  private void genWMBasedAPL(PlanTable pt, WorldModelTable wm,
			     IntentionStructure intentionStructure,
			     int metaLevel)
  {
    Plan		plan;
    Binding		planBinding;
    Relation		concludeRelation;
    Binding		oldBinding;
    Binding		newBinding;

    // Do a quick check to see if anything changed in the World Model.
    // If not then there's nothing to match so simply return.
    if (!wm.anyNew()) {
      if (_interpreter.getShowAPL()) {
	System.out.println("APL::genWMBasedAPL: No new World Model entries.");
      }
      return;
    }

    // Go through each plan and find those that have CONCLUDE
    // specifications.
    for (int planLoop=0; planLoop < pt.getSize(); planLoop++) {
      plan = (Plan) pt.lookup(planLoop);

      concludeRelation =  plan.getConcludeSpecification();

      if (concludeRelation != null) {

	if (_interpreter.getShowAPL()) {
	  System.out.println("APL::genWMBasedAPL: Checking plan (" + planLoop + " of " +
			     pt.getSize() + "): " + plan.getName());
	  System.out.println("          w/ CONCLUDE relation of: " + concludeRelation.getName());
	}
	// Of those that do, check to see if the world model reference
	// matches a newly changed WM entry.
	planBinding = new Binding(plan.getSymbolTable());

	// Loop through all World Model entries matching the relation
	BindingList		bl = new BindingList(planBinding);
	BindingListEnumerator	ble = new BindingListEnumerator(bl);
	Binding			b;

	// Loop through bl
	while (ble.hasMoreElements()) {
	  oldBinding = (Binding) ble.nextElement();

	  WorldModelTableBucketEnumerator wmtbe;
	  wmtbe = new WorldModelTableBucketEnumerator(wm, concludeRelation);
	  for (newBinding = new Binding(oldBinding);
	       wmtbe.getNext(newBinding) != null;
	       newBinding = new Binding(oldBinding)) {
	    if (newBinding.isNewWMBinding()) {
	      ble.insertHere(newBinding);
	    }
	  }
	  ble.removeThis();
	}

	if (_interpreter.getShowAPL()) {
	  System.out.println("APL::genWMBasedAPL: BindingList after loop is " + bl.getCount() + " elements long.\n");
	}

	// If no matches then continue with next plan;
	if (bl.getCount() == 0)
	    //return;
	    continue;

	// If at least one match then check context and add to APL if context passes
	if (plan.checkContext(bl) &&
	    plan.checkPrecondition(bl)) {

	  // Iterate through the bindings in the list
	  ble = new BindingListEnumerator(bl);
	  while (ble.hasMoreElements()) {
	    b = (Binding) ble.nextElement();

	    if (_interpreter.getShowAPL()) {
	      System.out.print("APL::genWMBasedAPL: Adding APL element w/ plan: " +
			       plan.getName() + " and binding: ");
	      b.format(System.out);
	      System.out.println();
	    }

	    // Create a temporary goal to "hold" the intention on the intention
	    // structure.
	    Goal g;
	    if (_interpreter.getShowAPL() || _interpreter.getShowIntentionStructure()) {
	      System.out.println("APL::genWMBasedAPL: Adding null \"CONCLUDE\" goal to IntentionStructure");
	    }
	    // Maybe I should wait to add a goal to the Intention Structure until the
	    // plan is actually intended??
	    //System.out.println("Adding a data-driven goal to the intention structure.  Relation is:");
	    //concludeRelation.print(System.out, b);
	    g = intentionStructure.addUnique((GoalAction) null, concludeRelation, (Goal) null, b);
	    //System.out.println("Intention structure is now:");
	    //intentionStructure.print(System.out);
	    add(plan, g, b);
	  }
	}
	else {
	  if (_interpreter.getShowAPL()) {
	    System.out.println("APL::genWMBasedAPL: Plan: \"" + plan.getName() +
			       "\" did not pass context/precondition check.");
	  }
	}
      }
      else {
	  if (_interpreter.getShowAPL()) {
	      System.out.println("APL::genWMBasedAPL: Ignoring plan (" + planLoop + " of " +
			     pt.getSize() + "): \"" + plan.getName() + "\"");
	      System.out.println("          no CONCLUDE relation");
	  }
      }
    }
  }

  /**
   * Append an applicable plan onto the list of possibilities
   * 
   */
  protected APLElement add(Plan p, Goal g, Binding b)
  {
    APLElement se = new APLElement(p, g, b);
    _intentions.append(se);
    return se;
  }

  /**
   * Return a random number betweeon 0 and the indicated range
   * 
   */
  protected int randomUniform(int range)
  {
    Random ran = new Random();
    return Math.abs(ran.nextInt() % range);
  }

  /**
   * Determine the number of applicable plans
   * 
   */
  public int getSize()
  {
    return _intentions.getCount();
  }

  /**
   * Retrieve the first applicable plan in the list
   * 
   */
  public APLElement getFirst()
  {
    return (_intentions.getCount() > 0) ? (APLElement) _intentions.first() : null;
  }

  /**
   * Retrieve the nth element in the list
   * 
   */
  public APLElement nth(int num)
  {
    return (_intentions.getCount() > 0) ? (APLElement) _intentions.nth(num) : null;
  }

  /**
   * Retrieve a random applicable plan in the list
   * 
   */
  public APLElement getRandom()
  {
    Random rand = new Random();
    return (APLElement) _intentions.nth(Math.abs(rand.nextInt() % _intentions.getCount()));
  }

  /**
   * Retrieve a random applicable plan from a list of those with
   * the highest utility
   * 
   */
  public APLElement getUtilityRandom()
  {
    APLElement	intention;
    int[]	index = new int[_intentions.getCount()];
    int		maxCount = 0;
    int		i;
    double	p;
    double	maxUtility = -Double.MAX_VALUE;
    int		maxIndex;
    int		randNum;
    Random	rand = new Random();

    index[0] = 0;

    if (_intentions.getCount() == 0)
      return null;

    if (_interpreter.getShowAPL()) {
      System.out.println("APL: Searching through " + _intentions.getCount()
			 + " plans for maximal utility");
    }

    for (i = 1; i <= _intentions.getCount(); i++) {
      if ((p = ((APLElement) _intentions.nth(i)).evalUtility()) > maxUtility) {

	if (_interpreter.getShowAPL()) {
	  System.out.println("APL: Plan " + i + " has new maxUtility utility of: " + p);
	}

	maxUtility = p;
	maxCount = 1;
	index[maxCount - 1] = i;
      }
      else if (p == maxUtility) {
	index[++maxCount - 1] = i;
      }
    }

    if (_interpreter.getShowAPL()) {
      System.out.println("APL: " + maxCount + " plans found with maxUtility of: " + maxUtility);
    }

    randNum = Math.abs(rand.nextInt());
    maxIndex = index[randNum % maxCount];

    if (_interpreter.getShowAPL()) {
      System.out.println("APL: randomly selected item was index: " + maxIndex);
    }

    return (APLElement) _intentions.nth(maxIndex);
  }

  /**
   * Retrieve the first applicable plan from a list of those with
   * the highest utility
   * 
   */
  public APLElement getUtilityFirst()
  {
    APLElement	intention;
    int[]	index = new int[_intentions.getCount()];
    int		maxCount = 0;
    int		i;
    double	p;
    double	maxUtility = -Double.MAX_VALUE;

    index[0] = 0;

    for (i = 1; i <= _intentions.getCount(); i++)
      if ((p = ((APLElement) _intentions.nth(i)).evalUtility()) > maxUtility) {
	maxUtility = p;
	maxCount = 0;
	index[0] = i;
      }
      else if (p == maxUtility) {
	index[++maxCount] = i;
      }

    return (APLElement) _intentions.first();
  }

  /**
   * Go through and find all combinations of variable bindings
   * for the plan/goal combination
   * 
   */
  public void instantiate(Plan plan, Binding planBinding, Goal goal)
  {
    BindingList			bindingList = new BindingList(planBinding);
    BindingListEnumerator	ble;
    Binding			b;

    /*
    System.out.println("APL::instantiate: bindingList is:\n");
    bindingList.print(System.out);

    System.out.println("Checking bindingList against context and precondition expressions.");
    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException e) {
      System.out.println("Interrupted sleep primitive!");
    }
    */

    if (plan.checkContext(bindingList)) {

      //System.out.println("bindingList passed context check.");
//      bindingList.print(System.out);

      if (plan.checkPrecondition(bindingList)) {

	//System.out.println("bindingList passed precondition check.");
//	bindingList.print(System.out);

	ble = new BindingListEnumerator(bindingList);

	// Iterate through the bindings in the list
	while (ble.hasMoreElements()) {
	  b = (Binding) ble.nextElement();

	  if (goal.isNew() || b.isNewWMBinding()) {
	    if (_interpreter.getShowAPL()) {
	      System.out.println("APL::instantiate: instantiating APL for plan: " +
				 plan + ":\"" + plan.getName() + "\"\n" +
				 "goal: ");
	      goal.print(System.out);
	      System.out.println("and binding: ");
	      b.print(System.out);

	    }
	    add(plan, goal, b);
	  }
	}
      }
    }
  }

  /**
   * Display information about the applicable plans
   * 
   */
  public void print(PrintStream s)
  {
    int		i;
    APLElement	intention;
    
    s.println("Applicable Plan List:\nSize: " + _intentions.getCount());

    for (i = 1, intention = (APLElement) _intentions.nth(i);
	 i <= _intentions.getCount();
	 i++, intention = (APLElement) _intentions.nth(i)) {
      intention.print(s);
    }
  }

}
