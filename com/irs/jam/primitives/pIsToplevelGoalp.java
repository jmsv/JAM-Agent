//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: pIsToplevelGoalp.java,v 1.1 2000/04/17 02:26:29 marcush Exp $
//  $Source: /rfs/users1/reynolds/dd21/java/com/orincon/DD21/taskmanager/primitives/pIsToplevelGoalp.java,v $
//  
//  File              : pIsToplevelGoalp.java
//  Author(s)         : <marcush@irs.home.com>
//  
//  Description       : A predicate that indicates whether a goal
//                      is a toplevel goal or a subgoal.
//  
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Thu Mar 02 20:17:03 2000
//  Last Modified By  :
//  Last Modified On  : Sun Apr 16 19:19:50 2000
//  Update Count      : 21
//  
//  Copyright (C) 2000 Intelligent Reasoning Systems.
//  
//  
//////////////////////////////////////////////////////////////////////////////

package com.irs.jam.primitives;

import java.io.*;

import com.irs.jam.*;

/**
 * A JAM predicate that indicates whether a goal is a toplevel goal
 * or a subgoal.
 * <p>
 *
 * @author Marc Huber
 * @version $Id: pIsToplevelGoalp.java,v 1.1 2000/04/17 02:26:29 marcush Exp $ $Source: /rfs/users1/reynolds/dd21/java/com/orincon/DD21/taskmanager/primitives/pIsToplevelGoalp.java,v $
 *
 */
public class pIsToplevelGoalp implements PrimitiveAction
{
    //
    // Members
    //

    // none

    //
    // Constructors
    //

    // none

    //
    // Methods
    //

    /**
     *
     * Indicate whether the goal associated with the currently EXECUTING
     * plan is a subgoal or not.  Modified from the old IPC code to relate
     * only to the currently executing plan.  Note that the plan name
     * argument will not work across intention stacks.  That is, this
     * primitive only checks for the plan name being toplevel of THIS
     * intention stack (i.e., the one that's current running).
     * 
     * @return true if goal is a toplevel goal, false if a subgoal.
     *
     */
    public Value execute(String name, int arity, ExpList args,
			 Binding binding, Goal currentGoal)
    {
	if (arity != 1) {
	    System.out.println("Invalid number of arguments: " + arity +
			       " to function \"" + name + "\"\n");
	    return Value.FALSE;
	}

	ExpListEnumerator ele = new ExpListEnumerator(args);
	String planName = ((Expression) ele.nextElement()).eval(binding).getString();
	
	APLElement intention = currentGoal.getIntention();

	// currentGoal points to the top goal of the stack, so if planName
	if (intention.getPlan().getName().equals(planName)) {
	    return Value.TRUE;
	}

	// We must be in a subgoal since the toplevel plan name doesn't match.
	return Value.FALSE;
    }
}
