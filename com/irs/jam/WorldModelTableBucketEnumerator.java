//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: WorldModelTableBucketEnumerator.java,v 1.2 1998/11/04 17:59:53 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\WorldModelTableBucketEnumerator.java,v $
//  
//  File              : WorldModelTableBucketEnumerator.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:18:16 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Mon Sep 03 16:36:01 2001
//  Update Count      : 45
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
 * Helps iterate through World Model entries
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class WorldModelTableBucketEnumerator implements Serializable
{
  //
  // Members
  //
  protected WorldModelTable	_table;
  protected Relation		_relation;
  protected DListEnumerator 	_bucket;

  //
  // Constructors
  //

  /**
   * Constructor with World Model and relation arguments
   * 
   */
  public WorldModelTableBucketEnumerator(WorldModelTable wt, Relation relation)
  {
    _table = wt;
    _relation = relation;
    _bucket = new DListEnumerator(wt.getBucket(_relation.getName()));
  };

  //
  // Member functions
  //

  /**
   * Go to the next matching element
   * 
   */
  public WorldModelRelation getNext(Binding binding)
  {
    WorldModelRelation wr = (WorldModelRelation) _bucket.nextElement();

    if (wr == null) {
	return null;
    }

    boolean bm = wr.matchRelation(_relation, binding);

    if (bm) {
	return wr;
    }
    else {
	WorldModelRelation wmr = getNext(binding);
	return wmr;
    }
  }
  
  /**
   * Remove the current element
   * 
   */
  public void removeThis()
  {
    _table.removeThis(_bucket);
  }

  /**
   * Remove the current element
   * 
   */
  public void print(PrintStream s)
  {
      s.println("WorldModelTableBucketEnumerator:\n  relation name: " + _relation.getName());
      s.println("  length: " + _bucket._list.getCount());
  }

}
