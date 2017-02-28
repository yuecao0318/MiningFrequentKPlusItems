/**
 * Created by yuecao on 2/16/17.
 */
import java.util.*;


public class TransacManage {

    static class RecordItem implements Comparable<RecordItem> {
        public String value;
        private int occurrence = 1;
        public Set<Integer> recordIds = new HashSet<>();
        public RecordItem(String value, int firstId) {
            this.value = value;
            this.recordIds.add(firstId);}
        public void addOne(int id) {
            this.occurrence++;
            this.recordIds.add(id);
        }
        public int getOccurrence() {return this.occurrence;}
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof RecordItem) {
                return this.value.equals(((RecordItem)o).value);
            } else {
                return false;
            }
        }
        @Override
        public int compareTo(RecordItem o) {
            return this.value.compareTo(o.value);
        }

    }



    public static Map<Integer, RecordItem> map = new HashMap<>();

    public static List<Transaction> recordList = new ArrayList<>();
    public static Map<String, Integer> convertMap = new HashMap<>();
    public static int count = 0;

    public static int addItem(String item, int recordId) {

        int hash = item.hashCode();
        if (!map.containsKey(hash)) {
            map.put(hash, new RecordItem(item, recordId));
        } else {
            map.get(hash).addOne(recordId);
        }
        return hash;
    }

    public static String getItem(int hashCode) {
        return map.get(hashCode).value;
    }



    public static Set<Integer> getUniqueItems(int min_sup) {
        Set<Integer> uniqueSet = new HashSet<>();
        for (Map.Entry<Integer, RecordItem> e : map.entrySet()) {
            if (e.getValue().getOccurrence() >= min_sup) {
                uniqueSet.add(e.getKey());
            }
        }
        return uniqueSet;
    }

    public static int getItemCount(int item) {
        return map.get(item).getOccurrence();
    }

    public static void addRecord(String strLine) {//db every line,
        Transaction t = new Transaction();
        String[] items = strLine.split("\\s");
        if (items.length > 0) {
            for (String item : items) {
                int convertInt = addItem(item, t.getId());
                //System.out.println(t.getId());
                t.addItem(convertInt);
            }
            recordList.add(t);
        }
    }




}

