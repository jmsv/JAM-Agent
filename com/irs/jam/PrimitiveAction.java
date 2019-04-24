//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PrimitiveAction.java,v 1.1 1998/10/30 01:10:47 marcush Exp marcush $
//  $Source: c:/com/irs/jam/RCS/PrimitiveAction.java,v $
//  
//  File              : PrimitiveAction.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 08:49:11 1998
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:23 1999
//  Update Count      : 7
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

/**
 *
 * The interface to which all primitive actions must define
 *
 * @author Marc Huber
 *
 */

public interface PrimitiveAction
{

  public Value execute(String name, int arity, ExpList args,
		       Binding binding, Goal currentGoal);
}
