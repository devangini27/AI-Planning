package planning2;

import java.util.ArrayList;

public class ProblemConstant {
	static BaseActionClass initialState;

	static BaseActionClass originalInitialState;

	static BaseActionClass goalState;

	public static void makeClosedWorldAssumptions(boolean apply) {
		originalInitialState = initialState;
		if (apply) {
			// get list of states
			for (ActionConstants state : ActionConstants.possiblePredicates) {
				// see which state is not already present (in normal/complement
				// form
				// in initial state)
				if (!initialState.checkIfPresent(state)) {
					// System.out.println("not present " +
					// state.getActionName());
					// add negated form of this to initial state
					if (initialState instanceof ActionClass) {
						ActionClass leafInitialState = (ActionClass) initialState;
						initialState = new CompoundActionClass(
								new ActionClass[] {
										(ActionClass) initialState,
										new ActionClass(state, null,
												new boolean[] { false }) },
								new ConnectiveConstants[] { ConnectiveConstants.AND },
								new boolean[] { true, true });
					} else if (initialState instanceof CompoundActionClass) {
						CompoundActionClass compoundInitialState = (CompoundActionClass) initialState;
						initialState = new CompoundActionClass(
								concat(compoundInitialState.getChildActions(),
										new ActionClass(state, null,
												new boolean[] { false })),
								new ConnectiveConstants[] { ConnectiveConstants.AND },
								new boolean[] { true, true });
					}
				}

			}
		}
	}

	public static BaseActionClass[] concat(BaseActionClass[] A,
			BaseActionClass B) {
		BaseActionClass[] C = new BaseActionClass[(A.length + 1)];
		System.arraycopy(A, 0, C, 0, A.length);
		// System.arraycopy(B, 0, C, A.length, B.length);
		C[A.length] = B;
		return C;
	}

	// static {
	// if (Planner.getProblemNumber() == 0) {
	// initialState = new ActionClass(ActionConstants.HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true });
	// goalState = new CompoundActionClass(new ActionClass[] {
	// new ActionClass(ActionConstants.HAVE,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true }),
	// new ActionClass(ActionConstants.EATEN,
	// new VariableConstants[] { VariableConstants.CAKE },
	// new boolean[] { true }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true });
	//
	// } else if (Planner.getProblemNumber() == 1) {
	// initialState = new CompoundActionClass(new ActionClass[] {
	// new ActionClass(ActionConstants.AT,
	// new VariableConstants[] { VariableConstants.FLAT,
	// VariableConstants.AXLE },
	// new boolean[] { true }),
	// new ActionClass(ActionConstants.AT,
	// new VariableConstants[] { VariableConstants.SPARE,
	// VariableConstants.TRUNK },
	// new boolean[] { true }) },
	// new ConnectiveConstants[] { ConnectiveConstants.AND },
	// new boolean[] { true, true });
	// // System.out.println("initial state " + initialState);
	// goalState = new ActionClass(ActionConstants.AT,
	// new VariableConstants[] { VariableConstants.SPARE,
	// VariableConstants.AXLE }, new boolean[] { true });
	// }
	//
	// // add closed world assumptions to the initial state
	// makeClosedWorldAssumptions();
	// }

	public static boolean matchInitialState(ArrayList<State> matchedStates) {
		ArrayList<ActionClass> initialStates = originalInitialState
				.getAllActions();

		for (int i = 0; i < matchedStates.size(); i++) {
			System.out
					.println("state :::: "
							+ matchedStates.get(i).getPresentState()
									.getQualifiedName());
		}
		// check if length are same
		if (initialStates.size() == matchedStates.size()) {
			for (int j = 0; j < initialStates.size(); j++) {
				boolean match = false;
				// boolean conflict = false;

				for (int i = 0; i < matchedStates.size(); i++) {

					// if
					// (!originalInitialState.canBeApplied(matchedStates.get(i)
					// .getPresentState())) {
					if (matchedStates.get(i).getPresentState()
							.fitsFreeVariables(initialStates.get(j))) {
						System.out.println("   match "
								+ matchedStates.get(i).getPresentState()
										.getQualifiedName());
						match = true;
					}
				}
				if (!match) {
					return false;
				}
			}
		}
		return true;
	}

	// public static boolean matchInitialState(ArrayList<State> matchedStates) {
	// for (int i = 0; i < matchedStates.size(); i++) {
	// if (!originalInitialState.canBeApplied(matchedStates.get(i)
	// .getPresentState())) {
	// System.out.println("  doesn't match "
	// + matchedStates.get(i).getPresentState()
	// .getQualifiedName());
	// return false;
	// }
	// }
	// return true;
	// }
}
