package planning2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class CompoundActionClass extends BaseActionClass {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(childActions);
		result = prime * result + Arrays.hashCode(connectives);
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
		CompoundActionClass other = (CompoundActionClass) obj;
		if (!Arrays.equals(childActions, other.childActions))
			return false;
		if (!Arrays.equals(connectives, other.connectives))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CompoundActionClass [childActions="
				+ Arrays.toString(childActions) + ", connectives="
				+ Arrays.toString(connectives) + "]";
	}

	private BaseActionClass[] childActions;
	private ConnectiveConstants[] connectives;

	CompoundActionClass(BaseActionClass[] childActions,
			ConnectiveConstants[] connectives, boolean[] complementForms) {
		super(complementForms);
		this.connectives = connectives;
		this.childActions = childActions;
	}

	public boolean canBeApplied(ActionClass currentState) {
		// check if any of the preconditions violate it
		// TODO need to complete truth value
		// System.out.println("need to do this @@@@@@@@@@@@@@@@@@@@@@@  ");
		// apply or and and
		int i = 0;
		boolean previousValue = false;
		while (i < this.getChildActions().length) {
			// get value for an individual
			if (i == 0) {
				previousValue = this.getChildActions()[0]
						.canBeApplied(currentState);
				// System.out.println("init value = " + previousValue);
			} else {
				previousValue = connectives[i - 1].truthtable(previousValue,
						this.getChildActions()[i].canBeApplied(currentState));
				// System.out.println("new value "
				// + this.getChildActions()[i].canBeApplied(currentState)
				// + " -> " + previousValue);
			}
			i++;
		}
		return previousValue;
	}

	public boolean checkIfPresent(ActionConstants state) {
		boolean resultant = false;
		for (BaseActionClass childAction : childActions) {
			if (childAction instanceof ActionClass) {
				return ((ActionClass) childAction).checkIfPresent(state);
			} else if (childAction instanceof CompoundActionClass) {
				resultant = resultant | checkIfPresent(state);
			}
		}
		return resultant;
	}

	public HashSet<State> getPreconditionsForAction(ArrayList<State> states) {

		HashSet<State> predecessorStates = new HashSet<State>();

		for (BaseActionClass childAction : ((CompoundActionClass) this)
				.getChildActions()) {
			predecessorStates.addAll(childAction
					.getPreconditionsForAction(states));
		}

		return predecessorStates;
	}

	public void setChildActions(BaseActionClass[] childActions) {
		this.childActions = childActions;
	}

	public BaseActionClass[] getChildActions() {
		return childActions;
	}

	public void setConnectives(ConnectiveConstants[] connectives) {
		this.connectives = connectives;
	}

	public ConnectiveConstants[] getConnectives() {
		return connectives;
	}

	@Override
	public int getCount() {
		int sum = 0;
		for (BaseActionClass childAction : childActions) {
			sum += childAction.getCount();
		}
		return sum;
	}

	@Override
	public ArrayList<ActionConstants> getUniqueCount() {
		ArrayList<ActionConstants> child = new ArrayList<ActionConstants>();
		for (BaseActionClass childAction : childActions) {
			ArrayList<ActionConstants> result = childAction.getUniqueCount();
			for (int j = 0; j < result.size(); j++) {
				if (!child.contains(result.get(j))) {
					child.add(result.get(j));
				}
			}

		}
		return child;
	}

	// @Override
	// public boolean isMutexState(State state) {
	// for (BaseActionClass childAction : ((CompoundActionClass) this)
	// .getChildActions()) {
	// if (childAction.isMutexState(state))
	// return true;
	// }
	// return false;
	// }

	@Override
	public ArrayList<ActionClass> findMatching(ActionConstants action) {
		ArrayList<ActionClass> states = new ArrayList<ActionClass>();
		for (BaseActionClass childAction : childActions) {
			if (childAction.findMatching(action) != null)
				states.addAll(childAction.findMatching(action));

		}
		return states;
	}

	@Override
	public ArrayList<ActionClass> getAllActions() {
		ArrayList<ActionClass> states = new ArrayList<ActionClass>();
		for (BaseActionClass childAction : childActions) {
			states.addAll(childAction.getAllActions());
		}
		return states;
	}

	@Override
	public ArrayList<VariableConstants> getParameters() {
		HashSet<VariableConstants> setOfParams = new HashSet<VariableConstants>();
		for (BaseActionClass childAction : childActions) {
			setOfParams.addAll(childAction.getParameters());
		}
		return new ArrayList<VariableConstants>(setOfParams);
	}

}
