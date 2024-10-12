import java.util.*;

public class SharedData
{
    public static void main(String[] args)
    {
        long start = System.nanoTime();
        useThreads(4, true, true);
        useThreads(7, true, false);
        long end = System.nanoTime();
        System.out.println("Time taken (in milliseconds): " + (end-start)/1_000_000);
    }

    public static void useThreads(int n, boolean isSynchronized, boolean isInOrder) {
        Thread[] ts = new Thread[n];
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < ts.length; i++) {
            int finali = i;
            boolean finalIsSynchronized = isSynchronized;
            boolean finaIsInOrder = isInOrder;
            ts[i] = new Thread(() -> {
                for (int j = 1 + finali; j <= 1_000_000; j+=n) {
                    if(finalIsSynchronized) {
                        synchronized (l) {
                            l.add(j);
                        }
                    } else {
                        l.add(j);
                    }
                }
            });
        }
        for (int i = 0; i < ts.length; i++) ts[i].start();
        try {
            for (int i = 0; i < ts.length; i++) ts[i].join();
        } catch (InterruptedException e) {}
        int inversions = 0, nulls = 0;
        for (int i = 1; i < l.size(); i++) {
            if (l.get(i) == null) nulls++;
            if (l.get(i-1) == null || l.get(i) == null) continue;
            if (l.get(i-1) > l.get(i)) inversions++;
        }
        System.out.println("Size: " + l.size() +
                            " Inversions: " + inversions +
                            " Nulls: " + nulls);

        if(isInOrder) {
            l.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        }

        System.out.println(l.subList(20, 40));
    }
}