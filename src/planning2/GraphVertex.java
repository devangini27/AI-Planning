package planning2;

import java.util.ArrayList;

public class GraphVertex {

	public GraphVertex(Object vertex, int top, int left,
			ArrayList<State> predecessorStates/*
											 * , ArrayList<GraphVertex>
											 * predecessors
											 */) {
		super();
		this.setVertex(vertex);
		this.top = top;
		this.left = left;
		// this.setPredecessors(predecessors);
		this.predecessorStates = predecessorStates;

	}

	private Object vertex;
	private int top;
	private int left;
	// private ArrayList<GraphVertex> predecessors;
	private ArrayList<State> predecessorStates;

	public static GraphVertex NullNode = new GraphVertex(null, 0, 300, null);

	public void setTop(int top) {
		this.top = top;
	}

	public int getTop() {
		return top;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getLeft() {
		return left;
	}

	public void setVertex(Object vertex) {
		this.vertex = vertex;
	}

	public Object getVertex() {
		return vertex;
	}

	// public void setPredecessors(ArrayList<GraphVertex> predecessors) {
	// this.predecessors = predecessors;
	// }
	//
	// public ArrayList<GraphVertex> getPredecessors() {
	// return predecessors;
	// }

	public void setPredecessorStates(ArrayList<State> predecessorStates) {
		this.predecessorStates = predecessorStates;
	}

	public ArrayList<State> getPredecessorStates() {
		return predecessorStates;
	}

}
