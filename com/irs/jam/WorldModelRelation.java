//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: WorldModelRelation.java,v 1.2 1998/11/04 17:45:50 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\WorldModelRelation.java,v $
//  
//  File              : WorldModelRelation.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:18:52 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Thu Oct 25 15:18:50 2001
//  Update Count      : 24
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
 * A World Model entry
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class WorldModelRelation extends Symbol implements Serializable
{

  //
  // Members
  //
  protected Relation	_relation;	// The content of the WM Relation Table
  protected boolean	_newTag;	// set to 1 for new entry and cleared at every cycle

  //
  // Constructors
  //

  /**
   * Constructor based on an existing relation
   * 
   */
  public WorldModelRelation(Relation rel)
  {
    super(rel.getName());
    init(rel, true);
  }

  //
  // Member functions
  //
  public Relation	getRelation()	{ return _relation; }
  public boolean	isNew()		{ return _newTag; }
  public void		clearNew()	{ _newTag = false; }
  public void		setNew()	{ _newTag = true; }

  /**
   * Initialize values
   * 
   */
  private void init(Relation r, boolean n)
  {
    _relation = r;
    _newTag = n;
  }

  /**
   * Return whether a match can be found for the specified relation
   * and variable binding.
   * 
   */
  public boolean matchRelation(Relation pattRelation, Binding pattBinding)
  {
    if (_relation.unify(pattRelation, pattBinding, _relation, (Binding) null)) {
      if (pattBinding != null)
	pattBinding.checkNewWMBinding(_newTag);
      return true;
    }
    else
      return false;
  }

}
