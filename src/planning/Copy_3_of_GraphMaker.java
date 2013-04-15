//package planning;
//
//import java.awt.BasicStroke;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Paint;
//import java.awt.Stroke;
//import java.awt.geom.Point2D;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//
//import javax.imageio.ImageIO;
//import javax.swing.JFrame;
//
//import org.apache.commons.collections15.Transformer;
//
//
//import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
//import edu.uci.ics.jung.algorithms.layout.CircleLayout;
//import edu.uci.ics.jung.algorithms.layout.DAGLayout;
//import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
//import edu.uci.ics.jung.algorithms.layout.KKLayout;
//import edu.uci.ics.jung.algorithms.layout.Layout;
//import edu.uci.ics.jung.algorithms.layout.SpringLayout;
//import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
//import edu.uci.ics.jung.algorithms.layout.TreeLayout;
//import edu.uci.ics.jung.graph.DelegateTree;
//import edu.uci.ics.jung.graph.DirectedGraph;
//import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
//import edu.uci.ics.jung.graph.Forest;
//import edu.uci.ics.jung.graph.Graph;
//import edu.uci.ics.jung.graph.SparseMultigraph;
//import edu.uci.ics.jung.graph.util.EdgeType;
//import edu.uci.ics.jung.visualization.BasicVisualizationServer;
//import edu.uci.ics.jung.visualization.VisualizationImageServer;
//import edu.uci.ics.jung.visualization.decorators.EdgeShape;
//import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
//import edu.uci.ics.jung.visualization.renderers.Renderer;
//import edu.uci.ics.jung.visualization.renderers.Renderer.Edge;
//import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
//
//public class GraphMaker extends JFrame {
//
//	public static void main(String[] args) {
//		// Graph<V, E> where V is the type of the vertices
//		// and E is the type of the edges
//		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
//		// Add some vertices. From above we defined these to be type Integer.
//		g.addVertex((Integer) 1);
//		g.addVertex((Integer) 2);
//		g.addVertex((Integer) 3);
//		// Add some edges. From above we defined these to be of type String
//		// Note that the default is for undirected edges.
//		g.addEdge("Edge-A", 1, 2, EdgeType.DIRECTED); // Note that Java 1.5
//														// auto-boxes primitives
//		g.addEdge("Edge-B", 2, 3, EdgeType.DIRECTED);
//		// Let's see what we have. Note the nice output from the
//		// SparseMultigraph<V,E> toString() method
//		System.out.println("The graph g = " + g.toString());
//		// Layout<V, E>, BasicVisualizationServer<V,E>
//		Layout<Integer, String> layout = new CircleLayout(g);
//		layout.setSize(new Dimension(300, 300));
//		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(
//				layout);
//		vv.setPreferredSize(new Dimension(350, 350));
//		// Setup up a new vertex to paint transformer...
//		Transformer<Integer, Paint> vertexPaint = new Transformer<Integer, Paint>() {
//			public Paint transform(Integer i) {
//				System.out.println(" i = " + i);
//				return Color.GREEN;
//			}
//		};
//		// Set up a new stroke Transformer for the edges
//		float dash[] = { 10.0f };
//		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
//				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
//		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
//			public Stroke transform(String s) {
//				return edgeStroke;
//			}
//		};
//		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
//
//		JFrame frame = new JFrame("Simple Graph View 2");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().add(vv);
//		frame.pack();
//		frame.setVisible(true);
//
//	}
//
//	public String getNameOfNode(State state) {
//		return state.getPresentState().getQualifiedName();
//	}
//
//	public String getEdgeIndentifier(Action actionObject) {
//
//		// use name if not unique
//		if (actionObject.getAction().isUnique())
//			return actionObject.getAction().getQualifiedName()
//					+ actionObject.hashCode();
//
//		// else combine hash code
//		else
//			return actionObject.getAction().getQualifiedName();
//	}
//
//	public GraphMaker(ArrayList<StateLayer> stateLayers,
//			ArrayList<ActionLayer> actionLayers,
//			ArrayList<ActionLayer> solutionPath) {
//
//		// Graph<V, E> where V is the type of the vertices
//		// and E is the type of the edges
//		Graph<String, String> graph = new SparseMultigraph<String, String>();
//		//DirectedGraph<String, String> graph = new DirectedSparseMultigraph<String, String>();
//		
//		//DelegateTree tree = new DelegateTree<String, String>(graph);
//		
//		// Forest<String, String> forest = new DelegateT
//		// Layout<V, E>, BasicVisualizationServer<V,E>
//		//Layout<String, String> layout = new ISOMLayout<String, String>(graph);
//		Layout<String, String> layout = new DAGLayout<String, String>(graph);
//		//Layout<String, String> layout = new SpringLayout<String, String>(graph);
//		//Layout<String, String> layout = new SpringLayout2<String, String>(graph);
//		//Layout<String, String> layout = new KKLayout<String, String>(graph);
//		//Layout<String, String> layout  = new TreeLayout<String, String>(tree);
//		layout.setSize(new Dimension(900, 900));
//		BasicVisualizationServer<String, String> vv = new BasicVisualizationServer<String, String>(
//				layout);
//		vv.setPreferredSize(new Dimension(1000, 1000));
//		// Setup up a new vertex to paint transformer...
//		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
//			public Paint transform(String name) {
//				System.out.println(" i = " + name);
//				return Color.GREEN;
//			}
//		};
//		// Set up a new stroke Transformer for the edges
//		float dash[] = { 10.0f };
//		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
//				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
//		Transformer<String, Stroke> edgeStrokeTransformer = new Transformer<String, Stroke>() {
//			public Stroke transform(String s) {
//				return edgeStroke;
//			}
//		};
//		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
//		vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
//
//		// using hashCode as code because the name of the state is
//		// vulnerable because it may not contain sufficient information to
//		// differentiate states in one layer i.e. complement form and
//		// variables
//		HashMap<Integer, GraphVertex> previousLayerNodes = new HashMap<Integer, GraphVertex>();
//		HashMap<Integer, GraphVertex> currentLayerNodes = new HashMap<Integer, GraphVertex>();
//		HashMap<String, GraphVertex> currentActionNodes = new HashMap<String, GraphVertex>();
//		// add states
//		int level = 0;
//		while (level < stateLayers.size()) {
//
//			System.out.println("+++++++++++++++++++++++++++ " + level);
//			StateLayer currentLayer = stateLayers.get(level);
//
//			int j = 0;
//			while (j < currentLayer.getStates().size()) {
//				String name = level
//						+ " "
//						+ currentLayer.getStates().get(j).getPresentState()
//								.getQualifiedName();
//				graph.addVertex(name);
//				currentLayerNodes.put(currentLayer.getStates().get(j)
//						.hashCode(), new GraphVertex(name, null));
//				System.out.println("adding " + j + " hashcode "
//						+ currentLayer.getStates().get(j).hashCode() + " "
//						+ name);
//				j++;
//			}
//
//			j = 0;
//			ArrayList<State> alreadyShown = new ArrayList<State>();
//			// show all mutex states in one layer
//			while (j < currentLayer.getStates().size()) {
//				// if mut ex exists
//				if (currentLayer.getStates().get(j).getMutExState() != null
//						&& !alreadyShown.contains(currentLayer.getStates()
//								.get(j).getMutExState())) {
//					System.out.println("finding "
//							+ currentLayer.getStates().get(j).hashCode()
//							+ " & "
//							+ currentLayer.getStates().get(j).getMutExState()
//									.hashCode());
//					addMutExEdges(
//							graph,
//							true,
//							currentLayerNodes.get(currentLayer.getStates()
//									.get(j).hashCode()),
//							currentLayerNodes.get(currentLayer.getStates()
//									.get(j).getMutExState().hashCode()));
//					alreadyShown.add(currentLayer.getStates().get(j));
//					System.out.println("adding mut ex "
//							+ getNameOfNode(currentLayer.getStates().get(j))
//							+ " & "
//							+ getNameOfNode(currentLayer.getStates().get(j)
//									.getMutExState()));
//				}
//				j++;
//			}
//
//			// System.out.println("current nodes $$$$$$$$$$ "
//			// + currentLayerNodes.size());
//
//			// add actions
//			if (level != 0) {
//				// System.out.println("---- " + i);
//				ActionLayer currentActionLayer = actionLayers.get(level - 1);
//
//				int k = 0;
//				// System.out.println("+++++++++++ "
//				// + currentLayerNodes.size() + " "
//				// + previousLayerNodes.size());
//				while (k < currentActionLayer.getActions().size()) {
//
//					System.out.println("adding action "
//							+ currentActionLayer.getActions().hashCode());
//
//					// TODO added outer for loop
//					for (int o = 0; o < currentActionLayer.getActions().get(k)
//							.getSuccessiveStates().size(); o++) {
//
//						GraphVertex vertex2 = currentLayerNodes
//								.get(currentActionLayer.getActions().get(k)
//										.getSuccessiveStates().get(o)
//										.hashCode());
//
//						System.out.println("looking up for hashcode "
//								+ currentActionLayer.getActions().get(k)
//										.getSuccessiveStates().get(o)
//										.hashCode());
//
//						String displayName = addActionToGraph(graph,
//								currentActionNodes, previousLayerNodes,
//								vertex2, currentActionLayer.getActions().get(k)
//										.getPreviousStates(), level, j,
//								currentLayer, currentActionLayer.getActions()
//										.get(k).hashCode(), currentActionLayer
//										.getActions().get(k), false);
//
//						addActionEdgeToGraph(graph, displayName, vertex2);
//					}
//					k++;
//				}
//
//				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//
//				// add mutex actions
//				for (int l = 0; l < currentActionLayer.getActions().size(); l++) {
//					// check if mutex exists
//					if (currentActionLayer.getActions().get(l)
//							.getMutExActions().size() != 0) {
//						GraphVertex action1 = currentActionNodes
//								.get(getEdgeIndentifier(currentActionLayer
//										.getActions().get(l)));
//
//						// for all display edge
//						Iterator<Action> iterator = currentActionLayer
//								.getActions().get(l).getMutExActions()
//								.iterator();
//						while (iterator.hasNext()) {
//							Action mutexAction = iterator.next();
//
//							GraphVertex action2 = currentActionNodes
//									.get(getEdgeIndentifier(mutexAction));
//							addMutExEdges(graph, false, action1, action2);
//							System.out.println("adding mut ex "
//									+ getEdgeIndentifier(currentActionLayer
//											.getActions().get(l)) + " & "
//									+ getEdgeIndentifier(mutexAction));
//						}
//					}
//				}
//				// add solution path
//				ActionLayer solutionLayer = solutionPath.get(level - 1);
//				k = 0;
//				System.out.println("+++++++++++ solutionLayer size "
//						+ solutionLayer.getActions().size());
//				while (k < solutionLayer.getActions().size()) {
//
//					System.out.println("adding solution "
//							+ solutionLayer.getActions().get(k).getAction()
//									.getQualifiedName()
//							+ solutionLayer.getActions().get(k).hashCode());
//
//					// TODO added outer for loop
//					for (int o = 0; o < solutionLayer.getActions().get(k)
//							.getSuccessiveStates().size(); o++) {
//
//						GraphVertex vertex2 = currentLayerNodes
//								.get(solutionLayer.getActions().get(k)
//										.getSuccessiveStates().get(o)
//										.hashCode());
//
//						System.out.println("looking up for hashcode "
//								+ solutionLayer.getActions().get(k)
//										.getSuccessiveStates().get(o)
//										.hashCode());
//
//						String displayName = addActionToGraph(graph,
//								currentActionNodes, previousLayerNodes,
//								vertex2, solutionLayer.getActions().get(k)
//										.getPreviousStates(), level, j,
//								currentLayer, solutionLayer.getActions().get(k)
//										.hashCode(), solutionLayer.getActions()
//										.get(k), true);
//
//						// addSolutionEdgeToGraph(graph, displayName, vertex2);
//					}
//					k++;
//				}
//			}
//
//			previousLayerNodes = currentLayerNodes;
//			currentLayerNodes = new HashMap<Integer, GraphVertex>();
//			currentActionNodes = new HashMap<String, GraphVertex>();
//			level++;
//		}
//
//		this.getContentPane().add(vv);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setAlwaysOnTop(true);
//		this.setSize(2000, 2000);
//		// frame.pack();
//		this.setVisible(true);
//	}
//
//	public boolean compareArrayLists(ArrayList<State> predecessorStates,
//			ArrayList<State> predecessorStates2) {
//		if (predecessorStates == predecessorStates2)
//			return true;
//		return predecessorStates2.equals(predecessorStates);
//	}
//
//	public GraphVertex getActionIfPresent(
//			HashMap<String, GraphVertex> currentActionNodes, Action actionObject) {
//
//		// get name of action
//
//		String name = getEdgeIndentifier(actionObject);
//
//		// check whether there is an element in arrayList whose name is the
//		// same, hashCode may be different, but predecessors must be the same
//
//		GraphVertex result = null;
//		Iterator<String> keysetIterator = currentActionNodes.keySet()
//				.iterator();
//		while (keysetIterator.hasNext()) {
//			String actionName = keysetIterator.next();
//
//			if (actionName.startsWith(name)) {
//
//				// System.out.println("^^^^^^^^^^^ some considerate action "
//				// + actionName + " and " + name + ";");
//
//				// check if predessors are same
//
//				if (currentActionNodes.get(actionName).getPredecessorStates() != null
//						&& currentActionNodes.get(actionName)
//								.getPredecessorStates()
//								.equals(actionObject.getPreviousStates())) {
//					result = currentActionNodes.get(actionName);
//					// System.out.println("same predecessor~!!!!!!!!!!!!!!");
//				} else {
//					// System.out.println("something wrong~~~~~~~~~~~~~~~~~~");
//				}
//			}
//		}
//
//		return result;
//	}
//
//	public String addActionToGraph(Graph graph,
//			HashMap<String, GraphVertex> currentActionNodes,
//			HashMap<Integer, GraphVertex> previousLayerNodes,
//			GraphVertex vertex2, ArrayList<State> predecessorStates, int level,
//			int j1, StateLayer stateLayer, int hashCode, Action actionObject,
//			boolean solution) {
//
//		String displayName = null;
//		System.out.println("ADDING ACTION TO GRAPH");
//
//		// TODO extract actual name
//
//		GraphVertex alreadyPresentVertex = getActionIfPresent(
//				currentActionNodes, actionObject);
//
//		// check if action exists for group of predecessors
//		// if (currentActionNodes.containsKey(name)
//		// && compareArrayLists(predecessorStates,
//		// currentActionNodes.get(name).getPredecessorStates())) {
//		if (alreadyPresentVertex != null) {
//
//			System.out.println("already present");
//		} else {
//			// create action
//			// get first left value
//			int left = 0;
//			// get average top value
//			int top = 0;
//
//			String name = getEdgeIndentifier(actionObject);
//
//			displayName = level + " "
//					+ actionObject.getAction().getQualifiedName();
//
//			graph.addVertex(displayName);
//
//			currentActionNodes.put(name, new GraphVertex(displayName,
//					predecessorStates));
//
//			System.out.println("adding action " + (name + hashCode) + " for "
//					+ name);
//
//			if (predecessorStates != null) {
//				// draw edge for all predecessor
//				for (int i = 0; i < predecessorStates.size(); i++) {
//					if (solution)
//						addSolutionEdgeToGraph(graph,
//								previousLayerNodes.get(predecessorStates.get(i)
//										.hashCode()), displayName);
//					else
//						addActionEdgeToGraph(graph,
//								previousLayerNodes.get(predecessorStates.get(i)
//										.hashCode()), displayName);
//				}
//			}
//		}
//		return displayName;
//	}
//
//	public void addActionEdgeToGraph(Graph graph, GraphVertex vertex1,
//			String vertex2Name) {
//		try {
//			graph.addEdge("->", vertex1.getName(), vertex2Name,
//					EdgeType.DIRECTED);
//		} catch (IllegalArgumentException e) {
//
//		}
//	}
//
//	public void addActionEdgeToGraph(Graph graph, String vertex1Name,
//			GraphVertex vertex2) {
//		try {
//			graph.addEdge("->", vertex1Name, vertex2.getName(),
//					EdgeType.DIRECTED);
//		} catch (IllegalArgumentException e) {
//
//		}
//	}
//
//	public void addSolutionEdgeToGraph(Graph graph, GraphVertex vertex1,
//			String vertex2Name) {
//		graph.addEdge("~", vertex1.getName(), vertex2Name, EdgeType.DIRECTED);
//	}
//
//	public void addSolutionEdgeToGraph(Graph graph, String vertex1Name,
//			GraphVertex vertex2) {
//		graph.addEdge("~", vertex1Name, vertex2.getName(), EdgeType.DIRECTED);
//	}
//
//	public void addMutExEdges(Graph graph, boolean state, GraphVertex vertex1,
//			GraphVertex vertex2) {
//		try {
//			graph.addEdge("-", vertex1.getName(), vertex2.getName(),
//					EdgeType.UNDIRECTED);
//		} catch (IllegalArgumentException e) {
//
//		}
//	}
//
//	public void printGraph(BasicVisualizationServer vv) {
//		// Create the VisualizationImageServer
//		// vv is the VisualizationViewer containing my graph
//		VisualizationImageServer vis = new VisualizationImageServer(
//				vv.getGraphLayout(), vv.getGraphLayout().getSize());
//
//		// Configure the VisualizationImageServer the same way
//		// you did your VisualizationViewer. In my case e.g.
//
//		vis.setBackground(Color.WHITE);
//		vis.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
//		vis.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
//		vis.getRenderContext()
//				.setVertexLabelTransformer(new ToStringLabeller());
//		vis.getRenderer().getVertexLabelRenderer()
//				.setPosition(Renderer.VertexLabel.Position.CNTR);
//
//		// Create the buffered image
//		BufferedImage image = (BufferedImage) vis.getImage(new Point2D.Double(
//				vv.getGraphLayout().getSize().getWidth() / 2, vv
//						.getGraphLayout().getSize().getHeight() / 2),
//				new Dimension(vv.getGraphLayout().getSize()));
//
//		// Write image to a png file
//		File outputfile = new File("graph.png");
//
//		try {
//			ImageIO.write(image, "png", outputfile);
//		} catch (IOException e) {
//			// Exception handling
//		}
//	}
//}
