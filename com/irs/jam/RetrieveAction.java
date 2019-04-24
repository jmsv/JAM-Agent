//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: RetrieveAction.java,v 1.4 1998/11/04 18:14:37 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\RetrieveAction.java,v $
//  
//  File              : RetrieveAction.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:36 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:22 1999
//  Update Count      : 23
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
 * A built-in JAM primitive action for binding plan variables
 * with world model entries.  This is similar to a FactAction,
 * but passed-in variables bindings are NOT used for matching.
 * Rather, the variables are overwritten with data from the
 * world model whenever any world model entry exists with the
 * given name.
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class RetrieveAction extends WorldModelAction implements Serializable
{
  //
  // Constructors
  //

  /**
   * Constructor w/ relation to retrieve from the World Model as
   * an argument in addition to the interpreter.
   *
   */
  RetrieveAction(Relation r, Interpreter interpreter)
  {
    super(r, interpreter);
    _actType = ACT_RETRIEVE;
  }

  //
  // Member functions
  //
  public boolean	isExecutableAction()	{ return true; }

  /**
   * Retrieve the relation from the World Model.
   *
   */
  public int execute(Binding b, Goal currentGoal)
  {
    // unbound variables before matching
    if (b != null)
      b.unbindVariables(_relation.getArgs());
    
    // now try match to get new values
    return getInterpreter().getWorldModel().match(_relation, b) ? ACT_SUCCEEDED : ACT_FAILED; 
  }


  /**
   * Output information to the stream in an in-line manner.
   *
   */
  public void format(PrintStream s, Binding b)
  {
    s.print("RETRIEVE ");
    _relation.format(s, b);
    s.print(";");
  }

}
