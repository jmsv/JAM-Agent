//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: DList.java,v 1.1 1998/05/09 17:55:05 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\DList.java,v $
//  
//  File              : DList.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:25 1997
//  Last Modified By  : <marcush@irs.home.com>
//  Last Modified On  : Sat Mar 18 07:58:23 2000
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

import java.lang.Object;
import java.util.Enumeration;

/**
 *
 * DList (doubly linked list)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class DList extends Object implements Serializable
{
  //
  // Members
  //
  protected DLink	_head;
  protected int		_count;

  //
  // Member functions and constructors
  //

  /**
   * 
   * 
   */
  public DList()
  {
    createHead();
  }

  /**
   * 
   * 
   */
  public DList(Object ent)
  {
    createHead();
    insert(ent);
  }

  /**
   * add at head of list
   * 
   */
  public void insert(Object ent)
  {
    addAfter(_head, ent);
  }

  /**
   * 
   * 
   */
  public void insert(DList list)
  {
    addAfter(_head, list);
  }

  /**
   * Add at tail of list
   * 
   */
  public void append(Object ent)
  {
    addBefore(_head, ent);
  }

  /**
   * Add at tail of list
   * 
   */
  public void append(DList list)
  {
    addBefore(_head, list);
  }


  /**
   * Remove all links
   * 
   */
  public void clear()
  {
    DLink current = _head._next;

    while (current != _head) {
      DLink n = current._next;
      // delete current;
      current = n; 
    }

    _count = 0;
    _head._prev = _head._next = _head;
  }

  /**
   * Return the first element of the list
   * 
   */
  public Object first()
  {
    if (isEmpty())
      return null;
    else
      return _head._next._ent;
  }

  /**
   * MJH - Return the nth (1-based) element of the list
   * 
   */
  public Object nth(int n)
  {
    DLink	current = _head._next;
    int	cnt = 1;

    if (n < 1 || n > _count)
      return null;

    while (cnt < n) {
      current = current._next;
      cnt++;
    }

    return current._ent;
  }

  /**
   * Return the last element of the list
   * 
   */
  public Object last()
  {
    if (isEmpty())
      return null;
    else
      return _head._prev._ent;
  }

  /**
   * Return and remove head of list
   * 
   */
  public Object pop()
  {
    if (isEmpty())
      return null;

    DLink first = _head._next;

    _head._next = first._next;
    first._next._prev = _head;

    Object ent = first._ent;
    // delete first;
    first = null;
    _count--;

    return ent;
  }

  //
  
  /**
   * Returns the number of elements in the list
   * 
   */
  public int getCount()
  {
    return _count;
  }

  /**
   * Returns whether the list has elements or not
   * 
   */
  public boolean isEmpty()
  {
    return _count <= 0;
  }

  ///
  ///
  ///

  /**
   * Create the start of the list
   * 
   */
  protected void createHead()
  {
    _head = new DLink();
    _count = 0;
  }

  /**
   * Add a single entry after the given element
   * 
   */
  protected void addAfter(DLink current, Object ent)
  {
    DLink link = new DLink(ent, current, current._next);

    current._next._prev = link;
    current._next = link;

    _count++;
  }

  /**
   * Add a list after the given element
   * 
   */
  protected void addAfter(DLink current, DList list)
  {
    DLink c = list._head;

    while (c._next != list._head) {
      addAfter(current, (c=c._next)._ent);
    }
  }

  /**
   * Add a single entry before the given element
   * 
   */
  protected void addBefore(DLink current, Object ent)
  {
    DLink link = new DLink(ent, current._prev, current);
  
    current._prev._next = link;
    current._prev = link;

    _count++;
  }

  /**
   * Add a list after the given element
   * 
   */
  protected void addBefore(DLink current, DList list)
  {
    DLink c = list._head;

    while (c._next != list._head) {
      addBefore(current, (c=c._next)._ent);
    }
  }

  /**
   * 
   * 
   */
  protected DLink remove(DLink current)
  {
    if (current == _head)
      return current;

    DLink previous = current._prev;
  
    previous._next = current._next;
    current._next._prev = current._prev;

    current = null;    // delete current
    _count--;

    return previous;
  }

  /**
   * MJH - Remove the nth (1-based) element of the list
   * 
   */
  public Object removeNth(int n)
  {
    DLink	current = _head._next;
    int	cnt = 1;

    if (n < 1 || n > _count)
      return null;

    while (cnt < n) {
      current = current._next;
      cnt++;
    }

    remove(current);
    return current._ent;
  }

  /**
   * 
   * 
   */
  protected DLink replace(DLink current, Object ent)
  {
    if (current == _head)
      return current;
  
    DLink link = new DLink(ent, current._prev, current._next);
    current._prev._next = link;
    current._next._prev = link;

    // delete current
    return current = link;
  }

  /**
   * MJH - Set the nth (1-based) element of the list
   * 
   */
  public Object replaceNth(Object ent, int n)
  {
    DLink	current = _head._next;
    int	cnt = 1;

    if (n < 1 || n > _count)
      return null;

    while (cnt < n) {
      current = current._next;
      cnt++;
    }

    replace(current, ent);
    return ent;
  }

}
