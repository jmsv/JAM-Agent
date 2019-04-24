//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: GoalAction.java,v 1.4 1998/11/04 17:47:01 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\GoalAction.java,v $
//  
//  File              : GoalAction.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:56 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:32 1999
//  Update Count      : 35
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
 * A subgoal action within a plan.
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class GoalAction extends Action implements Serializable
{
  //
  // Members
  //
  protected Relation 		_goal;
  protected Expression 		_utility;

  protected ExpList		_by;
  protected ExpList		_notBy;

  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Full constructor
   * 
   */
  GoalAction(String name, Relation goal,
	     Expression utility,
	     ExpList by, ExpList not_by,
	     Interpreter interpreter)
  {
    super(name);

    _goal = goal;
    _utility = utility;
    _by = by;
    _notBy = not_by;
    _interpreter = interpreter;
  }
  
  /**
   * Partial constructor
   * 
   */
  GoalAction(String name, Relation goal, Expression utility,
	     Interpreter interpreter)
  {
    super(name);

    _goal = goal;
    _utility = utility;
    _by = null;
    _notBy = null;
    _interpreter = interpreter;
  }
  
  //
  // Member functions
  //
  public Relation	getGoal()			{ return _goal; }
  public Relation	getRelation()			{ return _goal; }
  public Relation	setRelation(Relation r)		{ return _goal = r ; }
  public Expression	getUtility()			{ return _utility; }
  public Expression 	setUtility(Expression utility)	{ return _utility = utility; }
  public ExpList	setBy(ExpList by)		{ return _by = by; }
  public ExpList	getBy()				{ return _by; }
  public ExpList	setNotBy(ExpList notBy)		{ return _notBy = notBy; }
  public ExpList	getNotBy()			{ return _notBy; }
  public boolean	isExecutableAction()		{ return false; }
  public int		execute(Binding b, Goal currentGoal) { return ACT_CANNOT_EXECUTE; }

  //

  /**
   * Check to see if the goal is applicable to the specified plan
   *
   */
  public boolean	isEligible(Plan plan, Binding binding)
  {
    Value	str;
    String	planName = plan.getName();
    
    // Check goal specifications
    // Currently, PERFORMS can use any plans, but ACHIEVE and MAINTAIN
    // can only use plans with an ACHIEVE GOAL: specification.
    if (((getType() == ACT_ACHIEVE) ||
	 (getType() == ACT_MAINTAIN)) &&
	(plan.getGoalSpecification().getType() != ACT_ACHIEVE)) {
      return false;
    }
    else if (getType() == ACT_PERFORM) {
      // Accept everything
    }
    //    else if (getType() == ACT_QUERY) {
      // 
    //    }


    // Check ":BY" list
    if (_by != null) {
      ExpListEnumerator bys = new ExpListEnumerator(_by);
      
      while(bys.hasMoreElements()) {
	str = ((Expression) bys.nextElement()).eval(binding);

	if (str.isDefined() && str.getString().compareTo(planName) == 0)
	  return true;
      }

      return false;
    }

    // Check ":NOT-BY" list
    if (_notBy != null) {

      ExpListEnumerator notbys = new ExpListEnumerator(_notBy);

      while(notbys.hasMoreElements()) {
	str = ((Expression) notbys.nextElement()).eval(binding);

	if (str.isDefined() && str.getString().compareTo(planName) == 0)
	  return false;
      }

      return true;
    }

    return true;
  }

  /**
    * Format the output and don't worry about being being printed out
    * in-line with other information.
    * 
    */
  public void print(PrintStream s, Binding b,
		    String head, String tail)
  {
    s.print(head);
    _goal.print(s, b);
    s.print(tail);
  }

  /**
    * Format the output so that it's conducive to being printed out
    * in-line with other information.
    * 
    */
  public void formatArgs(PrintStream s, Binding b,
			 String head, String tail)
  {
    s.print(head);
    _goal.format(s, b);
    s.print(tail);
  }

  /**
    * 
    * 
    */
  public double evalUtility(Binding binding)
  {
    return (_utility != null) ? _utility.eval(binding).getReal() : 0.0;
  }

  public abstract void format(PrintStream s, Binding b);

}
