package planning2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Action {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((action == null) ? 0 : action.hashCode());
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
		Action other = (Action) obj;
		if (action == null) {
			if (other.action != null)
				return false;
		} else if (!action.equals(other.action))
			return false;
		if (!Arrays.equals(variables, other.variables))
			return false;
		return true;
	}

	public Action(ArrayList<State> previousStates,
			ArrayList<State> successiveStates, ActionConstants action,
			VariableConstants[] variables) {
		super();
		this.setPreviousStates(previousStates);
		this.successiveStates = successiveStates;
		// this.successiveState = successiveState;
		this.action = action;
		this.variables = variables;
		this.mutExActions = new HashSet<Action>();
	}

	private ArrayList<State> previousStates;
	private ArrayList<State> successiveStates;
	// private State successiveState;
	private ActionConstants action;
	private VariableConstants[] variables;
	// private ArrayList<Action> mutExActions;
	private HashSet<Action> mutExActions;

	@Override
	public String toString() {
		return "Action [previousStates=" + previousStates
				+ ", successiveState=" + ", action=" + action + ", variables="
				+ Arrays.toString(variables) + ", mutExActions=" + mutExActions
				+ "]";
	}

	public String getQualifiedName() {
		String name = this.getAction().getActionName();
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
		}
		// System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");

		return name;
	}

	// public void setSuccessiveState(State successiveState) {
	// this.successiveState = successiveState;
	// }
	//
	// public State getSuccessiveState() {
	// return successiveState;
	// }

	public void setAction(ActionConstants action) {
		this.action = action;
	}

	public ActionConstants getAction() {
		return action;
	}

	public void setVariables(VariableConstants[] variables) {
		this.variables = variables;
	}

	public VariableConstants[] getVariables() {
		return variables;
	}

	public void setPreviousStates(ArrayList<State> previousStates) {
		this.previousStates = previousStates;
	}

	public ArrayList<State> getPreviousStates() {
		return previousStates;
	}

	public void setMutExActions(HashSet<Action> mutExActions) {
		this.mutExActions = mutExActions;
	}

	public HashSet<Action> getMutExActions() {
		return mutExActions;
	}

	public void setSuccessiveStates(ArrayList<State> successiveStates) {
		this.successiveStates = successiveStates;
	}

	public ArrayList<State> getSuccessiveStates() {
		return successiveStates;
	}

}
