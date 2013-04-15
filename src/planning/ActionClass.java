package planning;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionClass extends BaseActionClass {

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
							.getVariables().length)
						return false;
					for (int i = 0; i < this.getVariables().length; i++) {
						if (!this.getVariables()[i].equals(currentState
								.getVariables()[i]))
							return false;
					}
					return true;
				} else
					return false;
			else
				return false;
		} else
			return false;
	}

	public ArrayList<State> getPreconditionsForAction(ArrayList<State> states) {

		ArrayList<State> predecessorStates = new ArrayList<State>();
		for (int i = 0; i < states.size(); i++) {
			State state = states.get(i);

			if (this.canBeApplied(state.getPresentState())) {
				predecessorStates.add(state);
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
	public boolean isMutexState(State state) {
		// check if same action constant
		if (state.getPresentState().getActionConstants()
				.equals(this.getActionConstants())) {
			// if in complement form of each other
			if (state.getPresentState().getComplementForms()[0] != this
					.getComplementForms()[0])
				return true;
		}
		return false;
	}

}
