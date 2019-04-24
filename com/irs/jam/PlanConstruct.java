//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanConstruct.java,v 1.2 1998/05/09 18:32:13 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanConstruct.java,v $
//  
//  File              : PlanConstruct.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:38 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:30 1999
//  Update Count      : 15
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
 * Represents the basic procedural components within JAM agents
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class PlanConstruct implements Serializable
{

  //
  // Members
  //
  public final static int	PLAN_UNDEFINED = 0;
  public final static int	PLAN_SEQUENCE = 1;
  public final static int	PLAN_SIMPLE = 2;
  public final static int	PLAN_BRANCH = 3;
  public final static int	PLAN_WHEN = 4;
  public final static int	PLAN_WHILE = 5;
  public final static int	PLAN_DO = 6;
  public final static int	PLAN_ATOMIC = 7;
  public final static int	PLAN_PARALLEL = 8;
  public final static int	PLAN_DOANY = 9;
  public final static int	PLAN_DOALL = 10;
  public final static int	PLAN_WAIT = 11;

  protected int			_constructType;

  //
  // Constructors
  //

  //
  // Member functions
  //

  protected abstract PlanRuntimeState	newRuntimeState();
  protected int			 	getType() { return _constructType; }

}
