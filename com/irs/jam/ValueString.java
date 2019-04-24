//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueString.java,v 1.2 1998/05/09 18:32:38 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ValueString.java,v $
//  
//  File              : ValueString.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:01 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Tue Oct 16 22:31:28 2001
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

/**
 *
 * Represents a built-in JAM String data-type
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class ValueString extends Value implements Serializable
{
  //
  // Members
  //
  protected String	_value = null;

  //
  // Constructors
  //
  public ValueString(ValueString v)
  {
    _value = v._value;
  }

  public ValueString(String s)
  {
    _value = s;
  }

  //
  // Member functions
  //

  public String		getName()	{ return "ValueString"; }
  public int		type()		{ return VAL_STRING; }
  public boolean	isTrue()	{ return _value.length() > 0; }

  public long		getLong()	{ return 0; }
  public double		getReal()	{ return 0.0; }
  public String		getString()	{ return _value; }
  public Object		getObject()	{ return _value; }

  public String		toString()	{ return getString(); }

  //

  public Value		neg()		{ return new Value(_value); }

  public Value		add(Value v)	{ return v.strAdd(this); }
  public Value		sub(Value v)	{ return v.strSub(this); }
  public Value		mul(Value v)	{ return v.strMul(this); }
  public Value		div(Value v)	{ return v.strDiv(this); }
  public Value		mod(Value v)	{ return v.strMod(this); }
							      
  public boolean	not()		{ return !isTrue();     }
  public boolean	lt(Value v)	{ return v.strLt(this); }
  public boolean	gt(Value v)	{ return v.strGt(this); }
  public boolean	le(Value v)	{ return v.strLe(this); }
  public boolean	ge(Value v)	{ return v.strGe(this); }
  public boolean	eq(Value v)	{ return v.strEq(this); }
  public boolean	ne(Value v)	{ return v.strNe(this); }

  //

  public void print(PrintStream s, Binding b)
  {
    s.print(_value);
  }

  public void format(PrintStream s, Binding b)
  {
    print(s, b);
  }

  //
  // Protected Member functions
  // 
  protected Value	longAdd(Value v) { return new Value(((ValueLong) v).toString() + _value); }
  protected Value	realAdd(Value v) { return new Value(((ValueReal) v).toString() + _value); }
  protected Value	strAdd(Value v)	 { return new Value(((ValueString) v)._value  + _value); }

  protected boolean	longEq(Value v)	{ return false; }
  protected boolean	longNe(Value v)	{ return true;  }
  		     				  
  protected boolean	realEq(Value v) { return false; }
  protected boolean	realNe(Value v) { return true;  }
  		     				  
  protected boolean	strEq(Value v) { return _value.equals(((ValueString) v)._value);  }
  protected boolean	strNe(Value v) { return !_value.equals(((ValueString) v)._value); }
  protected boolean	strLt(Value v) { return ((ValueString) v)._value.compareTo(_value) <  0; }
  protected boolean	strLe(Value v) { return ((ValueString) v)._value.compareTo(_value) <= 0; }
  protected boolean	strGt(Value v) { return ((ValueString) v)._value.compareTo(_value) >  0; }
  protected boolean	strGe(Value v) { return ((ValueString) v)._value.compareTo(_value) >= 0; }

}
