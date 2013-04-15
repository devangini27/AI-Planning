package planning2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.JFrame;

//slide 11, 12, 13, 23
public class Planner {

	public static final boolean TRACE = false;

	private static int problemNumber = 0;
	// private StateLayer currentStateLayer;
	private ArrayList<StateLayer> stateLayers = new ArrayList<StateLayer>();
	private ArrayList<ActionLayer> actionLayers = new ArrayList<ActionLayer>();

	Planner() {

		ProblemInputReader inputReader = new ProblemInputReader();
		// inputReader.readInputs("sussman-anomaly.pddl",
		// "blocks-world-domain.pddl");
		inputReader.readInputs("gripper-1.pddl", "gripper-domain.pddl");
		//inputReader.readInputs("gripper-22.pddl", "gripper-domain.pddl");
		// inputReader.readInputs("monkeytest1.pddl", "monkey-domain.pddl");
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		StateLayer initialStateLayer = new StateLayer();
		initialStateLayer.addState(ProblemConstant.initialState);
		stateLayers.add(initialStateLayer);
		// mark mut ex states in initial state
		initialStateLayer.markMutExStates();

	}

	public static void main(String[] args) {
		Planner planner = new Planner();

		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

		int i = 0;
		ArrayList<ActionLayer> solutionPath = null;
		// while (i < 3) {
		while (planner.stateLayers.size() == 1
				|| planner.stateLayers.get(planner.stateLayers.size() - 1)
						.getStates().size() != planner.stateLayers
						.get(planner.stateLayers.size() - 2).getStates().size()) {
			planner.getNextStates();

			if (i != 0) {
				try {
					solutionPath = planner.extractPath(planner.stateLayers,
							planner.actionLayers);
				} catch (Exception e) {
					solutionPath = null;
					e.printStackTrace();
				}
			}
			if (solutionPath != null)
				break;
			System.out.println("############################ " + i);
			i++;
		}
		// solutionPath = planner.extractPath(planner.stateLayers,
		// planner.actionLayers);
		// CopyOfGraphMaker frame = new CopyOfGraphMaker(planner.stateLayers,
		// planner.actionLayers);
		GraphMaker frame = new GraphMaker(planner.stateLayers,
				planner.actionLayers, solutionPath);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setSize(2000, 2000);
		// frame.pack();
		frame.setVisible(true);

	}

