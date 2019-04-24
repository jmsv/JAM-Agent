//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: BindingValue.java,v 1.2 1998/05/09 17:55:02 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\BindingValue.java,v $
//  
//  File              : BindingValue.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:22:34 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:35 1999
//  Update Count      : 22
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

/**
 *
 * Represents a particular variable binding
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class BindingValue implements Serializable
{
  //
  // Members
  //

  // Binding value can be either a constant value
  protected Value	_value;

  // or a variable defined in other external binding
  protected Variable	_externalVariable;
  protected Binding	_externalBinding;

  //
  // Constructors
  //

  /**
   * 
   * 
   */
  public BindingValue()
  {
    _value = new Value();
    _externalVariable = null;
    _externalBinding = null;
  }

  /**
   * 
   * 
   */
  public BindingValue(Value val)
  {
    _value = val;
    _externalVariable = null;
    _externalBinding = null;
  }

  /**
   * Copy constructor
   * 
   */
  public BindingValue(BindingValue bval)
  {
    _value = new Value(bval.getValue());
    _externalVariable = bval.getExternalVariable();
    _externalBinding = bval.getExternalBinding();
  }

  //
  // Member functions
  //

  //  public boolean	isNewWMBinding()		{ return true; }
  public Value		getValue()			{ return _value; }
  public Value		setValue(Value v)		{ return _value = v; }
  public Variable	getExternalVariable()		{ return _externalVariable; }
  public Variable	setExternalVariable(Variable v)	{ return _externalVariable = v; }
  public Binding	getExternalBinding()		{ return _externalBinding; }
  public Binding	setExternalBinding(Binding b)	{ return _externalBinding = b; }

}
