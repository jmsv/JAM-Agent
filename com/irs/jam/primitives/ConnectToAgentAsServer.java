//  -*- Mode: Java -*-
//////////////////////////////////////////////////////////////////////////////
//  
//  $Id: ConnectToAgentAsServer.java,v 1.1 1998/11/04 17:07:52 marcush Exp $
//  $Source: c:/com/irs/jam/primitives/RCS/ConnectToAgentAsServer.java,v $
//  
//  File              : ConnectToAgentAsServer.java
//  Author(s)         : marcush <marcush@irs.home.com>
//  
//  Description       : A very simple socket-based implementation of
//                      making contact to another agent where the agent
//                      calling _this_ primitive will wait for the
//                      connection.
//  
//  Original author(s): marcush <marcush@irs.home.com>
//  Organization      : Intelligent Reasoning Systems
//  Created On        : Fri Oct  2 14:34:47 1998
//  Last Modified By  :  <marcush@irs.home.com>
//  Last Modified On  : Tue Aug 01 11:04:30 2000
//  Update Count      : 6
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

import java.io.*;
import java.net.*;

import com.irs.jam.*;

public class ConnectToAgentAsServer implements PrimitiveAction
{

  //
  // Contact another agent as a server to that agent using a
  // low-level socket interface.
  //
  public Value execute(String name, int arity, ExpList args,
		       Binding binding, Goal currentGoal)
  {
    if (arity != 3) {
      System.out.println("Invalid number of arguments: " + arity +
			 " to function \"" + name + "\"\n");
      return Value.FALSE;
    }

    ExpListEnumerator	ele = new ExpListEnumerator(args);
    Expression exp = (Expression) ele.nextElement();
    int port = (int) exp.eval(binding).getLong();

    DataInputStream	in;
    PrintWriter		out;
    Socket		socket;
    ServerSocket	server;

    try {
      server = new ServerSocket(port);

      socket	= server.accept();
      in	= new DataInputStream(socket.getInputStream());
      out	= new PrintWriter(socket.getOutputStream());
	
      exp = (Expression) ele.nextElement();
      binding.setValue(exp, new Value(in));
      exp = (Expression) ele.nextElement();
      binding.setValue(exp, new Value(out));
	
      return Value.TRUE;
    }
    catch (IOException e) {
      System.out.println("JAM::ConnectToAgentAsServer:IOException : " + e);
      return Value.FALSE;
    }
  }
}
