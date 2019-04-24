//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanTable.java,v 1.1 1998/05/09 18:32:25 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanTable.java,v $
//  
//  File              : PlanTable.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:20:54 1997
//  Last Modified By  : <marcush@irs.home.com>
//  Last Modified On  : Sat Mar 18 07:58:19 2000
//  Update Count      : 18
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
 * A JAM agent's plan library
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PlanTable extends Table implements Serializable
{

  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  PlanTable()
  {
    super(128, 128, 63);
  }

  //
  // Member functions
  //

  /**
   * Returns all the plans with the given name.
   * 
   */
  public DList getPlans(String name)
  {
    return getBucket(name);
  }

  /**
   * Marks all plans with the given name so that it will not be
   * considered during APL generation.
   * 
   */
  public void disable(String name)
  {
    Plan	plan;
    int		tableLoop;

    // Go through each entry
    for (tableLoop = 0; tableLoop < getSize(); tableLoop++) {
      plan = (Plan) _table[tableLoop];

      if (plan.getName().equals(name)) {
	plan.disable();
      }
    }
  }

  /**
   * Displays a summary of all of the plans in the plan library
   * 
   */
  public void print(PrintStream s)
  {
    //    super.print(s);

    Plan	plan;
    int		tableLoop;

    s.println("PlanTable: ");

    // Go through each entry
    for (tableLoop = 0; tableLoop < _nextID; tableLoop++) {
      plan = (Plan) _table[tableLoop];
      s.println("Plan " + tableLoop + ":");
      s.println("  Name:\t" + plan.getName());
      s.println("  #Constructs:\t" + plan.getBody().getNumConstructs());
    }
  }

}
