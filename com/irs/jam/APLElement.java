//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: APLElement.java,v 1.2 1998/05/09 17:53:21 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\APLElement.java,v $
//  
//  File              : APLElement.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:23:11 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Tue Aug 14 15:25:21 2001
//  Update Count      : 23
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
 * Represents an agent's Applicable Plans List (APL)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class APLElement implements Serializable
{

  //
  // Members
  //
  protected Plan	_plan;
  protected Goal	_fromGoal;
  protected Binding	_binding;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  APLElement()
  {
    _plan = null;
    _fromGoal = null;
    _binding = null;
  }

  /**
   * 
   * 
   */
  APLElement(APLElement ae)
  {
    _plan = ae.getPlan();
    _fromGoal = ae.getFromGoal();
    _binding = ae.getBinding();
  }

  /**
   * 
   * 
   */
  APLElement(Plan p, Goal g, Binding b)
  {
    _plan = p;
    _fromGoal = g;
    _binding = b;
  }

  //
  // Member functions
  //
  public Plan		getPlan()	{ return _plan; }
  public Goal		getFromGoal()	{ return _fromGoal; }
  public Binding	getBinding()	{ return _binding; }
  protected void	setBinding(Binding b) { _binding = b; }

  /**
   * Determine the instantiated plan's utility (defined currently as
   * goal utility + plan utility)
   * 
   */
  public double evalUtility()
  {
    // Note that goal utility uses its own internal binding and
    // the plan uses the APLElement binding
    double goalUtility = (_fromGoal != null) ? _fromGoal.evalUtility() : 0.0;
    double planUtility = _plan.evalUtility(_binding);
    
    // Default to simple addition at this point.
    return goalUtility + planUtility;
  }

  /**
   * Make a copy of the applicable plan
   * 
   */
  public void copy(APLElement ae)
  {
    if (this == ae) return;

    _plan = ae.getPlan();
    _fromGoal = ae.getFromGoal();
    _binding = new Binding(ae.getBinding());
  }

  /**
   * Display information about the applicable plan
   * 
   */
  public void print(PrintStream s)
  {
    s.println("APLElement:");
    s.println("  From goal: " + _fromGoal);
    s.println("  Goal name: " + ((_fromGoal != null) ? _fromGoal.getName() : "none"));
    s.println("  Plan name: " + _plan.getName());
    s.println("  Utility: " + _plan.evalUtility(_binding));
    _binding.print(s);
    s.println();
  }

}
