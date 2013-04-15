package planning;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

//slide 11, 12, 13, 23
public class Planner {

	private static int problemNumber = 1;
	// private StateLayer currentStateLayer;
	private ArrayList<StateLayer> stateLayers = new ArrayList<StateLayer>();
	private ArrayList<ActionLayer> actionLayers = new ArrayList<ActionLayer>();

	Planner() {
		StateLayer initialStateLayer = new StateLayer();
		initialStateLayer.addState(ProblemConstant.initialState);
		stateLayers.add(initialStateLayer);
		// mark mut ex states in initial state
		initialStateLayer.markMutExStates();

	}

	public static void main(String[] args) {
		Planner planner = new Planner();
		int i = 0;
		// while (i < 2) {
		while (planner.stateLayers.size() == 1
				|| planner.stateLayers.get(planner.stateLayers.size() - 1)
						.getStates().size() != planner.stateLayers
						.get(planner.stateLayers.size() - 2).getStates().size()) {
			planner.getNextStates();
			i++;
		}
		
		ArrayList<ActionLayer> solutionPath = planner.extractPath(
				planner.stateLayers, planner.actionLayers);
		
		// CopyOfGraphMaker frame = new CopyOfGraphMaker(planner.stateLayers,
		// planner.actionLayers);
		GraphMaker frame = new GraphMaker(planner.stateLayers,
				planner.actionLayers, solutionPath);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setAlwaysOnTop(true);
		frame.setSize(2000, 2000);
		//frame.pack();
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
		// System.out.println("currentState " + currentState);

		// consider all null/unique actions separately
		for (int index_state = 0; index_state < currentStateLayer.getStates()
				.size(); index_state++) {
			State currentState = currentStateLayer.getStates().get(index_state);
			System.out.println("currentState " + currentState);

			// if null then they remain the same
			BaseActionClass effects = currentState.getPresentState();
			ArrayList<State> predecessorStates = new ArrayList<State>();
			predecessorStates.add(currentState);

			// check if they are present in the new layer
			ArrayList<State> successiveStates = newStateLayer
					.addNewEffects(effects);

			// System.out.println("sucessor size for action "
			// + ActionConstants.NULL.getQualifiedName() + " is "
			// + successiveStates.size());

			Action newAction = new Action(predecessorStates, successiveStates,
					ActionConstants.NULL, null);
			newActionLayer.addState(newAction);

			// TODO comment this block and added two previous lines
			// for (int i = 0; i < successiveStates.size(); i++) {
			// add action
			// Action newAction = new Action(predecessorStates,
			// successiveStates.get(i), ActionConstants.NULL,
			// variables);
			// newActionLayer.addState(newAction);
			// }
		}

		// see which pure actions can be taken from this state

		for (ActionConstants possibleActionConstants : ActionConstants.possibleActions) {
			boolean canBeApplied = false;
			ArrayList<State> predecessorStates = null;

			// if (possibleActionConstants.getPrecondition() != null) {
			predecessorStates = possibleActionConstants
					.getPreconditionsForAction(currentStateLayer.getStates());
			if (predecessorStates != null)
				canBeApplied = true;

			if (canBeApplied) {
				// apply the action
				System.out.println("applying the action "
						+ possibleActionConstants.getQualifiedName()
						+ " to state  ");

				// get the effect states
				BaseActionClass effects;
				VariableConstants[] variables = null;

				if (possibleActionConstants.getEffects() == null) {
					// if null then they remain the same
					effects = possibleActionConstants.getPrecondition();
				} else {
					effects = possibleActionConstants.getEffects();
					// System.out.println("effects " + effects);
				}
				// substitute vars in fopl
				if (possibleActionConstants.getVariables() != null) {
					// System.out.println("1");
					variables = possibleActionConstants.getVariables();
					// check if any are null and replace by real

					// variables = possibleActionConstants.getVariables();
					for (int i = 0; i < variables.length; i++) {
						if (variables[i] == null) {
							System.out.println("2 --- ");
						}
					}
				}

				// check if they are present in the new layer
				ArrayList<State> successiveStates = newStateLayer
						.addNewEffects(effects);

				// System.out.println("newStateLayer " + newStateLayer);

				// if nothing is specified in action constants then make
				// predecessor null
				if (possibleActionConstants.getPrecondition() == null) {
					System.out.println("!!!!!!no predecessor for "
							+ possibleActionConstants.getQualifiedName());
					predecessorStates = null;
				}

				// TODO uncomment for block and comment three previous lines
				// for (int i = 0; i < successiveStates.size(); i++) {
				// Action newAction = new Action(null,
				// successiveStates.get(i),
				// possibleActionConstants, variables);
				// newActionLayer.addState(newAction);
				// System.out.println("action " + newAction);
				// }

				Action newAction = new Action(predecessorStates,
						successiveStates, possibleActionConstants, variables);
				newActionLayer.addState(newAction);
				System.out.println("action " + newAction);

			} else {
				System.out.println("can't be applied-------------- "
						+ possibleActionConstants.getActionName());
			}
		}

		// }
		stateLayers.add(newStateLayer);
		actionLayers.add(newActionLayer);
		System.out.println("*************************");
		System.out.println("size of actions " + actionLayers.size());
		System.out.println("size of states " + stateLayers.size());

		// set mut ex states
		newStateLayer.markMutExStates();

		// set the mutex actions
		newActionLayer.markMutExActions(currentStateLayer, newStateLayer);

		// if they lead from opposite preconditions or effects then they are
		// opposites

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
