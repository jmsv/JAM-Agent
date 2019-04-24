//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ValueObject.java,v 1.2 1998/05/09 18:32:39 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\ValueObject.java,v $
//  
//  File              : ValueObject.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:03 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Sat Oct 27 20:24:02 2001
//  Update Count      : 63
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998-1999 Marcus J. Huber and Intelligent Reasoning Systems.
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
//  Marcus J. Huber and Intelligent Reasoning Systems shall not be
//  liable for any damages, including special, indirect, incidental, or
//  consequential damages, with respect to any claim arising out of or
//  in connection with the use of the software, even if they have been
//  or are hereafter advised of the possibility of such damages.
// 
//////////////////////////////////////////////////////////////////////////////

package com.irs.jam;

import java.io.*;

/**
 *
 * Represents a built-in JAM Java Object data-type
 *
 * @author Marc Huber
 *
 **/

public class ValueObject extends Value implements Serializable
{
  //
  // Members
  //
  protected Object	_value;

  //
  // Constructors
  //
  public ValueObject(ValueObject v)
  {
    _value = v._value;
  }

  public ValueObject(Object o)
  {
    _value = o;
  }

  //
  // Member functions
  //

  public String		getName()	{ return "ValueObject"; }
  public int		type()		{ return VAL_OBJECT; }
  public boolean	isTrue()	{ return _value != null; }

  public long		getLong()	{ return (long) 0; }
  public double		getReal()	{ return (double) 0.0; }
  public String		getString()  	{ return (_value.toString()); }
  public Object		getObject()  	{ return _value; }

  public String		toString()	{ return getString(); }

  //

  public Value		neg()		{ return new Value(_value); }
  public Value		add(Value v)	{ return v; }
  public Value		sub(Value v)	{ return v; }
  public Value		mul(Value v)	{ return v; }
  public Value		div(Value v)	{ return v; }
  public Value		mod(Value v)	{ return v; }
							      
  public boolean	not()		{ return !isTrue(); }
  public boolean	lt(Value v)	{ return false; }
  public boolean	gt(Value v)	{ return false; }
  public boolean	le(Value v)	{ return false; }
  public boolean	ge(Value v)	{ return false; }
  public boolean	eq(Value v)	{ return this.objEq(v); }
  public boolean	ne(Value v)	{ return this.objNe(v); }

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

  // Do special checks to allow for null Object comparison
  protected boolean	objEq(Value v) {
      if (!isDefined())
	  return false;

      if (v.type() == VAL_LONG) {
	  if (v.getLong() == 0) {
	      if (_value == null) return true;
	      else return false;
	  }
	  else {
	      if (_value == null || ((Value)_value).getType() == VAL_VOID) return false;
	      else return true;
	  }
      }
      /*    
      if (v == UNDEFINED) {
	  System.out.println("ValueObject:: v is UNDEFINED!");
	  return false;
      }

      if (v.getType() == VAL_VOID ) {
	  System.out.println("ValueObject:: v is VOID!");
      }
      */

      ValueObject vo;
      try {
	  System.out.println("ValueObject::Before ValueObject cast");
	  vo = (ValueObject) v;
	  System.out.println("ValueObject::After ValueObject cast");
	  boolean b = ((ValueObject) v)._value == _value;
      }
      catch (java.lang.ClassCastException cce) {
	  System.out.println("Sorry about the following, but I'm trying to track down");
	  System.out.println("a bug that only shows up infrequently.  If you could distill");
	  System.out.println("a JAM file that reproduces this exception repeatedly and send");
	  System.out.println("it to me at marcush@home.com I would greatly appreciate it");
	  
	  System.out.println("ValueObject::value being cast to ValueObject is: " + v);
	  System.out.println("ValueObject::  v.getClass(): " + v.getClass());
	  System.out.println("ValueObject::  v.type(): " + v.type());
	  System.out.println("ValueObject::  v.isDefined(): " + v.isDefined());
	  System.out.println("ValueObject::  v.isTrue(): " + v.isTrue());
	  System.out.println("ValueObject::  v.isVariable(): " + v.isVariable());
	  System.out.println("ValueObject::  v.getObject(): " + v.getObject());
	  System.out.println("ValueObject::  v.getObject().toString(): " + v.getObject().toString());

	  /*
	  System.out.println("ValueObject::  this.type(): " + this.type());
	  System.out.println("ValueObject::  this.isDefined(): " + this.isDefined());
	  System.out.println("ValueObject::  this.isTrue(): " + this.isTrue());
	  System.out.println("ValueObject::  this.isVariable(): " + this.isVariable());
	  System.out.println("ValueObject::  _value.getClass(): " + _value.getClass());
	  System.out.println("ValueObject::  _value: " + _value);
	  System.out.println("ValueObject::  _value.toString(): " + _value.toString());
	  */

	  throw new ClassCastException("ValueObject:: Dammit:\n  v="+v+"\n  _value="+_value);
      }

      return ((ValueObject) v)._value == _value;
  }

  // Do special checks to allow for null Object comparison
  protected boolean	objNe(Value v) {
      if (v.type() == VAL_LONG) {
	  if (v.getLong() == 0) {
	      if (_value == null) return false;
	      else return true;
	  }
	  else {
	      if (_value == null) return true;
	      else return false;
	  }
      }

      return ((ValueObject) v)._value != _value;
  }

}
