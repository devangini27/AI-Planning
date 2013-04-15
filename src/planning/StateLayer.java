package planning;

import java.util.ArrayList;

public class StateLayer {

	static int maxNoStates = 0;
	private int noOfStates = 0;

	@Override
	public String toString() {
		return "StateLayer [states=" + states + "]";
	}

	private ArrayList<State> states = new ArrayList<State>();

	public void setStates(ArrayList<State> states) {
		this.states = states;
	}

	public ArrayList<State> getStates() {
		return states;
	}

	public void addState(State newState) {
		states.add(newState);
	}

	public ArrayList<State> addNewEffects(BaseActionClass effects/*
																 * , StateLayer
																 * newStateLayer
																 */) {
		ArrayList<State> successiveState = new ArrayList<State>();
		if (effects instanceof ActionClass) {
			ActionClass actualActionClass = (ActionClass) effects;
			int index = this.indexOfState(effects);
			// System.out.println("index " + index);
			if (index == -1) {
				// add this state
				this.addState(actualActionClass);
				//System.out.println("added " + actualActionClass);
				index = this.states.size() - 1;
			}
			successiveState.add(this.states.get(index));

		} else if (effects instanceof CompoundActionClass) {

			CompoundActionClass compoundActionClass = (CompoundActionClass) effects;
			for (BaseActionClass innerActionClass : compoundActionClass
					.getChildActions()) {
				successiveState.addAll(addNewEffects(innerActionClass));
			}

		}
		return successiveState;

	}

	public int indexOfState(BaseActionClass state) {
		for (int i = 0; i < states.size(); i++) {

			if (states.get(i).getPresentState().equals(state))
				return i;
		}
		return -1;
	}

	public void addState(BaseActionClass actionClass) {

		if (actionClass instanceof ActionClass) {
			ActionClass actualActionClass = (ActionClass) actionClass;
			states.add(new State(actualActionClass));
			noOfStates++;
			if (noOfStates > maxNoStates) {
				maxNoStates = noOfStates;
			}
		} else if (actionClass instanceof CompoundActionClass) {

			CompoundActionClass compoundActionClass = (CompoundActionClass) actionClass;
			for (BaseActionClass innerActionClass : compoundActionClass
					.getChildActions()) {
				addState(innerActionClass);
			}
		}

	}

	public void markMutExStates() {
		for (int i = 0; i < states.size(); i++) {
			for (int j = 0; j < states.size(); j++) {
				if (i == j)
					continue;
				// if they have the same action constant and opposite complement
				// form
				if (states
						.get(i)
						.getPresentState()
						.getActionConstants()
						.equals(states.get(j).getPresentState()
								.getActionConstants())
						&& states.get(i).getPresentState().getComplementForms()[0] != states
								.get(j).getPresentState().getComplementForms()[0]) {
					System.out.println("found mut ex states "
							+ states.get(i).getPresentState()
									.getQualifiedName()
							+ " and "
							+ states.get(j).getPresentState()
									.getQualifiedName());
					// allocate mut ex state to that state
					states.get(i).setMutExState(states.get(j));
					// states.get(j).setMutExState(states.get(i));
				}
			}
		}
	}

}
