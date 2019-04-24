//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Table.java,v 1.2 1998/11/04 18:00:31 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Table.java,v $
//  
//  File              : Table.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:23 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:20 1999
//  Update Count      : 19
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
 * Represents a general table of String-hashed and linearly stored objects
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Table implements Serializable
{
  //
  // Members
  //
  public final static int	_NullId	= -1;

  protected TableObject[]	_table;
  protected DList[]		_hashTable;
  protected int			_tableSize;
  protected int			_hashTableSize;
  protected int			_nextID;
  protected int			_tableIncrementSize;

  //
  // Constructors
  //

  /**
   * Default constructor
   * 
   */
  public Table()
  {
    int	tableLoop;

    _tableSize = 16;
    _hashTableSize = 7;
    _table = new TableObject[_tableSize];
    _hashTable = new DList[_hashTableSize];
    _tableIncrementSize = 16;
    _nextID = 0;

    for (tableLoop = 0; tableLoop < _hashTableSize; tableLoop++) {
      _hashTable[tableLoop] = new DList();
    }
  }

  /**
   * 
   * Constructor with suggested sizes
   * 
   */
  public Table(int tabSz, int tabIncSz, int hashTabSz)
  {
    _tableSize = tabSz;
    _hashTableSize = hashTabSz;
    _table = new TableObject[_tableSize];
    _hashTable = new DList[_hashTableSize];
    _tableIncrementSize = tabIncSz;
    _nextID = 0;

    for (int tableLoop = 0; tableLoop < _hashTableSize; tableLoop++) {
      _hashTable[tableLoop] = new DList();
    }
  }

  //
  // Member functions
  //
  public int getSize()	{ return _nextID; }

  /**
   * Calculate the hash for the symbol with the specified label
   * 
   */
  protected int hash(String name)
  {
    int		key = 0;
    
    for (int stringLoop = 0; stringLoop < name.length(); stringLoop++) {
      key += name.charAt(stringLoop);
    }

    return (key % _hashTableSize); 
  }
  
  /**
   * Retrieve the symbol referenced by the specified identifier
   * 
   */
  public TableObject lookup(int id)
  {
    return (id < 0 || id > _tableSize) ? null : _table[id];
  }

  /**
   * Retrieve the symbol referenced by the specified label
   * 
   */
  public TableObject lookup(String name)
  {
    if (getBucket(name) == null)
      return null;

    DListEnumerator	objs = null;

    objs = new DListEnumerator(getBucket(name));

    // Go through each possible entry
    while (objs.hasMoreElements()) {

      TableObject o;

      o = (TableObject) objs.nextElement();
      if (name.equals(o.getName()))
	return o;
    }
    return null;
  }

  /**
   * Return the first symbol entry with the same hash as the specified
   * object.
   * 
   */
  public TableObject lookup(TableObject obj)
  {
    return (TableObject) getBucket(obj).first();
  }

  /**
   * Add an entry to the table based on the objects name.
   * 
   */
  public TableObject add(TableObject obj)
  {
    // First see if the object table has any room left and expand
    // it if it doesn't.
    if (_nextID >= _tableSize) {
      TableObject[] newTable = new TableObject[_tableSize + _tableIncrementSize];

      for (int i = 0; i < _tableSize; i++) 
	newTable[i] = _table[i];

      _table = newTable;
      _tableSize += _tableIncrementSize;
    }
  
    // Put new entry in object table.
    obj.setID(_nextID);
    _table[_nextID++] = obj;
  
    _hashTable[hash(obj.getName())].insert(obj);

    return obj;
  }

  /**
   * Add an entry to the table based on the name specified as
   * a parameter.
   * 
   */
  public TableObject add(TableObject obj, String hashName)
  {
    // First see if the object table has any room left and expand
    // it if it doesn't.
    if (_nextID >= _tableSize) {
      TableObject[] newTable = new TableObject[_tableSize + _tableIncrementSize];

      for (int i = 0; i < _tableSize; i++) 
	newTable[i] = _table[i];

      _table = newTable;
      _tableSize += _tableIncrementSize;
    }
  
    // Put new entry in object table.
    obj.setID(_nextID);
    _table[_nextID++] = obj;
  
    _hashTable[hash(hashName)].insert(obj);

    return obj;
  }

  /**
   * Put the new object in place of the old object.
   * 
   */
  public TableObject replace(TableObject oldObj, TableObject newObj)
  {
    // replacement is only applicable to the object with the same
    // object name because it should not change the hash table.
    int		oldHash = hash(oldObj.getName());
    int		newHash = hash(newObj.getName());

    if (oldHash != newHash) return null;

    DList	bucket;

    bucket = getBucket(oldObj);

    // Go through each entry
    for (int objLoop = 1; objLoop <= bucket.getCount(); objLoop++) {

      TableObject	obj;

      obj = (TableObject) bucket.nth(objLoop);
      if (oldObj.getName().equals(obj.getName())) {

	// replace from the hashtable bucket
	bucket.replaceNth(newObj, objLoop);

	// replace from the object table
	newObj.setID(oldObj.getID());
	_table[oldObj.getID()] = newObj;

	return newObj;
      }
    }
    return null;
  }

  /**
   * Return the entire list of symbols in the table that
   * match the specified label.
   * 
   */
  public DList getBucket(String name)
  {
    return _hashTable[hash(name)];
  }

  /**
   * Return the entire list of symbols in the table that
   * match the objects label.
   * 
   */
  public DList getBucket(TableObject obj)
  {
    return getBucket(obj.getName());
  }
  
  /**
   * Output information in a non inline manner.
   * 
   */
  public void print(PrintStream s)
  {
    s.println("Table.print(): tableSize=" + _tableSize + ", _hashTableSize=" +
	      _hashTableSize + ", nextID=" + _nextID);

    s.println("Print sequential table:");
    for (int i = 0; i < _nextID; i++) {
      lookup(i).print(s);
    }
    s.println();

    s.println("Print hash table:");
    for (int i = 0; i < _hashTableSize; i++) {

      s.println("Bucket#" + i);
      DListEnumerator	dle = new DListEnumerator(_hashTable[i]);
      while (dle.nextElement() != null)
	((TableObject) dle.getThis()).print(s);
      s.println();
    }
  }

}
