//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PlanWaitConstruct.java,v 1.2 1998/05/09 18:32:42 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PlanWaitConstruct.java,v $
//  
//  File              : PlanWaitConstruct.java
//  Original author(s): Marcus J. Huber <marcush@home.com>
//  Created On        : Tue Sep 30 14:18:56 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:25 1999
//  Update Count      : 27
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998-1999 Marcus J. Huber and Intelligent Reasoning System.
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
 * A built-in JAM construct for conditionally delayed execution.
 *
 * @author Marc Huber
 *
 **/

public class PlanWaitConstruct extends PlanConstruct implements Serializable
{

  //
  // Members
  //
  protected Action	_action;
  protected Relation	_rel;

  //
  // Constructors
  //

  /**
   * Wait on successful completion of an action
   * 
   */
  public PlanWaitConstruct(Action a)
  {
    _action = a;
    _rel = null;
    _constructType = PLAN_WAIT;
  }

  /**
   * Wait for a goal relation to be achieved
   * 
   */
  public PlanWaitConstruct(Relation r)
  {
    _action = null;
    _rel = r;
    _constructType = PLAN_WAIT;
  }

  //
  // Member functions
  //
  public Action			getAction()		{ return _action; }
  public Relation		getRelation()		{ return _rel; }

  /**
   * Construct an appropriate RuntimeState
   * 
   */
  public PlanRuntimeState newRuntimeState()
  {
    return new PlanRuntimeWaitState(this);
  }

}
