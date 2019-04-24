//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: RetrieveAllAction.java,v 1.4 1998/11/04 18:14:37 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\RetrieveAllAction.java,v $
//  
//  File              : RetrieveAllAction.java
//  Original author(s): Marcus J. Huber <marcush@irs.home.com>
//  Created On        : Tue Sep 30 14:19:36 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Mon Sep 03 16:33:02 2001
//  Update Count      : 103
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998-1999 Marcus J. Huber and Intelligent Reasoning Systems.
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
//  Marcus J. Huber and Intelligent Reasoning Systems shall not be
//  liable for any damages, including special, indirect, incidental, or
//  consequential damages, with respect to any claim arising out of or
//  in connection with the use of the software, even if they have been
//  or are hereafter advised of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package com.irs.jam;

import java.io.*;

/**
 *
 * A built-in JAM primitive action for binding plan variables with world
 * model entries and providing a means for access to ALL of the matching
 * relations rather than just one as RETRIEVE does.
 *
 * @author Marc Huber
 *
 **/

public class RetrieveAllAction extends WorldModelAction implements Serializable
{
  Variable	_var;

  //
  // Constructors
  //

  /**
   * Constructor w/ relation to retrieve from the World Model as
   * an argument in addition to the interpreter.
   *
   */
  RetrieveAllAction(Variable v, Relation r, Interpreter interpreter)
  {
    super(r, interpreter);
    _var = v;
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
    WorldModelTableBucketEnumerator	wmtbe;
    WorldModelRelation			wrel;
    Relation				rel;

    wmtbe = new WorldModelTableBucketEnumerator(_interpreter.getWorldModel(),
						_relation);

    // Bind the local variable to the wmtbe
    b.setValue(_var, new Value(wmtbe));

    return ACT_SUCCEEDED;
  }


  /**
   * Output information to the stream in an in-line manner.
   *
   */
  public void format(PrintStream s, Binding b)
  {
    s.print("RETRIEVEALL ");
    _relation.format(s, b);
    s.print(";");
  }

}
