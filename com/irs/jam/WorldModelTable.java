//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: WorldModelTable.java,v 1.3 1998/11/04 17:56:25 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\WorldModelTable.java,v $
//  
//  File              : WorldModelTable.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:18:47 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Thu Oct 25 15:19:00 2001
//  Update Count      : 55
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
 * A JAM agent's knowledge about the world
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class WorldModelTable extends SymbolTable implements Serializable
{

  //
  // Members
  //
  private Interpreter	_interpreter;

  //
  // Constructors
  //
  public WorldModelTable(Interpreter interpreter)
  {
    super(128, 128, 63);
    _interpreter = interpreter;
  };

  //
  // Member functions
  //

  /**
   * Check to see if any World Model entries match the
   * specified relation and binding
   * 
   */
  public boolean match(Relation relation, Binding binding)
  {
    WorldModelTableBucketEnumerator wmtbe;

    wmtbe = new WorldModelTableBucketEnumerator(this, relation);

    return (wmtbe.getNext(binding) != null) ? true : false;
  }
	
  /**
   * Add a new World Model entry
   * 
   */
  public synchronized void assert(Relation r, Binding b)
  {
    if (!match(r, b)) {
      // Note: Creating a new relation with args evaluated
      add(new WorldModelRelation(new Relation(r, b, _interpreter)));
    }

    if (_interpreter.getShowWorldModel()) {
      System.out.println("JAM::WorldModel:assert");
      print(System.out);
    }
  }

  /**
   * Remove a World Model entry
   * 
   */
  public synchronized void retract(Relation r, Binding b)
  {
    WorldModelTableBucketEnumerator	wmtbe;
    WorldModelRelation			wr;
  
    wmtbe = new WorldModelTableBucketEnumerator(this, r);
    while ((wr = (WorldModelRelation) wmtbe.getNext(b)) != null) {
      if (_interpreter.getShowWorldModel()) {
	  System.out.print("Retracting relation:");
	  r.format(System.out, b);
	  System.out.println();
      }
      wmtbe.removeThis();
    }

    if (_interpreter.getShowWorldModel()) {
      print(System.out);
    }
  }

  /**
   * Change a World Model entry
   * 
   */
  public synchronized void update(Relation oldRel, Relation newRel, Binding b)
  {
    if (_interpreter.getShowWorldModel()) {
      System.out.println("JAM::WorldModel:update (via retract then assert)");
    }
    retract(oldRel, b);
    assert(newRel, b);

  }
	
  /**
   * Check to see if the entry is brand new
   * 
   */
  public boolean isNew(int id)
  {
    return ((WorldModelRelation)lookup(id)).isNew();
  }

  /**
   * See if there are ANY new World Model entries
   * 
   */
  public boolean anyNew()
  {
    int		i;
    for (i = 0; i < _nextID; i++) {
      if (((WorldModelRelation)lookup(i)).isNew()) {
	return true;
      }
    }
    return false;
  }

  /**
   * Set all World Model entries to be "aged"
   * 
   */
  public void clearNewAll()
  {
    int	i;
    for (i = 0; i < _nextID; i++)
      ((WorldModelRelation)lookup(i)).clearNew();
  }

  /**
   * Set all World Model entries to be "new"
   * 
   */
  public void setNewAll()
  {
    int	i;
    for (i = 0; i < _nextID; i++)
      ((WorldModelRelation)lookup(i)).setNew();
  }

  /**
   * Output information related to the World Model
   * 
   */
  public void print(PrintStream s)
  {
    Relation		r;
    WorldModelRelation	w;
    Expression		e;
    int			i;

    s.println("JAM:Agent's World Model (" + _nextID + " entries) is now:");
    for (i = 0; i < _nextID; i++) {
      w = (WorldModelRelation) lookup(i);
      r = w.getRelation();
      s.print(i + ":[" + (w.isNew() ? "N" : " ") + "] : ");
      r.format(s, null);
      s.println();
    }
  }

}
