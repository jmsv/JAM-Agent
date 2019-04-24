//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: RestoreAgentCheckpoint.java,v 1.1 1998/11/03 17:55:30 marcush Exp $
//  $Source: c:/com/irs/jam/RCS/RestoreAgentCheckpoint.java,v $
//  
//  File              : RestoreAgentCheckpoint.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : Read a checkpoint file and restart the JAM
//                      agent from where it was saved.
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Tue Nov  3 09:42:04 1998
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Wed Jun 21 19:21:09 2000
//  Update Count      : 15
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

package com.irs.jam;

import java.io.*;
import java.util.*;

import com.irs.jam.*;

/**
 *
 * Mobility utility class
 *
 * @author Marc Huber
 *
 **/

public class RestoreAgentCheckpoint
{
  public static void main(String argv[])
  {
    FileInputStream	fis;
    ObjectInputStream	in;

    JAM			agent = new JAM();
    Interpreter		interp = new Interpreter();
    WorldModelTable	wm;
    PlanTable		plib;
    Plan		obs;
    IntentionStructure	is;
    Functions		sys;
    Functions		user;
    Object		o;

    try {
      fis  = new FileInputStream(argv[0]);
      in   = new ObjectInputStream(fis);
      JAM jamAgent = new JAM();

      interp = (Interpreter) in.readObject();
      jamAgent.think(interp);
    }
    catch (Exception e) {
      System.out.println("Error *" + e +
			 "* reading JAM agent checkpoint file!");
      e.printStackTrace();
    }
  }
} 
