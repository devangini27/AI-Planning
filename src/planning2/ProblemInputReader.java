package planning2;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import planning2.DomainInputReader.TreeNode;

public class ProblemInputReader {

	ProblemInputReader() {
		this.domainInputReader = new DomainInputReader();
	}

	DomainInputReader domainInputReader;

	public void readInputs(String problemFile, String domainFile) {
		String[] problemContent = readFromFile(problemFile);

		// System.out.println("problem " + Arrays.toString(problemContent));

		makeVariables(problemContent);

		processDomainFile(domainFile);

		makeInitialGoalStates(problemContent);
	}

	public static void main(String[] args) {

		ProblemInputReader problemInputReader = new ProblemInputReader();
		String[] problemContent = problemInputReader
				.readFromFile("gripper-2.pddl");

		// System.out.println("problem " + Arrays.toString(problemContent));

		problemInputReader.makeVariables(problemContent);

		problemInputReader.processDomainFile("gripper-domain.pddl");

		problemInputReader.makeInitialGoalStates(problemContent);

	}

	public void processDomainFile(String fileName) {

		domainInputReader.readFromDomainFile(fileName);

	}

	public void makeVariables(String[] problemContent) {
		ArrayList<VariableConstants> variablesList = new ArrayList<VariableConstants>();
		for (int i = 0; i < problemContent.length; i++) {

			if (problemContent[i].startsWith("objects")) {
				// if end
				// then make start false
				if (problemContent[i].contains("(")) {
					problemContent[i] = problemContent[i].replace("(", "");
				}
				if (problemContent[i].contains(")")) {
					problemContent[i] = problemContent[i].replace(")", "");
				}

				String[] objects = problemContent[i].replace("objects", "")
						.trim().split("\\s+");
				for (String object : objects) {
					variablesList.add(new VariableConstants(object, false));
					// System.out.println("found " + object);
				}

			}

		}

		VariableConstants[] variables = new VariableConstants[variablesList
				.size()];
		variables = variablesList.toArray(variables);
		// finally set the field in variable constants
		VariableConstants object = new VariableConstants("x", false);
		Class<?> c = object.getClass();

		Field variableField;
		try {
			variableField = c.getDeclaredField("variables");

			variableField.setAccessible(true);
			variableField.set(object, variables);
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// System.out.println("vars "
		// + Arrays.toString(VariableConstants.variables));

	}

	public void makeInitialGoalStates(String[] problemContent) {
		BaseActionClass initialState = null;
		BaseActionClass goalState = null;

		for (int i = 0; i < problemContent.length; i++) {

			if (problemContent[i].startsWith("init")) {
				// System.out.println("in init");
				// remove init
				String line = problemContent[i].replace("init", "").trim();

				TreeNode tree = domainInputReader.makeTree(
						"(and " + line + ")", null);
				initialState = domainInputReader.makeActions(tree,
						new ArrayList<ActionConstants>(),
						VariableConstants.variables);

				// System.out.println("initial state " + initialState);
			} else if (problemContent[i].startsWith("goal")) {
				// System.out.println("in goal");
				// remove init
				String line = problemContent[i].replace("goal", "").trim();

				TreeNode tree = domainInputReader.makeTree(
						"(and " + line + ")", null);
				// System.out.println("tree " + tree);
				goalState = domainInputReader.makeActions(tree,
						new ArrayList<ActionConstants>(),
						VariableConstants.variables);

				// System.out.println("goalState " + goalState);
			}
		}

		ProblemConstant.initialState = initialState;
		ProblemConstant.goalState = goalState;
		ProblemConstant.makeClosedWorldAssumptions(false);

		// System.out.println("INIT " + ProblemConstant.initialState);
		// System.out.println("GOAL " + ProblemConstant.goalState);

	}

	public String[] readFromFile(String fileName) {
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
		String[] content = contents.split(":");
		return content;
	}

}
