//package planning;
//
//import java.util.ArrayList;
//
//import javax.swing.JFrame;
//
//public class CopyOfPlanner {
//
//	private static int problemNumber = 1;
//	// private StateLayer currentStateLayer;
//	private ArrayList<StateLayer> stateLayers = new ArrayList<StateLayer>();
//	private ArrayList<ActionLayer> actionLayers = new ArrayList<ActionLayer>();
//
//	CopyOfPlanner() {
//		StateLayer currentStateLayer = new StateLayer();
//		currentStateLayer.addState(ProblemConstant.initialState);
//		stateLayers.add(currentStateLayer);
//	}
//
//	public static void main(String[] args) {
//		CopyOfPlanner planner = new CopyOfPlanner();
//		int i = 0;
//		while (i < 2) {
//			// while (planner.stateLayers.size() == 1
//			// || planner.stateLayers.get(planner.stateLayers.size() - 1)
//			// .getStates().size() != planner.stateLayers
//			// .get(planner.stateLayers.size() - 2).getStates().size()) {
//			planner.getNextStates();
//			i++;
//		}
//		GraphMaker frame = new GraphMaker(planner.stateLayers,
//				planner.actionLayers);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400, 320);
//		frame.setVisible(true);
//	}
//
//	public void getNextStates() {
//		// loop the states in the last state layers
//		StateLayer currentStateLayer = stateLayers.get(stateLayers.size() - 1);
//		StateLayer newStateLayer = new StateLayer();
//		ActionLayer newActionLayer = new ActionLayer();
//		for (int index_state = 0; index_state < currentStateLayer.getStates()
//				.size(); index_state++) {
//			State currentState = currentStateLayer.getStates().get(index_state);
//			System.out.println("currentState " + currentState);
//
//			// see which pure actions can be taken from this state
//
//			for (ActionConstants possibleActionConstants : ActionConstants.possibleActions) {
//				boolean canBeApplied = false;
//				if (possibleActionConstants.getPrecondition() != null) {
//					canBeApplied = possibleActionConstants.getPrecondition()
//							.canBeApplied(currentState.getPresentState());
//				} else {
//					System.out.println("can always be applied");
//					canBeApplied = true;
//				}
//				if (canBeApplied) {
//					// apply the action
//					System.out
//							.println("applying the action "
//									+ possibleActionConstants
//											.getQualifiedName()
//									+ " to state  "
//									+ currentState.getPresentState()
//											.getQualifiedName());
//
//					// get the effect states
//					BaseActionClass effects;
//					VariableConstants[] variables = null;
//
//					if (possibleActionConstants.getEffects() == null) {
//						// if null then they remain the same
//						effects = currentState.getPresentState();
//					} else {
//						effects = possibleActionConstants.getEffects();
//						// System.out.println("effects " + effects);
//					}
//					// substitute vars in fopl
//					if (currentState.getPresentState().getActionConstants()
//							.getVariables() != null) {
//						// System.out.println("1");
//						variables = currentState.getPresentState()
//								.getActionConstants().getVariables();
//						// check if any are null and replace by real
//
//						// variables = possibleActionConstants.getVariables();
//						for (int i = 0; i < variables.length; i++) {
//							if (variables[i] == null) {
//								// System.out.println("2 ");
//							}
//						}
//					}
//
//					// check if they are present in the new layer
//					ArrayList<State> successiveStates = newStateLayer
//							.addNewEffects(effects);
//
//					State predecessor = currentState;
//					// if nothing is specified in action constants then make
//					// predecessor null
//					if (possibleActionConstants.getPrecondition() == null
//							&& !possibleActionConstants.isUnique()) {
//						System.out.println("!!!!!!no predecessor for "
//								+ possibleActionConstants.getQualifiedName());
//						predecessor = null;
//					}
//
//					// System.out.println("newStateLayer " + newStateLayer);
//
//					for (int i = 0; i < successiveStates.size(); i++) {
//						Action newAction = new Action(predecessor,
//								successiveStates.get(i),
//								possibleActionConstants, variables);
//						newActionLayer.addState(newAction);
//						System.out.println("action " + newAction);
//						// System.out.println("effects are "
//						// + successiveStates.get(i).getPresentState());
//					}
//
//				}else{
//					System.out.println("can't be applied-------------- " + possibleActionConstants.getActionName());
//				}
//			}
//
//		}
//		stateLayers.add(newStateLayer);
//		actionLayers.add(newActionLayer);
//		System.out.println("*************************");
//		System.out.println("size of actions " + actionLayers.size());
//		System.out.println("size of states " + stateLayers.size());
//
//	}
//
//	public static void setProblemNumber(int problemNumber) {
//		CopyOfPlanner.problemNumber = problemNumber;
//	}
//
//	public static int getProblemNumber() {
//		return problemNumber;
//	}
//
//}
