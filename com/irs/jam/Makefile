## -*- Mode: Makefile -*-
###############################################################################
## 
## $Id: Makefile,v 1.4 1998/04/22 18:27:12 marcush Exp $
## $Source: /usr/home/marcush/Java/Jam/jam/RCS/Makefile,v $
## 
## SCCS Status	     : %W%	%G%
## 
## File		     : Makefile
## Author(s)	     : Jaeho Lee and Marc Huber
## 
## Description	     : 
## 
## Original author(s): Jaeho Lee
## Organization	     : 
## Created On	     : Wed Apr 30 17:54:30 1997
## Last Modified By  : <marcush@marcush.net>
## Last Modified On  : Sun Oct 28 15:58:01 2001
## Update Count	     : 77
## 
## Copyright (C) 1997 Jaeho Lee and Marcush Huber
## 
###############################################################################

JAVA		= java
JAVAC		= javac -g
JAVACC		= javacc.bat

RM		= /bin/rm -f
CP		= /bin/cp
CD		= cd


##
##
##

JJ_INPUT	= JAMParser

JJ_OUTPUT	= $(JJ_INPUT).java			\
		  $(JJ_INPUT)Constants.java		\
		  $(JJ_INPUT)TokenManager.java		\
		  ASCII_CharStream.java			\
		  ParseError.java			\
		  ParseException.java			\
		  Token.java				\
		  TokenMgrError.java

ALL_CLASS =	\
		$(JJ_INPUT).class \
		APL.class \
		APLElement.class \
		AchieveGoalAction.class \
		Action.class \
		AssertAction.class \
		AssignAction.class \
		Binding.class \
		BindingList.class \
		BindingListEnumerator.class \
		BindingValue.class \
		Condition.class \
		ConditionList.class \
		ConditionListEnumerator.class \
		DLink.class \
		DList.class \
		DListEnumerator.class \
		ExpList.class \
		ExpListEnumerator.class \
		Expression.class \
		ExpressionCondition.class \
		FactAction.class \
		FactCondition.class \
		FailAction.class \
		Functions.class \
		FunctionCall.class \
		Goal.class \
		GoalAction.class \
		IntentionStructure.class \
		Interpreter.class \
		JAM.class \
		LoadAction.class \
		MaintainGoalAction.class \
		NextFactAction.class \
		ObjectAction.class \
		PerformGoalAction.class \
		Plan.class \
		PlanAtomicConstruct.class \
		PlanBranchConstruct.class \
		PlanConstruct.class \
		PlanContext.class \
		PlanDoConstruct.class \
		PlanDoAnyConstruct.class \
		PlanDoAllConstruct.class \
		PlanRuntimeAtomicState.class \
		PlanRuntimeBranchState.class \
		PlanRuntimeDoState.class \
		PlanRuntimeDoAnyState.class \
		PlanRuntimeDoAllState.class \
		PlanRuntimeGoalState.class \
		PlanRuntimeSequenceState.class \
		PlanRuntimeSimpleState.class \
		PlanRuntimeParallelState.class \
		PlanRuntimeState.class \
		PlanRuntimeThreadState.class \
		PlanRuntimeWaitState.class \
		PlanRuntimeWhenState.class \
		PlanRuntimeWhileState.class \
		PlanSequenceConstruct.class \
		PlanSimpleConstruct.class \
		PlanParallelConstruct.class \
		PlanTable.class \
		PlanWaitConstruct.class \
		PlanWhenConstruct.class \
		PlanWhileConstruct.class \
		PostAction.class \
		Predicate.class \
		PredicateAchieve.class \
		PredicateFact.class \
		PredicateRetrieve.class \
		QueryGoalAction.class \
		Relation.class \
		RelationCondition.class \
		RetractAction.class \
		RetrieveAction.class \
		RetrieveCondition.class \
		SimpleAction.class \
		Symbol.class \
		SymbolTable.class \
		SystemFunctions.class \
		Table.class \
		TableObject.class \
		TestAction.class \
		UnpostAction.class \
		UpdateAction.class \
		UserFunctions.class \
		Value.class \
		ValueLong.class \
		ValueObject.class \
		ValueReal.class \
		ValueString.class \
		Variable.class \
		WorldModelAction.class \
		WorldModelRelation.class \
		WorldModelTableBucketEnumerator.class  \
		WorldModelTable.class \
		primitives/ConnectToAgentAsClient.class \
		primitives/ConnectToAgentAsServer.class \
		primitives/Exec.class \
		primitives/Exit.class \
		primitives/Fail.class \
		primitives/GetHostname.class \
		primitives/GetHostname2.class \
		primitives/HelloWorld.class \
		primitives/ReceiveMessage.class \
		primitives/SendMessage.class \
		primitives/clean.bat \
		primitives/make.bat \
		primitives/pIsToplevelGoalp.class \
		primitives/pIsVariableInitializedp.class \
		examples/ReentryTest.class \
		examples/TestClass.class

.SUFFIXES :.java .class
.java.class : ; $(JAVAC) $*.java


all:		$(JJ_INPUT).java $(ALL_CLASS) primitives examples

$(JJ_INPUT).java: $(JJ_INPUT).jj
		$(JAVACC) $(JJ_INPUT).jj

%.class:	%.java
		$(JAVAC) $*.java

primitives/%.class:	primitives/%.java
		$(JAVAC) primitives/$*.java

(CLASS_DEST):
		@mkdir -p $(CLASS_DEST)

tags:		;
		jtags *.java

clean:		;
		$(RM) *.class $(JJ_OUTPUT) TAGS
		$(RM) primitives/*.class $(JJ_OUTPUT) TAGS
		$(RM) examples/*.class $(JJ_OUTPUT) TAGS

Clean:		clean
		make all
