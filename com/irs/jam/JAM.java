//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: JAM.java,v 1.6 1998/11/04 18:48:21 marcush Exp marcush $
//  $Source: C:\\com\\irs\\jam\\RCS\\JAM.java,v $
//  
//  File              : JAM.java
//  Original author(s): Marcus J. Huber <marcush@heron.eecs.umich.edu>
//                    : Jaeho Lee <jaeho@heron.eecs.umich.edu>
//  Created On        : Tue Sep 30 14:21:51 1997
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Tue Nov 06 17:28:33 2001
//  Update Count      : 168
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997 Marcus J. Huber and Jaeho Lee.
//  Copyright (C) 1997-2001 Intelligent Reasoning Systems
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
 * The JAM Agent application interface
 *
 * @author Marc Huber
 * @author Jaeho Lee
 *
 **/

public class JAM implements Serializable, Runnable
{
  //
  // Members
  //
  private Interpreter	_interpreter;

  //
  // Constructors
  //

  /**
   * Default constructor
   * 
   */
  public JAM()
  {
    _interpreter = new Interpreter();
  }
    
  /**
   * Constructor w/ command-line arguments
   * 
   */
  public JAM(String argv[]) {
    try {
      _interpreter = new Interpreter(argv);
    }
    catch (Exception e) {
	e.printStackTrace();
    }
  }
    
  /**
   * Constructor with Interpreter
   * 
   */
  public JAM(Interpreter interpreter)
  {
    _interpreter = interpreter;
  }
    

  //
  // Member functions
  //

  public Interpreter getInterpreter() { return _interpreter; }

  /** 
   * Stop the currently executing thread safely. 
   *
   */
  public void myStop()
  {
    if (_interpreter != null) {
        _interpreter.myStop();
    }
    else {
        throw new NullPointerException("JAM interpreter not initialized."); 
    }
  }
  
  /** 
   * Returns <code>true</code> if this thread has been stopped.
   * @see #myStop()
   *
   */
  public boolean stopped()
  {
    if (_interpreter != null) {
        return _interpreter.stopped();
    }
    else {
        throw new NullPointerException("JAM interpreter not initialized."); 
    }
  }
  
  /** 
   * Returns <code>true</code> if the interpreter is running.
   *
   */
  public boolean running() {
    return ((_interpreter != null) && !(_interpreter.stopped()));
  }

  
  /**
   * Command-line interface for users to start Jam agent.
   * 
   */
  public static void main(String argv[])
  {
    JAM jamAgent = new JAM(argv);

    try {
      jamAgent.think();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Entry-point for deserialized agents to restart.
   * 
   */
  public boolean think(Interpreter interpreter)
  {
     // Stop JAM from thinking if it is doing so and then replace its
     // "mind" so to speak.
    if (running()) myStop();

    if (interpreter != null) {
      _interpreter = interpreter;
      return (_interpreter.think());
    }
    else {
      throw new NullPointerException("JAM interpreter not initialized.");
    }
  }

  /**
   * Re-entry point for restartable JAM agents.
   * 
   */
  public boolean think()
  {
    if (_interpreter != null) {
      return (_interpreter.think());
    }
    else
      return false;
  }

  /** Overrides the run() method in the Thread parent class.
   *
   * @see java.lang.Thread#run()
   *
   * Dan Damouth (ORINCON Corp): added for invocation as a thread.
   * Exception throwing from Martin Klesen (DFKI)
   *
   **/
  public void run()
  {
    if (_interpreter != null) {
      _interpreter.think();
    }
    else {
        throw new NullPointerException("JAM interpreter not initialized."); 
    }
  }

  /**
   * Entry-point for invoking or re-invoking agents from Java
   * code (in contrast to starting it from a command-line).
   * 
   */
  public boolean think(String argv[]) throws IOException, ParseException
  {
    if (_interpreter == null) {
      System.out.println("\n\nJam Agent Architecture");
      System.out.println("Version 0.65 + 0.76i [November, 2001]");
      System.out.println("Copyright (C) 1997 Marcus J. Huber and Jaeho Lee");
      System.out.println("Copyright (C) 1997-2001 Intelligent Reasoning Systems");
      System.out.println("All Rights Reserved.\n");

      _interpreter = new Interpreter(argv);
    }
    else {
      _interpreter.parse(argv);
    }

    return _interpreter.think();
  }

}
