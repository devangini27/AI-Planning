package planning2;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

public class ActionConstants {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actionName == null) ? 0 : actionName.hashCode());
		result = prime * result + ((effects == null) ? 0 : effects.hashCode());
		result = prime * result
				+ ((precondition == null) ? 0 : precondition.hashCode());
		result = prime * result + problemNumber;
		result = prime * result + (pureAction ? 1231 : 1237);
		result = prime * result + Arrays.hashCode(variables);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionConstants other = (ActionConstants) obj;
		if (actionName == null) {
			if (other.actionName != null)
				return false;
		} else if (!actionName.equals(other.actionName))
			return false;
		if (effects == null) {
			if (other.effects != null)
				return false;
		} else if (!effects.equals(other.effects))
			return false;
		if (precondition == null) {
			if (other.precondition != null)
				return false;
		} else if (!precondition.equals(other.precondition))
			return false;
		if (problemNumber != other.problemNumber)
			return false;
		if (pureAction != other.pureAction)
			return false;
		if (!Arrays.equals(variables, other.variables))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nActionConstants [actionName=" + actionName + ", variables="
				+ Arrays.toString(variables) + ", precondition=" + precondition
				+ ", effects=" + /* effects + */", pureAction=" + pureAction
				+ "]";
	}

	static ActionConstants[] possibleActions;
	static ActionConstants[] possiblePredicates;

	public static ActionConstants NULL = new ActionConstants("null action", null,
			null, null, false, -1, true);

	static ActionConstants EQUAL = new ActionConstants("=", null, null, null,
			false, -1, true);

	// static ActionConstants HAVE;
	// static ActionConstants EAT;
	// static ActionConstants EATEN;
	// static ActionConstants BAKE;
	//
	// static ActionConstants REMOVE1;
	// static ActionConstants REMOVE2;
	// static ActionConstants PUTON;
	// static ActionConstants AT;
	// static ActionConstants LEAVEOVERNIGHT;

	// static {
	//
	// /**
	// * Common to all problems
	// */
	// NULL =
	//
	// /**
	// * Cake baking problem
	// */
	// HAVE = new ActionConstants("have",
	// new VariableConstants[] { VariableConstants.CAKE }, null, null,
	// false, 0, false);
	// EATEN = new ActionConstants("eaten",
	// new VariableConstants[] { VariableConstants.CAKE }, null, null,
	// false, 0, false);
	// EAT = new ActionConstants(
	// "eat",
	// new VariableConstants[] { VariableConstants.CAKE },
	// new ActionClass(HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true }),
	// new CompoundActionClass(
	// new ActionClass[] {
	// new ActionClass(
	// HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { false }),
	// new ActionClass(
	// EATEN,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), true, 0, false);
	//
	// BAKE = new ActionConstants("bake",
	// new VariableConstants[] { VariableConstants.CAKE },
	// new ActionClass(HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { false }), new ActionClass(HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true }), true, 0, false);
	//
	// /**
	// * Spare tyre problem
	// */
	// AT = new ActionConstants("at", new VariableConstants[] { null, null },
	// null, null, false, 1, true);
	// REMOVE1 = new ActionConstants("remove", new VariableConstants[] {
	// VariableConstants.SPARE, VariableConstants.TRUNK },
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE, VariableConstants.TRUNK },
	// new boolean[] { true }), new CompoundActionClass(
	// new ActionClass[] {
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.TRUNK },
	// new boolean[] { false }),
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.GROUND },
	// new boolean[] { true }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), true, 1, false);
	// REMOVE2 = new ActionConstants("remove", new VariableConstants[] {
	// VariableConstants.FLAT, VariableConstants.AXLE },
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.FLAT, VariableConstants.AXLE },
	// new boolean[] { true }), new CompoundActionClass(
	// new ActionClass[] {
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.FLAT,
	// VariableConstants.AXLE },
	// new boolean[] { false }),
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.FLAT,
	// VariableConstants.GROUND },
	// new boolean[] { true }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), true, 1, false);
	//
	// PUTON = new ActionConstants("puton", new VariableConstants[] {
	// VariableConstants.SPARE, VariableConstants.AXLE },
	// new CompoundActionClass(new ActionClass[] {
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.GROUND },
	// new boolean[] { true }),
	// new ActionClass(AT,
	// new VariableConstants[] {
	// VariableConstants.FLAT,
	// VariableConstants.AXLE },
	// new boolean[] { false }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), new CompoundActionClass(
	// new ActionClass[] {
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.AXLE },
	// new boolean[] { true }),
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.GROUND },
	// new boolean[] { false }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), true, 1, false);
	// LEAVEOVERNIGHT = new ActionConstants("leave overnight", null, null,
	// new CompoundActionClass(new ActionClass[] {
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.GROUND },
	// new boolean[] { false }),
	// new ActionClass(AT,
	// new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.AXLE },
	// new boolean[] { false }),
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.SPARE,
	// VariableConstants.TRUNK },
	// new boolean[] { false }),
	// new ActionClass(AT, new VariableConstants[] {
	// VariableConstants.FLAT,
	// VariableConstants.GROUND },
	// new boolean[] { false }),
	// new ActionClass(AT,
	// new VariableConstants[] {
	// VariableConstants.FLAT,
	// VariableConstants.AXLE },
	// new boolean[] { false }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true }), true, 1, false);
	//
	// try {
	// addAllActions();
	// } catch (NoSuchFieldException e) {
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// e.printStackTrace();
	// } catch (IllegalArgumentException e) {
	// e.printStackTrace();
	// } catch (IllegalAccessException e) {
	// e.printStackTrace();
	// } catch (InvocationTargetException e) {
	// e.printStackTrace();
	// } catch (NoSuchMethodException e) {
	// e.printStackTrace();
	// }
	//
	// }

	private String actionName;
	private VariableConstants variables[];
	private BaseActionClass precondition;
	private BaseActionClass effects;
	private boolean pureAction;
	private int problemNumber;
	/**
	 * Makes a difference to the ones that have null preconditions
	 */
	private boolean unique;

	ActionConstants(String actionName, VariableConstants variables[],
			BaseActionClass precondition, BaseActionClass effects,
			boolean pureAction, int problemNumber, boolean unique) {
		this.actionName = actionName;
		this.setVariables(variables);
		this.precondition = precondition;
		this.effects = effects;
		this.pureAction = pureAction;
		this.setProblemNumber(problemNumber);
		this.unique = unique;
	}

	ActionConstants() {

	}

	public int getFreeVariableCount() {
		// find how many free vars there are
		int freeVarCount = 0;
		for (VariableConstants vars : variables) {
			if (vars.isFree())
				freeVarCount++;
		}
		return freeVarCount;
	}

	public VariableConstants[] getParameters(ArrayList<State> predecessorStates)
			throws ApplicationException {

		// System.out.println("size --- " + predecessorStates.size());
		// get all the values for variables
		ArrayList<VariableConstants> argumentVars = this.precondition
				.getParameters();
		ArrayList<VariableConstants> argumentValues = new ArrayList<VariableConstants>();
		for (int i = 0; i < predecessorStates.size(); i++) {
			for (int j = 0; j < predecessorStates.get(i).getPresentState()
					.getVariables().length; j++) {
				if (!argumentValues.contains(predecessorStates.get(i)
						.getPresentState().getVariables()[j])) {
					// System.out.println("????? adding unique var "
					// + predecessorStates.get(i).getPresentState()
					// .getVariables()[j]);
					argumentValues.add(predecessorStates.get(i)
							.getPresentState().getVariables()[j]);
				}
			}
		}

		VariableConstants[] parameters = new VariableConstants[this.variables.length];

		// find unique action constants
		ArrayList<State> uniqueActionConstants = new ArrayList<State>();
		for (int i = 0; i < predecessorStates.size(); i++) {
			boolean unique = true;
			for (int j = 0; j < predecessorStates.size(); j++) {
				if (i == j)
					continue;
				if (predecessorStates
						.get(i)
						.getPresentState()
						.getActionConstants()
						.equals(predecessorStates.get(j).getPresentState()
								.getActionConstants())) {
					unique = false;
					break;
				}
			}
			if (unique) {
				// System.out.println("adding unique  action constant "
				// + predecessorStates.get(i).getPresentState()
				// .getQualifiedName()
				// + " "
				// + predecessorStates.get(i).getPresentState()
				// .getVariables().length
				// + "  , "
				// + Arrays.toString(predecessorStates.get(i)
				// .getPresentState().getVariables()));
				uniqueActionConstants.add(predecessorStates.get(i));
			}
		}

		// fix these variables
		// get all the variables in the states that are substituted
		for (int i = 0; i < uniqueActionConstants.size(); i++) {
			// System.out.println("* "
			// + uniqueActionConstants.get(i).getPresentState()
			// .getQualifiedName()
			// + " ,  "
			// + Arrays.toString(uniqueActionConstants.get(i)
			// .getPresentState().getVariables()) + " , "
			// + Arrays.toString(this.getVariables()));

			ActionClass action = (ActionClass) precondition.findMatching(
					uniqueActionConstants.get(i).getPresentState()
							.getActionConstants()).get(0);
			// System.out.println(" matching " + action.getVariables());
			//
			// System.out.println(" to substitute "
			// + Arrays.toString(this.getVariables()));
			// find which ones are free
			for (int j = 0; j < action.getVariables().length; j++) {
				if (action.getVariables()[j].isFree()) {

					// find corresponding entry in action constant
					for (int k = 0; k < this.getVariables().length; k++) {
						if (this.getVariables()[k].getSymbolName().equals(
								action.getVariables()[j].getSymbolName())) {

							// System.out
							// .println("to substiute "
							// + uniqueActionConstants.get(i)
							// .getPresentState()
							// .getVariables()[j]
							// .getSymbolName()
							// + " for "
							// + this.getVariables()[k]
							// .getSymbolName());
							// check if there is already a value and it doesn't
							// conflict
							// if it conflicts then escape
							if (parameters[k] == null
									|| parameters[k] == uniqueActionConstants
											.get(i).getPresentState()
											.getVariables()[j]) {

								parameters[k] = uniqueActionConstants.get(i)
										.getPresentState().getVariables()[j];
								argumentValues.remove(parameters[j]);
								argumentVars.remove(this.getVariables()[k]);
							} else {
								// System.out
								// .println("conficting var substituion "
								// + parameters[k]
								// + " , "
								// + uniqueActionConstants.get(i)
								// .getPresentState()
								// .getVariables()[j]);

								throw new ApplicationException();
							}

						}
					}

				}
			}

		}

		// find which values are left
		for (int j = 0; j < predecessorStates.size(); j++) {
			if (!uniqueActionConstants.contains(predecessorStates.get(j))) {

				ArrayList<ActionClass> actions = (ArrayList<ActionClass>) precondition
						.findMatching(predecessorStates.get(j)
								.getPresentState().getActionConstants());
				// now check what is the variable of this action
				for (int k = 0; k < actions.size(); k++) {

					for (int i = 0; i < actions.get(k).getVariables().length; i++) {
						if (actions.get(k).getVariables()[i].isFree()) {
							if (argumentVars.contains(actions.get(k)
									.getVariables()[i])) {

								for (int l = 0; l < this.getVariables().length; l++) {
									if (this.getVariables()[l].getSymbolName()
											.equals(actions.get(k)
													.getVariables()[i]
													.getSymbolName())
											&& argumentValues
													.contains(predecessorStates
															.get(j)
															.getPresentState()
															.getVariables()[i])) {
										// take the value in this
										parameters[l] = predecessorStates
												.get(j).getPresentState()
												.getVariables()[i];

										// System.out
										// .println("need to get "
										// + actions.get(k)
										// .getVariables()[i]
										// + " with "
										// + predecessorStates
										// .get(j)
										// .getPresentState()
										// .getVariables()[i]);
									}
								}
							}
						}
					}
				}
			}
		}

		// for (int i = 0; i < parameters.length; i++) {
		// System.out.println("-------param " + parameters[i] + " -- "
		// + this.getVariables()[i]);
		// }
		return parameters;
	}

	public static void main(String[] args) throws Exception {
		addAllActions();
	}

	public String getQualifiedName() {
		String name = this.getActionName();
		if (this.getVariables() != null) {
			name = name + "(";
			for (int i = 0; i < this.getVariables().length; i++) {

				VariableConstants variable = this.getVariables()[i];
				if (i != 0)
					name = name + ",";
				if (variable != null) {
					name = name + variable.getSymbolName();
					// } else {
					// name = name + this.getVariables()[i].getSymbolName();
				}
			}
			name = name + ")";
			// System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");
		}
		return name;
	}

	// public String getQualifiedName() {
	// String name = actionConstant.getActionName();
	//
	// if (actionConstant.getVariables() != null) {
	// name = name + "(";
	// for (int i = 0; i < actionConstant.getVariables().length; i++) {
	//
	// VariableConstants variable = actionConstant.getVariables()[i];
	// if (i != 0)
	// name = name + ",";
	// name = name + variable.getSymbolName();
	// }
	// name = name + ")";
	// // System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");
	// }
	// return name;
	// }

	public static void addAllActions() throws NoSuchFieldException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		ActionConstants a = new ActionConstants();
		Field[] fields = a.getClass().getDeclaredFields();

		ArrayList<ActionConstants> listActions = new ArrayList<ActionConstants>();
		ArrayList<ActionConstants> listStates = new ArrayList<ActionConstants>();

		for (Field field : fields) {

			field.setAccessible(true); // You might want to set modifier to
										// public first.
			if (field.getModifiers() == 8
					&& field.getType() == planning.ActionConstants.class) {
				// System.out.println("field " + field);
				if (field.get(a) != null) {

					// System.out.println("--- " + (field.get(a)).toString());
					Method problemNumberMethod = (field.get(a)).getClass()
							.getDeclaredMethod("getProblemNumber");
					Method pureActionMethod = (field.get(a)).getClass()
							.getDeclaredMethod("isPureAction");

					if (pureActionMethod.invoke(field.get(a)).equals(true)
							&& (problemNumberMethod.invoke(field.get(a))
									.equals(Planner.getProblemNumber()) || problemNumberMethod
									.invoke(field.get(a)).equals(-1))) {
						// System.out.println("field " + field.getName() + " ");
						listActions.add((ActionConstants) field.get(a));
					} else if (pureActionMethod.invoke(field.get(a)).equals(
							false)
							&& (problemNumberMethod.invoke(field.get(a))
									.equals(Planner.getProblemNumber()))) {
						// System.out.println("field " + field.getName() + " ");
						listStates.add((ActionConstants) field.get(a));
					}

				}
			}
		}

		possibleActions = new ActionConstants[listActions.size()];
		possibleActions = listActions.toArray(possibleActions);
		possiblePredicates = new ActionConstants[listStates.size()];
		possiblePredicates = listStates.toArray(possiblePredicates);

	}

	public HashSet<State> getPreconditionsForAction(ArrayList<State> states) {
		if (this.precondition == null) {
			// possibilities.add(states);
			return new HashSet<State>(states);
		}
		HashSet<State> predecessorStates = this.precondition
				.getPreconditionsForAction(states);
		// System.out.println("$$$$$$ count for action " + this.actionName
		// + " is = " + precondition.getCount() + " and size = "
		// + predecessorStates.size());

		// if size is same then return else return null
		if (precondition.getCount() == predecessorStates.size()) {
			// check if there are as many actions constant in both

			// ArrayList<ActionConstants> constants = precondition
			// .getUniqueCount();
			//
			// boolean bothSame = true;
			// for (int i = 0; i < constants.size(); i++) {
			// boolean exists = false;
			// Iterator<State> it = predecessorStates.iterator();
			// while (it.hasNext()) {
			// State state = it.next();
			// if (constants.get(i).equals(
			// state.getPresentState().getActionConstants())) {
			// // System.out.println("exists " + exists + " "
			// // + constants.get(i).getQualifiedName() + " & "
			// // + state.getPresentState().getQualifiedName());
			//
			// exists = true;
			// }
			// }
			// if (!exists) {
			// // System.out.println("missing "
			// // + constants.get(i).getQualifiedName());
			// return null;
			// }
			// bothSame = bothSame | exists;
			// }
			//
			// if (bothSame)
			// // possibilities.add(predecessorStates);
			return new HashSet<State>(predecessorStates);
		}

		return null;
	}

	public ArrayList<ArrayList<State>> getPossibilities(ArrayList<State> states)
			throws ApplicationException {
		HashSet<State> predecessorStates = this.precondition
				.getPreconditionsForAction(states);
		ArrayList<ActionClass> requiredActions = this.precondition
				.getAllActions();

		ArrayList<State> states2 = new ArrayList<State>(predecessorStates);

		ArrayList<ArrayList<State>> possibilities = new ArrayList<ArrayList<State>>();

		for (int i = 0; i < requiredActions.size(); i++) {
			ActionClass requiredAction = requiredActions.get(i);

			ArrayList<State> matchingStates = new ArrayList<State>();

			int j = 0;
			while (j < states2.size()) {
				// see if matching
				// no need to remove because there can be two rooms
				// System.out.println("eligible state "
				// + states2.get(j).getPresentState().getQualifiedName());
				if (states2.get(j).getPresentState()
						.fitsFreeVariables(requiredAction)) {
					State oneState = states2.get(j);
					matchingStates.add(oneState);
				}
				j++;
			}
			// if matching state is zero then there is some problem
			if (matchingStates.size() == 0) {
				// System.out.println("missing action class "
				// + requiredAction.getQualifiedName());
				throw new ApplicationException();
			}
			possibilities.add(matchingStates);

		}

		// System.out.println(":final size " + possibilities.size() + " -=== "
		// + precondition.getCount());
		// obviously will work
		if (precondition.getCount() == possibilities.size()) {
			return possibilities;
		}
		return null;

	}

	public ArrayList<ArrayList<State>> getPossibilities2(ArrayList<State> states) {
		HashSet<State> predecessorStates = this.precondition
				.getPreconditionsForAction(states);
		ArrayList<ArrayList<State>> possibilities = new ArrayList<ArrayList<State>>();

		if (predecessorStates != null
				&& precondition.getCount() == predecessorStates.size()) {

			ArrayList<State> states4 = new ArrayList<State>(predecessorStates);

			for (int i = 0; i < states4.size(); i++) {
				ArrayList<State> states5 = new ArrayList<State>();
				states5.add(states4.get(i));
				possibilities.add(states5);
			}

			// System.out.println(":final size ::: " + possibilities.size()
			// + " -=== " + precondition.getCount());
			return possibilities;

		}

		ArrayList<State> states2 = new ArrayList<State>(predecessorStates);

		// System.out.println("size %%%%---" + states2.size());

		while (states2.size() > 0) {
			State oneState = states2.remove(0);
			// System.out.println("size %%%%" + states2.size());

			ArrayList<State> matchingStates = new ArrayList<State>();
			matchingStates.add(oneState);
			int j = 0;
			while (j < states2.size()) {
				if (oneState
						.getPresentState()
						.getActionConstants()
						.equals(states2.get(j).getPresentState()
								.getActionConstants())) {
					// System.out.println("::: "
					// + oneState.getPresentState().getActionConstants()
					// .getQualifiedName()
					// + " & "
					// + states2.get(j).getPresentState()
					// .getActionConstants().getQualifiedName());
					matchingStates.add(states2.get(j));
					states2.remove(j);
				}
				// increment j
				else
					j++;

			}
			possibilities.add(matchingStates);
		}

		// System.out.println(":final size " + possibilities.size() + " -=== "
		// + precondition.getCount());
		if (precondition.getCount() == possibilities.size()) {
			return possibilities;
		}
		return null;

	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setPrecondition(BaseActionClass precondition) {
		this.precondition = precondition;
	}

	public BaseActionClass getPrecondition() {
		return precondition;
	}

	public void setEffects(BaseActionClass effects) {
		this.effects = effects;
	}

	public BaseActionClass getEffects() {
		return effects;
	}

	public void setVariables(VariableConstants variables[]) {
		this.variables = variables;
	}

	public VariableConstants[] getVariables() {
		return variables;
	}

	public void setProblemNumber(int problemNumber) {
		this.problemNumber = problemNumber;
	}

	public int getProblemNumber() {
		return problemNumber;
	}

	public boolean isPureAction() {
		return pureAction;
	}

	public void setPureAction(boolean pureAction) {
		this.pureAction = pureAction;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

}
