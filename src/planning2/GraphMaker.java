package planning2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class GraphMaker extends JFrame {

	private final int LEFT_VALUE = -400;

	private final int TOP_VALUE = 20; // 150

	private final int VERTICAL_DISTANCE_NODES = 40; // 150

	private final int HORIZONTAL_DISTANCE_NODES = 400;

	private int TOTAL_VERTICAL_DISTANCE;

	public GraphMaker(ArrayList<StateLayer> stateLayers,
			ArrayList<ActionLayer> actionLayers,
			ArrayList<ActionLayer> solutionPath) {

		super("Hello, World!");

		TOTAL_VERTICAL_DISTANCE = StateLayer.maxNoStates
				* VERTICAL_DISTANCE_NODES;

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		graph.getModel().beginUpdate();
		try {

			// using hashCode as code because the name of the state is
			// vulnerable because it may not contain sufficient information to
			// differentiate states in one layer i.e. complement form and
			// variables
			HashMap<Integer, GraphVertex> previousLayerNodes = new HashMap<Integer, GraphVertex>();
			HashMap<Integer, GraphVertex> currentLayerNodes = new HashMap<Integer, GraphVertex>();
			HashMap<String, GraphVertex> currentActionNodes = new HashMap<String, GraphVertex>();
			// add states
			int i = 0;
			while (i < stateLayers.size()) {

				if (Planner.TRACE)
					System.out.println("+++++++++++++++++++++++++++ " + i);
				StateLayer currentLayer = stateLayers.get(i);

				int j = 0;
				while (j < currentLayer.getStates().size()) {

					String name = currentLayer.getStates().get(j)
							.getPresentState().getQualifiedName();
					int left = LEFT_VALUE + (i + 1) * HORIZONTAL_DISTANCE_NODES;
					int top = TOP_VALUE + (VERTICAL_DISTANCE_NODES * j);
					Object node = graph.insertVertex(parent, null, name, left,
							top, 80, 30);
					currentLayerNodes.put(currentLayer.getStates().get(j)
							.getPresentState().hashCode(), new GraphVertex(
							node, top, left, null));
					if (Planner.TRACE)
						System.out.println("adding node "
								+ j
								+ " hashcode "
								+ currentLayer.getStates().get(j)
										.getPresentState().hashCode() + " "
								+ name);
					j++;
				}

				j = 0;
				ArrayList<State> alreadyShown = new ArrayList<State>();
				// show all mutex states in one layer
				while (j < currentLayer.getStates().size()) {
					// if mut ex exists
					if (currentLayer.getStates().get(j).getMutExState() != null
							&& !alreadyShown.contains(currentLayer.getStates()
									.get(j).getMutExState())) {
						addMutExEdges(
								true,
								graph,
								parent,
								currentLayerNodes.get(
										currentLayer.getStates().get(j)
												.getPresentState().hashCode())
										.getVertex(),
								currentLayerNodes.get(
										currentLayer.getStates().get(j)
												.getMutExState()
												.getPresentState().hashCode())
										.getVertex());
						alreadyShown.add(currentLayer.getStates().get(j));
						if (Planner.TRACE)
							System.out
									.println("adding mut ex states :::: to graph ::::  "
											+ getNameOfNode(currentLayer
													.getStates().get(j))
											+ " & "
											+ getNameOfNode(currentLayer
													.getStates().get(j)
													.getMutExState()));
					}
					j++;
				}

				// System.out.println("current nodes $$$$$$$$$$ "
				// + currentLayerNodes.size());

				// add actions
				if (i != 0) {
					// System.out.println("---- " + i);
					ActionLayer currentActionLayer = actionLayers.get(i - 1);

					int k = 0;
					// System.out.println("+++++++++++ "
					// + currentLayerNodes.size() + " "
					// + previousLayerNodes.size());
					while (k < currentActionLayer.getActions().size()) {

						if (Planner.TRACE)
							System.out.println("adding action "
									+ currentActionLayer.getActions().get(k)
											.getQualifiedName());

						// TODO added outer for loop
						for (int o = 0; o < currentActionLayer.getActions()
								.get(k).getSuccessiveStates().size(); o++) {

							GraphVertex vertex2 = currentLayerNodes
									.get(currentActionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());

							if (Planner.TRACE)
								System.out.println("looking up for hashcode "
										+ currentActionLayer.getActions()
												.get(k).getSuccessiveStates()
												.get(o).getPresentState()
												.hashCode());

							Object action = addActionToGraph(graph, parent,
									currentActionNodes, previousLayerNodes,
									vertex2, currentActionLayer.getActions()
											.get(k).getPreviousStates(), i, j,
									currentLayer, currentActionLayer
											.getActions().get(k).getAction()
											.hashCode(), currentActionLayer
											.getActions().get(k), false,
									currentActionLayer.getActions().size(), k,
									true);

							addActionEdgeToGraph(graph, parent, action,
									vertex2.getVertex());
						}
						k++;
					}

					if (Planner.TRACE)
						System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");

					// add mutex actions
					for (int l = 0; l < currentActionLayer.getActions().size(); l++) {
						// check if mutex exists
						if (currentActionLayer.getActions().get(l)
								.getMutExActions().size() != 0) {
							Object action1 = currentActionNodes.get(
									getEdgeIndentifier(currentActionLayer
											.getActions().get(l))).getVertex();

							// for all display edge
							Iterator<Action> iterator = currentActionLayer
									.getActions().get(l).getMutExActions()
									.iterator();
							while (iterator.hasNext()) {
								Action mutexAction = iterator.next();

								Object action2 = currentActionNodes.get(
										getEdgeIndentifier(mutexAction))
										.getVertex();
								addMutExEdges(false, graph, parent, action1,
										action2);
								if (Planner.TRACE)
									System.out
											.println("adding mut ex action in graph :::: "
													+ getEdgeIndentifier(currentActionLayer
															.getActions()
															.get(l))
													+ " & "
													+ getEdgeIndentifier(mutexAction));
							}
						}
					}
					// add solution path
					ActionLayer solutionLayer = solutionPath.get(i - 1);
					k = 0;
					if (Planner.TRACE)
						System.out.println("+++++++++++ solutionLayer size "
								+ solutionLayer.getActions().size());
					while (k < solutionLayer.getActions().size()) {
						if (Planner.TRACE)
							System.out.println("adding solution "
									+ solutionLayer.getActions().get(k)
											.getAction().getQualifiedName()
									+ solutionLayer.getActions().get(k)
											.hashCode());

						// TODO added outer for loop
						for (int o = 0; o < solutionLayer.getActions().get(k)
								.getSuccessiveStates().size(); o++) {

							GraphVertex vertex2 = currentLayerNodes
									.get(solutionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());
							if (Planner.TRACE)
								System.out.println("looking up for hashcode "
										+ solutionLayer.getActions().get(k)
												.getSuccessiveStates().get(o)
												.getPresentState().hashCode());
							if (Planner.TRACE)
								System.out.println("----adding solution path "
										+ solutionLayer.getActions().get(k)
												.getPreviousStates().get(0)
												.getPresentState()
												.getQualifiedName()
										+ " ,  "
										+ solutionLayer.getActions().get(k)
												.getQualifiedName());

							Object action = addActionToGraph(graph, parent,
									currentActionNodes, previousLayerNodes,
									vertex2, solutionLayer.getActions().get(k)
											.getPreviousStates(), i, j,
									currentLayer, solutionLayer.getActions()
											.get(k).getAction().hashCode(),
									solutionLayer.getActions().get(k), true,
									solutionLayer.getActions().size(), k, false);

							addSolutionEdgeToGraph(graph, parent, action,
									vertex2.getVertex());
						}
						k++;
					}
				}

				previousLayerNodes = currentLayerNodes;
				currentLayerNodes = new HashMap<Integer, GraphVertex>();
				currentActionNodes = new HashMap<String, GraphVertex>();
				i++;
			}
		} finally {
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	private String getNameOfNode(State state) {
		return state.getPresentState().getQualifiedName();
	}

	private String getEdgeIndentifier(Action actionObject) {

		// use name if not unique
		if (actionObject.getAction().equals(ActionConstants.NULL))
			return actionObject.getQualifiedName()
					+ actionObject.getPreviousStates().get(0).getPresentState()
							.getQualifiedName();

		// else combine hash code
		else
			return actionObject.getQualifiedName();
	}

	private GraphVertex getActionIfPresent(
			HashMap<String, GraphVertex> currentActionNodes, Action actionObject) {

		// get name of action

		String name = getEdgeIndentifier(actionObject);

		// check whether there is an element in arrayList whose name is the
		// same, hashCode may be different, but predecessors must be the same

		GraphVertex result = null;
		Iterator<String> keysetIterator = currentActionNodes.keySet()
				.iterator();
		while (keysetIterator.hasNext()) {
			String actionName = keysetIterator.next();

			if (actionName.startsWith(name)) {
				if (Planner.TRACE)
					System.out.println("^^^^^^^^^^^ some considerate action "
							+ actionName + " and " + name + ";");
				// check if predessors are same
				if (currentActionNodes.get(actionName).getPredecessorStates() != null
						&& currentActionNodes.get(actionName)
								.getPredecessorStates()
								.equals(actionObject.getPreviousStates())) {
					result = currentActionNodes.get(actionName);
					// System.out.println("same predecessor~!!!!!!!!!!!!!!");
				} else {
					// System.out.println("something wrong~~~~~~~~~~~~~~~~~~");
				}
			}
		}

		return result;
	}

	private Object addActionToGraph(mxGraph graph, Object parent,
			HashMap<String, GraphVertex> currentActionNodes,
			HashMap<Integer, GraphVertex> previousLayerNodes,
			GraphVertex vertex2, ArrayList<State> predecessorStates, int i1,
			int j1, StateLayer stateLayer, int hashCode, Action actionObject,
			boolean solution, int size, int index, boolean isAddIfNotPresent) {
		Object action;

		if (Planner.TRACE)
			System.out.println("ADDING ACTION TO GRAPH");

		// TODO extract actual name

		GraphVertex alreadyPresentVertex = getActionIfPresent(
				currentActionNodes, actionObject);

		if (alreadyPresentVertex == null && !isAddIfNotPresent) {
			return null;
		}

		// check if action exists for group of predecessors
		// if (currentActionNodes.containsKey(name)
		// && compareArrayLists(predecessorStates,
		// currentActionNodes.get(name).getPredecessorStates())) {
		if (alreadyPresentVertex != null) {

			if (Planner.TRACE)
				System.out.println("already present");
			// && currentActionNodes.get(name).getPredecessors()
			// .equals(vertex1)) {
			action = alreadyPresentVertex.getVertex();
			// action = currentActionNodes.get(name).getVertex();
			// System.out.println("contains " +
			// name);
		} else {
			// create action
			// get first left value
			int left = 0;
			// get average top value
			int top = 0;
			if (predecessorStates == null) {
				left = (i1 + 1) * 400 - 300;
				// int left = (j + 1) * 150;
				top = (1000 * j1) / StateLayer.maxNoStates;
				// vertex1 = GraphVertex.NullNode;
			} else {
				left = previousLayerNodes.get(
						predecessorStates.get(0).getPresentState().hashCode())
						.getLeft() + 200;
				// add random value to left
				// left += (int) (Math.random() * 200 - 100);
				left += (int) (Math.random() * HORIZONTAL_DISTANCE_NODES / 2 - HORIZONTAL_DISTANCE_NODES / 4);

				for (int i = 0; i < predecessorStates.size(); i++)
					top += previousLayerNodes.get(
							predecessorStates.get(i).getPresentState()
									.hashCode()).getTop();
				top /= predecessorStates.size();

				// add random value to top
				// top += (int) (Math.random() * 200 - 100);
				top += (int) (Math.random() * VERTICAL_DISTANCE_NODES / 2 - VERTICAL_DISTANCE_NODES / 4);
			}

			// left = (i1 + 1) * 400 - 300;
			left = (i1 + 1) * HORIZONTAL_DISTANCE_NODES + LEFT_VALUE * 3 / 2;
			top = TOTAL_VERTICAL_DISTANCE * (index + 1) / size;
			left += (int) (Math.random() * HORIZONTAL_DISTANCE_NODES / 2 - HORIZONTAL_DISTANCE_NODES / 4);

			String name = getEdgeIndentifier(actionObject);

			String displayName = actionObject.getQualifiedName();

			action = graph.insertVertex(parent, null, displayName, left, top,
					80, 30, "fillColor=orange");

			currentActionNodes.put(name, new GraphVertex(action, left, top,
					predecessorStates));

			if (Planner.TRACE)
				System.out.println("adding action " + (name + hashCode)
						+ " for " + name);
		}
		if (predecessorStates != null) {
			// System.out.println("predecssors not null");
			// draw edge for all predecessor
			for (int i = 0; i < predecessorStates.size(); i++) {
				if (solution)
					addSolutionEdgeToGraph(
							graph,
							parent,
							previousLayerNodes.get(
									predecessorStates.get(i).getPresentState()
											.hashCode()).getVertex(), action);
				else
					addActionEdgeToGraph(
							graph,
							parent,
							previousLayerNodes.get(
									predecessorStates.get(i).getPresentState()
											.hashCode()).getVertex(), action);
			}
		}
		return action;
	}

	private void addActionEdgeToGraph(mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {

		graph.insertEdge(parent, null, "->", vertex1, vertex2);
	}

	private void addSolutionEdgeToGraph(mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {

		graph.insertEdge(parent, null, "->", vertex1, vertex2,
				"dashed=1;arcsize=50;rounded=true;strokeColor=#000000;");
	}

	private void addMutExEdges(boolean state, mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {
		if (state) {
			graph.insertEdge(parent, null, "-", vertex1, vertex2,
					"dashed=1;arcsize=50;rounded=true;strokeColor=#00FFFF;edgeStyle="
							+ mxConstants.EDGESTYLE_LOOP + ";");
			/* "dashed=1;arcsize=50;rounded=true;elbow=elbow_vertical;strokeColor=#00FFFF;edgeStyle=elbowEdgeStyle;shape=curve;" */
			/* "rounded=1;edgeStyle=orthogonalEdgeStyle;" */
		} else
			graph.insertEdge(
					parent,
					null,
					"-",
					vertex1,
					vertex2,
					"dashed=1;arcsize=50;rounded=true;strokeColor=#FF00FF;edgeStyle=sideToSideEdgeStyle;");
	}

}
