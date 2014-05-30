import com.javalinq.QList;

import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class Program {
    public static void main(String[] args) {
        QList<String> list = new QList<>();

        list.add("One");
        list.add("Two");
        list.add("Three");
        list.add("Four");
        list.add("Five");

        for (String item : list.where(str -> str.length() == 3)) {
            System.out.println(item);
        }
    }
}
