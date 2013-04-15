package planning;

import java.util.Arrays;

public class State {
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
}
