//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Plan.java,v 1.4 1998/05/09 18:32:08 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Plan.java,v $
//  
//  File              : Plan.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:44 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Mon Oct 15 19:47:04 2001
//  Update Count      : 37
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
 * Represents the basic plan within JAM
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Plan extends TableObject implements Serializable
{

  //
  // Members
  //

  // Required Plan fields
  protected Action			_goalSpecification;
  protected Relation			_concludeSpecification;
  protected Expression			_utility;
  protected PlanSequenceConstruct	_body;

  // Common optional Plan fields
  protected PlanContext			_context;
  protected PlanContext			_precondition;
  protected String			_documentation;
  protected PlanAtomicConstruct		_failure;

  // Optional Plan fields
  protected PlanAtomicConstruct		_effects;
  protected String			_attributes;

  // Other member fields
  protected SymbolTable			_symbolTable;
  protected boolean			_valid;

  //
  // Constructors
  //
  public Plan()
  {
    _symbolTable		= new SymbolTable();
    _name 			= null;
    _documentation 		= null;
    _valid 			= true;
    _goalSpecification		= null;
    _concludeSpecification	= null;
    _context 			= new PlanContext();
    _precondition		= new PlanContext();
    _utility 			= null;
    _effects 			= null;
    _failure 			= null;
    _body 			= null;
    _attributes 		= null;
  }

  //
  // Member functions
  //
  public String			getName()			{ return _name; }
  public void			setName(String planName)	{ _name = planName; }
  public Action			getGoalSpecification()		{ return _goalSpecification; }
  public Action			setGoalSpecification(Action p)	{ return _goalSpecification = p; }
  public Relation		getConcludeSpecification()	{ return _concludeSpecification; }
  public Relation		setConcludeSpecification(Relation r) { return _concludeSpecification = r; }
  public Expression		setUtility(Expression p)	{ return _utility = p; }
  public Expression		getUtility()                    { return _utility; }  
  public PlanSequenceConstruct	getBody()			{ return _body; }
  public PlanContext		getContext()			{ return _context; }
  public PlanContext		setContext(PlanContext c)	{ return _context = c; }
  public PlanContext		getPrecondition()		{ return _precondition; }
  public PlanContext		setPrecondition(PlanContext c)	{ return _precondition = c; }
  public PlanAtomicConstruct	getEffects()			{ return _effects; }
  public PlanAtomicConstruct	setEffects(PlanAtomicConstruct c) { return _effects = c; }
  public PlanAtomicConstruct	getFailure()			{ return _failure; }
  public PlanAtomicConstruct	setFailure(PlanAtomicConstruct f) { return _failure = f; }
  public String			getDocumentation()		{ return _documentation; }
  public boolean		isValid()			{ return _valid; }
  public void			disable()			{ _valid = false; }
  public SymbolTable		getSymbolTable()		{ return _symbolTable; }
  public String			setDocumentation(String planDoc)   { return _documentation = planDoc; }
  public String			getAttributes()			{ return _attributes; }
  public String			setAttributes(String planAtt)   { return _attributes = planAtt; }
  public PlanSequenceConstruct	setBody(PlanSequenceConstruct c)   { return _body = c; }

  /**
   * Calculate the utility value of the plan instance
   * 
   */
  public double			evalUtility(Binding binding)
  {
    return (_utility != null) ? _utility.eval(binding).getReal() : 0.0;
  }

  /**
   * 
   * 
   */
  public PlanContext		addContext(ConditionList cl)
  {
    _context.addConditions(cl);
    return _context;
  }

  /**
   * 
   * 
   */
  public PlanContext		addPrecondition(ConditionList cl)
  {
    _precondition.addConditions(cl);
    return _precondition;
  }

  /**
   * Evaluate truth value of context expression
   * 
   */
  public boolean		checkContext(BindingList bindingList)
  {
    return (_context != null) ? _context.check(bindingList) : true;
  }

  /**
   * Evaluate truth value of context expression
   * 
   */
  public boolean		confirmContext(Binding b)
  {
    return (_context != null) ? _context.confirm(b) : true;
  }

  /**
   * Evaluate truth value of precondition expression
   * 
   */
  public boolean		checkPrecondition(BindingList bindingList)
  {
    return (_precondition != null) ? _precondition.check(bindingList) : true;
  }

  /**
   * Print out without worrying about being in-line with other output
   * 
   */
  public void print(PrintStream s)
  {
    s.println("Plan");
    s.println("  NAME: " + _name);
    if (_goalSpecification != null) {
      s.print("  GOAL: ");
      _goalSpecification.format(s, null);
      s.println();
    }
    if (_concludeSpecification != null) {
      s.print("  CONCLUDE: ");
      _concludeSpecification.format(s, null);
      s.println();
    }
    if (_documentation != null) {
      s.println("  DOCUMENTATION: " + _documentation);
    }
  }

  /**
   * Print out so that it can be in-line with other output
   * 
   */
  public void format(PrintStream s)
  {
    s.print("Plan: " + _name + " ");
  }

}
