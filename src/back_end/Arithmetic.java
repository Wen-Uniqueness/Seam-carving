package back_end;

import java.util.PriorityQueue;

public class Arithmetic {
    public static <T extends Comparable<T>> int find_min(T[] array){
        if (array == null || array.length <= 0) throw new IllegalArgumentException();
        int index = 0;
        for (int i = 1; i < array.length; i++){
            if (array[i].compareTo(array[index]) < 0){
                index = i;
            }
        }
        return index;
    }

    static class Entry implements Comparable<Entry> {
        int index;
        double value;

        public Entry(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Entry other) {
            return Double.compare(this.value, other.value);
        }
    }

    public static int[] findKSmallest(double[] arr, int k) {
        PriorityQueue<Entry> minHeap = new PriorityQueue<>(k);
        for (int i = 0; i < arr.length; i++) {
            Entry entry = new Entry(i, arr[i]);
            minHeap.offer(entry);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll().index;
        }
        return result;
    }
}
