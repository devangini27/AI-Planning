package planning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PathTracker {

	public ArrayList<ActionLayer> trackPath(ArrayList<StateLayer> stateLayers,
			ArrayList<ActionLayer> actionLayers) {

		ArrayList<ActionLayer> solutionPath = new ArrayList<ActionLayer>();

		int actionLayerLevel = actionLayers.size() - 1;
		int stateLayerLevel = stateLayers.size() - 1;

		ActionLayer solutionLayer;

		// extract goal state from last level
		StateLayer lastLayer = stateLayers.get(stateLayerLevel);

		// take the states that match the goal state
		ArrayList<State> matchedStates = match(ProblemConstant.goalState,
				lastLayer);

		while (actionLayerLevel >= 0) {
			System.out.println("=======================================");

			solutionLayer = new ActionLayer();

			// get all the actions from the previous layer that lead from the
			// previous level
			ArrayList<Action> listOfActions = getActionsToSuccessors(
					matchedStates, actionLayers.get(actionLayerLevel));

			CSPResult result = generateMutexConstraints(
					new ArrayList<Action>(), listOfActions,
					new ArrayList<State>(), matchedStates, 0);

			ArrayList<Action> newActions = result.newActions;
			solutionLayer.setActions(newActions);
			solutionPath.add(solutionLayer);

			for (int i = 0; i < newActions.size(); i++)
				System.out.println("$$$$ "
						+ newActions.get(i).hashCode()
						+ " "
						+ newActions.get(i).getAction().getQualifiedName()
						+ " "
						+ newActions.get(i).getPreviousStates().get(0)
								.getPresentState().getQualifiedName());

			// find the predecessor states from these actions
			matchedStates = findPredecessorStates(newActions);

			actionLayerLevel--;
			stateLayerLevel--;
		}

		// check whether the initial states and matchedStates are the same

		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%% "
				+ ProblemConstant.matchInitialState(matchedStates));

		// reverse arraylist
		ArrayList<ActionLayer> reverseSolutionPath = new ArrayList<ActionLayer>();
		for (int i = 0; i < solutionPath.size(); i++)
			reverseSolutionPath.add(solutionPath.get(solutionPath.size() - i
					- 1));

		return reverseSolutionPath;

	}

	private ArrayList<State> match(BaseActionClass goalState,
			StateLayer lastLayer) {

		ArrayList<State> lastLayerStates = goalState
				.getPreconditionsForAction(lastLayer.getStates());

		ArrayList<State> matchedStates = new ArrayList<State>();

		for (int i = 0; i < lastLayerStates.size(); i++) {
			System.out.println(" -- " + lastLayerStates.get(i));
			matchedStates.add(lastLayerStates.get(i));
		}
		return matchedStates;

	}

	private ArrayList<Action> getActionsToSuccessors(
			ArrayList<State> successorStates, ActionLayer actionLayer) {
		ArrayList<Action> matchedActions = new ArrayList<Action>();
		for (int i = 0; i < actionLayer.getActions().size(); i++) {
			for (int j = 0; j < successorStates.size(); j++) {
				if (actionLayer.getActions().get(i).getSuccessiveStates()
						.contains(successorStates.get(j))) {

					// TODO changed if condition
					// if (actionLayer.getActions().get(i).getSuccessiveState()
					// .equals(successorStates.get(j))) {
					// if goal states are subset of the effects of any of the
					// actions
					matchedActions.add(actionLayer.getActions().get(i));
					// System.out.println("matching action "
					// + actionLayer.getActions().get(i).getAction()
					// .getQualifiedName() + " "
					// + actionLayer.getActions().get(i).hashCode());
				}
			}
		}

		return matchedActions;
	}

	private CSPResult generateMutexConstraints(
			ArrayList<Action> selectedActions, ArrayList<Action> leftActions,
			ArrayList<State> currrentPreconditions,
			ArrayList<State> successorStates, int level) {
		printLine(level, " - action size : " + selectedActions.size());

		// check if the states lead to goal state
		if (coverGoalStates(selectedActions, successorStates)) {
			printLine(level, " - covered goal state ");

			// check whether they are all null
			boolean allNull = true;
			for (int i = 0; i < selectedActions.size(); i++) {
				if (!selectedActions.get(i).getAction()
						.equals(ActionConstants.NULL)) {
					allNull = false;
					break;
				}
			}
			if (!allNull) {
				// generate new set of actions
				return new CSPResult(true, selectedActions);
			} else {
				printLine(level, " -found all NULL");
			}
		}
		int actionIndex = 0;

		// pick random state
		while (actionIndex < leftActions.size()) {

			// (int) (Math.random() * leftActions.size());
			Action newAction = leftActions.get(actionIndex);

			// check if not already included
			boolean covered = alreadyIncluded(selectedActions, newAction, level);

			if (!covered) {

				boolean constraintsSatisfied = true;

				// make sure none of the actions are mutEx
				if (isMutexAction(selectedActions, newAction))
					constraintsSatisfied = false;

				// make sure any of the preconditions are not in mutEx
				if (isMutExPreconditionState(currrentPreconditions, newAction))
					constraintsSatisfied = false;

				if (constraintsSatisfied) {

					// make copies first
					ArrayList<Action> newSelectedActions = new ArrayList<Action>();
					for (int i = 0; i < selectedActions.size(); i++) {
						newSelectedActions.add(selectedActions.get(i));
					}
					ArrayList<State> newPreconditions = new ArrayList<State>();
					for (int i = 0; i < currrentPreconditions.size(); i++) {
						newPreconditions.add(currrentPreconditions.get(i));
					}
					ArrayList<Action> newLeftActions = new ArrayList<Action>();
					for (int i = 0; i < leftActions.size(); i++) {
						newLeftActions.add(leftActions.get(i));
					}

					// add this action to selected actions
					newSelectedActions.add(newAction);
					// add preconditions too
					newPreconditions.addAll(newAction.getPreviousStates());

					// remove action from left actions
					newLeftActions.remove(newAction);
					printLine(level, " -considering "
							+ newAction.getAction().getQualifiedName() + " "
							+ newAction.hashCode());

					CSPResult result = generateMutexConstraints(
							newSelectedActions, newLeftActions,
							newPreconditions, successorStates, level + 1);

					if (result != null && result.found)
						return result;

					printLine(level, " -unconsidering "
							+ newAction.getAction().getQualifiedName() + " "
							+ newAction.hashCode());

				} else {
					printLine(level, " - conflicting so far");
				}
			}
			actionIndex++;
		}
		printLine(level, " leaving");
		return null;

	}

	private boolean isMutexAction(ArrayList<Action> selectedActions,
			Action newAction) {
		for (int i = 0; i < selectedActions.size(); i++) {
			if (selectedActions.get(i).getMutExActions().contains(newAction))
				return true;
		}
		return false;
	}

	private boolean isMutExPreconditionState(
			ArrayList<State> currrentPreconditions, Action newAction) {

		for (int i = 0; i < currrentPreconditions.size(); i++) {

			if (currrentPreconditions.get(i).getMutExState() != null
					&& newAction.getAction().getPrecondition() != null
					&& newAction
							.getAction()
							.getPrecondition()
							.isMutexState(
									currrentPreconditions.get(i)
											.getMutExState()))
				return true;

		}
		return false;
	}

	private boolean coverGoalStates(ArrayList<Action> matchedActions,
			ArrayList<State> successorStates) {
		boolean covered = false;
		// System.out
		// .println("size of successor states " + successorStates.size());
		// System.out.println("size =" + matchedActions.size());

		Set<State> generatedStates = new HashSet<State>();
		for (int i = 0; i < matchedActions.size(); i++) {

			// TODO added one inner for loop
			for (int j = 0; j < matchedActions.get(i).getSuccessiveStates()
					.size(); j++) {
				if (successorStates.contains(matchedActions.get(i)
						.getSuccessiveStates().get(j))) {
					// System.out.println("adding covered state "
					// + matchedActions.get(i).getSuccessiveStates()
					// .get(j).getPresentState()
					// .getQualifiedName());
					generatedStates.add(matchedActions.get(i)
							.getSuccessiveStates().get(j));
				}
			}
		}

		// check if sizes are same
		if (generatedStates.size() == successorStates.size())
			covered = true;
		return covered;
	}

	private boolean alreadyIncluded(ArrayList<Action> selectedActions,
			Action newAction, int level) {
		for (int i = 0; i < selectedActions.size(); i++) {
			if (selectedActions.get(i).getAction()
					.equals(newAction.getAction())) {
				// check if previous states are the same

				printLine(level, "same action constant "
						+ newAction.getAction().getQualifiedName());

				if (selectedActions.get(i).getPreviousStates()
						.equals(newAction.getPreviousStates())) {
					return true;
				}
			}
		}
		return false;
	}

	private ArrayList<State> findPredecessorStates(ArrayList<Action> newActions) {
		ArrayList<State> predecessorStates = new ArrayList<State>();
		for (int i = 0; i < newActions.size(); i++) {
			for (int j = 0; j < newActions.get(i).getPreviousStates().size(); j++) {
				if (!predecessorStates.contains(newActions.get(i)
						.getPreviousStates().get(j)))
					predecessorStates.addAll(newActions.get(i)
							.getPreviousStates());
			}
		}
		return predecessorStates;
	}

	class CSPResult {
		public CSPResult(boolean found, ArrayList<Action> newActions) {
			super();
			this.found = found;
			this.newActions = newActions;
		}

		boolean found;
		ArrayList<Action> newActions;

	}

	public void printLine(int level, String line) {
		// for (int i = 0; i < level; i++)
		// System.out.print("\t");
		// System.out.println(level + " - " + line);
	}

}
