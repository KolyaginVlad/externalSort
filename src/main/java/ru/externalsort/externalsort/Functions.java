package ru.externalsort.externalsort;

public class Functions {
    private long SimpleSort(long size){
        return 1*size;
    }
    private long NaturalSort(long size){
        return 2*size;
    }
    private long AbsorbSort(long size){
        return 3*size;
    }
    public long [] startSort(long size){
        //Создаём массив на 3 элемента - простая сортировка, естественная и метод поглощения
        long [] massOfResults = new long[3];
        massOfResults[0] = SimpleSort(size);
        massOfResults[1] = NaturalSort(size);
        massOfResults[2] = AbsorbSort(size);

        return massOfResults;
    }
}
