package planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

public class GraphMaker extends JFrame {

	private final int LEFT_VALUE = -100;

	private final int TOP_VALUE = 150;

	private final int VERTICAL_DISTANCE_NODES = 60;

	private final int HORIZONTAL_DISTANCE_NODES = 300;

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

				System.out.println("+++++++++++++++++++++++++++ " + i);
				StateLayer currentLayer = stateLayers.get(i);

				int j = 0;
				while (j < currentLayer.getStates().size()) {

					String name = currentLayer.getStates().get(j)
							.getPresentState().getQualifiedName();
					// System.out
					// .println("adding node " + name + " to level " + i);
					int left = LEFT_VALUE + (i + 1) * HORIZONTAL_DISTANCE_NODES;
					// int left = (j + 1) * 150;
					int top = TOP_VALUE + (VERTICAL_DISTANCE_NODES * j);
					// int left = (i + 1) * 400 - 100;
					// // int left = (j + 1) * 150;
					// int top = 200 + (1000 * j) / StateLayer.maxNoStates;
					Object node = graph.insertVertex(parent, null, name, left,
							top, 80, 30);
					currentLayerNodes.put(currentLayer.getStates().get(j)
							.getPresentState().hashCode(), new GraphVertex(
							node, top, left, null));
					System.out.println("adding "
							+ j
							+ " hashcode "
							+ currentLayer.getStates().get(j).getPresentState()
									.hashCode() + " " + name);
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
						System.out
								.println("adding mut ex "
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

						System.out.println("adding action "
								+ currentActionLayer.getActions().hashCode());

						// TODO added outer for loop
						for (int o = 0; o < currentActionLayer.getActions()
								.get(k).getSuccessiveStates().size(); o++) {

							GraphVertex vertex2 = currentLayerNodes
									.get(currentActionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());

							System.out.println("looking up for hashcode "
									+ currentActionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());

							Object action = addActionToGraph(graph, parent,
									currentActionNodes, previousLayerNodes,
									vertex2, currentActionLayer.getActions()
											.get(k).getPreviousStates(), i, j,
									currentLayer, currentActionLayer
											.getActions().get(k).getAction()
											.hashCode(), currentActionLayer
											.getActions().get(k), false,
									currentActionLayer.getActions().size(), k);

							addActionEdgeToGraph(graph, parent, action,
									vertex2.getVertex());
						}
						k++;
					}

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
								System.out.println("adding mut ex "
										+ getEdgeIndentifier(currentActionLayer
												.getActions().get(l)) + " & "
										+ getEdgeIndentifier(mutexAction));
							}
						}
					}
					// add solution path
					ActionLayer solutionLayer = solutionPath.get(i - 1);
					k = 0;
					System.out.println("+++++++++++ solutionLayer size "
							+ solutionLayer.getActions().size());
					while (k < solutionLayer.getActions().size()) {

						System.out.println("adding solution "
								+ solutionLayer.getActions().get(k).getAction()
										.getQualifiedName()
								+ solutionLayer.getActions().get(k).hashCode());

						// TODO added outer for loop
						for (int o = 0; o < solutionLayer.getActions().get(k)
								.getSuccessiveStates().size(); o++) {

							GraphVertex vertex2 = currentLayerNodes
									.get(solutionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());

							System.out.println("looking up for hashcode "
									+ solutionLayer.getActions().get(k)
											.getSuccessiveStates().get(o)
											.getPresentState().hashCode());

							Object action = addActionToGraph(graph, parent,
									currentActionNodes, previousLayerNodes,
									vertex2, solutionLayer.getActions().get(k)
											.getPreviousStates(), i, j,
									currentLayer, solutionLayer.getActions()
											.get(k).getAction().hashCode(),
									solutionLayer.getActions().get(k), true,
									solutionLayer.getActions().size(), k);

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
			graph.getModel().endUpdate();
		}

		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}

	public String getNameOfNode(State state) {
		return state.getPresentState().getQualifiedName();
	}

	// public String getEdgeIndentifier(Action actionObject) {
	//
	// // use name if not unique
	// if (actionObject.getAction().isUnique())
	// return actionObject.getAction().getQualifiedName()
	// + actionObject.hashCode();
	//
	// // else combine hash code
	// else
	// return actionObject.getAction().getQualifiedName();
	// }

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

	public boolean compareArrayLists(ArrayList<State> predecessorStates,
			ArrayList<State> predecessorStates2) {
		if (predecessorStates == predecessorStates2)
			return true;
		return predecessorStates2.equals(predecessorStates);
	}

	public GraphVertex getActionIfPresent(
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

				// System.out.println("^^^^^^^^^^^ some considerate action "
				// + actionName + " and " + name + ";");

				// check if predessors are same

				if (currentActionNodes.get(actionName).getPredecessorStates() != null
						&& currentActionNodes.get(actionName)
								.getPredecessorStates()
								.equals(actionObject.getPreviousStates())) {
					result = currentActionNodes.get(actionName);
					// System.out.println("same predecessor~!!!!!!!!!!!!!!");
				} else if (currentActionNodes.get(actionName)
						.getPredecessorStates() == null
						&& actionObject.getPreviousStates() == null) {
					result = currentActionNodes.get(actionName);
					// System.out.println("something wrong~~~~~~~~~~~~~~~~~~");
				}
			}
		}

		return result;
	}

	public Object addActionToGraph(mxGraph graph, Object parent,
			HashMap<String, GraphVertex> currentActionNodes,
			HashMap<Integer, GraphVertex> previousLayerNodes,
			GraphVertex vertex2, ArrayList<State> predecessorStates, int i1,
			int j1, StateLayer stateLayer, int hashCode, Action actionObject,
			boolean solution, int size, int index) {
		Object action;

		System.out.println("ADDING ACTION TO GRAPH");

		// TODO extract actual name

		GraphVertex alreadyPresentVertex = getActionIfPresent(
				currentActionNodes, actionObject);

		// check if action exists for group of predecessors
		// if (currentActionNodes.containsKey(name)
		// && compareArrayLists(predecessorStates,
		// currentActionNodes.get(name).getPredecessorStates())) {
		if (alreadyPresentVertex != null) {

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

			left = (2 * i1 + 1) / 2 * HORIZONTAL_DISTANCE_NODES + LEFT_VALUE
					+ HORIZONTAL_DISTANCE_NODES / 2;
			top = TOTAL_VERTICAL_DISTANCE * (index + 1) / size;
			left += (int) (Math.random() * HORIZONTAL_DISTANCE_NODES / 3 - HORIZONTAL_DISTANCE_NODES / 6);

			String name = getEdgeIndentifier(actionObject);

			String displayName = actionObject.getAction().getQualifiedName();

			action = graph.insertVertex(parent, null, displayName, left, top,
					80, 30, "fillColor=orange");

			currentActionNodes.put(name, new GraphVertex(action, left, top,
					predecessorStates));

			System.out.println("adding action " + (name + hashCode) + " for "
					+ name);

		}
		if (predecessorStates != null) {
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

	public void addActionEdgeToGraph(mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {

		graph.insertEdge(parent, null, "->", vertex1, vertex2);
	}

	public void addSolutionEdgeToGraph(mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {

		graph.insertEdge(parent, null, "->", vertex1, vertex2,
				"dashed=1;arcsize=50;rounded=true;strokeColor=#000000;");
	}

	public void addMutExEdges(boolean state, mxGraph graph, Object parent,
			Object vertex1, Object vertex2) {
		if (state)
			graph.insertEdge(parent, null, "-", vertex1, vertex2,
					"dashed=1;arcsize=50;rounded=true;strokeColor=#00FFFF;edgeStyle=loopEdgeStyle;"
			/* "dashed=1;arcsize=50;rounded=true;elbow=elbow_vertical;strokeColor=#00FFFF;edgeStyle=elbowEdgeStyle;shape=curve;" */
			/* "rounded=1;edgeStyle=orthogonalEdgeStyle;" */);
		else
			graph.insertEdge(
					parent,
					null,
					"-",
					vertex1,
					vertex2,
					"dashed=1;arcsize=50;rounded=true;strokeColor=#FF00FF;edgeStyle=sideToSideEdgeStyle;");
	}

}
