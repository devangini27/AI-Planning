//package planning;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class InputReader {
//
//	public static void main(String[] args) {
//		String[] problemContent = readFromFile("gripper-4.pddl");
//		
//		
//		
//		System.out.println("problem " + Arrays.toString(problemContent));
//	}
//	
//	public static void makeVariables(String[] problemContent){
//		boolean start = false;
//		ArrayList<VariableConstants> variablesList = new ArrayList<VariableConstants>();
//		int level = 0;
//		for(int i = 0 ; i < problemContent.length; i ++){
//			if(start) {
//				
//			}else{
//				if(problemContent[i].contains("(:objects")){
//					start = true;
//					
//					String[] objects = problemContent[i].replace("\\s+(:objects\\s+", "").split("\\s+");
//					for(String object : objects){
//						variablesList.add(new Var)
//					}
//					
//					//if end 
//					//then make start false
//					
//				}
//			}
//		}
//		
//	}
//
//	public static String[] readFromFile(String fileName) {
//		ArrayList<String> contentList = new ArrayList<String>();
//		try {
//
//			File file = new File(fileName);
//			Scanner scanner = new Scanner(file);
//			while (scanner.hasNextLine()) {
//				String line = scanner.nextLine();
//				if (line.charAt(0) != ';') {
//					contentList.add(line);
//				}
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String[] content = new String[contentList.size()];
//		content = contentList.toArray(content);
//		return content;
//	}
//
//}
