//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Symbol.java,v 1.2 1998/11/04 17:48:07 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Symbol.java,v $
//  
//  File              : Symbol.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:30 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:21 1999
//  Update Count      : 20
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
 * Represents a symbolic concept within a plan
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Symbol extends TableObject implements Serializable
{

  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * Constructor based on a label and identifier
   * 
   */
  public Symbol(String name, int id)
  {
    init(name, id);
  }

  /**
   * Constructor with symbol label
   * 
   */

  public Symbol(String name)
  {
    init(name, -1);
  }

  /**
   * Copy constructor
   * 
   */
  public Symbol(Symbol s)
  {
    init(s.getName(), s.getID());
  }

  //
  // Member functions
  //

  /**
   * Initialize object members
   * 
   */
  private void init(String name, int id)
  {
    _name = name;
    _id = id;
  }

  /**
   * Output information in a non inline manner
   * 
   */
  public void print(PrintStream s)
  {
    s.println(_id + ": " + _name);
  }

  /**
   * Return this object's hash ID.
   * 
   */
  public int hashCode()
  {
    return (int) _name.hashCode();
  }

  /**
   * Check whether specified object matches this object
   * 
   */
  public boolean equals(Object obj)
  {
    return ((Symbol) obj).equals(_name);
  }

}
