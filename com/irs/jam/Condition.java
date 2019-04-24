//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Condition.java,v 1.1 1998/05/09 17:55:03 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Condition.java,v $
//  
//  File              : Condition.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:32 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:35 1999
//  Update Count      : 14
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
 * A boolean-evaluable object
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class Condition  implements Serializable
{

  //
  // Members
  //
  protected Condition		_rep;
  protected int 		_activeValue;

  public final static int	COND_GOAL	= 1;
  public final static int	COND_EXP	= 2;
  public final static int	COND_FACT	= 3;
  public final static int	COND_RETRIEVE	= 4;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public Condition()
  {
    _rep = null;
    _activeValue = 1;
  }

  //
  // Member functions
  //

  /**
   * 
   * 
   */
  public Condition	setPositive()
  {
    _activeValue = 1;
    return this;
  }

  /**
   * 
   * 
   */
  public Condition	setNegative()
  {
    _activeValue = 0;
    return this;
  }
  
  public abstract String   getName();
  public abstract int	   getType();
  public abstract boolean  check(BindingList bl);
  public abstract boolean  confirm(Binding b);

}
