//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: Interpreter.java,v 1.4 1998/11/04 17:51:30 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\Interpreter.java,v $
//  
//  File              : Interpreter.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:53 1997
//  Last Modified By  :  <Marcus J. Huber Ph.D@irs.home.com>
//  Last Modified On  : Thu Aug 09 09:00:05 2001
//  Update Count      : 181
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
 * The JAM agent's architectural core 
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class Interpreter implements Serializable
{
  //
  // Members
  //
  protected	PlanTable		_planLibrary;
  protected	WorldModelTable		_worldModel;
  protected	IntentionStructure	_intentionStructure;
  protected	Plan			_observer;
  protected	Functions		_systemFunctions;
  protected	Functions		_userFunctions;
  private  	SymbolTable		_relationTable;


  // Runtime Statistics
  private	int			_numAPLs;
  private	int			_numNullAPLs;
  private	int			_numGoals;
  private	int			_numCycles;
  private	double			_totalUtility;

  // Mobility flag
  private	boolean			_agentHasMoved;

  // Debug output flags
  protected	boolean			_showWorldModel;
  protected	boolean			_showGoalList;
  protected	boolean			_showIntentionStructure;
  protected	boolean			_showAPL;
  protected	boolean			_showActionFailure;

  // Threading
  private volatile boolean		_threadStopped;
  private volatile boolean		_threadSuspended;

  //
  // Constructors
  //

  /**
   * Default constructor
   * 
   */
  public Interpreter()
  {
    init();
  }

  /**
   * 
   * 
   */
  public Interpreter(String argv[]) throws ParseException, IOException
  {
    int				argNum;
    Vector			pStrings = new Vector();
    Vector			argV = new Vector();

    init();

    // Copy argv into new Vector
    for (argNum=0; argNum < argv.length; argNum++) {
      argV.addElement(argv[argNum]);
    }

    argNum = 0;
    while (argNum < argV.size() &&
	   ((String)argV.elementAt(argNum)).charAt(0) == '-') {

      switch(((String)argV.elementAt(argNum)).charAt(1)) {

      case 'f':
	_showActionFailure = true;
	System.out.println("*** JAM: Showing Action Failures turned ON. ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
	
      case 'g':
	_showGoalList = true;
	System.out.println("*** JAM: Showing the Goal List turned ON. ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
	
      case 'i':
	_showIntentionStructure = true;
	System.out.println("*** JAM: Showing the Intention Structure turned ON. ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
	
      case 's':
      case 'a':
	_showAPL = true;
	System.out.println("*** JAM: Showing APL generation turned ON. ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
	
      case 'w':
	_showWorldModel = true;
	System.out.println("*** JAM: Showing the World Model turned ON. ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;

      case 'p':

	// Save the parse string to parse after file list.  Read and
	// save between quotation marks.
	System.out.println("*** JAM: Parsable command-line string found. ***\n");
	String pString = ((String)argV.elementAt(argNum)).substring(2);
	pStrings.addElement(pString);

	// Remove command-line arguments
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
	
      default:  
	System.out.println("*** JAM: Ignoring unknown flag '" +
			   ((String)argV.elementAt(argNum)).charAt(1) + "' ***\n");
	for (int i=argNum; i < argV.size()-1; i++) {
	  argV.setElementAt(argV.elementAt(i+1),i);
	}
	argV.removeElementAt(argV.size()-1);
	break;
      }
    }

    // Rebuild argv from argV
    String[]	newArgv = new String[argV.size()];
    for (int i=0; i < argV.size(); i++) {
      newArgv[i] = new String((String)argV.elementAt(i));
    }

    // Parse Jam agent definitions
    parse(newArgv);

    // Parse command-line Jam agent definitions
    for (int i=0; i < pStrings.size(); i++) {
      parseString(this, (String)pStrings.elementAt(i));
    }

    //System.out.println("JAM: Parser finished.");
  }

  //
  // Member functions
  //
  public PlanTable		getPlanLibrary()	{ return _planLibrary; }
  public void			setPlanLibrary(PlanTable t) { _planLibrary = t; }
  public WorldModelTable	getWorldModel()		{ return _worldModel; }
  public void			setWorldModel(WorldModelTable t) { _worldModel = t; }
  public IntentionStructure	getIntentionStructure() { return _intentionStructure; }
  public void			setIntentionStructure(IntentionStructure is) { _intentionStructure = is; }
  public Functions		getSystemFunctions()	{ return _systemFunctions; }
  public void			setSystemFunctions(Functions f) { _systemFunctions = f; }
  public Functions		getUserFunctions()	{ return _userFunctions; }
  public void			setUserFunctions(Functions f) { _userFunctions = f; }
  public Plan			getObserver() 		{ return _observer; }
  public void			setObserver(Plan p) 	{ _observer = p; }
  public SymbolTable		getRelationTable()	{ return _relationTable; }

  public int			getNumAPLsStat() 	{ return _numAPLs; }
  public void			setNumAPLsStat(int num) { _numAPLs = num; }
  public int			getNumNullAPLsStat() 	{ return _numNullAPLs; }
  public void			setNumNullAPLsStat(int num) { _numNullAPLs = num; }
  public int			getNumGoalsStat() 	{ return _numGoals; }
  public void			setNumGoalsStat(int num) { _numGoals = num; }
  public int			getNumCyclesStat() 	{ return _numCycles; }
  public void			setNumCyclesStat(int num) { _numCycles = num; }
  public double			getTotalUtilityStat() 	{ return _totalUtility; }
  public void			setTotalUtilityStat(double num) { _totalUtility = num; }

  public boolean		getAgentHasMoved() 	{ return _agentHasMoved; }
  public void			setAgentHasMoved(boolean flag) { _agentHasMoved = flag; }

  public boolean		getShowWorldModel()	{ return _showWorldModel; }
  public boolean		getShowGoalList()	{ return _showGoalList; }
  public boolean		getShowAPL()		{ return _showAPL; }
  public boolean		getShowIntentionStructure() { return _showIntentionStructure; }
  public boolean		getShowActionFailure()	{ return _showActionFailure; }

  public boolean		setShowWorldModel(boolean flag)	{ return _showWorldModel = flag; }
  public boolean		setShowGoalList(boolean flag)	{ return _showGoalList = flag; }
  public boolean		setShowAPL(boolean flag)	{ return _showAPL = flag; }
  public boolean		setShowIntentionStructure(boolean flag) { return _showIntentionStructure = flag; }
  public boolean		setShowActionFailure(boolean flag) { return _showActionFailure = flag; }


  /** 
   * Suspend the currently executing thread safely. 
   *
   */
  protected void mySuspend()
  {
    _threadSuspended = true;
  }             
  
  /** 
   * Returns <code>true</code> if this thread has been suspended. 
   * @see #mySuspend()
   *
   */
  protected boolean suspended()
  {
    return _threadSuspended;
  }

  /** 
   * Resume the currently executing thread safely. 
   *
   */
  protected synchronized void myResume() {
    _threadSuspended = false;
    notify(); 
  }
  
   
  /** 
   * Stop the currently executing thread safely. 
   *
   */
  protected synchronized void myStop() {
    _threadStopped = true;
    notify();
  }

  /** 
   * Returns <code>true</code> if this thread has been stopped.
   * @see #myStop()
   *
   */
  protected boolean stopped()
  {
    return _threadStopped;
  }
 
  /**
   * Parse the list of files for plans, goals, world model
   * entries, and Observer specifications.
   * 
   */
  public void parse(String argv[]) throws IOException, ParseException
  {
    int			argNum;
    StringBuffer	fileBuf = new StringBuffer();
    String		line;
    File		planFile;
    BufferedReader	dStream;
    String		lsep = System.getProperty("line.separator");
    JAMParser		parser = new JAMParser();

    for (argNum = 0; argNum < argv.length; argNum++) {
      fileBuf.setLength(0);
      //System.out.println("Arg[" + argNum + "] = " + argv[argNum]);
      
      try {
	planFile = new File(argv[argNum]);
	dStream = new BufferedReader(new FileReader(planFile));
	line = dStream.readLine();
	while (line != null) {
	  fileBuf.append(line);
	  fileBuf.append(lsep);
	  line = dStream.readLine();
	}
      }
      catch (EOFException eof) {
	System.out.println("\n--> Unexpected end-of-file in + " + argv[argNum]);
      }
      catch (FileNotFoundException noFile) {
	System.out.println("File " + argv[argNum] + " not found!");
	//continue;
      }
      catch (IOException io) {
	System.out.println("I/O error occured while reading " + argv[argNum]);
      }

      System.out.println("Interpreter::parse: Building interpreter from: " + argv[argNum]);
      parser.buildInterpreter(argv[argNum], fileBuf.toString(), this);
    }
  }

  /**
   * Execute the agent's behavior
   * 
   */
  public boolean think()
  {
    APL			apl = null;
    APL			last_apl = null;
    APLElement		selectedElement;
    int			returnValue;
    Relation		rel;
    ExpList		ex;
    int			metaLevel;
    Date		runStart, runEnd;
    long		runTotal = 0;
    Date		observerStart, observerEnd;
    long		observerTotal = 0;
    Date		aplStart, aplEnd;
    long		aplTotal = 0;
    Date		planExecutionStart, planExecutionEnd;
    long		planExecutionTotal = 0;
    Date		intendStart, intendEnd;
    long		intendTotal = 0;

    runStart = new Date();
    //System.out.println("Starting to execute.");
    //System.out.println("Start time: " + runStart.getTime());

    // Loop forever until agent completes all of its goals
    while (true) { // outer, infinite loop

      setNumCyclesStat(getNumCyclesStat() + 1);

      // Execute the "execute" body here when it's implemented
//      returnValue = _intentionStructure.executePlan(_execute);

      // Execute the Observer procedure
      observerStart = new Date();
      returnValue = _intentionStructure.executePlan(_observer);
      observerEnd = new Date();
      observerTotal += observerEnd.getTime() - observerStart.getTime();

      // Loop until agent completes metalevel "ramp up"
      metaLevel = 0;
      while (true) {// metalevel loop
	// Generate an Applicable Plan List (APL) if necessary
	aplStart = new Date();
	apl = new APL(_planLibrary, _worldModel, _intentionStructure, metaLevel);
	aplEnd = new Date();
	aplTotal += aplEnd.getTime() - aplStart.getTime();
	setNumAPLsStat(getNumAPLsStat() + 1);

	if (_showAPL) {
	  System.out.println("JAM: APL generated has " +
			     apl.getSize() + " element(s) at metalevel " +
			     metaLevel);
	}

	// If the new or previous APL is not empty then add entry to the
	// World Model to trigger the next level of reasoning.
	//	if ((apl.getSize() != 0) || (last_apl != null && last_apl.getSize() != 0)) {
	if (apl.getSize() != 0) {

	  ex = new ExpList();
	  ex.append(new Value(metaLevel));
	  ex.append(new Value(apl));
	  ex.append(new Value(apl.getSize()));
	  rel = new Relation("APL", ex, this);
	  getWorldModel().assert(rel, null);
	}

	// If this APL is empty then no new level of reasoning
	if (apl.getSize() == 0) {
	  setNumNullAPLsStat(getNumNullAPLsStat() + 1);

	  //	  System.out.println("APL: APL was empty.\n");

	  // Make this run-time configurable to an alternative, where
	  // the system keeps running, even after there are no goals.
	  if ((_intentionStructure.allGoalsDone())) {
	    System.out.println("\nJAM: All of the agent's top-level goals have been achieved!  Returning...");

	    System.out.println("\nRuntime statistics follow:\n");
	    System.out.println("  Number of APLs generated:\t" + getNumAPLsStat());
	    System.out.println("  Number of Null APLs:\t\t" + getNumNullAPLsStat());
	    System.out.println("  Number of Goals established:\t" + getNumGoalsStat());
	    System.out.println("  Number of interpreter cycles:\t" + getNumCyclesStat());
	    runEnd = new Date();
	    runTotal = runEnd.getTime() - runStart.getTime();
	    System.out.println("\n  APL generation time:\t\t" +
			       aplTotal/1000.0 + " seconds.");
	    System.out.println("  Intending time:\t\t" +
			       intendTotal/1000.0 + " seconds.");
	    System.out.println("  Plan execution time:\t\t" +
			       planExecutionTotal/1000.0 + " seconds.");
	    System.out.println("  Observer execution time:\t" +
			       observerTotal/1000.0 + " seconds.");
	    System.out.println("  Total run time:\t\t" +
			       runTotal/1000.0 + " seconds.");

	    return true;
	  }

	  // If the previous APL was empty then execute something in the
	  // intention structure, otherwise select something from the previous
	  // APL, intend it, and then run something in the intention structure.
	  if (last_apl == null || last_apl.getSize() == 0) {
	    //	    System.out.println("APL: last_APL was empty also.\n");
	    planExecutionStart = new Date();
	    _intentionStructure.think();
	    planExecutionEnd = new Date();
	    planExecutionTotal += planExecutionEnd.getTime() - planExecutionStart.getTime();
	    break;
	  }
	  else {

	    //System.out.println("APL: last_APL was NOT empty, picking element.\n");

	    // selectedElement = apl.getRandom();
	    // selectedElement = apl.getUtilityFirst();
	    intendStart = new Date();
	    selectedElement = last_apl.getUtilityRandom();

	    if (_showAPL || _showIntentionStructure) {
	      System.out.println("\nJAM: Selected plan \"" +
				 selectedElement.getPlan().getName() +
				 "\" from APL.\n");
	    }

	    _intentionStructure.intend(selectedElement, false);
	    intendEnd = new Date();
	    intendTotal += intendEnd.getTime() - intendStart.getTime();

	    if (_showIntentionStructure) {
	      System.out.println("\nJAM: Intended element, Intention structure now:");
	      _intentionStructure.print(System.out);
	    }

	    planExecutionStart = new Date();
	    _intentionStructure.think();
	    planExecutionEnd = new Date();
	    planExecutionTotal += planExecutionEnd.getTime() - planExecutionStart.getTime();
	    last_apl = null;
	    break;
	  }
	}
	else {

	  if (_showAPL) {
	    System.out.println("JAM: APL generated:");
	    apl.print(System.out);
	  }

	  last_apl = apl;
	  metaLevel++;
	  if (_showAPL) {
	    System.out.println("JAM: Metalevel now at: " + metaLevel);
	  }
	}
      } // Inner, metalevel loop


      // NOTE: Any metalevel plan should do the necessary WM clearing.
      // However, if there is no metalevel plans then we need to clear
      // the World Model.
      rel = new Relation("APL", null, this);
      getWorldModel().retract(rel, null);

    } // Outer infinite loop

  }

  /**
   * Parse the string for plans, goals, world model entries,
   * and Observer specifications.
   * 
   */
  public void parseString(Interpreter interpreter, String pString)
  {
    JAMParser parser = new JAMParser();
    parser.parseString(interpreter, null, pString);
  }

  /**
   * Set up the internal state of the object
   * 
   */
  private void init()
  {
    _planLibrary = new PlanTable();
    _worldModel = new WorldModelTable(this);
    _intentionStructure = new IntentionStructure(this);
    _observer = null;
    _systemFunctions = new SystemFunctions(this);
    _userFunctions = new UserFunctions(this);
    _relationTable = new SymbolTable(512, 128, 253);

    _numAPLs = 0;
    _numNullAPLs = 0;
    _numGoals = 0;
    _numCycles = 0;
    _totalUtility = 0.0;

    _agentHasMoved = false;

    _showWorldModel = false;
    _showGoalList = false;
    _showIntentionStructure = false;
    _showAPL = false;
    _showActionFailure = false;

    _threadStopped = true;
    _threadSuspended = false;
  }

}
