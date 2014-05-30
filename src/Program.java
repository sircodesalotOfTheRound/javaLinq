import com.javalinq.QList;

import java.util.List;

/**
 * Created by sircodesalot on 14-5-30.
 */
public class Program {
    public static void main(String[] args) {
        QList<String> list = new QList<>();

        list.add("Something");

        for (String item : list) {
            System.out.println(item);
        }
    }
}
