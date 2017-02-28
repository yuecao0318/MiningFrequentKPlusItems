/**
 * Created by yuecao on 2/16/17.
 */

import java.util.HashSet;
import java.util.Set;

public class Transaction {
    static Integer recordCounter = 0;

    private int recordId;
    private Set<Integer> set = new HashSet<>();

    public Transaction() {
        this.recordId = recordCounter++;
    }

    public int getId() {
        return this.recordId;
    }

    public void addItem(Integer item) {
        this.set.add(item);
    }
}

