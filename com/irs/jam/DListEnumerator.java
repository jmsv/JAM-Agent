//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: DListEnumerator.java,v 1.1 1998/05/09 17:55:06 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\DListEnumerator.java,v $
//  
//  File              : DListEnumerator.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:23 1997
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
import java.lang.*;
import java.util.*;

/**
 *
 * Link data structure for DList (doubly linked list)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class DListEnumerator implements Enumeration, Serializable
{
  //
  // Members
  //
  protected DList	 _list;
  protected DLink	 _current;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public DListEnumerator(DList list)
  {
    reset(list);
  }

  //
  // Member functions
  //

  public boolean hasMoreElements()
  {
    return !isEndOfList();
  }

  public Object nextElement()
  {
    return isEndOfList() ? null : (_current=_current._next)._ent;
  }

  /**
   * Reinitialize the enumerator
   * 
   */
  public void reset()
  {
    _current = _list._head;
  }

  /**
   * 
   * 
   */
  public void reset(DList list)
  {
    _current = (_list=list)._head;
  }

  /**
   * True if there is no more element in the list
   * 
   */
  public boolean isEndOfList()
  {
    return _current._next == _list._head;
  }

  /**
   * Remove the current link from the list
   * 
   */
  public void removeThis()
  {
    _current = _list.remove(_current);
  }

  /**
   * Replace the current one with an element
   * 
   */
  public void replaceThis(Object ent)
  {
    _current = _list.replace(_current, ent);
  }

  /**
   * Add an element before the current one
   * 
   */
  public void insertHere(Object ent)
  {
    _list.addBefore(_current, ent);
  }

  /**
   * 
   * 
   */
  public void insertHere(DList list)
  {
    _list.addBefore(_current, list);
  }

  /**
   * Add an element after the current one
   * 
   */
  public void appendHere(Object ent)
  {
    _list.addAfter(_current, ent);
  }

  /**
   * 
   * 
   */
  public void appendHere(DList list)
  {
    _list.addAfter(_current, list);
  }

  /**
   * Return the current element in the list in sequence
   * 
   */
  public Object getThis() 
  {
    return (_current==_list._head) ?  null : _current._ent;
  }

  /**
   * Return an element in the list in sequence satisfying the
   * matching function.
   * 
   */
  public Object getNext(Object argument)
  {
    while (!isEndOfList()) {
      _current = _current._next;
      if (_current._ent == argument) {
	return _current._ent;
      }
    }
    return null;
  }

}
