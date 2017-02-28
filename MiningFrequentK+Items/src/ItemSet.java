/**
 * Created by yuecao on 2/16/17.
 */
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class ItemSet {
    private TreeSet<Integer> set = new TreeSet<>();
    private int count = 0;

    public ItemSet() {}

    public ItemSet(Integer init, int count) {
        this.set.add(init);
        this.count = count;
    }

    public void addItem(Integer item) {
        this.set.add(item);
    }

    public void addItem(Set<Integer> s) {
        this.set.addAll(s);
    }

    public TreeSet<Integer> getItems() {
        return this.set;
    }



    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public boolean canJoin(ItemSet otherSet) {
        if(this.set.size() == 1)
            return this.set.last() < otherSet.set.last();  // try to speed up the c2 pass

        Integer lastInt = this.set.last();
        Iterator<Integer> iter1 = this.set.iterator();
        Iterator<Integer> iter2 = otherSet.set.iterator();
        while(iter1.hasNext() && iter2.hasNext()) {
            Integer current = iter1.next();
            if(current != lastInt && current != iter2.next())
                return false;
        }

        if(lastInt < otherSet.set.last())
            return true;
        else
            return false;
    }

    public ItemSet[] getKMinusOneSubset() {
        ItemSet[] subsets = new ItemSet[set.size()];
        int index = 0;
        for(Integer item : set) {
            ItemSet subset = new ItemSet();
            subset.addItem(this.set);
            subset.set.remove(item);
            subsets[index] = subset;
            index++;
        }
        return subsets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ItemSet) {
            return this.hashCode() == ((ItemSet)o).hashCode();
        } else {
            return false;
        }
    }

    public int hashCode() {
        return set.hashCode();
    }
}

