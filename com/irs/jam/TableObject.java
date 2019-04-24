//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: TableObject.java,v 1.2 1998/11/04 17:58:34 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\TableObject.java,v $
//  
//  File              : TableObject.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:21 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:20 1999
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
 *
 * Represents a general object to put in Tables
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class TableObject extends Object implements Serializable
{

  //
  // Members
  //
  public String			_name = null;
  public int			_id = -1;

  //
  // Constructors
  //

  //
  // Member functions
  //
  public String		getName()	{ return _name; }
  public int		getID()		{ return _id; }
  public int		setID(int id)	{ return _id = id; }

  /**
   * Output information in a non inline manner
   * 
   */
  public void print(PrintStream s)
  {
    s.println(_name + " : " + _id);
  }

}
