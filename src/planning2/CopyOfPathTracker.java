//package planning2;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//
//public class CopyOfPathTracker {
//
//	private ArrayList<ActionLayer> actionLayers;
//	private ArrayList<StateLayer> stateLayers;
//
//	public ArrayList<ActionLayer> trackPath(ArrayList<StateLayer> stateLayers,
//			ArrayList<ActionLayer> actionLayers) {
//
//		this.actionLayers = actionLayers;
//		this.stateLayers = stateLayers;
//		ArrayList<ActionLayer> solutionPath = new ArrayList<ActionLayer>();
//
//		int actionLayerLevel = actionLayers.size() - 1;
//		int stateLayerLevel = stateLayers.size() - 1;
//
//		// ActionLayer solutionLayer;
//
//		// extract goal state from last level
//		StateLayer lastLayer = stateLayers.get(stateLayerLevel);
//
//		// take the states that match the goal state
//		ArrayList<State> matchedStates = match(ProblemConstant.goalState,
//				lastLayer);
//
//		// while (actionLayerLevel >= 1) {
//		// System.out.println("=======================================");
//		//
//		// solutionLayer = new ActionLayer();
//		//
//		// // get all the actions from the previous layer that lead from the
//		// // previous level
//		// ArrayList<Action> listOfActions = reverseList(getActionsToSuccessors(
//		// matchedStates, actionLayers.get(actionLayerLevel)));
//		//
//		// CSPResult result = new CSPResult(false,
//		// new ArrayList<ArrayList<Action>>());
//		// generateMutexConstraints(new ArrayList<Action>(), listOfActions,
//		// new ArrayList<State>(), matchedStates, 0, result);
//		//
//		// System.out.println("$$$$$$$$$$$$ size :::  " + result.found
//		// + " size " + result.newActions.size());
//		// for (int i = 0; i < result.newActions.size(); i++) {
//		// for (int j = 0; j < result.newActions.get(i).size(); j++)
//		// System.out.println("::::::: %%%%%%%% "
//		// + result.newActions.get(i).get(j)
//		// .getQualifiedName());
//		// System.out.println("__________________");
//		// }
//		// // if (result == null)
//		// // return null;
//		// ArrayList<Action> newActions = result.newActions.get(0);
//		// solutionLayer.setActions(newActions);
//		// solutionPath.add(solutionLayer);
//		//
//		// for (int i = 0; i < newActions.size(); i++)
//		// System.out.println("$$$$ "
//		// + newActions.get(i).hashCode()
//		// + " "
//		// + newActions.get(i).getQualifiedName()
//		// + " "
//		// + newActions.get(i).getPreviousStates().get(0)
//		// .getPresentState().getQualifiedName());
//		//
//		// // find the predecessor states from these actions
//		// matchedStates = findPredecessorStates(newActions);
//		//
//		// actionLayerLevel--;
//		// stateLayerLevel--;
//		// }
//
//		solutionPath = trackPathRecursive(actionLayerLevel, matchedStates,
//				solutionPath, stateLayerLevel, false);
//		// check whether the initial states and matchedStates are the same
//
//		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%% "
//				+ matchedStates.size() + " "
//				+ ProblemConstant.matchInitialState(matchedStates));
//
//		if (ProblemConstant.matchInitialState(matchedStates)) {
//			// reverse arraylist
//			ArrayList<ActionLayer> reverseSolutionPath = new ArrayList<ActionLayer>();
//			for (int i = 0; i < solutionPath.size(); i++)
//				reverseSolutionPath.add(solutionPath.get(solutionPath.size()
//						- i - 1));
//
//			return reverseSolutionPath;
//		} else {
//			return null;
//		}
//
//	}
//
//	private ArrayList<ActionLayer> trackPathRecursive(int actionLayerLevel,
//			ArrayList<State> matchedStates,
//			ArrayList<ActionLayer> solutionPath, int stateLayerLevel,
//			boolean isStrictMatch) {
//		if (actionLayerLevel >= 0) {
//			printLine2(actionLayerLevel,
//					"======================================= "
//							+ actionLayerLevel);
//
//			// get all the actions from the previous layer that lead from the
//			// previous level
//			ArrayList<Action> listOfActions = reverseList(getActionsToSuccessors(
//					matchedStates, actionLayers.get(actionLayerLevel)));
//
//			CSPResult result = new CSPResult(false,
//					new ArrayList<ArrayList<Action>>());
//			generateMutexConstraints(new ArrayList<Action>(), listOfActions,
//					new ArrayList<State>(), matchedStates, 0, result,
//					isStrictMatch);
//
//			printLine2(actionLayerLevel, "$$$$$$$$$$$$ size :::  "
//					+ result.found + " size " + result.newActions.size());
//			for (int i = 0; i < result.newActions.size(); i++) {
//
//				for (int j = 0; j < result.newActions.get(i).size(); j++)
//					printLine2(actionLayerLevel, "::::::: %%%%%%%% "
//							+ result.newActions.get(i).get(j)
//									.getQualifiedName()
//							+ " : "
//							+ result.newActions.get(i).get(j)
//									.getPreviousStates().get(0)
//									.getPresentState().getQualifiedName());
//				printLine2(actionLayerLevel, "__________________");
//
//				// if (result == null)
//				// return null;
//				ArrayList<Action> newActions = result.newActions.get(i);
//				ActionLayer solutionLayer = new ActionLayer();
//				// make copies first
//				ArrayList<ActionLayer> newSolutionPath = new ArrayList<ActionLayer>();
//				for (int k = 0; k < solutionPath.size(); k++) {
//					newSolutionPath.add(solutionPath.get(k));
//				}
//
//				solutionLayer.setActions(newActions);
//				newSolutionPath.add(solutionLayer);
//
//				// for (int k = 0; k < newActions.size(); k++)
//				// printLine2(actionLayerLevel, "$$$$ "
//				// + newActions.get(k).hashCode()
//				// + " "
//				// + newActions.get(k).getQualifiedName()
//				// + " "
//				// + newActions.get(k).getPreviousStates().get(0)
//				// .getPresentState().getQualifiedName());
//
//				// find the predecessor states from these actions
//				ArrayList<State> newMatchedStates = findPredecessorStates(
//						newActions, actionLayerLevel);
//
//				int newActionLayerLevel = actionLayerLevel - 1;
//				int newStateLayerLevel = stateLayerLevel - 1;
//
//				ArrayList<ActionLayer> solutionPathResult = trackPathRecursive(
//						newActionLayerLevel, newMatchedStates, newSolutionPath,
//						newStateLayerLevel, false);
//				if (solutionPathResult != null) {
//					return solutionPathResult;
//				}
//			}
//		} else {
//			printLine2(actionLayerLevel,
//					"0000000000000000000000000000000000000000000000000");
//			// if u found initial state
//			if (ProblemConstant.matchInitialState(matchedStates)) {
//				printLine2(actionLayerLevel,
//						"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ ");
//				return solutionPath;
//			}
//		}
//		return null;
//	}
//
//	private ArrayList<State> match(BaseActionClass goalState,
//			StateLayer lastLayer) {
//
//		ArrayList<State> lastLayerStates = new ArrayList<State>(
//				goalState.getPreconditionsForAction(lastLayer.getStates()));
//
//		ArrayList<State> matchedStates = new ArrayList<State>();
//
//		for (int i = 0; i < lastLayerStates.size(); i++) {
//			System.out.println(" -- goal states : " + lastLayerStates.get(i));
//			matchedStates.add(lastLayerStates.get(i));
//		}
//		return matchedStates;
//
//	}
//
//	private ArrayList<Action> getActionsToSuccessors(
//			ArrayList<State> successorStates, ActionLayer actionLayer) {
//		ArrayList<Action> matchedActions = new ArrayList<Action>();
//		for (int i = 0; i < actionLayer.getActions().size(); i++) {
//			for (int j = 0; j < successorStates.size(); j++) {
//				if (actionLayer.getActions().get(i).getSuccessiveStates()
//						.contains(successorStates.get(j))) {
//
//					matchedActions.add(actionLayer.getActions().get(i));
//					System.out.println("matching action "
//							+ actionLayer.getActions().get(i)
//									.getQualifiedName()
//							+ " "
//							+ actionLayer.getActions().get(i).hashCode()
//							+ " pred "
//							+ actionLayer.getActions().get(i)
//									.getPreviousStates().get(0)
//									.getPresentState().getQualifiedName());
//				}
//			}
//		}
//
//		return matchedActions;
//	}
//
//	public ArrayList<Action> reverseList(ArrayList<Action> list) {
//		ArrayList<Action> newList = new ArrayList<Action>();
//		for (int i = 0; i < list.size(); i++) {
//			newList.add(list.get(list.size() - 1 - i));
//		}
//		return newList;
//	}
//
//	private void generateMutexConstraints(ArrayList<Action> selectedActions,
//			ArrayList<Action> leftActions,
//			ArrayList<State> currrentPreconditions,
//			ArrayList<State> successorStates, int level, CSPResult newResult,
//			boolean isStrictMatch) {
//		// printLine(level, " - action size : " + selectedActions.size());
//
//		// check if the states lead to goal state
//		if (coverGoalStates(selectedActions, successorStates, isStrictMatch)) {
//			printLine(level, " - covered goal state ");
//
//			// check whether they are all null
//			boolean allNull = true;
//			for (int i = 0; i < selectedActions.size(); i++) {
//				if (!selectedActions.get(i).getAction()
//						.equals(ActionConstants.NULL)) {
//					allNull = false;
//					break;
//				}
//			}
//			if (!allNull) {
//				// generate new set of actions
//				newResult.addResultList(selectedActions);
//				return;
//			} else {
//				printLine(level, " -found all NULL");
//			}
//		}
//		int actionIndex = 0;
//
//		// pick random state
//		while (actionIndex < leftActions.size()) {
//
//			// (int) (Math.random() * leftActions.size());
//			Action newAction = leftActions.get(actionIndex);
//
//			// check if not already included
//			boolean covered = alreadyIncluded(selectedActions, newAction, level);
//
//			if (!covered) {
//
//				boolean constraintsSatisfied = true;
//
//				// make sure none of the actions are mutEx
//				if (isMutexAction(selectedActions, newAction))
//					constraintsSatisfied = false;
//
//				// make sure any of the preconditions are not in mutEx
//				if (isMutExPreconditionState(currrentPreconditions, newAction))
//					constraintsSatisfied = false;
//
//				if (constraintsSatisfied) {
//
//					// make copies first
//					ArrayList<Action> newSelectedActions = new ArrayList<Action>();
//					for (int i = 0; i < selectedActions.size(); i++) {
//						newSelectedActions.add(selectedActions.get(i));
//					}
//					ArrayList<State> newPreconditions = new ArrayList<State>();
//					for (int i = 0; i < currrentPreconditions.size(); i++) {
//						newPreconditions.add(currrentPreconditions.get(i));
//					}
//					ArrayList<Action> newLeftActions = new ArrayList<Action>();
//					for (int i = 0; i < leftActions.size(); i++) {
//						newLeftActions.add(leftActions.get(i));
//					}
//
//					// add this action to selected actions
//					newSelectedActions.add(newAction);
//					// add preconditions too
//					newPreconditions.addAll(newAction.getPreviousStates());
//
//					// remove action from left actions
//					newLeftActions.remove(newAction);
//					printLine(level,
//							" -considering "
//									+ newAction.getQualifiedName()
//									+ " "
//									+ newAction.hashCode()
//									+ " "
//									+ newAction.getPreviousStates().get(0)
//											.getPresentState()
//											.getQualifiedName());
//
//					generateMutexConstraints(newSelectedActions,
//							newLeftActions, newPreconditions, successorStates,
//							level + 1, newResult, isStrictMatch);
//
//					// if (result != null && result.found)
//					// return result;
//
//					printLine(level,
//							" -unconsidering " + newAction.getQualifiedName()
//									+ " " + newAction.hashCode());
//
//				} else {
//					printLine(level, " - conflicting so far");
//				}
//			}
//			actionIndex++;
//		}
//		printLine(level, " leaving");
//		return;
//
//	}
//
//	private boolean isMutexAction(ArrayList<Action> selectedActions,
//			Action newAction) {
//		for (int i = 0; i < selectedActions.size(); i++) {
//			if (selectedActions.get(i).getMutExActions().contains(newAction)) {
//				// System.out.println("mutex action ::: "
//				// + newAction.getQualifiedName() + " and "
//				// + selectedActions.get(i).getQualifiedName());
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean isMutExPreconditionState(
//			ArrayList<State> currrentPreconditions, Action newAction) {
//
//		for (int i = 0; i < currrentPreconditions.size(); i++) {
//
//			if (currrentPreconditions.get(i).getMutExState() != null
//					&& newAction.getAction().getPrecondition() != null
//					&& newAction
//							.getAction()
//							.getPrecondition()
//							.isMutexState(
//									currrentPreconditions.get(i)
//											.getMutExState())) {
//				// System.out
//				// .println("states in mutex are :::: precond of action and state "
//				// + newAction.getQualifiedName()
//				// + " and "
//				// + currrentPreconditions.get(i)
//				// .getPresentState().getQualifiedName());
//				return true;
//			}
//		}
//		return false;
//	}
//
//	private boolean coverGoalStates(ArrayList<Action> matchedActions,
//			ArrayList<State> successorStates, boolean isStrictMatch) {
//
//		boolean covered = false;
//		// System.out
//		// .println("size of successor states " + successorStates.size());
//
//		Set<State> generatedStates = new HashSet<State>();
//		for (int i = 0; i < matchedActions.size(); i++) {
//			for (int j = 0; j < matchedActions.get(i).getSuccessiveStates()
//					.size(); j++) {
//				if (!isStrictMatch) {
//					if (successorStates.contains(matchedActions.get(i)
//							.getSuccessiveStates().get(j))) {
//						// System.out.println("adding covered state "
//						// + matchedActions.get(i).getSuccessiveStates()
//						// .get(j).getPresentState()
//						// .getQualifiedName());
//						generatedStates.add(matchedActions.get(i)
//								.getSuccessiveStates().get(j));
//					}
//				} else {
//					generatedStates.add(matchedActions.get(i)
//							.getSuccessiveStates().get(j));
//				}
//			}
//		}
//
//		// check if sizes are same
//		if (generatedStates.size() == successorStates.size())
//			covered = true;
//		// if it also ok if there are more
//
//		return covered;
//
//	}
//
//	private boolean alreadyIncluded(ArrayList<Action> selectedActions,
//			Action newAction, int level) {
//		for (int i = 0; i < selectedActions.size(); i++) {
//			if (selectedActions.get(i).getAction()
//					.equals(newAction.getAction())) {
//				// check if previous states are the same
//
//				// printLine(level, "same action constant "
//				// + newAction.getAction().getQualifiedName());
//
//				if (selectedActions.get(i).getPreviousStates()
//						.equals(newAction.getPreviousStates())) {
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	private ArrayList<State> findPredecessorStates(
//			ArrayList<Action> newActions, int level) {
//		ArrayList<State> predecessorStates = new ArrayList<State>();
//		for (int i = 0; i < newActions.size(); i++) {
//			for (int j = 0; j < newActions.get(i).getPreviousStates().size(); j++) {
//				if (!predecessorStates.contains(newActions.get(i)
//						.getPreviousStates().get(j)))
//					predecessorStates.add(newActions.get(i).getPreviousStates()
//							.get(j));
//				printLine2(level, "predecessor states are :::: "
//						+ newActions.get(i).getPreviousStates().get(j)
//								.getPresentState().getQualifiedName());
//			}
//		}
//		return predecessorStates;
//	}
//
//	class CSPResult {
//		public CSPResult(boolean found, ArrayList<ArrayList<Action>> newActions) {
//			super();
//			this.found = found;
//			this.newActions = newActions;
//		}
//
//		boolean found;
//		ArrayList<ArrayList<Action>> newActions;
//
//		public void addResultList(ArrayList<Action> newAction) {
//			boolean exists = false;
//			// check if such an arraylist already exists
//			for (int i = 0; i < newActions.size(); i++) {
//				// first compare length
//				if (newActions.get(i).size() == newAction.size()) {
//					// compare each and every element
//					for (int j = 0; j < newActions.get(i).size(); j++) {
//						for (int k = 0; k < newAction.size(); k++) {
//							if (newActions.get(i).get(j)
//									.equals(newAction.get(k)))
//								exists = true;
//						}
//					}
//				}
//			}
//			if (!exists) {
//				newActions.add(newAction);
//			}
//		}
//
//	}
//
//	public void printLine(int level, String line) {
//		// for (int i = 0; i < level; i++)
//		// System.out.print("\t");
//		// System.out.println(level + " - " + line);
//	}
//
//	public void printLine2(int level, String line) {
//		for (int i = 0; i < level + 1; i++)
//			System.out.print("\t");
//		System.out.println(level + " - " + line);
//	}
//
//}
