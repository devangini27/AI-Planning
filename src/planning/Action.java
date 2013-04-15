package planning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Action {

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

	public String getQualifiedName() {
		String name = this.getAction().getActionName();
		if (this.getAction().getVariables() != null) {
			name = name + "(";
			for (int i = 0; i < this.getAction().getVariables().length; i++) {

				VariableConstants variable = this.getAction().getVariables()[i];
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
