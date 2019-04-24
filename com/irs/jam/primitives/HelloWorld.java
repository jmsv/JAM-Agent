//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id$
//  $Source$
//  
//  File              : HelloWorld.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : 
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Wed Feb  3 18:56:20 1999
//  Last Modified By  : marcush <marcush@irs.home.com>
//  Last Modified On  : Tue Jul 27 13:44:39 1999
//  Update Count      : 18
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1998-1999 Marcus J. Huber and Intelligent Reasoning Systems
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

package com.irs.jam.primitives;

import com.irs.jam.*;

public class HelloWorld implements PrimitiveAction 
{
  public Value execute(String name, int arity, ExpList args,
                       Binding binding, Goal currentGoal) 
    {
      //
      // Create an instance of the class HelloWorld and return it
      //
      if (arity != 4) {
        System.out.println("Invalid number of arguments: " + arity +
                           " to function \"" + name + "\"\n");
        return Value.FALSE;
      }

      ExpListEnumerator ele = new ExpListEnumerator(args);
      Expression exp1 = (Expression) ele.nextElement();
      Expression exp2 = (Expression) ele.nextElement();
      Expression exp3 = (Expression) ele.nextElement();
      Expression exp4 = (Expression) ele.nextElement();
      System.out.println("Type of exp1 is: " + exp1.eval(binding).type());
      System.out.println("Type of exp2 is: " + exp2.eval(binding).type());
      System.out.println("Type of exp3 is: " + exp3.eval(binding).type());
      System.out.println("Type of exp4 is: " + exp4.eval(binding).type());
      if ((exp1.eval(binding).type() == Value.VAL_STRING) &&
	  (exp2.eval(binding).type() == Value.VAL_REAL) &&
	  (exp3.eval(binding).type() == Value.VAL_LONG) &&
	  (exp4.eval(binding).type() == Value.VAL_OBJECT)) {
	System.out.println("All arguments of correct type.\n");
      }
      else {
	System.out.println("AT LEAST ONE ARGUMENT NOT OF THE CORRECT TYPE.\n");
      }

      return Value.TRUE;
    }

}
