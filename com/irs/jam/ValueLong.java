//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueLong.java,v 1.2 1998/05/09 18:32:37 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ValueLong.java,v $
//  
//  File              : ValueLong.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:06 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:19 1999
//  Update Count      : 51
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
 * Represents a built-in JAM Long data-type
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class ValueLong extends Value implements Serializable
{
  //
  // Members
  //
  public long		_value;

  //
  // Constructors
  //
  public ValueLong(ValueLong v)
  {
    _value = v._value;
  }

  public ValueLong(long i)
  {
    _value = i;
  }

  //
  // Member functions
  //

  public String		getName()	{ return "ValueLong"; }
  public int		type()		{ return VAL_LONG; }
  public boolean	isTrue()	{ return _value != 0; }

  public long		getLong()	{ return _value; }
  public double		getReal()	{ return (double) _value; }
  public String		getString()	{ return (new Long(_value)).toString(); }
  public Object		getObject()	{ return new Long(_value); }

  public String		toString()	{ return getString(); }

  //

  public Value		neg()		{ return new Value(-_value); }
  public Value		add(Value v)	{ return v.longAdd(this); }
  public Value		sub(Value v)	{ return v.longSub(this); }
  public Value		mul(Value v)	{ return v.longMul(this); }
  public Value		div(Value v)	{ return v.longDiv(this); }
  public Value		mod(Value v)	{ return v.longMod(this); }
							      
  public boolean	not()		{ return !isTrue();    }
  public boolean	lt(Value v)	{ return v.longLt(this); }
  public boolean	gt(Value v)	{ return v.longGt(this); }
  public boolean	le(Value v)	{ return v.longLe(this); }
  public boolean	ge(Value v)	{ return v.longGe(this); }
  public boolean	eq(Value v)	{ return this.longEq(v); }
  public boolean	ne(Value v)	{ return this.longNe(v); }

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
  protected Value	longAdd(Value v)	 { return new Value(((ValueLong) v)._value + _value); }
  protected Value	longSub(Value v)	 { return new Value(((ValueLong) v)._value - _value); }
  protected Value	longMul(Value v)	 { return new Value(((ValueLong) v)._value * _value); }
  protected Value	longDiv(Value v)	 { return new Value(((ValueLong) v)._value / _value); }
  protected Value	longMod(Value v)	 { return new Value(((ValueLong) v)._value % _value); }
			
  protected Value	realAdd(Value v) { return new Value(((ValueReal) v)._value + _value); }
  protected Value	realSub(Value v) { return new Value(((ValueReal) v)._value - _value); }
  protected Value	realMul(Value v) { return new Value(((ValueReal) v)._value * _value); }
  protected Value	realDiv(Value v) { return new Value(((ValueReal) v)._value / _value); }
			
  protected Value	strAdd(Value v) { return new Value(((ValueString) v)._value + _value); }

  protected boolean	longLt(Value v) { return ((ValueLong) v)._value <  _value; }
  protected boolean	longLe(Value v) { return ((ValueLong) v)._value <= _value; }
  protected boolean	longGt(Value v) { return ((ValueLong) v)._value >  _value; }
  protected boolean	longGe(Value v) { return ((ValueLong) v)._value >= _value; }

  // Do special checks to allow for comparison to null Objects
  protected boolean	longEq(Value v) {
    if (v.type() == VAL_OBJECT) {
      if (v.getObject() == null) {
	if (_value == 0) return true;
	else return false;
      }
      else {
	if (_value == 0) return false;
	else return true;
      }
    }

    return v.getLong() == _value;
  }

  // Do special checks to allow for comparison to null Objects
  protected boolean	longNe(Value v) {
    if (v.type() == VAL_OBJECT) {
      if (v.getObject() == null) {
	if (_value == 0) return false;
	else return true;
      }
      else {
	if (_value == 0) return false;
	else return true;
      }
    }

    return v.getLong() != _value;
  }
						  
  protected boolean	realEq(Value v) { return ((ValueReal) v)._value == _value; }
  protected boolean	realNe(Value v) { return ((ValueReal) v)._value != _value; }
  protected boolean	realLt(Value v) { return ((ValueReal) v)._value <  _value; }
  protected boolean	realLe(Value v) { return ((ValueReal) v)._value <= _value; }
  protected boolean	realGt(Value v) { return ((ValueReal) v)._value >  _value; }
  protected boolean	realGe(Value v) { return ((ValueReal) v)._value >= _value; }
						  
  protected boolean	strEq(Value v) { return false; }
  protected boolean	strNe(Value v) { return true; }

}
