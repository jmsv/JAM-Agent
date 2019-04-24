package com.irs.jam.primitives;

import java.io.*;

import com.irs.jam.*;

/**
 * 
 * A predicate indicating whether the variable has a value assigned to it.
 * <p>
 *
 * @author Marc Huber
 * @version $Id: isVariableInitializedp.java,v 1.1 2000/04/04 22:17:41 marcush Exp $ $Source: /rfs/users1/reynolds/dd21/java/com/orincon/DD21/taskmanager/primitives/isVariableInitializedp.java,v $
 *
 */
public class pIsVariableInitializedp implements PrimitiveAction
{
    //
    // Members
    //

    // none

    //
    // Constructors
    //

    // none

    //
    // Methods
    //

    /**
     *
     * A predicate indicating whether the variable has
     * a value assigned to it.
     * 
     * @param variable the variable to test
     * @param variableName the string rep of the variable to test
     * @return true if variable has a value assigned to it, false otherwise
     *
     */
    public Value execute(String name, int arity, ExpList args,
			 Binding binding, Goal currentGoal)
    {
	if (arity != 1) {
	    System.out.println("Invalid number of arguments: " + arity +
			       " to function \"" + name + "\"\n");
	    return Value.FALSE;
	}

	ExpListEnumerator ele = new ExpListEnumerator(args);
	Value variable = (Value) ((Expression) ele.nextElement()).eval(binding);

	return (variable.isDefined() ? Value.TRUE : Value.FALSE);
    }
}
