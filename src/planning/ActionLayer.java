package planning;

import java.util.ArrayList;
import java.util.Iterator;

public class ActionLayer {
	private ArrayList<Action> actions = new ArrayList<Action>();

	public void addState(Action action) {
		actions.add(action);
	}

	public void setActions(ArrayList<Action> actions) {
		this.actions = actions;
	}

	public ArrayList<Action> getActions() {
		return actions;
	}

	public void markMutExActions(StateLayer previousStateLayer,
			StateLayer currentStateLayer) {
		// take all states from previous layer and find their mutexes

		System.out.println("action size " + actions.size());

		for (int i = 0; i < actions.size(); i++) {
			System.out.println(" - "
					+ actions.get(i).getAction().getQualifiedName()
					+ actions.get(i).hashCode() + "    "
			/*
			 * + actions.get(i).getPreviousStates().get(0)
			 * .getPresentState().getQualifiedName()
			 */);
		}

		// all the actions from these states are in mutex

		for (int j = 0; j < actions.size(); j++) {
			Action action = actions.get(j);

			for (int k = j + 1; k < actions.size(); k++) {

				Action mutExAction = actions.get(k);

				// be sure they are not the same actions be checking the
				// action constant and the predecessor
				if (action.getAction().equals(mutExAction.getAction())
						&& action.getPreviousStates() != null
						&& action.getPreviousStates().equals(
								mutExAction.getPreviousStates())) {
					continue;
				}

				// System.out.println("here "
				// + action.getAction().getQualifiedName() + " : "
				// + mutExAction.getAction().getQualifiedName());

				// if they have mutex predecessors
				for (int i = 0; i < previousStateLayer.getStates().size(); i++) {
					State oneState = previousStateLayer.getStates().get(i);
					State mutExState = previousStateLayer.getStates().get(i)
							.getMutExState();

					// if previous states contains both
					if (action.getPreviousStates() != null
							&& action.getPreviousStates().contains(oneState)
							&& mutExAction.getPreviousStates() != null
							&& mutExAction.getPreviousStates().contains(
									mutExState)) {
						// then mut ex action
						action.getMutExActions().add(mutExAction);
						mutExAction.getMutExActions().add(action);

						System.out
								.println("found mut ex actions due to source "
										+ action.getAction().getQualifiedName()
										+ " & "
										+ mutExAction.getAction()
												.getQualifiedName() + " --- "
										+ action.hashCode() + " & "
										+ mutExAction.hashCode());
					}
					// or vice versa
					// if (mutExAction.getPreviousStates().contains(oneState)
					// && action.getPreviousStates().contains(mutExState)) {
					// // then mut ex action
					// action.getMutExActions().add(mutExAction);
					// // mutExAction.getMutExActions().add(action);
					//
					// System.out.println("found mut ex actions "
					// + action.getAction().getQualifiedName() + " & "
					// + mutExAction.getAction().getQualifiedName());
					//
					// }

				}

				// if mutex states are the successors of the actions

				for (int i = 0; i < currentStateLayer.getStates().size(); i++) {
					State oneState = currentStateLayer.getStates().get(i);
					State mutExState = currentStateLayer.getStates().get(i)
							.getMutExState();

					// for (int l = 0; l < mutExAction.getSuccessiveStates()
					// .size(); l++) {

					if (action.getSuccessiveStates() != null
							&& action.getSuccessiveStates().contains(oneState)
							&& mutExAction.getSuccessiveStates() != null
							&& mutExAction.getSuccessiveStates().contains(
									mutExState)) {

						// if
						// (action.getSuccessiveStates().get(l).equals(oneState)
						// && mutExAction.getSuccessiveStates().get(l)
						// .equals(mutExState)) {
						// TODO if condition changed
						// if (action.getSuccessiveStates().equals(oneState)
						// && mutExAction.getSuccessiveState().equals(
						// mutExState)) {
						// then mutex action
						action.getMutExActions().add(mutExAction);
						mutExAction.getMutExActions().add(action);

						System.out
								.println("found mut ex actions due to destinations ^^^^^^^ "
										+ action.hashCode()
										+ " & "
										+ mutExAction.hashCode()
										+ " "
										+ action.getQualifiedName()
										+ " & "
										+ mutExAction.getQualifiedName()
										+ " -> "
										+ oneState.getPresentState()
												.getQualifiedName()
										+ " & "
										+ mutExState.getPresentState()
												.getQualifiedName());
					}
					// or vice versa
					// if (action.getSuccessiveState().equals(mutExState)
					// && mutExAction.getSuccessiveState()
					// .equals(oneState)) {
					// // then mutex action
					// action.getMutExActions().add(mutExAction);
					// mutExAction.getMutExActions().add(action);
					//
					// System.out.println("found mut ex actions "
					// + action.getAction().getQualifiedName() + " & "
					// + mutExAction.getAction().getQualifiedName());
					// }

					// }
				}
			}
		}

		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		for (int i = 0; i < actions.size(); i++) {
			System.out.println(" - "
					+ actions.get(i).getAction().getQualifiedName()
					+ actions.get(i).hashCode() + "    "
			/*
			 * + actions.get(i).getPreviousStates().get(0)
			 * .getPresentState().getQualifiedName()
			 */);

			Iterator<Action> iterator = actions.get(i).getMutExActions()
					.iterator();
			while (iterator.hasNext()) {
				Action mutexAction = iterator.next();
				System.out.println("mut ex = "
						+ mutexAction.getAction().getQualifiedName()
						+ mutexAction.hashCode());
			}
		}

	}
}
