import java.util.Stack;


public class StackElement {
	public Intersection node;
	public StackElement parent;
	public double depth;
	public double reach;
	public Stack<Intersection> children;
	
	public StackElement(Intersection node, double depth, StackElement parent){
		this.node = node;
		this.depth = depth;
		this.parent = parent;
		children = null;
	}
}
