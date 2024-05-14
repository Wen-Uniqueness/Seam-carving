package back_end;

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
}
