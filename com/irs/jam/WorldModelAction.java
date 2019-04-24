//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: WorldModelAction.java,v 1.4 1998/11/04 18:16:02 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\WorldModelAction.java,v $
//  
//  File              : WorldModelAction.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:18:54 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:18 1999
//  Update Count      : 26
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
 * A JAM primitive action modifying the JAM world model
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class WorldModelAction extends Action implements Serializable
{
  //
  // Members
  //
  protected Relation 	_relation;
  protected Interpreter	_interpreter;

  //
  // Constructors
  //

  /**
   * Constructor w/ World Model relation and interpreter (to simplify
   * access to the agent's World Model) as arguments.
   *
   */
  WorldModelAction(Relation relation, Interpreter interpreter)
  {
    super(relation.getName());

    _relation = relation;
    _interpreter = interpreter;
  }
  
  //
  // Member functions
  //

  public Relation	getRelation()		{ return _relation; }
  public Interpreter	getInterpreter()	{ return _interpreter; }

  /**
   * Perform the necessary world model function.
   *
   */
  public int execute(Binding b, Goal currentGoal)
  {
    return ACT_CANNOT_EXECUTE;
  }

}
