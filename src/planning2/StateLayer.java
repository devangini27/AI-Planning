package planning2;

import java.util.ArrayList;
import java.util.Arrays;

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

	public static ArrayList<ActionClass> cloneList1(ArrayList<ActionClass> list) {
		ArrayList<ActionClass> actions = new ArrayList<ActionClass>();
		for (int j = 0; j < list.size(); j++) {
			actions.add((ActionClass) list.get(j).clone());
		}

		return actions;
	}

	public ArrayList<State> addNewEffects(BaseActionClass effects,
			VariableConstants[] parameters, VariableConstants[] variables) {
		ArrayList<State> successiveState = new ArrayList<State>();
		ArrayList<ActionClass> effectActionClassList = cloneList1(effects
				.getAllActions());
		for (int i = 0; i < effectActionClassList.size(); i++) {
			ActionClass newEffect = effectActionClassList.get(i);

			// apply effects
			if (Planner.TRACE)
				System.out.println("already variables "
						+ Arrays.toString(newEffect.getVariables())
						+ " to replaced by  " + Arrays.toString(variables));
			// set the free variables in new effect
			for (int j = 0; j < newEffect.getVariables().length; j++) {
				if (newEffect.getVariables()[j] == null
						|| newEffect.getVariables()[j].isFree()) {
					// find corresponding in variables in variables
					for (int k = 0; k < variables.length; k++) {
						if (variables[k].getSymbolName().equals(
								newEffect.getVariables()[j].getSymbolName())) {
							if (Planner.TRACE)
								System.out.println("substituting --> "
										+ newEffect.getVariables()[j] + " for "
										+ parameters[k].getSymbolName());

							newEffect.getVariables()[j] = parameters[k];
							// break;
						}
					}
				}
				// System.out.println("substituted--> "
				// + newEffect.getVariables()[i]);
			}

			// for (int j = 0; j < newEffect.getVariables().length; j++) {
			// if (newEffect.getVariables()[j] == null
			// || newEffect.getVariables()[j].isFree()) {
			// // find corresponding in variables
			// for (int k = 0; k < variables.length; k++) {
			// if (variables[k].getSymbolName().equals(
			// newEffect.getVariables()[j].getSymbolName())) {
			// System.out.println("substituting--> "
			// + newEffect.getVariables()[j] + " for "
			// + parameters[k].getSymbolName());
			//
			// newEffect.getVariables()[j] = parameters[k];
			// break;
			// }
			// }
			// }
			// // System.out.println("substituted--> "
			// // + newEffect.getVariables()[i]);
			// }

			int index = this.indexOfState(newEffect);
			if (Planner.TRACE)
				System.out.println("index for " + newEffect.getQualifiedName()
						+ " : " + index);
			if (index == -1) {
				// add this state
				this.addState(newEffect);
				// System.out.println("added ^^^^^^^^ " + newEffect);
				index = this.states.size() - 1;
			}
			successiveState.add(this.states.get(index));
			// System.out.println("adding effects "
			// + this.states.get(index).getPresentState()
			// .getQualifiedName());
		}
		return successiveState;

	}

	// public ArrayList<State> addNewEffects2(BaseActionClass effects,
	// VariableConstants[] parameters, VariableConstants[] variables) {
	// ArrayList<State> successiveState = new ArrayList<State>();
	// if (effects instanceof ActionClass) {
	// ActionClass actualActionClass = (ActionClass) effects;
	//
	// int index = this.indexOfState(actualActionClass);
	// System.out.println("index for "
	// + actualActionClass.getQualifiedName() + " : " + index);
	// if (index == -1) {
	// // add this state
	// this.addState(actualActionClass);
	// System.out.println("added ^^^^^^^^ " + actualActionClass);
	// index = this.states.size() - 1;
	// }
	// successiveState.add(this.states.get(index));
	// // System.out.println("adding effects "
	// // + this.states.get(index).getPresentState()
	// // .getQualifiedName());
	//
	// } else if (effects instanceof CompoundActionClass) {
	//
	// CompoundActionClass compoundActionClass = (CompoundActionClass) effects;
	// for (BaseActionClass innerActionClass : compoundActionClass
	// .getChildActions()) {
	// successiveState.addAll(addNewEffects(innerActionClass,
	// parameters, variables));
	// }
	//
	// }
	// return successiveState;
	//
	// }

	public int indexOfState(ActionClass state) {
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).getPresentState().equals(state)) {
				return i;
			}
		}
		return -1;
	}

	public void addState(BaseActionClass actionClass) {

		if (actionClass instanceof ActionClass) {
			ActionClass actualActionClass = (ActionClass) actionClass;

			ActionClass newEffect = new ActionClass(
					actualActionClass.getActionConstants(),
					actualActionClass.getVariables(),
					actualActionClass.getComplementForms());
			State newState = new State(newEffect);

			// System.out.println("already variables "
			// + Arrays.toString(actualActionClass.getVariables()));
			// set the free variables in new effect
			// for (int i = 0; i < newEffect.getVariables().length; i++) {
			// if (newEffect.getVariables()[i] == null
			// || newEffect.getVariables()[i].isFree()) {
			// // find corresponding in variables
			// for (int j = 0; j < variables.length; j++) {
			// if (variables[j].getSymbolName().equals(
			// newEffect.getVariables()[i].getSymbolName())) {
			// // System.out.println("substituting--> "
			// // + newEffect.getVariables()[i] + " for "
			// // + parameters[j].getSymbolName());
			//
			// newEffect.getVariables()[i] = parameters[j];
			// break;
			// }
			// }
			// }
			// // System.out.println("substituted--> "
			// // + newEffect.getVariables()[i]);
			// }

			// check if it already exists
			boolean exists = false;
			for (int k = 0; k < states.size(); k++) {
				if (states.get(k).equals(newState))
					exists = true;
			}
			if (!exists) {
				states.add(newState);
				// System.out.println("adding " + newState);
			}
			noOfStates++;
			if (noOfStates > maxNoStates) {
				maxNoStates = noOfStates;
			}
		} else if (actionClass instanceof CompoundActionClass) {
			// System.out.println("%%%%%%%%%%%%%%%%%%%%%% coming here");
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
				// if (states
				// .get(i)
				// .getPresentState()
				// .getActionConstants()
				// .equals(states.get(j).getPresentState()
				// .getActionConstants())
				// && states.get(i).getPresentState().getComplementForms()[0] !=
				// states
				// .get(j).getPresentState().getComplementForms()[0]) {
				// // checking if all the vars are the same
				// boolean sameVars = true;
				// for (int k = 0; k < states.get(i).getPresentState()
				// .getVariables().length; k++) {
				// if (!states.get(i).getPresentState().getVariables()[k]
				// .equals(states.get(j).getPresentState()
				// .getVariables()[k])) {
				// sameVars = false;
				// break;
				// }
				// }

				if (states.get(i).isMutexState(states.get(j))) {
					// if (sameVars) {
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
			// }
		}
	}

}
