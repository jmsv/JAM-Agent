//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: FactCondition.java,v 1.2 1998/11/04 18:13:39 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\FactCondition.java,v $
//  
//  File              : FactCondition.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:07 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:33 1999
//  Update Count      : 39
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
 * A boolean-evaluable World Model fact
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class FactCondition extends RelationCondition implements Serializable
{

  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * Constructor w/ World Model relation to check and interpreter
   * as arguments.
   * 
   */
  public FactCondition(Relation r, Interpreter interpreter)
  {
    super(r, interpreter);
  }

  //
  // Member functions
  //
  public int		getType()	{ return Condition.COND_FACT; }

  /**
   * Compare the relation against the world model and add and/or
   * delete bindings as appropriate
   *
   */
  public boolean	check(BindingList bl)
  {
    BindingListEnumerator ble = new BindingListEnumerator(bl);
    Binding		oldBinding;
    Binding		newBinding;
    int			bindingLoop;
    /*
    System.out.println("fact::check: bl is\n");
    bl.print(System.out);
    */
    // Loop through bl
    while (ble.hasMoreElements()) {
      oldBinding = (Binding) ble.nextElement();

      // Loop through all World Model entries matching the relation
      WorldModelTableBucketEnumerator wmtbe;
      wmtbe = new WorldModelTableBucketEnumerator(getInterpreter().getWorldModel(),
						  getRelation());
      int cnt = 0;
      /*
      System.out.println("fact::check: wmtbe for relation: " +
			 _relation.getName());
			 */
      for (newBinding = new Binding(oldBinding);
	   wmtbe.getNext(newBinding) != null;
	   newBinding = new Binding(oldBinding)) {
	cnt++;
	ble.insertHere(newBinding);
	/*
	System.out.println("\n**** Adding newBinding of:\n     ");
	newBinding.format(System.out);
	*/
      }
      //System.out.println("\ncnt after loop is: " + cnt);

      ble.removeThis();
    }

    return (bl.getCount() == 0) ? false : true;
  }

  /**
   * Confirm whether the binding is still valid against the current
   * World Model
   *
   */
  public boolean	confirm(Binding b)
  {
    return getInterpreter().getWorldModel().match(_relation, b);
  }

}
