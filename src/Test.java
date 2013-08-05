// Test.java
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Comparator;
public class Test
{
    public static void main(String[] args)
    {
        Comparator<fringeObject> comparator = new fringeObjectComparator();
        PriorityQueue<fringeObject> queue = 
            new PriorityQueue<fringeObject>(10, comparator);
        queue.add(new fringeObject(null, null, 121, 22222222));
        queue.add(new fringeObject(null, null, 121, 321));
        queue.add(new fringeObject(null, null, 121, 864));
        queue.add(new fringeObject(null, null, 121, 1646));
        queue.offer(new fringeObject(null, null, 121, 23));
        while (queue.size() != 0)
        {
            System.out.println(queue.remove().toString());
        }
    }
}

// StringLengthComparator.java

//
//class fringeObjectComparator implements Comparator<fringeObject>
//{
//    @Override
//    public int compare(fringeObject x, fringeObject y)
//    {
//        // Assume neither string is null. Real code should
//        // probably be more robust
//        if (x.getTotalCostToGoal() < y.getTotalCostToGoal())
//        {
//            return -1;
//        }
//        if (x.getTotalCostToGoal() > y.getTotalCostToGoal())
//        {
//            return 1;
//        }
//        return 0;
//    }
//}