	public void getNextStates() {
		// loop the states in the last state layers
		StateLayer currentStateLayer = stateLayers.get(stateLayers.size() - 1);
		StateLayer newStateLayer = new StateLayer();
		ActionLayer newActionLayer = new ActionLayer();
		// for (int index_state = 0; index_state < currentStateLayer.getStates()
		// .size(); index_state++) {
		// State currentState = currentStateLayer.getStates().get(index_state);
		if (Planner.TRACE)
			System.out.println("currentState SIZE "
					+ currentStateLayer.getStates().size());

		// consider all null/unique actions separately
		for (int index_state = 0; index_state < currentStateLayer.getStates()
				.size(); index_state++) {
			State currentState = currentStateLayer.getStates().get(index_state);
			System.out.println("currentState "
					+ currentState.getPresentState().getQualifiedName());

			// if null then they remain the same
			BaseActionClass effects = currentState.getPresentState();
			ArrayList<State> predecessorStates = new ArrayList<State>();
			predecessorStates.add(currentState);

			// check if they are present in the new layer
			ArrayList<State> successiveStates = newStateLayer.addNewEffects(
					effects, new VariableConstants[] {},
					new VariableConstants[] {});

			// System.out.println("successor size for action "
			// + ActionConstants.NULL.getQualifiedName() + " is "
			// + successiveStates.size());

			Action newAction = new Action(predecessorStates, successiveStates,
					ActionConstants.NULL, null);
			newActionLayer.addState(newAction);
		}

		// see which pure actions can be taken from this state
		System.out.println("ACTIONS SIZE "
				+ ActionConstants.possibleActions.length);

		for (ActionConstants possibleActionConstants : ActionConstants.possibleActions) {

			ArrayList<ArrayList<State>> possibilities2 = null;
			try {
				possibilities2 = possibleActionConstants
						.getPossibilities(currentStateLayer.getStates());
			} catch (ApplicationException e) {
			}
			if (possibilities2 != null) {

				ArrayList<ArrayList<State>> possibilities = getPossibilities(possibilities2);
				if (TRACE)
					System.out.println("###########--- no of possibilities "
							+ possibilities.size());

				for (int d = 0; d < possibilities.size(); d++) {
					boolean canBeApplied = false;
					HashSet<State> hashStates = possibleActionConstants
							.getPreconditionsForAction(possibilities.get(d));
					if (hashStates != null) {
						canBeApplied = true;
					}

					if (canBeApplied) {
						ArrayList<State> predecessorStates = new ArrayList<State>(
								hashStates);
						for (int i = 0; i < predecessorStates.size(); i++) {
							// System.out.println(";;;;;; "
							// + predecessorStates.get(i)
							// .getPresentState()
							// .getQualifiedName());
						}
						// apply the action
						System.out
								.println("applying the action$$$$$$$$$$$$$$$$$$$ "
										+ possibleActionConstants
												.getQualifiedName()
										+ " to state  ");
						try {
							VariableConstants[] parameters = possibleActionConstants
									.getParameters(predecessorStates);
							if (TRACE)
								System.out.println("variables are "
										+ Arrays.toString(parameters));
							// get the effect states
							BaseActionClass effects;

							if (possibleActionConstants.getEffects() == null) {
								// if null then they remain the same
								effects = possibleActionConstants
										.getPrecondition();
							} else {
								effects = possibleActionConstants.getEffects();
							}

							// check if they are present in the new layer
							ArrayList<State> successiveStates = newStateLayer
									.addNewEffects(effects, parameters,
											possibleActionConstants
													.getVariables());

							// if nothing is specified in action constants then
							// make
							// predecessor null
							if (possibleActionConstants.getPrecondition() == null) {
								System.out.println("!!!!!!no predecessor for "
										+ possibleActionConstants
												.getQualifiedName());
								predecessorStates = null;
							}

							// check if such an action already exists

							Action newAction = new Action(predecessorStates,
									successiveStates, possibleActionConstants,
									parameters);
							if (!newActionLayer.getActions()
									.contains(newAction))

								newActionLayer.addState(newAction);
							// System.out.println("action " + newAction);
						} catch (ApplicationException e) {
							// e.printStackTrace();
						}
					} else {
						System.out.println("can't be applied-------------- "
								+ possibleActionConstants.getActionName());
					}
				}

			} else {
				System.out.println("possibilies null ----- "
						+ possibleActionConstants.getActionName());
			}
		}

		// }
		stateLayers.add(newStateLayer);
		actionLayers.add(newActionLayer);
		System.out.println("*************************");
		System.out.println("size of actions " + actionLayers.size() + " : "
				+ newActionLayer.getActions().size());
		System.out.println("size of states " + stateLayers.size() + " : "
				+ newStateLayer.getStates().size());

		// set mut ex states
		newStateLayer.markMutExStates();

		// set the mutex actions
		newActionLayer.markMutExActions(currentStateLayer, newStateLayer);

		// if they lead from opposite preconditions or effects then they are
		// opposites

	}

