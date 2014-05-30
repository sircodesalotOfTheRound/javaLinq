import com.javalinq.implementations.QList;

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

        for (Integer item : list.ofType(Integer.class)) {
            System.out.println(item);
        }

        System.out.println(list.ofType(Integer.class).single());
    }
}
