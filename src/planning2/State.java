package planning2;

import java.util.Arrays;

public class State implements Comparable/* , Cloneable */{
	// @Override
	// public Object clone() {
	// State state = new State(this.presentState);
	// state.action = this.action;
	// state.mutExState = this.mutExState;
	//
	// return state;
	// }

	@Override
	public String toString() {
		return "State [presentState=" + presentState + ", action="
				+ ", mutExStates=" + "]";
	}

	State(ActionClass presentState) {
		this.presentState = presentState;

	}

	// private State successiveState;
	private ActionClass presentState;
	private ActionClass action;
	private State mutExState;

	// public void setSuccessiveState(State successiveState) {
	// this.successiveState = successiveState;
	// }
	//
	// public State getSuccessiveState() {
	// return successiveState;
	// }

	public void setAction(ActionClass action) {
		this.action = action;
	}

	public ActionClass getAction() {
		return action;
	}

	public void setMutExState(State mutExState) {
		this.mutExState = mutExState;
	}

	public State getMutExState() {
		return mutExState;
	}

	public void setPresentState(ActionClass presentState) {
		this.presentState = presentState;
	}

	public ActionClass getPresentState() {
		return presentState;
	}

	@Override
	public int compareTo(Object arg0) {

		if (this.getPresentState().getActionConstants().getFreeVariableCount() > ((State) arg0)
				.getPresentState().getActionConstants().getFreeVariableCount())
			return -1;
		else
			return 1;
	}

	public boolean isMutexState(State state) {
		boolean same = false;
		// if they have the same action constant and opposite complement
		// form
		if (this.getPresentState().getActionConstants()
				.equals(state.getPresentState().getActionConstants())) {
			if (this.getPresentState().getComplementForms()[0] != state
					.getPresentState().getComplementForms()[0]) {
				// checking if all the vars are the same
				boolean sameVars = true;
				for (int k = 0; k < this.getPresentState().getVariables().length; k++) {
					if (!this.getPresentState().getVariables()[k].equals(state
							.getPresentState().getVariables()[k])) {
						sameVars = false;
						break;
					}
				}
				same = sameVars;
			}
		}

		return same;
	}
}
