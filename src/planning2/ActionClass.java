package planning2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class ActionClass extends BaseActionClass implements Cloneable {

	@Override
	public String toString() {
		return "ActionClass [actionConstants=" + actionConstants
				+ ", variables=" + Arrays.toString(variables)
				+ ", getComplementForms()="
				+ Arrays.toString(getComplementForms()) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((actionConstants == null) ? 0 : actionConstants.hashCode());
		result = prime * result + Arrays.hashCode(variables);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionClass other = (ActionClass) obj;
		if (actionConstants == null) {
			if (other.actionConstants != null)
				return false;
		} else if (!actionConstants.equals(other.actionConstants))
			return false;
		if (!Arrays.equals(variables, other.variables))
			return false;
		if (!Arrays.equals(this.getComplementForms(),
				other.getComplementForms()))
			return false;
		return true;
	}

	@Override
	public Object clone() {
		ActionClass action = new ActionClass(
				this.actionConstants,
				Arrays.copyOf(this.variables, this.variables.length),
				Arrays.copyOf(this.complementForms, this.complementForms.length));
		return action;
	}

	public boolean fitsFreeVariables(ActionClass other) {
		if (!actionConstants.equals(other.actionConstants)) {
			// System.out.println("reason 1");
			return false;
		}
		if (variables.length != other.variables.length) {
			// System.out.println("reason 2");

			return false;
		}
		if (!Arrays.equals(this.getComplementForms(),
				other.getComplementForms())) {
			// System.out.println("reason 3");
		}
		return true;
	}

	private ActionConstants actionConstants;
	private VariableConstants[] variables;

	ActionClass(ActionConstants actionConstants, VariableConstants[] variables,
			boolean[] complementForms) {
		super(complementForms);
		this.setActionConstants(actionConstants);
		this.variables = variables;
	}

	public void setActionConstants(ActionConstants actionConstants) {
		this.actionConstants = actionConstants;
	}

	public ActionConstants getActionConstants() {
		return actionConstants;
	}

	public String getQualifiedName() {
		String name = this.getActionConstants().getActionName();

		if (!this.getComplementForms()[0])
			name = "~" + name;

		if (this.getActionConstants().getVariables() != null) {
			name = name + "(";
			for (int i = 0; i < this.getActionConstants().getVariables().length; i++) {

				VariableConstants variable = this.getActionConstants()
						.getVariables()[i];
				if (i != 0)
					name = name + ",";
				if (variable != null) {
					name = name + variable.getSymbolName();
				} else {
					// if (this.getActionConstants().isPureAction())
					name = name + this.getVariables()[i].getSymbolName();
				}
			}
			name = name + ")";
			// System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");
		}

		return name;
	}

	@Override
	boolean canBeApplied(ActionClass currentState) {
		// check if action constants are same

		// System.out.println("leaf action class");
		if (currentState.getActionConstants().equals(this.getActionConstants())) {

			// System.out.println("same action constants");
			// check if they are complements
			if (currentState.getComplementForms()[0] == this
					.getComplementForms()[0])
				// check if all the variables match

				if (currentState.getVariables() == null
						&& this.getVariables() == null)
					return true;
				else if (currentState.getVariables() != null
						&& this.getVariables() != null) {
					// compare sizes
					if (currentState.getVariables().length != this
							.getVariables().length) {
						// System.out.println("reason 1");
						return false;
					}
					for (int i = 0; i < this.getVariables().length; i++) {
						if (this.getVariables()[i].equals(currentState
								.getVariables()[i])) {

						} else if (this.getVariables()[i].isFree()) {
							// System.out.println(this.getActionConstants()
							// .getActionName());

							// System.out.println("not equal "
							// + this.getVariables()[i] + " & "
							// + currentState.getVariables()[i]);
						} else {
							// System.out.println("reason 2");
							return false;
						}
					}
					return true;
				} else {
					// System.out.println("reason 3");

					return false;
				}
			else {
				// System.out.println("reason 4");

				return false;
			}
		} else {
			// System.out.println("reason 5 " + this.getQualifiedName());

			return false;
		}
	}

	public HashSet<State> getPreconditionsForAction(ArrayList<State> states) {

		HashSet<State> predecessorStates = new HashSet<State>();
		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);

			if (this.canBeApplied(state.getPresentState())) {
				predecessorStates.add(state);
				// System.out.println("adding " +
				// state.getPresentState().getQualifiedName());
			}
		}
		return predecessorStates;
	}

	public void setVariables(VariableConstants[] variables) {
		this.variables = variables;
	}

	public VariableConstants[] getVariables() {
		return variables;
	}

	@Override
	public boolean checkIfPresent(ActionConstants state) {
		if (state.equals(this.getActionConstants())) {
			return true;
		} else
			return false;
	}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public ArrayList<ActionConstants> getUniqueCount() {
		ArrayList<ActionConstants> one = new ArrayList<ActionConstants>();
		one.add(this.getActionConstants());
		return one;
	}

	// @Override
	// public boolean isMutexState(State state) {
	// boolean same = false;
	// // check if same action constant
	// if (state.getPresentState().getActionConstants()
	// .equals(this.getActionConstants())) {
	// // if in complement form of each other
	// if (state.getPresentState().getComplementForms()[0] != this
	// .getComplementForms()[0]) {
	// // check if variables are same
	// // if (Arrays.equals(this.getVariables(),
	// // state.getPresentState()
	// // .getVariables()))
	// // return true;
	// // checking if all the vars are the same
	// boolean sameVars = true;
	// for (int k = 0; k < this.getVariables().length; k++) {
	// if (this.getVariables()[k].isFree()) {
	// // simply ignore
	// } else if (!this.getVariables()[k].equals(state
	// .getPresentState().getVariables()[k])) {
	// sameVars = false;
	// break;
	// }
	// }
	// same = sameVars;
	// }
	// }
	// // return false;
	// return same;
	// }

	@Override
	public ArrayList<ActionClass> findMatching(ActionConstants action) {
		if (this.getActionConstants().equals(action)) {
			ArrayList<ActionClass> oneState = new ArrayList<ActionClass>();
			oneState.add(this);
			return oneState;
		}
		return null;
	}

	@Override
	public ArrayList<ActionClass> getAllActions() {
		ArrayList<ActionClass> oneState = new ArrayList<ActionClass>();
		oneState.add(this);
		return oneState;
	}

	@Override
	public ArrayList<VariableConstants> getParameters() {
		return new ArrayList(Arrays.asList(this.getVariables()));
	}

}
