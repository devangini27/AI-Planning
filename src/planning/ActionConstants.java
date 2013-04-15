package planning;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

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
		return "ActionConstants [actionName=" + actionName + ", variables="
				+ Arrays.toString(variables) + ", precondition=" + precondition
				+ ", effects=" + /* effects + */", pureAction=" + pureAction
				+ "]";
	}

	static ActionConstants[] possibleActions;
	static ActionConstants[] possibleStates;

	static ActionConstants NULL;

	static ActionConstants HAVE;
	static ActionConstants EAT;
	static ActionConstants EATEN;
	static ActionConstants BAKE;

	static ActionConstants REMOVE1;
	static ActionConstants REMOVE2;
	static ActionConstants PUTON;
	static ActionConstants AT;
	static ActionConstants LEAVEOVERNIGHT;

	static {

		/**
		 * Common to all problems
		 */
		NULL = new ActionConstants("null action", null, null, null, false, -1,
				true);

		/**
		 * Cake baking problem
		 */
		HAVE = new ActionConstants("have",
				new VariableConstants[] { VariableConstants.CAKE }, null, null,
				false, 0, false);
		EATEN = new ActionConstants("eaten",
				new VariableConstants[] { VariableConstants.CAKE }, null, null,
				false, 0, false);
		EAT = new ActionConstants(
				"eat",
				new VariableConstants[] { VariableConstants.CAKE },
				new ActionClass(HAVE,
						new VariableConstants[] { VariableConstants.CAKE },
						new boolean[] { true }),
				new CompoundActionClass(
						new ActionClass[] {
								new ActionClass(
										HAVE,
										new VariableConstants[] { VariableConstants.CAKE },
										new boolean[] { false }),
								new ActionClass(
										EATEN,
										new VariableConstants[] { VariableConstants.CAKE },
										new boolean[] { true }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), true, 0, false);

		BAKE = new ActionConstants("bake",
				new VariableConstants[] { VariableConstants.CAKE },
				new ActionClass(HAVE,
						new VariableConstants[] { VariableConstants.CAKE },
						new boolean[] { false }), new ActionClass(HAVE,
						new VariableConstants[] { VariableConstants.CAKE },
						new boolean[] { true }), true, 0, false);

		/**
		 * Spare tyre problem
		 */
		AT = new ActionConstants("at", new VariableConstants[] { null, null },
				null, null, false, 1, true);
		REMOVE1 = new ActionConstants("remove", new VariableConstants[] {
				VariableConstants.SPARE, VariableConstants.TRUNK },
				new ActionClass(AT, new VariableConstants[] {
						VariableConstants.SPARE, VariableConstants.TRUNK },
						new boolean[] { true }), new CompoundActionClass(
						new ActionClass[] {
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.SPARE,
										VariableConstants.TRUNK },
										new boolean[] { false }),
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.SPARE,
										VariableConstants.GROUND },
										new boolean[] { true }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), true, 1, false);
		REMOVE2 = new ActionConstants("remove", new VariableConstants[] {
				VariableConstants.FLAT, VariableConstants.AXLE },
				new ActionClass(AT, new VariableConstants[] {
						VariableConstants.FLAT, VariableConstants.AXLE },
						new boolean[] { true }), new CompoundActionClass(
						new ActionClass[] {
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.FLAT,
										VariableConstants.AXLE },
										new boolean[] { false }),
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.FLAT,
										VariableConstants.GROUND },
										new boolean[] { true }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), true, 1, false);

		PUTON = new ActionConstants("puton", new VariableConstants[] {
				VariableConstants.SPARE, VariableConstants.AXLE },
				new CompoundActionClass(new ActionClass[] {
						new ActionClass(AT, new VariableConstants[] {
								VariableConstants.SPARE,
								VariableConstants.GROUND },
								new boolean[] { true }),
						new ActionClass(AT,
								new VariableConstants[] {
										VariableConstants.FLAT,
										VariableConstants.AXLE },
								new boolean[] { false }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), new CompoundActionClass(
						new ActionClass[] {
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.SPARE,
										VariableConstants.AXLE },
										new boolean[] { true }),
								new ActionClass(AT, new VariableConstants[] {
										VariableConstants.SPARE,
										VariableConstants.GROUND },
										new boolean[] { false }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), true, 1, false);
		LEAVEOVERNIGHT = new ActionConstants("leave overnight", null, null,
				new CompoundActionClass(new ActionClass[] {
						new ActionClass(AT, new VariableConstants[] {
								VariableConstants.SPARE,
								VariableConstants.GROUND },
								new boolean[] { false }),
						new ActionClass(AT,
								new VariableConstants[] {
										VariableConstants.SPARE,
										VariableConstants.AXLE },
								new boolean[] { false }),
						new ActionClass(AT, new VariableConstants[] {
								VariableConstants.SPARE,
								VariableConstants.TRUNK },
								new boolean[] { false }),
						new ActionClass(AT, new VariableConstants[] {
								VariableConstants.FLAT,
								VariableConstants.GROUND },
								new boolean[] { false }),
						new ActionClass(AT,
								new VariableConstants[] {
										VariableConstants.FLAT,
										VariableConstants.AXLE },
								new boolean[] { false }) },
						new ConnectiveConstants[] { ConnectiveConstants.AND },
						new boolean[] { true, true }), true, 1, false);

		try {
			addAllActions();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

	}
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
				} else {
					name = name + this.getVariables()[i].getSymbolName();
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
						System.out.println("field " + field.getName() + " ");
						listStates.add((ActionConstants) field.get(a));
					}

				}
			}
		}

		possibleActions = new ActionConstants[listActions.size()];
		possibleActions = listActions.toArray(possibleActions);
		possibleStates = new ActionConstants[listStates.size()];
		possibleStates = listStates.toArray(possibleStates);

	}

	public ArrayList<State> getPreconditionsForAction(ArrayList<State> states) {
		// if null then return all the states
		if (this.precondition == null) {
			return states;
		}
		ArrayList<State> predecessorStates = this.precondition
				.getPreconditionsForAction(states);
		System.out.println("$$$$$$ count for action " + this.actionName
				+ " is = " + precondition.getCount() + " and size = "
				+ predecessorStates.size());

		// if size is same then return else return null
		if (precondition.getCount() == predecessorStates.size())
			return predecessorStates;
		else
			return null;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setPrecondition(ActionClass precondition) {
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
