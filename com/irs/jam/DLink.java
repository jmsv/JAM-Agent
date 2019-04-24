//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: DLink.java,v 1.1 1998/05/09 17:55:05 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\DLink.java,v $
//  
//  File              : DLink.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:27 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:34 1999
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

import java.lang.Object;

/**
 *
 * Link data structure for DList (doubly linked list)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class DLink extends Object implements Serializable
{

  //
  // Members
  //
  protected DLink	_prev;
  protected DLink	_next;
  protected Object	_ent;


  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public DLink()
  {
    _ent = null;
    _prev = _next = this;
  }

  /**
   * 
   * 
   */
  public DLink(Object ent, DLink prev, DLink next) 
  {
    _ent = ent;
    _prev = prev;
    _next = next;
  }

  //
  // Member functions
  //

}