	// private ArrayList<ArrayList<State>> getPossibilities(
	// ArrayList<ArrayList<State>> predecessors,
	// ActionConstants actionConstant) {
	//
	// ArrayList<ArrayList<State>> setOfPredecessors = new
	// ArrayList<ArrayList<State>>();
	//
	// // for (int i = 0; i < predecessors.size(); i++) {
	// // for (int k = 0; k < predecessors.get(i).size(); k++)
	// // // System.out.print(" -- "
	// // // + predecessors.get(i).get(k).getPresentState()
	// // // .getQualifiedName());
	// // //System.out.println();
	// // }
	// // take one from each
	// for (int i = 0; i < predecessors.size(); i++) {
	// if (predecessors.get(i).size() == 1) {
	// // just add to previous ones
	// if (i != 0) {
	// ArrayList<ArrayList<State>> setOfPredecessorsCopy =
	// cloneList(setOfPredecessors);
	// setOfPredecessors = new ArrayList<ArrayList<State>>();
	// for (int k = 0; k < setOfPredecessorsCopy.size(); k++) {
	// ArrayList<State> states5 = cloneList1(setOfPredecessorsCopy
	// .get(k));
	// states5.add(predecessors.get(i).get(0));
	// setOfPredecessors.add(states5);
	// }
	//
	// } else {
	// // just add
	// ArrayList<State> first = new ArrayList<State>();
	// first.add(predecessors.get(i).get(0));
	// setOfPredecessors.add(first);
	//
	// }
	// // System.out.println("just one added " +
	// // setOfPredecessors.size()
	// // + " -> " + setOfPredecessors.get(0).size());
	// } else {
	// // take one from each add and add to all
	// if (i != 0) {
	//
	// ArrayList<ArrayList<State>> setOfPredecessorsCopy =
	// cloneList(setOfPredecessors);
	// setOfPredecessors = new ArrayList<ArrayList<State>>();
	//
	// for (int k = 0; k < setOfPredecessorsCopy.size(); k++) {
	// for (int j = 0; j < predecessors.get(i).size(); j++) {
	//
	// ArrayList<State> states5 = cloneList1(setOfPredecessorsCopy
	// .get(k));
	// // System.out.println("initial size is "
	// // + states5.size());
	// states5.add(predecessors.get(i).get(j));
	// setOfPredecessors.add(states5);
	// // System.out.println("size becomes--- "
	// // + setOfPredecessors.size() + " - > "
	// // + setOfPredecessors.get(0).size() + " & "
	// // + states5.size() + " -- "
	// // + setOfPredecessorsCopy.get(0).size());
	// }
	// }
	// } else {
	// // just add
	// for (int k = 0; k < predecessors.get(i).size(); k++) {
	// ArrayList<State> states5 = new ArrayList<State>();
	// states5.add(predecessors.get(i).get(k));
	// setOfPredecessors.add(states5);
	//
	// // System.out.println("size becomes--- "
	// // + setOfPredecessors.size() + " - > "
	// // + setOfPredecessors.get(k).size() + " & "
	// // + states5.size());
	// }
	//
	// }
	// // System.out.println("size becomes " + setOfPredecessors.size()
	// // + " - > " + setOfPredecessors.get(0).size());
	// }
	// }
	//
	// int total = 0;
	// for (int i = 0; i < setOfPredecessors.size(); i++) {
	// for (int k = 0; k < setOfPredecessors.get(i).size(); k++)
	// total++;
	// }
	// // System.out.println("size ::::::::::: " + setOfPredecessors.size() +
	// // " "
	// // + total);
	//
	// return setOfPredecessors;
	// }

