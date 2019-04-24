//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: PredicateRetrieve.java,v 1.2 1998/11/04 18:10:43 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\PredicateRetrieve.java,v $
//  
//  File              : PredicateRetrieve.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:46 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:23 1999
//  Update Count      : 16
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
 * Retrieve Predicate (a World Model Retrieve expression evaluable to true/false)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class PredicateRetrieve extends Predicate implements Serializable
{
  //
  // Members
  //

  //
  // Constructors
  //

  /**
   * Primary constructor
   * 
   */
  public PredicateRetrieve(String name, Relation relation, Interpreter interpreter)
  {
    super(name, relation, interpreter);
  }

  //
  // Member functions
  //

  /**
   * Calculate the truth value of the World Model relation
   * 
   */
  public Value eval(Binding binding)
  {
    // unbind variables before matching
    binding.unbindVariables(_relation.getArgs());
    
    // now try match to get new values
    if (getInterpreter().getWorldModel().match(_relation, binding))
      return Value.TRUE;
    else
      return Value.FALSE;
  }

}
