package parallelize;
import java.util.Stack;

import query.QueryGroup;
import node.LCAStackNode;
import node.NodePath;
import query.Query;


public class ParallelTasks implements Runnable{
	
	private QueryGroup group;
	private int currentHeight;
	private NodePath nodePath;
	private String label;
	private String term;
	private Integer tagId;
	private int numberOfQueries;

	
	
	public ParallelTasks(QueryGroup group, int currentHeight,
			NodePath nodePath, String label, String term, Integer tagId,
			int numberOfQueries) {
		super();
		this.group = group;
		this.currentHeight = currentHeight;
		this.nodePath = nodePath;
		this.label = label;
		this.term = term;
		this.tagId = tagId;
		this.numberOfQueries = numberOfQueries;
	}



	public void run(){
		//System.out.println();
		//System.out.println("Entrei na classe ParallelTasks.");
		int stackHeight;
		
		Stack<LCAStackNode> stack = group.getStack();

        LCAStackNode topNode;
        if (stack.empty()) {
            stackHeight = 0;
        } else {
            stackHeight = stack.size();
        }

        if (stackHeight < currentHeight) { // Stack with height smallest than the other stack
            long time1 = System.currentTimeMillis();
            for (int i = stackHeight+1; i < currentHeight; i++) {
                LCAStackNode newNode = new LCAStackNode(this.nodePath.getLabel(i-1),
                        this.nodePath.getTagId(i-1), i, group.getNumberOfTerms(), this.numberOfQueries);

                stack.push(newNode); // Initializing a new node
            }
            topNode = new LCAStackNode(label, tagId, currentHeight,group.getNumberOfTerms(), this.numberOfQueries);
            stack.push(topNode);
        }
        else{ //Stack with the same height of the tree
            topNode = stack.peek(); // Peek the stack top node
        }
        topNode.incrementMatchingTerms();
        
        //topNode.printTermsInstaces();

        if(group.verifyGetTermExistence(term)) {
        	//System.out.println("Term in ParalleTasks: "+term);
        	topNode.getTermInstances().set(group.getTermPosition(term));

        	topNode.getQueryList().addAll(group.getInvertedList().get(term));
        	//group.addEvaluatedQueryToGroup(topNode.getQueryList());
        	//for (Query q: topNode.getQueryList()) {
        	//	System.out.println("Dentro de ParalleStacks: "+q.getQueryNumber());
        	//}
            //added only to count the occurences of term
            group.addOneToTermOccurrenceNumber(term);
        	
        }
        
        //topNode.printTermInstances();
        //topNode.printQueryList();
	}
	
}
