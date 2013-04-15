package planning2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

abstract public class BaseActionClass {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(complementForms);
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
		BaseActionClass other = (BaseActionClass) obj;
		if (!Arrays.equals(complementForms, other.complementForms))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BaseActionClass [complementForms="
				+ Arrays.toString(complementForms) + "]";
	}

	protected boolean[] complementForms;

	BaseActionClass(boolean[] complementForms) {
		this.setComplementForms(complementForms);
	}

	public void setComplementForms(boolean[] complementForms) {
		this.complementForms = complementForms;
	}

	public boolean[] getComplementForms() {
		return complementForms;
	}

	abstract boolean canBeApplied(ActionClass currentState);

	abstract public HashSet<State> getPreconditionsForAction(
			ArrayList<State> states);

	//abstract public boolean isMutexState(State state);

	abstract public int getCount();

	abstract public ArrayList<ActionConstants> getUniqueCount();

	abstract public boolean checkIfPresent(ActionConstants state);

	abstract public ArrayList<ActionClass> findMatching(ActionConstants action);

	abstract public ArrayList<ActionClass> getAllActions();

	abstract public ArrayList<VariableConstants> getParameters();
}
