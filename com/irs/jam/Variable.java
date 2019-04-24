//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Variable.java,v 1.1 1998/05/09 18:32:39 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Variable.java,v $
//  
//  File              : Variable.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:18:59 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Fri Aug 10 12:22:54 2001
//  Update Count      : 20
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
 * Represents plan variables
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Variable extends Expression implements Serializable
{

  //
  // Members
  //
  protected SymbolTable		_symbolTable;
  protected int			_ID;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public Variable(SymbolTable t, String s)
  {
    init(t, s);
  }

  /**
   * 
   * 
   */
  public Variable(Binding b, String s)
  {
    init(b.getSymbolTable(), s);
  }

  //
  // Member functions
  //

  /**
   * 
   * 
   */
  protected void init(SymbolTable t, String s)
  {
    _symbolTable = t;
    if ((_ID = _symbolTable.getID(s)) < 0)
      _ID = _symbolTable.add(new Symbol(s)).getID();
  }

  public boolean	isVariable()	{ return true; }
  public Variable	getVariable()	{ return this; }
  public int		getID()		{ return _ID; }
  public int		getType()	{ return EXP_VARIABLE; }

  /**
   * 
   * 
   */
  public String getName()
  {
    return _symbolTable.lookup(getID()).getName();
  }

  /**
   * 
   * 
   */
  public Value eval(Binding b)
  {
      //System.out.println("Variable::eval: b=" + b);
      return (b == null) ? Value.UNDEFINED : b.getValue(this);
  }

  /**
   * Output information to the stream not in an in-line manner.
   * 
   */
  public void print(PrintStream s, Binding b)
  {
    eval(b).print(s, b);
  }
  
  /**
   * Output information to the stream in an in-line manner.
   * 
   */
  public void format(PrintStream s, Binding b)
  {
    eval(b).format(s, b);
  }

}
