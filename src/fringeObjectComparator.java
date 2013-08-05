import java.util.Comparator;

public class fringeObjectComparator implements Comparator<fringeObject> {

	@Override
	public int compare(fringeObject x, fringeObject y) {
		if (x != null && y != null) {
			if (x.getTotalCostToGoal() < y.getTotalCostToGoal()) {
				return -1;
			}
			if (x.getTotalCostToGoal() > y.getTotalCostToGoal()) {
				return 1;
			}
			
		}
		return 0;
	}

}