	private ArrayList<ArrayList<State>> getPossibilities(
			ArrayList<ArrayList<State>> predecessors) {

		ArrayList<ArrayList<State>> setOfPredecessors = new ArrayList<ArrayList<State>>();

		// for (int i = 0; i < predecessors.size(); i++) {
		// for (int k = 0; k < predecessors.get(i).size(); k++)
		// // System.out.print(" -- "
		// // + predecessors.get(i).get(k).getPresentState()
		// // .getQualifiedName());
		// //System.out.println();
		// }
		// take one from each
		for (int i = 0; i < predecessors.size(); i++) {
			if (predecessors.get(i).size() == 1) {
				// just add to previous ones
				if (i != 0) {
					ArrayList<ArrayList<State>> setOfPredecessorsCopy = copyList(setOfPredecessors);
					setOfPredecessors = new ArrayList<ArrayList<State>>();
					for (int k = 0; k < setOfPredecessorsCopy.size(); k++) {
						// check if this element is already not present in the
						// list once
						if (!setOfPredecessorsCopy.get(k).contains(
								predecessors.get(i).get(0))) {
							ArrayList<State> states5 = copyList1(setOfPredecessorsCopy
									.get(k));
							states5.add(predecessors.get(i).get(0));
							setOfPredecessors.add(states5);
						}
					}

				} else {
					// just add
					ArrayList<State> first = new ArrayList<State>();
					first.add(predecessors.get(i).get(0));
					setOfPredecessors.add(first);

				}
				// System.out.println("just one added " +
				// setOfPredecessors.size()
				// + " -> " + setOfPredecessors.get(0).size());
			} else {
				// take one from each add and add to all
				if (i != 0) {

					ArrayList<ArrayList<State>> setOfPredecessorsCopy = copyList(setOfPredecessors);
					setOfPredecessors = new ArrayList<ArrayList<State>>();

					for (int k = 0; k < setOfPredecessorsCopy.size(); k++) {
						for (int j = 0; j < predecessors.get(i).size(); j++) {

							// check if this element is already not present in
							// the
							// list once
							if (!setOfPredecessorsCopy.get(k).contains(
									predecessors.get(i).get(j))) {

								ArrayList<State> states5 = copyList1(setOfPredecessorsCopy
										.get(k));
								// System.out.println("initial size is "
								// + states5.size());
								states5.add(predecessors.get(i).get(j));
								setOfPredecessors.add(states5);
								// System.out.println("size becomes--- "
								// + setOfPredecessors.size() + " - > "
								// + setOfPredecessors.get(0).size()
								// + " & " + states5.size() + " -- "
								// + setOfPredecessorsCopy.get(0).size());
							}
						}
					}
				} else {
					// just add
					for (int k = 0; k < predecessors.get(i).size(); k++) {
						ArrayList<State> states5 = new ArrayList<State>();
						states5.add(predecessors.get(i).get(k));
						setOfPredecessors.add(states5);

						// System.out.println("size becomes--- "
						// + setOfPredecessors.size() + " - > "
						// + setOfPredecessors.get(k).size() + " & "
						// + states5.size());
					}

				}
				// System.out.println("size becomes " + setOfPredecessors.size()
				// + " - > " + setOfPredecessors.get(0).size());
			}
		}

		int total = 0;
		for (int i = 0; i < setOfPredecessors.size(); i++) {
			for (int k = 0; k < setOfPredecessors.get(i).size(); k++)
				total++;
		}
		// System.out.println("size ::::::::::: " + setOfPredecessors.size() +
		// " "
		// + total);

		return setOfPredecessors;
	}

	public static ArrayList<ArrayList<State>> copyList(
			ArrayList<ArrayList<State>> list) {
		ArrayList<ArrayList<State>> copy = new ArrayList<ArrayList<State>>();
		for (int i = 0; i < list.size(); i++) {
			ArrayList<State> states = new ArrayList<State>();
			for (int j = 0; j < list.get(i).size(); j++) {
				states.add((State) list.get(i).get(j));
			}
			// System.out.println("size of i " + i + " is " + states.size());
			copy.add(states);
		}
		return copy;
	}

	public static ArrayList<State> copyList1(ArrayList<State> list) {
		ArrayList<State> states = new ArrayList<State>();
		for (int j = 0; j < list.size(); j++) {
			states.add((State) list.get(j));
		}

		return states;
	}

	public ArrayList<ActionLayer> extractPath(
			ArrayList<StateLayer> stateLayers,
			ArrayList<ActionLayer> actionLayers) {
		return new PathTracker().trackPath(stateLayers, actionLayers);

	}

	public static void setProblemNumber(int problemNumber) {
		Planner.problemNumber = problemNumber;
	}

	public static int getProblemNumber() {
		return problemNumber;
	}

}
