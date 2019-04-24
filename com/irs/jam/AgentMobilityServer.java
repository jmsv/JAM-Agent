//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id$
//  $Source$
//  
//  File              : AgentMobilityServer.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : Receive an agent checkpoint and reinstate it
//                      to execution status.
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Thu Nov 19 21:17:07 1998
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Wed Jun 21 19:21:10 2000
//  Update Count      : 44
//  
//////////////////////////////////////////////////////////////////////////////
//
//  JAM agent architecture
//
//  Copyright (C) 1997-1999 Marcus J. Huber and Intelligent Reasoning Systems
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
import java.net.*;
import java.util.*;

import com.irs.jam.*;

public class AgentMobilityServer
{
  // parameters:
  //    int: port to await incoming agents
  //
  public static void main(String argv[])
  {
    // Connect to client as a server
    int port = Integer.parseInt(argv[0]);

    System.out.println("Waiting for incoming agent on port: " + port);
    ObjectInputStream	in;
    Socket		socket;
    ServerSocket	server;
    Interpreter		interp = null;
    JAM			agent = new JAM();

    try {
      server	= new ServerSocket(port);
      socket	= server.accept();
      in	= new ObjectInputStream(socket.getInputStream());
      try {
	interp	= (Interpreter) in.readObject();
	interp.setAgentHasMoved(true);
      }
      catch (ClassNotFoundException e) {
	System.out.println("JAM::AgentMobilityServer:Class Not Found! " + e);
	e.printStackTrace();
      }
      interp.think();
    }
    catch (IOException e) {
      System.out.println("JAM::ConnectToAgentAsServer:IOException : " + e);
      e.printStackTrace();
    }
  }
} 
