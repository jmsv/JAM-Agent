//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Value.java,v 1.2 1998/05/09 18:20:49 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Value.java,v $
//  
//  File              : Value.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:08 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Tue Oct 23 19:38:24 2001
//  Update Count      : 48
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
 * Represents a built-in JAM data-type
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Value extends Expression implements Serializable
{
  //
  // Members
  //
  public final static Value	UNDEFINED	= new Value();
  public final static Value	TRUE		= new Value(1);
  public final static Value	FALSE		= new Value(0);

  public final static int	VAL_VOID	= 0;
  public final static int	VAL_LONG	= 1;
  public final static int	VAL_REAL	= 2;
  public final static int	VAL_STRING	= 3;
  public final static int	VAL_OBJECT	= 4;

  protected Value		_rep		= null;

  //
  // Constructors
  //
  public Value()
  {
    _rep = null;
  }

  public Value(Long l)
  {
    //System.out.println("Converting from java.lang.Long");
    _rep = new ValueLong(l.longValue());
  }

  public Value(long i)
  {
    //System.out.println("Creating a long Value");
    _rep = new ValueLong(i);
  }

  public Value(Integer i)
  {
    //System.out.println("Converting from java.lang.Integer");
    _rep = new ValueLong(i.longValue());
  }

  public Value(int l)
  {
    //System.out.println("Creating an integer Value");
    _rep = new ValueLong((long) l);
  }

  public Value(Double d)
  {
    //System.out.println("Converting from java.lang.Double");
    _rep = new ValueReal(d.doubleValue());
  }

  public Value(double d)
  {
    //System.out.println("Creating a double Value");
    _rep = new ValueReal(d);
  }


  public Value(String s)
  {
    //System.out.println("Creating a String Value");
    _rep = new ValueString(s);
  }


  public Value(Object o)
  {
    if (o instanceof String) {
      _rep = new ValueString((String) o);
    }
    else {
      /*
        System.out.print("Creating an Object value: ");
        if (o != null)
        System.out.println(o.toString());
        else
        System.out.println("null");
      */
      _rep = new ValueObject(o);
    }
  }

  public Value(Value v)
  {
    //System.out.println("Copying a Value");
    _rep = v._rep;
  }

  //
  // Required Member functions for Expression
  //
  public String		getName()  	{ return "Value";  }
  public int		getType()  	{ return EXP_VALUE; }

  public Value 		eval(Binding b)	{ return this; }

  //
  // Member functions
  //
  public int		type()		{ return (_rep != null) ? _rep.type(): VAL_VOID; }
  public boolean	isTrue()	{ return (_rep != null) ? _rep.isTrue() : false; }
  public boolean	isVariable()	{ return false; }
  public boolean 	isDefined()	{ return _rep != null; }

  public long		getLong()	{ return (_rep != null) ? _rep.getLong() : 0; }
  public double		getReal()	{ return (_rep != null) ? _rep.getReal() : 0.0; }
  public String		getString()	{ return (_rep != null) ? _rep.getString() : null; }
  public Object		getObject()	{ return (_rep != null) ? _rep.getObject() : null; }

  public String		toString()	{ return (_rep != null) ? _rep.toString() : "null"; }

  //

  public Value		neg()	     	{ return _rep.neg();  }
  public Value		add(Value v) 	{ return _rep.add(v); }
  public Value		sub(Value v) 	{ return _rep.sub(v); }
  public Value		mul(Value v) 	{ return _rep.mul(v); }
  public Value		div(Value v) 	{ return _rep.div(v); }
  public Value		mod(Value v) 	{ return _rep.mod(v); }
						      	      
  public boolean	not()  	     	{ return _rep.not(); }
  public boolean	lt(Value v) 	{ return _rep.lt(v); }
  public boolean	gt(Value v) 	{ return _rep.gt(v); }
  public boolean	le(Value v) 	{ return _rep.le(v); }
  public boolean	ge(Value v) 	{ return _rep.ge(v); }
  public boolean	eq(Value v) 	{ return _rep.eq(v); }
  public boolean	ne(Value v) 	{ return _rep.ne(v); }
		   
  //

  public void print(PrintStream s, Binding b)
  {
    if (_rep == null)
      s.print("*Undefined Value*");
    else
      _rep.print(s, b);
  }

  public void format(PrintStream s, Binding b)
  {
    if (_rep == null)
      s.print("null");
    else
      _rep.format(s, b);
  }

  //
  // Protected Member functions
  //
  protected Value	longAdd(Value v) { return _rep.longAdd(v); }
  protected Value	longSub(Value v) { return _rep.longSub(v); }
  protected Value	longMul(Value v) { return _rep.longMul(v); }
  protected Value	longDiv(Value v) { return _rep.longDiv(v); }
  protected Value	longMod(Value v) { return _rep.longMod(v); }
			
  protected Value	realAdd(Value v) { return _rep.realAdd(v); }
  protected Value	realSub(Value v) { return _rep.realSub(v); }
  protected Value	realMul(Value v) { return _rep.realMul(v); }
  protected Value	realDiv(Value v) { return _rep.realDiv(v); }
  protected Value	realMod(Value v) { return _rep.realMod(v); }
			
  protected Value	strAdd(Value v) { return _rep.strAdd(v); }
  protected Value	strSub(Value v) { return _rep.strSub(v); }
  protected Value	strMul(Value v) { return _rep.strMul(v); }
  protected Value	strDiv(Value v) { return _rep.strDiv(v); }
  protected Value	strMod(Value v) { return _rep.strMod(v); }
			    
  protected boolean	longEq(Value v) { return _rep.longEq(v); }
  protected boolean	longNe(Value v) { return _rep.longNe(v); }
  protected boolean	longLt(Value v) { return _rep.longLt(v); }
  protected boolean	longLe(Value v) { return _rep.longLe(v); }
  protected boolean	longGt(Value v) { return _rep.longGt(v); }
  protected boolean	longGe(Value v) { return _rep.longGe(v); }
						
  protected boolean	realEq(Value v) { return _rep.realEq(v); }
  protected boolean	realNe(Value v) { return _rep.realNe(v); }
  protected boolean	realLt(Value v) { return _rep.realLt(v); }
  protected boolean	realLe(Value v) { return _rep.realLe(v); }
  protected boolean	realGt(Value v) { return _rep.realGt(v); }
  protected boolean	realGe(Value v) { return _rep.realGe(v); }
						
  protected boolean	strLt(Value v) { return _rep.strLt(v); }
  protected boolean	strLe(Value v) { return _rep.strLe(v); }
  protected boolean	strGt(Value v) { return _rep.strGt(v); }
  protected boolean	strGe(Value v) { return _rep.strGe(v); }
  protected boolean	strEq(Value v) { return _rep.strEq(v); }
  protected boolean	strNe(Value v) { return _rep.strNe(v); }

  protected boolean	objEq(Value v) { return _rep.objEq(v); }
  protected boolean	objNe(Value v) { return _rep.objNe(v); }
}
