//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Predicate.java,v 1.2 1998/11/04 17:44:35 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Predicate.java,v $
//  
//  File              : Predicate.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:20:48 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:24 1999
//  Update Count      : 17
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
 * Predicates (expressions evaluable to true/false)
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public abstract class Predicate extends Expression implements Serializable
{

  //
  // Members
  //
  protected String 		_name;
  protected Relation		_relation;
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Primary constructor
   * 
   */
  public Predicate(String name, Relation relation, Interpreter interpreter)
  {
    _name = name;
    _relation = relation;
    _interpreter = interpreter;
  }

  //
  // Member functions
  //

  public String		getName()		{ return _name; }
  public int		getType()		{ return EXP_PREDICATE; }
  public Relation	getRelation()		{ return _relation; }
  public Interpreter	getInterpreter()	{ return _interpreter; }

  /**
   * Output information without consideration of being inline with
   * other information.
   * 
   */
  public void print(PrintStream s, Binding b)
  {
    s.print("Name: " + _name);
    s.print(",\tValue = ");
    s.println((eval(b) != null ? "True" : "False"));
  }

  /**
   * Output information considering that it may be inline with
   * other information.
   * 
   */
  public void format(PrintStream s, Binding b)
  {
    s.print("(");
    s.print(_name + " ");
    _relation.format(s, b);
    s.print(")");
  }

  public abstract Value eval(Binding binding);

}
