//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PostAction.java,v 1.4 1998/11/04 18:06:06 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PostAction.java,v $
//  
//  File              : PostAction.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:20:50 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Tue Oct 23 19:29:43 2001
//  Update Count      : 39
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
 * A built-in JAM primitive action for adding a goal to the
 * JAM goal list.
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PostAction extends Action implements Serializable
{

  //
  // Members
  //
  protected GoalAction		_goalAction;
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  PostAction(GoalAction goalAction, Interpreter interpreter)
  {
    super("POST");
    _goalAction = goalAction;
    _interpreter = interpreter;
    _actType = ACT_POST;
  }

  //
  // Member functions
  //
  public boolean	isExecutableAction()	{ return true; }

  /**
   * Add a top-level goal to the agent
   * 
   */
  public int		execute(Binding b, Goal currentGoal)
  {
    _interpreter.getIntentionStructure().addUnique(_goalAction, null, (Goal) null, b);
    if (_interpreter.getShowIntentionStructure() ||
	_interpreter.getShowGoalList()) {
      _interpreter.getIntentionStructure().print(System.out);
    }
    return ACT_SUCCEEDED;
  }

  /**
   * Output information to the stream in an in-line manner.
   * 
   */
  public void		format(PrintStream s, Binding b)
  {
    s.print("POST: ");
    _goalAction.formatArgs(s, b, "", "");
  }

}
