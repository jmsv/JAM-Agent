//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Relation.java,v 1.5 1998/11/04 17:54:49 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Relation.java,v $
//  
//  File              : Relation.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:19:42 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Mon Sep 03 16:30:29 2001
//  Update Count      : 156
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
 * Represents an <attribute> <value_list> pair
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Relation implements Serializable
{
  //
  // Members
  //
//  private  SymbolTable	_relationTable = new SymbolTable(512, 128, 253);
  protected int			_ID;
  protected ExpList		_args;
  protected int			_arity;
  protected Interpreter		_interpreter;

  //
  // Constructors
  //

  /**
   * Basic, name-only constructor
   *
   */
  public Relation(String s, Interpreter interpreter)
  {
    init(s, null, interpreter);
  }


  /**
   * Name and parameter list constructor
   *
   */
  public Relation(String s, ExpList expList, Interpreter interpreter)
  {
    init(s, expList, interpreter);
  }

  /**
   * Name and binding constructor
   *
   */
  public Relation(Relation r, Binding binding, Interpreter interpreter)
  {
    _ID = r.getID();
    _arity = r.getArity();
    _args = r.getArgs().explistEval(binding);
    _interpreter = interpreter;
  }
  

  //
  // Member functions
  //
  public int		getArity()	{ return _arity; }
  public int		getID()		{ return _ID; }
  public ExpList	getArgs()	{ return _args; }

  /**
   * Initialize object values
   * 
   */
  private void init(String s, ExpList expList, Interpreter interpreter)
  {
    _interpreter = interpreter;
    if ((_ID = _interpreter.getRelationTable().getID(s)) < 0) {
      _ID = _interpreter.getRelationTable().add(new Symbol(s)).getID();
    }
    _args = (expList == null) ? new ExpList() : expList;
    _arity = _args.getCount();
  }
  
  /**
   * Return the relation's string label
   * 
   */
  public String getName()
  {
    return _interpreter.getRelationTable().lookup(_ID).getName();
  }


  /**
   * Convert all variables elements of the relation into constants
   * 
   */
  public Relation evalArgs(Binding b)
  {
    _args = _args.explistEval(b);
    return this;
  }

  /**
   * Format the output and don't worry about being printed out
   * in-line with other information.
   * 
   */
  public void print(PrintStream s, Binding b)
  {
    s.print(getName() + " ");
    _args.print(s, b);

    if (b != null)
      b.print(s);

  }

  /**
   * Format the output so that it's conducive to being printed out
   * in-line with other information.
   * 
   */
  public void format(PrintStream s, Binding b)
  {
    s.print(getName() + " ");
    _args.format(s, b);

    if (b != null)
      b.format(s);
  }

  /**
   * If the source & the destination relations do not match, return false.
   * Otherwise change the destination binding with linked variables to
   * the source relation binding and return true.
   *
   */
  public boolean unify(Relation dstRelation, Binding dstBinding,
		       Relation srcRelation, Binding srcBinding)
  {

    if (dstRelation == null) {
      //System.out.println("unify failed because dstRelation == null\n");
      return false;
    }

    if (srcRelation == null) {
      //System.out.println("unify failed because srcRelation == null\n");
      return false;
    }

    if (srcRelation.getID() != dstRelation.getID()) {
      //System.out.println("unify failed because src and dst IDs do not match\n");
      return false;
    }

    if (srcRelation.getArity() <= 0 && dstRelation.getArity() <= 0) {
      return true;
    }

    ExpListEnumerator srcArgsEnum = new ExpListEnumerator(srcRelation.getArgs());
    ExpListEnumerator dstArgsEnum = new ExpListEnumerator(dstRelation.getArgs());
    
    Expression srcArg; 
    Expression dstArg;

    Binding dstBindingCopy = new Binding(dstBinding);

    while (srcArgsEnum.hasMoreElements() && dstArgsEnum.hasMoreElements()) {
      srcArg = (Expression) srcArgsEnum.nextElement();
      dstArg = (Expression) dstArgsEnum.nextElement();

      Value srcVal = srcArg.eval(srcBinding);
      Value dstVal = dstArg.eval(dstBinding);
      /*
      System.out.println("srcArg= " + srcArg + ", dstArg= " + dstArg);
      System.out.println("srcVal= " + srcVal + ", dstVal= " + dstVal);
      */
      if (srcVal.isDefined() && dstVal.isDefined()) {
	if (srcVal.eq(dstVal))
	  continue;
	else {
	  /*
	  System.out.println("\nUnify failed because both defined but srcVal != dstVal (" +
			     srcVal.toString() + ", " +
			     dstVal.toString() + ")\n");
			     */
	  return false;
	}
      }

      if (dstArg.isVariable()) {
	if (srcArg.isVariable()) {
	  //System.out.println("Linking src and dst variables.");
	  dstBindingCopy.linkVariables(dstArg, srcArg, srcBinding);
	}
	else {
	  dstBindingCopy.setValue(dstArg, srcVal);
	  /*
	  System.out.println("srcVal type is: " + srcVal.getType() +
			     ", dstArg type is: " + dstArg.getType());
	  System.out.println("Setting dst value to src value (" +
			     srcVal.toString() + ").");
			     */
	}
      }
      else {
	if (!srcVal.eq(dstVal)) {
	  //System.out.println("\nUnify failed because dst is a variable but values different\n");
	  return false;
	}
      }
    }
    
    if (dstBinding != null) {
      dstBinding.copy(dstBindingCopy);
    }

    return true;
  }

}
