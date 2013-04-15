package planning2;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DomainInputReader {

	String[] content;

	public void readFromDomainFile(String fileName) {
		System.out.println("domain file " + fileName);
		String contents = "";
		try {

			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.length() != 0 && line.charAt(0) != ';') {
					contents += line;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		content = contents.split(":");

		makePredicates(content);

		makeActions(content);

	}

	public void makePredicates(String[] problemContent) {

		ArrayList<ActionConstants> predicateList = new ArrayList<ActionConstants>();

		for (int i = 0; i < problemContent.length; i++) {

			if (problemContent[i].startsWith("predicates")) {

				// remove init
				String line = problemContent[i].replace("predicates", "")
						.trim();

				int indexStart = line.indexOf("(");
				int index2 = indexStart;
				while (indexStart != -1 && line.indexOf(")", indexStart) != -1) {
					index2 = line.indexOf(")", indexStart);
					String word = line.substring(indexStart + 1, index2);
					String[] parts = word.split("\\s+");
					// AT = new ActionConstants("at", new
					// VariableConstants[] { null, null },
					// null, null, false, 1, true);
					VariableConstants[] vars = new VariableConstants[parts.length - 1];
					for (int j = 0; j < vars.length; j++) {
						if (!parts[j + 1].startsWith("?"))
							vars[j] = findVariable(parts[j + 1]);
					}

					ActionConstants action = new ActionConstants(parts[0],
							vars, null, null, false, 0, true);

					// System.out.println("predicates  -> " +
					// action.toString());
					predicateList.add(action);

					indexStart = line.indexOf("(", index2 + 1);
				}

			}

		}

		ActionConstants[] predicates = new ActionConstants[predicateList.size()];
		predicates = predicateList.toArray(predicates);

		ActionConstants.possiblePredicates = predicates;

		// System.out.println("predicates == "
		// + Arrays.toString(ActionConstants.possiblePredicates));

	}

	private VariableConstants findVariable(String varName) {
		VariableConstants variable = null;
		for (VariableConstants var : VariableConstants.variables) {
			if (var.getSymbolName().equals(varName))
				variable = var;
		}
		return variable;
	}

	private VariableConstants findVariable(String varName,
			VariableConstants[] params) {
		VariableConstants variable = null;
		for (int i = 0; i < params.length; i++) {
			if (params[i].getSymbolName().equalsIgnoreCase(varName)) {
				// System.out.println("found equal " + params[i]);
				variable = params[i];
			}
			// System.out.println("-- " + params[i]);
		}
		// System.out.println("found var " + variable + " for " + varName
		// + " from " + Arrays.toString(params));
		return variable;
	}

	private VariableConstants[] findVariables(String variables,
			VariableConstants[] params) {

		String[] vars = variables.trim().split("\\s+");
		// System.out.println("vars  " + variables + " " + vars.length);
		VariableConstants[] varArray = new VariableConstants[vars.length];
		for (int i = 0; i < vars.length; i++) {
			varArray[i] = findVariable(vars[i], params);
		}
		// System.out.println("found vars " + Arrays.toString(varArray) + "for "
		// + variables + " from " + Arrays.toString(params));
		return varArray;

	}

	private ActionConstants findAction(String varName,
			ArrayList<ActionConstants> actions) {
		ActionConstants predicate = null;
		for (ActionConstants action : ActionConstants.possiblePredicates) {
			if (action.getActionName().equals(varName))
				predicate = action;
		}
		for (int i = 0; i < actions.size(); i++) {
			if (actions.get(i).getActionName().equals(varName))
				predicate = actions.get(i);
		}
		return predicate;
	}

	public void makeActions(String[] problemContent) {
		boolean start = false;
		ArrayList<ActionConstants> actionList = new ArrayList<ActionConstants>();
		ActionConstants newAction = null;

		for (int i = 0; i < problemContent.length; i++) {
			if (start) {
				String line = problemContent[i].trim();

				if (line.startsWith("parameters")) {
					String params = line.substring(line.indexOf("(") + 1,
							line.indexOf(")"));
					String[] vars = params.split("\\s+");
					VariableConstants[] variables = new VariableConstants[vars.length];
					for (int j = 0; j < vars.length; j++) {
						variables[j] = new VariableConstants(vars[j], true);
					}
					newAction.setVariables(variables);

				} else if (line.startsWith("precondition")) {

					// System.out.println("precondition " + line);
					TreeNode tree = makeTree(line.replace("precondition", "")
							.trim(), null);
					// System.out.println(tree);
					BaseActionClass preconditions = makeActions(tree,
							actionList, newAction.getVariables());
					// System.out.println("preconditions " + preconditions);
					newAction.setPrecondition(preconditions);

				} else if (line.startsWith("effect")) {

					// System.out.println("effect " + line);
					TreeNode tree = makeTree(line.replace("effect", "").trim(),
							null);
					// System.out.println(tree);
					BaseActionClass effects = makeActions(tree, actionList,
							newAction.getVariables());
					// System.out.println("effects " + effects);
					newAction.setEffects(effects);
					start = false;
				}

				// if end
				// then make start false
				// if (line.indexOf(")", index2 + 1) != -1)
				// start = false;
			} else {
				if (newAction != null) {
					actionList.add(newAction);
					// System.out.println("action " + newAction);
					// System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^6");
				}

				if (problemContent[i].contains("action")) {
					start = true;

					// remove init
					String line = problemContent[i].replace("action", "")
							.trim();
					String actionName = line;

					newAction = new ActionConstants();
					newAction.setActionName(actionName);

				}
			}
		}
		if (newAction != null) {
			actionList.add(newAction);
			// System.out.println("action " + newAction);
			// System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^6");

		}

		ActionConstants[] actions = new ActionConstants[actionList.size()];
		actions = actionList.toArray(actions);

		ActionConstants.possibleActions = actions;

		// System.out.println("actions "
		// + Arrays.toString(ActionConstants.possibleActions));

	}

	public static void main(String[] args) {
		DomainInputReader domain = new DomainInputReader();
		System.out.println(domain.makeTree(
				"(and  (at-robby ?to) (not (at-robby ?from)))", null));
	}

	public BaseActionClass makeActions(TreeNode tree,
			ArrayList<ActionConstants> actionList, VariableConstants[] variables) {

		BaseActionClass baseAction = null;
		// check connective
		if (tree.connective != null) {
			// make compound
			BaseActionClass[] childActions = new BaseActionClass[tree.children.length];
			ConnectiveConstants[] connectives = new ConnectiveConstants[tree.children.length - 1];
			boolean[] complementForms = new boolean[tree.children.length];

			for (int i = 0; i < childActions.length; i++) {
				childActions[i] = makeActions(tree.children[i], actionList,
						variables);
				if (i != childActions.length - 1) {
					connectives[i] = tree.connective;

				}
			}
			if (tree.connective != null
					&& tree.connective.equals(ConnectiveConstants.NOT))
				childActions[0].getComplementForms()[0] = false;

			baseAction = new CompoundActionClass(childActions, connectives,
					complementForms);
		} else {
			// make simple
			baseAction = new ActionClass(findAction(tree.name, actionList),
					findVariables(tree.variables, variables),
					new boolean[] { true });
		}

		return baseAction;
	}

	public TreeNode makeTree(String content, TreeNode parent) {
		TreeNode tree = new TreeNode();
		// System.out.println("inpiut ^^^^^^" + content);

		// for this node
		int spaceIndex = content.indexOf(" ");
		String name;
		String vars;
		if (spaceIndex != -1) {
			name = content.substring(1, spaceIndex);
			vars = content.substring(spaceIndex);
		} else {
			// System.out.println();
			name = content.replaceAll("\\(", "").replaceAll("\\)", "");
			vars = "";
		}

		//System.out.println("name " + name);
		tree.name = name;
		tree.variables = vars;

		// check if there is a connective for it
		for (ConnectiveConstants connective : ConnectiveConstants.values()) {
			if (connective.getConnectiveName().equalsIgnoreCase(name)) {
				tree.connective = connective;
			}
		}

		if (parent != null) {
			TreeNode[] newChildren = new TreeNode[parent.children.length + 1];
			for (int j = 0; j < parent.children.length; j++) {
				newChildren[j] = parent.children[j];
			}
			newChildren[parent.children.length] = tree;
			parent.children = newChildren;
		}

		// get children
		ArrayList<String> levelString = new ArrayList<String>();
		int level = 0;
		int lastIndex = 0;
		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '(') {
				level++;
				if (level == 2) {
					lastIndex = i;
				}
			} else if (content.charAt(i) == ')') {
				level--;
				if (level == 1) {
					levelString.add(content.substring(lastIndex, i).trim());
				}
			}
		}

		// process children
		for (int i = 0; i < levelString.size(); i++) {
			// System.out.println(" -- " + levelString.get(i));
			makeTree(levelString.get(i), tree);
		}
		return tree;
	}

	class TreeNode {

		@Override
		public String toString() {
			return "TreeNode [name=" + name + ", variables=" + variables
					+ ", children=" + Arrays.toString(children)
					+ ", connective=" + connective + "]";
		}

		String name;
		String variables;
		TreeNode[] children = new TreeNode[0];
		ConnectiveConstants connective;
	}
}
