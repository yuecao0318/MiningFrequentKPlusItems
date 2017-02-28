/**
 * Created by yuecao on 2/16/17.
 */

import java.io.*;
import java.util.*;


public class AprioriAlgoMain {
    /**
     * Frequent ItemSet List
     */
    static List<Set<ItemSet>> allFrequentSet = new ArrayList<>();

    public static void getDataFromDB(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line;

                while (((line = reader.readLine()) != null)) {
                    if (line.length() > 0) {
                        TransacManage.addRecord(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println(file.getAbsoluteFile() + "does not exist");
        }
    }

    public static void getFreqSetOne(int min_sup) {
        Set<Integer> items = TransacManage.getUniqueItems(min_sup);
        Set<ItemSet> candidate = new HashSet<>();
        for (int i : items) {
            candidate.add(new ItemSet(i, TransacManage.getItemCount(i)));
        }
        allFrequentSet.add(null);
        allFrequentSet.add(candidate);
    }

    public static void apriori(int min_sup) {
        for (int k = 2; k - 1 < allFrequentSet.size(); k++) {
            ItemSet[] candidate;
            if (k == 2) {
                candidate_Gen2(allFrequentSet.get(k - 1), min_sup);
                continue;
            } else {
                candidate = candidate_Gen(allFrequentSet.get(k - 1));
            }

            for(ItemSet s : candidate) {
                Set<Integer> intersection = null;
                for(Integer i : s.getItems()) {
                    if(intersection == null)
                        intersection = new HashSet<>(TransacManage.map.get(i).recordIds);
                    else
                        intersection.retainAll(TransacManage.map.get(i).recordIds);
                }
                s.setCount(intersection.size());
            }

            HashSet<ItemSet> freqSet = new HashSet<>();
            for(ItemSet set : candidate) {
                if(set.getCount() >= min_sup)
                    freqSet.add(set);
            }
            if(freqSet.size() > 0)
                allFrequentSet.add(freqSet);
        }
    }

    private static ItemSet[] candidate_Gen(Set<ItemSet> prevFreqSet) {
        HashSet<ItemSet> nextCandidate = new HashSet<>();
        ItemSet[] nextset = prevFreqSet.toArray(new ItemSet[prevFreqSet.size()]);

        for(int i=0; i<nextset .length; i++) {
            for(int j=0; j<nextset .length; j++) {
                if(nextset [i].canJoin(nextset [j])) {
                    ItemSet newSet = new ItemSet();
                    newSet.addItem(nextset [i].getItems());
                    newSet.addItem(nextset [j].getItems().last());
                    nextCandidate.add(newSet);
                    if(newSet.getItems().size() > 2) {
                        for(ItemSet subset : newSet.getKMinusOneSubset()) {
                            if(!prevFreqSet.contains(subset)) {
                                nextCandidate.remove(newSet);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return nextCandidate.toArray(new ItemSet[nextCandidate.size()]);
    }

    public static void candidate_Gen2(Set<ItemSet> preFreqSet, int min_sup) {
        List<Integer> itemList = new ArrayList<>();
        for (ItemSet s : preFreqSet) {
            itemList.add(s.getItems().first());
        }
        Collections.sort(itemList);
        //System.out.println(itemList);
        Integer[] preItemArray = itemList.toArray(new Integer[itemList.size()]);
        Set<ItemSet> freqSet = new HashSet<>();
        for (int i = 0; i < preItemArray.length; i++) {
            for (int j = i + 1; j < preItemArray.length; j++) {
                Set<Integer> intersection = new HashSet<>(TransacManage.map.get(preItemArray[i]).recordIds);
                intersection.retainAll(TransacManage.map.get(preItemArray[j]).recordIds);
                if(intersection.size() >= min_sup) {
                    ItemSet s = new ItemSet();
                    s.addItem(preItemArray[i]);
                    s.addItem(preItemArray[j]);
                    s.setCount(intersection.size());
                    freqSet.add(s);
                }
            }
        }
        if(freqSet.size() > 0)
            allFrequentSet.add(freqSet);
    }

    public static void writeOutput(int min_sup, int k, String outFilePath) {
        BufferedWriter writer = null;
        try {
            File file = new File(outFilePath);
            file.createNewFile();
            writer = new BufferedWriter(new FileWriter(file));
            System.out.println("write");
            for (int i = k; i < allFrequentSet.size(); i++) {
                Set<ItemSet> kthSet = allFrequentSet.get(i);
                for (ItemSet s : kthSet) {
                    //System.out.println(s);
                    Iterator<Integer> it = s.getItems().iterator();
                    while (it.hasNext()) {
                        writer.write(TransacManage.getItem(it.next()) + " ");
                    }
                    writer.write("(" +s.getCount() + ")" + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.println("min_sup input");
        int min_sup = sc.nextInt();
        System.out.println("k input");
        int k = sc.nextInt();
        //int min_sup = new Integer(args[0]);
        //int k = new Integer(args[1]);
        String dataFilePath = "transactionDB.txt";

        //String dataFilePath = args[2];
        String outFilePath = "min_sup" + min_sup + "k" + k + "result";

        //int min_sup = 4;
        getDataFromDB(dataFilePath);
        getFreqSetOne(min_sup);
        apriori(min_sup);

        writeOutput(min_sup, k, outFilePath);

    }

}
