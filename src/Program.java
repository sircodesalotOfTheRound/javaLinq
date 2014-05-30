import com.javalinq.implementations.QList;
import com.javalinq.implementations.QSet;
import com.javalinq.interfaces.QIterable;
import com.javalinq.tools.Partition;

import java.util.Objects;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class Program {
    public static void main(String[] args) {
        QList<String> list = new QList<>();
        QSet<String> rhs = new QSet<>();

        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Three");
        list.add("Three");
        list.add("Four");
        list.add("Six");
        list.add("Five");

        rhs.add("One");
        rhs.add("Three");

        for (Object item : list.unionDistinct(rhs))
            System.out.println(item);
    }

}
