//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Action.java,v 1.2 1998/05/09 17:52:56 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Action.java,v $
//  
//  File              : Action.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:45 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:37 1999
//  Update Count      : 29
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

/**
 *
 * An abstract base class for representing the agent's actions
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class Action implements Serializable
{
  //
  // Members
  //
  public final static int	ACT_UNDEFINED		= -1;

  public final static int	ACT_CANNOT_EXECUTE 	= -2;
  public final static int	ACT_FAILED 		= -3;
  public final static int	ACT_SUCCEEDED 		= -4;

  public final static int	ACT_PRIMITIVE		= 1;
  public final static int	ACT_LOAD		= 2;
  public final static int	ACT_PARSE		= 3;
  public final static int	ACT_ASSIGN		= 4;
  public final static int	ACT_FACT		= 5;
  public final static int	ACT_RETRIEVE		= 6;
  public final static int	ACT_TEST		= 7;
  public final static int	ACT_ASSERT 		= 8;
  public final static int	ACT_FAIL		= 9;
  public final static int	ACT_RETRACT		= 10;
  public final static int	ACT_UPDATE		= 11;
  public final static int	ACT_POST		= 12;
  public final static int	ACT_UNPOST		= 13;
  public final static int	ACT_GOAL_ACTION 	= 14;
  public final static int	ACT_ACHIEVE		= 15;
  public final static int	ACT_MAINTAIN		= 16;
  public final static int	ACT_WAIT		= 17;
  public final static int	ACT_QUERY		= 18;
  public final static int	ACT_OBJECT		= 19;
  public final static int	ACT_PERFORM		= 20;

  String			_name;

  // trace information
  String			_file;
  int				_line;

  // Test information
  protected int			_actType;
  
  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public Action()
  {
    _actType = ACT_UNDEFINED;
  }

  /**
   * 
   * 
   */
  public Action(int actType)
  {
    _actType = actType;
  }

  /**
   * 
   * 
   */
  public Action(String name)
  {
    _name = new String(name);
    _actType = ACT_UNDEFINED;
  }

  //
  // Member functions
  //
  public String		setTraceFile(String file) { return _file = file; }
  public String		getTraceFile()		{ return _file; }
  public int		setTraceLine(int line)	{ return _line = line; }
  public int		getTraceLine()		{ return _line; }
  public String		getName()		{ return _name; }
  public Relation	getRelation()		{ return null; }
  public int		getType()		{ return _actType; }

  /**
   * Initialize values for the action name, filename, and file line number
   * 
   */
  private void init(String name, String file, int lineNum)
  {
    _file = file;
    _name = name;
    _line = lineNum;
  }

  /**
   * Set values for the filename and file linu number
   * 
   */
  public void setTrace(String file, int line)
  {
    _file = file;
    _line = line;
  }

  /**
   * Perform the action's functionality
   * 
   */
  public int execute(Binding b, Goal currentGoal)
  {
    return ACT_SUCCEEDED;
  }

  public abstract boolean	isExecutableAction();
  public abstract void		format(PrintStream s, Binding b);
}


