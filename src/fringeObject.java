
public class fringeObject {
	private Intersection node;
	private fringeObject from;
	private double costToHere;
	private double totalCostToGoal;
	/**
	 * @param node
	 * @param from
	 * @param costToHere
	 * @param totalCostToGoal
	 */
	public fringeObject(Intersection node, fringeObject from,
			double costToHere, double totalCostToGoal) {
		this.node = node;
		this.from = from;
		this.costToHere = costToHere;
		this.totalCostToGoal = totalCostToGoal;
	}
	/**
	 * @return the node
	 */
	public Intersection getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(Intersection node) {
		this.node = node;
	}
	/**
	 * @return the from
	 */
	public fringeObject getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(fringeObject from) {
		this.from = from;
	}
	/**
	 * @return the costToHere
	 */
	public double getCostToHere() {
		return costToHere;
	}
	/**
	 * @param costToHere the costToHere to set
	 */
	public void setCostToHere(double costToHere) {
		this.costToHere = costToHere;
	}
	/**
	 * @return the totalCostToGoal
	 */
	public double getTotalCostToGoal() {
		return totalCostToGoal;
	}
	/**
	 * @param totalCostToGoal the totalCostToGoal to set
	 */
	public void setTotalCostToGoal(double totalCostToGoal) {
		this.totalCostToGoal = totalCostToGoal;
	}
	
	public String toString(){
		return String.valueOf(totalCostToGoal);
	}
	
}
