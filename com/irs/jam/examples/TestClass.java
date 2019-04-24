//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id$
//  $Source$
//  
//  File              : TestClass.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : A class designed to be accessed from within JAM
//                      using JAM's reflection capabilities.
//  
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Thu Jul  1 06:44:22 1999
//  Last Modified By  : <marcush@marcush.net>
//  Last Modified On  : Sun Oct 28 14:58:41 2001
//  Update Count      : 4
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997-2001 Marcus J. Huber and Intelligent Reasoning Systems
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

public class TestClass
{
  public int	intMember;
  public double	realMember;
  public String	stringMember;

  public TestClass()
  {
    intMember = 1;
    realMember = 3.3;
    stringMember = "Default constructor used";
  }

  public TestClass(int i, double d, String s)
  {
    intMember = i;
    realMember = d;
    stringMember = "Full constructor used";
  }

  public TestClass(int i)
  {
    intMember = i;
    realMember = 9.9;
    stringMember = "Int constructor used";
  }

  public void setIntMember(int i)	{ intMember = i; }
  public void setRealMember(double d)	{ realMember = d; }
  public void setStringMember(String s)	{ stringMember = s; }

  public void print()
  {
    System.out.println("TestClass::print: intMember = " + intMember);
    System.out.println("TestClass::print: realMember = " + realMember);
    System.out.println("TestClass::print: stringMember = " + stringMember);
  }

  public Object memberFunction1(int a, String b, String c) {
    System.out.println("\nIn method 1: " + a + ", " + b + ", " + c + "\n");
    return null;
  }

  public String memberFunction2(int a, int b, String c) {
    System.out.println("\nIn method 2: " + a + ", " + b + ", " + c + "\n");
    return new String("hello " + c);
  }

  public int memberFunction3(String a, String b, String c) {
    System.out.println("\nIn method 3a: " + a + ", " + b + ", " + c + "\n");
    return 3;
  }

  public int memberFunction3(String a, String b, int c) {
    System.out.println("\nIn method 3b: " + a + ", " + b + ", " + c + "\n");
    return 3;
  }

  public int memberFunction3(String a, String b, double c) {
    System.out.println("\nIn method 3c: " + a + ", " + b + ", " + c + "\n");
    return 3;
  }

  public String memberFunction4(String a, String b, int c) {
    System.out.println("\nIn method 4: " + a + ", " + b + ", " + c + "\n");
    return "4";
  }

  public double memberFunction5(String a, String b, double c) {
    System.out.println("\nIn method 5a: " + a + ", " + b + ", " + c + "\n");
    return c*3.0;
  }

  public double memberFunction5(String a, double b, double c) {
    System.out.println("\nIn method 5b: " + a + ", " + b + ", " + c);
    System.out.println("   " + b + " + " + c + " = " + (b+c));
    return b+c;
  }

  public int memberFunction6(TestClass t) {
    System.out.println("\nIn method 6: " + t);
    t.setIntMember(54321);
    t.setRealMember(5.4321);
    t.setStringMember("54321");
    return 1;
  }
}
