package planning2;
//package planning;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import javax.swing.JFrame;
//
//import com.mxgraph.layout.mxParallelEdgeLayout;
//import com.mxgraph.swing.mxGraphComponent;
//import com.mxgraph.view.mxGraph;
//
//public class CopyOfGraphMaker extends JFrame {
//	public CopyOfGraphMaker() {
//		super("Hello, World!");
//
//		mxGraph graph = new mxGraph();
//
//		Object parent = graph.getDefaultParent();
//
//		new mxParallelEdgeLayout(graph).execute(parent);
//
//		graph.getModel().beginUpdate();
//		try {
//			Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80,
//					30);
//			Object v2 = graph.insertVertex(parent, null, "World!", 240, 150,
//					80, 30);
//			graph.insertEdge(parent, null, "Edge", v1, v2);
//		} finally {
//			graph.getModel().endUpdate();
//		}
//
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		getContentPane().add(graphComponent);
//	}
//
//	public String getNameOfNode(State state) {
//		return getNameOfNode(state.getPresentState());
//	}
//
//	public String getNameOfNode(ActionClass actionClass) {
//		String name = actionClass.getActionConstants().getActionName();
//		if (!actionClass.getComplementForms()[0])
//			name = "~" + name;
//		if (actionClass.getActionConstants().getVariables() != null) {
//			name = name + "(";
//			for (int i = 0; i < actionClass.getActionConstants().getVariables().length; i++) {
//
//				VariableConstants variable = actionClass.getActionConstants()
//						.getVariables()[i];
//				if (i != 0)
//					name = name + ",";
//				if (variable != null) {
//					name = name + variable.getSymbolName();
//				} else {
//					name = name + actionClass.getVariables()[i].getSymbolName();
//				}
//			}
//			name = name + ")";
//			// System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");
//		}
//		return name;
//	}
//
//	public String getNameOfEdge(ActionConstants actionConstant) {
//		String name = actionConstant.getActionName();
//
//		if (actionConstant.getVariables() != null) {
//			name = name + "(";
//			for (int i = 0; i < actionConstant.getVariables().length; i++) {
//
//				VariableConstants variable = actionConstant.getVariables()[i];
//				if (i != 0)
//					name = name + ",";
//				name = name + variable.getSymbolName();
//			}
//			name = name + ")";
//			// System.out.println("@@@@@@@@@@@ " + name + " @@@@@@@@@@@@@");
//		}
//		return name;
//	}
//
//	public CopyOfGraphMaker(ArrayList<StateLayer> stateLayers,
//			ArrayList<ActionLayer> actionLayers) {
//		super("Hello, World!");
//
//		mxGraph graph = new mxGraph();
//		Object parent = graph.getDefaultParent();
//
//		graph.getModel().beginUpdate();
//		try {
//
//			HashMap<String, GraphVertex> previousLayerNodes = new HashMap<String, GraphVertex>();
//			HashMap<String, GraphVertex> currentLayerNodes = new HashMap<String, GraphVertex>();
//			HashMap<String, GraphVertex> currentActionNodes = new HashMap<String, GraphVertex>();
//			// add states
//			int i = 0;
//			while (i < stateLayers.size()) {
//				
//				System.out.println("+++++++++++++++++++++++++++ " + i);
//				StateLayer currentLayer = stateLayers.get(i);
//
//				int j = 0;
//				while (j < currentLayer.getStates().size()) {
//
//					String name = getNameOfNode(currentLayer.getStates().get(j)
//							.getPresentState());
//					// System.out
//					// .println("adding node " + name + " to level " + i);
//					int left = (i + 1) * 400 - 100;
//					// int left = (j + 1) * 150;
//					int top = 200 + (1000 * j) / StateLayer.maxNoStates;
//					Object node = graph.insertVertex(parent, null, name, left,
//							top, 80, 30);
//					currentLayerNodes.put(name, new GraphVertex(node, top,
//							left, null));
//					j++;
//
//				}
//
//				j = 0;
//				ArrayList<State> alreadyShown = new ArrayList<State>();
//				// show all mutex states in one layer
//				while (j < currentLayer.getStates().size()) {
//					// if mut ex exists
//					if (currentLayer.getStates().get(j).getMutExState() != null
//							&& !alreadyShown.contains(currentLayer.getStates()
//									.get(j).getMutExState())) {
//						addMutExEdges(
//								true,
//								graph,
//								parent,
//								currentLayerNodes.get(
//										getNameOfNode(currentLayer.getStates()
//												.get(j))).getVertex(),
//								currentLayerNodes.get(
//										getNameOfNode(currentLayer.getStates()
//												.get(j).getMutExState()))
//										.getVertex());
//						alreadyShown.add(currentLayer.getStates().get(j));
//						System.out
//								.println("adding mut ex "
//										+ getNameOfNode(currentLayer
//												.getStates().get(j))
//										+ " & "
//										+ getNameOfNode(currentLayer
//												.getStates().get(j)
//												.getMutExState()));
//					}
//					j++;
//				}
//
//				// System.out.println("current nodes $$$$$$$$$$ "
//				// + currentLayerNodes.size());
//
//				// add actions
//				if (i != 0) {
//					// System.out.println("---- " + i);
//					ActionLayer currentActionLayer = actionLayers.get(i - 1);
//
//					int k = 0;
//					// System.out.println("+++++++++++ "
//					// + currentLayerNodes.size() + " "
//					// + previousLayerNodes.size());
//					while (k < currentActionLayer.getActions().size()) {
//
//						String name = getNameOfEdge(currentActionLayer
//								.getActions().get(k).getAction());
//
//						// System.out.println("adding action " + name);
//
//						GraphVertex vertex2 = currentLayerNodes
//								.get(getNameOfNode(currentActionLayer
//										.getActions().get(k)
//										.getSuccessiveState()));
//
//						Object action = addActionToGraph(graph, parent,
//								currentActionNodes, previousLayerNodes, name,
//								vertex2, currentActionLayer.getActions().get(k)
//										.getPreviousStates(), i, j,
//								currentLayer);
//
//						addActionToGraph(graph, parent, action,
//								vertex2.getVertex());
//
//						k++;
//					}
//
//					// add mutex actions
//					for (int l = 0; l < currentActionLayer.getActions().size(); l++) {
//						// check if mutex exists
//						if (currentActionLayer.getActions().get(l)
//								.getMutExActions().size() != 0) {
//							Object action1 = currentActionNodes.get(
//									getNameOfEdge(currentActionLayer
//											.getActions().get(l).getAction()))
//									.getVertex();
//
//							// for all display edge
//							for (int p = 0; p < currentActionLayer.getActions()
//									.get(l).getMutExActions().size(); p++) {
//								Object action2 = currentActionNodes.get(
//										getNameOfEdge(currentActionLayer
//												.getActions().get(l)
//												.getMutExActions().get(p)
//												.getAction())).getVertex();
//								addMutExEdges(false, graph, parent, action1,
//										action2);
//								System.out.println("adding mut ex "
//										+ getNameOfEdge(currentActionLayer
//												.getActions().get(l)
//												.getAction())
//										+ " & "
//										+ getNameOfEdge(currentActionLayer
//												.getActions().get(l)
//												.getMutExActions().get(p)
//												.getAction()));
//							}
//						}
//					}
//
//				}
//
//				previousLayerNodes = currentLayerNodes;
//				currentLayerNodes = new HashMap<String, GraphVertex>();
//				currentActionNodes = new HashMap<String, GraphVertex>();
//				i++;
//
//			}
//
//		} finally {
//			graph.getModel().endUpdate();
//		}
//
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		getContentPane().add(graphComponent);
//	}
//
//	public boolean compareArrayLists(ArrayList<State> predecessorStates,
//			ArrayList<State> predecessorStates2) {
//		if (predecessorStates == predecessorStates2)
//			return true;
//		return predecessorStates2.equals(predecessorStates);
//	}
//
//	public Object addActionToGraph(mxGraph graph, Object parent,
//			HashMap<String, GraphVertex> currentActionNodes,
//			HashMap<String, GraphVertex> previousLayerNodes, String name,
//			GraphVertex vertex2, ArrayList<State> predecessorStates, int i1,
//			int j1, StateLayer stateLayer) {
//		Object action;
//
//		// check if action exists for group of predecessors
//		if (currentActionNodes.containsKey(name)
//				&& compareArrayLists(predecessorStates,
//						currentActionNodes.get(name).getPredecessorStates())) {
//			// && currentActionNodes.get(name).getPredecessors()
//			// .equals(vertex1)) {
//			action = currentActionNodes.get(name).getVertex();
//			// System.out.println("contains " +
//			// name);
//		} else {
//			// create action
//			// get first left value
//			int left = 0;
//			// get average top value
//			int top = 0;
//			if (predecessorStates == null) {
//				left = (i1 + 1) * 400 - 300;
//				// int left = (j + 1) * 150;
//				top = (1000 * j1) / StateLayer.maxNoStates;
//				// vertex1 = GraphVertex.NullNode;
//			} else {
//				left = previousLayerNodes.get(
//						getNameOfNode(predecessorStates.get(0)
//								.getPresentState())).getLeft() + 200;
//				// add random value to left
//				left += (int) (Math.random() * 200 - 100);
//
//				for (int i = 0; i < predecessorStates.size(); i++)
//					top += previousLayerNodes.get(
//							getNameOfNode(predecessorStates.get(i)
//									.getPresentState())).getTop();
//				top /= predecessorStates.size();
//
//				// add random value to top
//
//				top += (int) (Math.random() * 200 - 100);
//			}
//
//			action = graph.insertVertex(parent, null, name, left, top, 80, 30,
//					"fillColor=orange");
//
//			currentActionNodes.put(name, new GraphVertex(action, left, top,
//					predecessorStates));
//
//			if (predecessorStates != null) {
//				// draw edge for all predecessor
//				for (int i = 0; i < predecessorStates.size(); i++) {
//					addActionToGraph(
//							graph,
//							parent,
//							previousLayerNodes.get(
//									getNameOfNode(predecessorStates.get(i)
//											.getPresentState())).getVertex(),
//							action);
//				}
//			}
//		}
//
//		return action;
//	}
//
//	public void addActionToGraph(mxGraph graph, Object parent, Object vertex1,
//			Object vertex2) {
//
//		graph.insertEdge(parent, null, "->", vertex1, vertex2);
//	}
//
//	public void addMutExEdges(boolean state, mxGraph graph, Object parent,
//			Object vertex1, Object vertex2) {
//		if (state)
//			graph.insertEdge(parent, null, "-", vertex1, vertex2,
//					"dashed=1;arcsize=50;rounded=true;strokeColor=#00FFFF;edgeStyle=loopEdgeStyle;"
//			/* "dashed=1;arcsize=50;rounded=true;elbow=elbow_vertical;strokeColor=#00FFFF;edgeStyle=elbowEdgeStyle;shape=curve;" */
//			/* "rounded=1;edgeStyle=orthogonalEdgeStyle;" */);
//		else
//			graph.insertEdge(parent, null, "-", vertex1, vertex2,
//					"dashed=1;arcsize=50;rounded=true;strokeColor=#FF00FF;edgeStyle=sideToSideEdgeStyle;");
//	}
//
//	public static void main(String[] args) {
//		CopyOfGraphMaker frame = new CopyOfGraphMaker();
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(400, 320);
//		frame.setVisible(true);
//	}
//}
