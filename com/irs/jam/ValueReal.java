//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueReal.java,v 1.3 1998/05/09 18:32:38 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ValueReal.java,v $
//  
//  File              : ValueReal.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:03 1997
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:31:19 1999
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

/**
 *
 * Represents a built-in JAM Real data-type
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class ValueReal extends Value implements Serializable
{
  //
  // Members
  //
  protected double	_value;

  //
  // Constructors
  //
  public ValueReal(ValueReal v)
  {
    _value = v._value;
  }

  public ValueReal(double d)
  {
    _value = d;
  }

  public ValueReal(float f)
  {
    _value = f;
  }

  //
  // Member functions
  //

  public String		getName()	{ return "ValueReal";   }
  public int		type()		{ return VAL_REAL;      }
  public boolean	isTrue()	{ return _value != 0.0; }

  public long		getLong()	{ return (long) _value;  }
  public double		getReal()	{ return _value;        }
  public String		getString()  	{ return (new Double(_value)).toString(); }
  public Object		getObject()  	{ return new Double(_value); }

  public String		toString()	{ return getString(); }

  //

  public Value		neg()		{ return new Value(-_value); }
  public Value		add(Value v)	{ return v.realAdd(this); }
  public Value		sub(Value v)	{ return v.realSub(this); }
  public Value		mul(Value v)	{ return v.realMul(this); }
  public Value		div(Value v)	{ return v.realDiv(this); }
  public Value		mod(Value v)	{ return v.realMod(this); }
							      
  public boolean	not()		{ return !isTrue();      }
  public boolean	lt(Value v)	{ return v.realLt(this); }
  public boolean	gt(Value v)	{ return v.realGt(this); }
  public boolean	le(Value v)	{ return v.realLe(this); }
  public boolean	ge(Value v)	{ return v.realGe(this); }
  public boolean	eq(Value v)	{ return v.realEq(this); }
  public boolean	ne(Value v)	{ return v.realNe(this); }

  //

  public void print(PrintStream s, Binding b)
  {
    s.print(_value);
  }

  public void format(PrintStream s, Binding b)
  {
    print(s,b);
  }

  //
  // Protected Member functions
  // 
  protected Value	longAdd(Value v) { return new Value(((ValueLong) v)._value + _value); }
  protected Value	longSub(Value v) { return new Value(((ValueLong) v)._value - _value); }
  protected Value	longMul(Value v) { return new Value(((ValueLong) v)._value * _value); }
  protected Value	longDiv(Value v) { return new Value(((ValueLong) v)._value / _value); }
  protected Value	longMod(Value v) { return new Value(((ValueLong) v)._value % _value); }
			
  protected Value	realAdd(Value v) { return new Value(((ValueReal) v)._value + _value); }
  protected Value	realSub(Value v) { return new Value(((ValueReal) v)._value - _value); }
  protected Value	realMul(Value v) { return new Value(((ValueReal) v)._value * _value); }
  protected Value	realDiv(Value v) { return new Value(((ValueReal) v)._value / _value); }
			
  protected Value	strAdd(Value v) { return new Value(((ValueString) v)._value + _value); }

  protected boolean	longEq(Value v) { return ((ValueLong) v)._value == _value; }
  protected boolean	longNe(Value v) { return ((ValueLong) v)._value != _value; }
  protected boolean	longLt(Value v) { return ((ValueLong) v)._value <  _value; }
  protected boolean	longLe(Value v) { return ((ValueLong) v)._value <= _value; }
  protected boolean	longGt(Value v) { return ((ValueLong) v)._value >  _value; }
  protected boolean	longGe(Value v) { return ((ValueLong) v)._value >= _value; }
  		     				  
  protected boolean	realEq(Value v) { return ((ValueReal) v)._value == _value; }
  protected boolean	realNe(Value v) { return ((ValueReal) v)._value != _value; }
  protected boolean	realLt(Value v) { return ((ValueReal) v)._value <  _value; }
  protected boolean	realLe(Value v) { return ((ValueReal) v)._value <= _value; }
  protected boolean	realGt(Value v) { return ((ValueReal) v)._value >  _value; }
  protected boolean	realGe(Value v) { return ((ValueReal) v)._value >= _value; }
  		     				  
  protected boolean	strEq(Value v) { return false; }
  protected boolean	strNe(Value v) { return true;  }

}
