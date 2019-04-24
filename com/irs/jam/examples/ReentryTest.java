//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id$
//  $Source$
//  
//  File              : ReentryClass.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : A class designed to test JAM's source-level
//                      invocation and re-invocation API
//  
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Thu Jul  1 06:44:22 1999
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Sun Oct 28 14:58:41 2001
//  Update Count      : 9
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1999-2001 Marcus J. Huber and Intelligent Reasoning Systems
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

package com.irs.jam.examples;

import com.irs.jam.*;

public class ReentryTest
{
  public static void main(String argv[])
  {
    boolean retVal;
    String argList1a[] = { "ex1.jam" };
    String argList1b = "GOALS: ACHIEVE printing_done \"goal3\";";
    String argList2a[] = { "ex2.jam" };
    String argList2b = "GOALS: ACHIEVE testing_done \"hello\";";
    /*
    System.out.println("argList1a[0]=" + argList1a[0]);
    System.out.println("argList1b[0]=" + argList1b[0]);
    System.out.println("argList2a[0]=" + argList2a[0]);
    System.out.println("argList2b[0]=" + argList2b[0]);
    */

    JAM jamAgent = new JAM();
    Interpreter i1 = null;
    Interpreter i2 = null;
    try {
      i1 = new Interpreter(argList1a);
    }
    catch (Exception e) {
      System.out.println("Error parsing file " + argList1a[0] + ".");
    }

    try {
      i2 = new Interpreter(argList2a);
    }
    catch (Exception e) {
      System.out.println("Error parsing file " + argList2a[0] + ".");
    }

    retVal = jamAgent.think(i1);
    System.out.println("Agent returned " + retVal);
    retVal = jamAgent.think(i1);
    System.out.println("Agent returned " + retVal);

    try {
      i1.parseString(i1, argList1b);
    }
    catch (Exception e) {
      System.out.println("Error parsing " + argList1b);
    }

    retVal = jamAgent.think(i1);
    System.out.println("Agent returned " + retVal);
  }
}
