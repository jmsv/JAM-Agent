//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Binding.java,v 1.4 1998/05/09 17:54:27 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Binding.java,v $
//  
//  File              : Binding.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:38 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Mon Sep 03 13:57:58 2001
//  Update Count      : 38
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
 * Represents a plan's variable bindings
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Binding implements Serializable
{
  //
  // Members
  //
  protected int			_size;

  // true if the binding is done with
  // at least one new WM entry
  protected boolean		_newWMBinding;

  protected SymbolTable		_symbolTable;

  // an array of values (variable-id is the index)
  protected BindingValue[]	_bvalues;	

  //
  // Constructors
  //

  /**
    * Constructor based on an externally-supplied symbol table
    * 
    */
  public Binding(SymbolTable symbolTable)
  {
    _size = symbolTable.getSize();
    _newWMBinding = false;

    _symbolTable = symbolTable;

    _bvalues = new BindingValue[_size];

    for (int i = 0; i < _size; i++)
      _bvalues[i] = new BindingValue();
  }

  /**
    * Copy constructor
    * 
    */
  public Binding(Binding b)
  {
    int	i;

    if (b != null) {
      _symbolTable = b._symbolTable;
      _newWMBinding = b._newWMBinding;
      _size = b._size;
      _bvalues = new BindingValue[_size];
      
      for (i = 0; i < _size; i++)
	_bvalues[i] = new BindingValue(b._bvalues[i]);
    }
    else {
      _symbolTable = null;
      _newWMBinding = false;
      _size = 0;
      _bvalues = null;
    }
  }

  //
  // Member functions
  //

  /**
    * Assignment operator
    * 
    */
  public void copy(Binding b)
  {
    _newWMBinding = b.isNewWMBinding();
    _symbolTable = b.getSymbolTable();
    _size = _symbolTable.getSize();
    _bvalues = b.getBindingValues();
  }

  /**
    * Restore variable in the expression to an undefined state
    * 
    */
  public void unbindVariable(Expression expression)
  {
    if (expression.isVariable())
      setValue(expression, Value.UNDEFINED);
  }

  /**
    * Restore variables in the expression list to an undefined state
    * 
    */
  public void unbindVariables(ExpList expressions)
  {
    ExpListEnumerator ele = new ExpListEnumerator(expressions);
      
    while (ele.hasMoreElements())
      unbindVariable((Expression) ele.nextElement());
  }

  /**
    * Tie this binding with an external variable binding
    * 
    */
  public void linkVariables(Expression var, Expression extVariable,
			    Binding extBinding)
  {
    BindingValue bval = _bvalues[var.getVariable().getID()];

    bval.setExternalVariable((Variable) extVariable.getVariable());
    bval.setExternalBinding(extBinding);
  }

  /**
    * See if binding is based solely on local values
    * 
    */
  public boolean isLocalBinding(Expression var)
  {
    return (!var.isVariable() ||
	    isLocalBinding(var.getVariable().getID()));
  } 

  /**
    * See if binding is based solely on local values
    * 
    */
  public boolean isLocalBinding(int varID)
  {
    return (_bvalues[varID].getExternalVariable() == null);
  }

  /**
    * Set the internal value of the variable
    * 
    */
  public void setValue(Expression var, Value val)
  {
    if (!var.isVariable()) {
      System.out.println("\nArgument should be variable!");
      return;
    }
    setValue(var.getVariable().getID(), val);
  }

  /**
    * Set the internal value of the variable
    * 
    */
  public void setValue(int varID, Value val)
  {
    BindingValue bval = _bvalues[varID];

    if (isLocalBinding(varID))
      bval.setValue(val);
    else
      bval.getExternalBinding().setValue(bval.getExternalVariable(), val);
  }

  /**
    * Get the variable's value by looking up the internal ID
    * 
    */
  public Value getValue(Expression var)
  {
    return getValue(var.getVariable().getID());
  }

  /**
    * Get the variable's value by using the internal ID
    * 
    */
  public Value getValue(int varID)
  {
    BindingValue bval = _bvalues[varID];

    if (isLocalBinding(varID))
      return bval.getValue();
    else
      return bval.getExternalBinding().getValue(bval.getExternalVariable());
  }

  /**
    * See if there are any references based upon a newly changed world model entry
    * 
    */
  public boolean isNewWMBinding()
  {
    return _newWMBinding;
  }

  /**
    * Check, and possibly alter the flag indicating a reference to a newly
    * changed world model entry
    * 
    */
  public boolean checkNewWMBinding(boolean newWM)
  {
    return _newWMBinding = _newWMBinding || newWM;
  }

  /**
    * Indicate that there are no references to newly changed world model entries
    * 
    */
  public void clearNewWMBinding()
  {
    _newWMBinding = false;
  }

  /**
    * Check to see if there are no variables
    * 
    */
  public boolean isEmpty()
  {
    return _size == 0;
  }

  /**
    * 
    * 
    */
  public SymbolTable getSymbolTable()
  {
    return _symbolTable;
  }

  /**
    * 
    * 
    */
  public BindingValue[] getBindingValues()
  {
    return _bvalues;
  }

  /**
    * Print out without worrying about being in-line with other output
    * 
    */
  public void print(PrintStream s)
  {
    int	i;

    s.println("  Binding:");
    s.println("    Size:        \t" + _size);
    s.println("    NewWMBinding:\t" + _newWMBinding);

    for (i = 0; i < _size; i++) {
      s.print("      [" + i + "] " + _symbolTable.lookup(i).getName() + " = ");
      if (getValue(i).isDefined()) {
	getValue(i).print(s, this);
	s.println();
      }
      else
	s.println("NOT BOUND");
    }
    s.println();
  }

  /**
    * Print out so that it can be in-line with other output
    * 
    */
  public void format(PrintStream s)
  {
    int	i;

    s.print("  Binding: size=" + _size + ", new=" + _newWMBinding + ", ");
    for (i = 0; i < _size; i++) {
      s.print("[" + i + "] " + _symbolTable.lookup(i).getName() + " = ");
      if (getValue(i).isDefined()) {
	getValue(i).format(s, this);
	if (i != _size - 1)
	    s.print(", ");
      }
      else
	s.print("NOT BOUND, ");
    }
  }

}
