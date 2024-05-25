package back_end;

import java.util.Comparator;
import java.util.PriorityQueue;

public class Arithmetic {
    public static <T extends Comparable<T>> int find_min(T[] array) {
        if (array == null || array.length <= 0)
            throw new IllegalArgumentException();
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i].compareTo(array[index]) < 0) {
                index = i;
            }
        }
        return index;
    }

    // 自定义类来存储数组元素及其索引
    static class Element {
        Double value;
        int index;

        Element(Double value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    public static int[] getKSmallestIndex(Double[] last_col, int k) {
        if (last_col == null || k <= 0 || k > last_col.length) {
            throw new IllegalArgumentException("Invalid input");
        }

        // 创建一个最大堆
        PriorityQueue<Element> maxHeap = new PriorityQueue<>(new Comparator<Element>() {
            @Override
            public int compare(Element e1, Element e2) {
                if (e2.value > e1.value) return 1;
                if (e2.value == e1.value) return 0;
                return -1;
            }
        });

        // 先将前k个元素添加到堆中
        for (int i = 0; i < k; i++) {
            maxHeap.add(new Element(last_col[i], i));
        }

        // 遍历数组中剩下的元素
        for (int i = k; i < last_col.length; i++) {
            if (last_col[i] < maxHeap.peek().value) {
                maxHeap.poll();
                maxHeap.add(new Element(last_col[i], i));
            }
        }

        // 将堆中的元素存储到结果数组中
        int[] result = new int[k]; // 每个元素包含值和索引
        for (int i = 0; i < k; i++) {
            Element e = maxHeap.poll();
            result[i] = e.index;
        }

        return result;
    }
}
