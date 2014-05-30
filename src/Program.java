import com.javalinq.implementations.QList;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.Partition;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class Program {
    public static void main(String[] args) {
        QList<Object> list = new QList<>();

        list.add("One");
        list.add(1);
        list.add("Two");
        list.add("Three");
        list.add("Three");
        list.add("Three");
        list.add("Four");
        list.add("Six");
        list.add("Five");

        Partition<Class, Object> parition = list.parition(item -> item.getClass());

        for (QIterable<Object> set : parition) {
            System.out.println(set.getClass());
            for (Object item : set) {
                System.out.println("    " + item);
            }
        }

    }
}
