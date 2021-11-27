package ru.externalsort.externalsort;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Functions {
    //Константы для файлов
    final String b = "b.txt";
    final String c = "c.txt";
    final String d = "d.txt";
    final String e = "e.txt";
    //Класс с произвольным доступом к файлу. Позволяет читать и писать в любое место, а не только в конец
    RandomAccessFile randomAccessFile;
    //Размер буфера в методе поглощения
    private int n = 10;
    //метод, считающий время методом простого слияния
    private long SimpleSort(long size) {
        //Записываем время начала
        long time = System.currentTimeMillis();
        //Используем try catch для отслеживания проблем с чтением и записью
        try {
            //Разделяем файл а на два файла b и c
            toTwoFiles();
            //Переменная для определения файлов для записи
            boolean direction = true;
            //i - количество элементов, которые должны быть переведены в другой файл вместе
            //1, 2, 4, 8, 16 ...
            //Их количество не должно превышать половину количества элементов
            for (long i = 1; i < size / 2; i *= 2) {
                //В зависимости от направления записи передаём файлы для чтения и записи
                if (direction) {
                    //Читать из b c, писать в d e
                    switchFiles(i, b, c, d, e);
                    //После этого меняем направление
                    direction = false;
                } else {
                    //Читать из d e, писать в b c
                    switchFiles(i, d, e, b, c);
                    //После этого меняем направление
                    direction = true;
                }
            }
            //Теперь объединяем обратно в файл a, используя последние редактируемые файлы
            if (direction)
                //Читать из b c
                toOneFile(size, b, c);
            else //Читать из d e
                toOneFile(size, d, e);
        } catch (IOException exception) {
            //Если что-то случилось с файлами - напечатать что именно
            exception.printStackTrace();
        }
        //возвращаем разницу во времени с начала сортировке и до нынешнего момента
        return System.currentTimeMillis() - time;
    }
    //метод, считающий время сортировки естественным слиянием
    private long NaturalSort() {
        //Записываем время начала
        long time = System.currentTimeMillis();
        //Используем try catch для отслеживания проблем с чтением и записью
        try {
            //Разделяем файл а на два файла b и c
            toTwoFilesNatural();
            //Переменная для определения файлов для записи
            boolean direction = true;
            //Флаг для определения когда нужно выходить из цикла
            boolean flag = true;
            while (flag) {
                //В зависимости от направления записи передаём файлы для чтения и записи
                if (direction) {
                    //Читать из b c, писать в d e
                    flag = switchFilesNatural(b, c, d, e);
                    //После этого меняем направление
                    direction = false;
                } else {
                    //Читать из d e, писать в b c
                    flag = switchFilesNatural(d, e, b, c);
                    //После этого меняем направление
                    direction = true;
                }
            }
            //Теперь объединяем обратно в файл a, используя последние редактируемые файлы
            if (direction)
                //Читать из b c
                toOneFileNatural(b, c);
            else //Читать из d e
                toOneFileNatural(d, e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //возвращаем разницу во времени с начала сортировке и до нынешнего момента
        return System.currentTimeMillis() - time;
    }
    //Перемещаем данные в файлах в методе естественного слияния. Принимает имена файлов для чтения и записи
    private boolean switchFilesNatural(String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        //Создаём классы для записи. в скобках имя файла
        PrintWriter writerToFirst = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecond = new PrintWriter(new FileWriter(secondToWrite));
        //Создаём классы для чтения
        //Использую самый быстрый класс для чтения из файла. При чтении возвращает токен, обозначающий, что он прочитал. Получить конкретное значение можно вызвав reader.nval
        StreamTokenizer readerFromFirst = new StreamTokenizer(new FileReader(firstToRead));
        StreamTokenizer readerFromSecond = new StreamTokenizer(new FileReader(secondToRead));
        //Переменная для определения файлов для записи
        boolean direction = true;
        //счётчик того, сколько цепочек будет в файлах после объединения (с прошлой версии поменял на окончание сортировки, если 2 цепочки, которые сольются в будущем)
        int counter = 0;
        //Читаем токены у первых чисел 2 файлов
        int first = readerFromFirst.nextToken();
        int second = readerFromSecond.nextToken();
        //Пока хотя в одном файле есть что читать, то продолжаем крутиться в цикле (StreamTokenizer.TT_EOF возвращется если файл закончился)
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            //Пока в обоих файлах есть что читать и не достигли конца цепочек, то продолжаем крутиться в цикле
            while (first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF && (int) readerFromFirst.nval != -2 && (int) readerFromSecond.nval != -2) {
                //Сравниваем 2 числа из разных файлов
                if ((int) readerFromFirst.nval < (int) readerFromSecond.nval) {
                    //В зависимости от направления записываем в необходимый файл число из первого файла если оно меньше
                    if (direction) {
                        writerToFirst.write((int) readerFromFirst.nval + " ");
                    } else {
                        writerToSecond.write((int) readerFromFirst.nval + " ");
                    }
                    //Читаем новый токен из первого файла
                    first = readerFromFirst.nextToken();
                } else {
                    //В зависимости от направления записываем в необходимый файл число из второго файла если оно меньше
                    if (direction) {
                        writerToFirst.write((int) readerFromSecond.nval + " ");
                    } else {
                        writerToSecond.write((int) readerFromSecond.nval + " ");
                    }
                    //Читаем новый токен из второго файла
                    second = readerFromSecond.nextToken();
                }
            }
            //Если вышли из цикла значит либо встретилось окончание цепочки либо закончился файл.
            //Дописываем числа из первого файла, если они остались в цепочке после сравнения со вторым файлом
            while (first != StreamTokenizer.TT_EOF && (int) readerFromFirst.nval != -2) {
                //В зависимости от направления записываем в необходимый файл число из первого файла
                if (direction)
                    writerToFirst.write((int) readerFromFirst.nval + " ");
                else writerToSecond.write((int) readerFromFirst.nval + " ");
                //Читаем новый токен из первого файла
                first = readerFromFirst.nextToken();
            }
            //Дописываем числа из второго файла, если они остались в цепочке после сравнения с первым файлом
            while (second != StreamTokenizer.TT_EOF && (int) readerFromSecond.nval != -2) {
                //В зависимости от направления записываем в необходимый файл число из второго файла
                if (direction)
                    writerToFirst.write((int) readerFromSecond.nval + " ");
                else writerToSecond.write((int) readerFromSecond.nval + " ");
                //Читаем новый токен из второго файла
                second = readerFromSecond.nextToken();
            }

            if (direction) {
                writerToFirst.write("-2 ");
            } else {
                writerToSecond.write("-2 ");
            }
            //Читаем новые токены, т.к. сейчас они указывают либо на конец файла либо на конец цепочки
            first = readerFromFirst.nextToken();
            second = readerFromSecond.nextToken();
            //Меняем направление
            if (direction) {
                direction = false;
            } else direction = true;
            //Прибавляем единицы к счётчику цепочек
            counter++;
        }
        //Даём команду классу для записи к добавлению текста в файл (до этого хранит текст в своём внутреннем буфере и если этого не сделать, то текст в файле не появится)
        writerToFirst.flush();
        writerToSecond.flush();
        //Закрываем потоки записи, чтобы освободить ресурсы и не вызвать после множества повторений краш из-за недостатка памяти
        writerToFirst.close();
        writerToSecond.close();
        //Если цепочек больше чем 2, то продолжаем сортировку
        return counter > 2;
    }
    //Объединение двух файлов в один для метода естественного слияния
    private void toOneFileNatural(String firstFile, String secondFile) throws IOException {
        //Создаём классы для записи.
        PrintWriter writer = new PrintWriter(new FileWriter("a.txt"));
        //Создаём классы для чтения
        StreamTokenizer readerFromFirstFile = new StreamTokenizer(new FileReader(firstFile));
        StreamTokenizer readerFromSecondFile = new StreamTokenizer(new FileReader(secondFile));
        //Читаем токены у первых чисел 2 файлов
        int first = readerFromFirstFile.nextToken();
        int second = readerFromSecondFile.nextToken();
        //Пока хотя в одном файле есть что читать, то продолжаем крутиться в цикле
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            //Пока хотя в обоих файлах есть что читать и не достигли конца цепочек, то продолжаем крутиться в цикле
            while (first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF && (int) readerFromFirstFile.nval != -2 && (int) readerFromSecondFile.nval != -2) {
                //Сравниваем 2 числа из разных файлов и записываем наименьшее в файл
                if (readerFromFirstFile.nval < readerFromSecondFile.nval) {
                    writer.write((int) readerFromFirstFile.nval + " ");
                    //Читаем новый токен для первого числа
                    first = readerFromFirstFile.nextToken();
                } else {
                    writer.write((int) readerFromSecondFile.nval + " ");
                    //Читаем новый токен для второго числа
                    second = readerFromSecondFile.nextToken();
                }
            }
            //Дописываем числа из первого файла, если они остались в цепочке после сравнения со вторым файлом
            while (first != StreamTokenizer.TT_EOF && (int) readerFromFirstFile.nval != -2) {
                writer.write((int) readerFromFirstFile.nval + " ");
                first = readerFromFirstFile.nextToken();
            }
            //Дописываем числа из второго файла, если они остались в цепочке после сравнения с числами из цепочки первого файла
            while (second != StreamTokenizer.TT_EOF && (int) readerFromSecondFile.nval != -2) {
                writer.write((int) readerFromSecondFile.nval + " ");
                second = readerFromSecondFile.nextToken();
            }
            //Читаем новые числа т.к. сейчас токены указывают на конец файла и конец цепочки
            first = readerFromFirstFile.nextToken();
            second = readerFromSecondFile.nextToken();
        }
        //Даём команду классу для записи к добавлению текста в файл
        writer.flush();
        //Закрываем поток записи, чтобы освободить ресурсы
        writer.close();
    }
    //Метод для замерки времени сортировки методом поглощения
    private long AbsorbSort(long size) {
        try {
            //Сохраняем время
            long time = System.currentTimeMillis();
            //Создаём объект, указываем ему файл с которым он работает и режим "чтение и запись". r - read, w - write
            randomAccessFile = new RandomAccessFile(new File("a.txt"), "rw");
            //Создаём буффер размера n
            int[] buffer = new int[n];
            //Заполняем буффер числами с конца файла
            for (int i = 0; i < n; i++) {
                buffer[i] = getNumberAt(size - 1 - i);
            }
            //Сортируем буффер (метод использует быструю сортировку, курсач про внешнюю сортировку, поэтому я думаю можно использовать системные методы сортировки)
            Arrays.sort(buffer);
            //Проверяем не больше ли размер буффера чем размер файла
            if (n > size) {
                //Если размер буффера больше, то поскольку он уже отсортирован, то записываем его в файл
                //ВАЖНО fileHelper.readNumber возвращет очень большое число, которое точно больше всех остальных чисел, если файл закончился и читать нечего
                //Поэтому при сортировке эти несуществующие числа будут в конце буффера.
                //Важно увидеть, что записываем только количество чисел, которое изначально было в файле
                for (int i = 0; i < size; i++) {
                    setNumberAt(i, buffer[i]);
                }
                //Освобождаем ресурсы
                randomAccessFile.close();
                //Возвращаем время т.к. сортировка окончена
                return System.currentTimeMillis() - time;
            }
            //Если буффер всё же меньше размера файла, то записываем его на прежнее место, только в отсортированном виде
            for (int i = 0; i < n; i++) {
                setNumberAt(size - n + i, buffer[i]);
            }
            //Количество прошедших повторений записи в буфер и сортировки. Нужно для определения положения чтения и записи
            int iterations = 1;
            //Крутимся в цикле пока количество итераций меньше либо равно числу итераций, которое должно пройти. Пример: 10/3 = 3.33(3), где 10 - размер файла, 3 - размер буффера; 3.33 - 1 = 2, где -1 нужен для учёта того, что после цикла придётся сортировать элементы, которые не занимают полностью буффер; 2 - Столько всего нужно итераций цикла для сортировки
            while (iterations <= size / n - 1) {
                //Заполняем буффер
                for (int i = 0; i < n; i++) {
                    /*Как получили число в скобках:
                    Число элементов size
                    Отчёт элементов с 0, поэтому последний индекс числа в файле size -1
                    n - размер буфера, iterations - количество итераций. При перемножении дают количество чисел, которые уже были отсортированы
                    i - вычитание индекса в буффере
                     */
                    buffer[i] = getNumberAt(size - 1 - (long) n * iterations - i);
                }
                //Сортируем буффер
                Arrays.sort(buffer);
                //Переменные для определения сколько чисел уже было записано из буффера и из файла
                int kBuffer = 0;
                int kFiles = 0;
                //Повторяем цикл столько раз, сколько отсортированных элементов у нас уже есть + размер буффера
                for (int i = 0; i < n + n * iterations; i++) {
                    //Если элемент буффера меньше элемента файла, то пишем из буффера,  иначе из файла
                    if (buffer[kBuffer] <= getNumberAt(size - (long) n * iterations + kFiles)) {
                        setNumberAt(size - (long) n * (iterations + 1) + i, buffer[kBuffer]);
                        //Прибавляем единицу к счётчику чисел, которые добавили из буффера
                        kBuffer++;
                        //Если все числа из буффера добавлены в файл, то значит, что остальная часть отсортирована и можно завершать цикл
                        /*Пример
                        Буффер 1 2 3 - отсортирован
                        Файл 3 1 2 4 5 6, где 4 5 6 - отсортированная ранее часть
                        Проходя по циклу заменяем в файле 3 на 1, 1 на 2, 2 на 3. 4 5 6 в замене не нуждаются - выходим из цикла
                         */
                        if (kBuffer == n) break;
                    } else {
                        /*Как получили число в скобках:
                        Число элементов count
                        -1 не нужен т.к. положение числа настраивается с помощью i
                        n - размер буфера, iterations+1 - количество итераций. При перемножении дают количество чисел, которые уже были отсортированы
                        i - прибавление индекса
                        */
                        setNumberAt(size - (long) n * (iterations + 1) + i, getNumberAt(size - (long) n * iterations + kFiles));
                        //Прибавляем единицу к переменной счётчика цифр, которые добавлены из файла
                        kFiles++;
                    }
                }
                //Прибавляем итерацию после её завершения
                iterations++;
            }
            //Количество элементов, которые останутся после сортировки, но не смогут полностью заполнить буффер. 10 по 3, останется 1 элемент
            int mod = (int) (size % n);
            //Заполняем буффер этими элементами
            for (int i = 0; i < mod; i++) {
                buffer[i] = getNumberAt(i);
            }
            //Заполняем буффер "большими числами", чтобы при сортировке они ушли в конец и не мешались
            for (int i = mod; i < n; i++) {
                buffer[i] = Integer.MAX_VALUE;
            }
            //Сортируем
            Arrays.sort(buffer);
            //Переменные для определения сколько чисел уже было записано из буффера и из файла
            int number = 0;
            int file = 0;
            //Проходим по всему файлу
            for (int i = 0; i < size - 1; i++) {
                //Сравниваем числа из буффера с числами из файла и записываем
                if (buffer[number] <= getNumberAt(mod + file)) {
                    setNumberAt(i, buffer[number]);
                    number++;
                    if (number == mod) break;
                } else {
                    setNumberAt(i, getNumberAt(mod + file));
                    file++;
                }
            }
            //Освобождаем ресурсы
            randomAccessFile.close();
            //Возвращаем время
            return System.currentTimeMillis() - time;
        } catch (IOException e) {
            return 0;
        }
    }
    //Метод для подсчёта времени, объединения в одну структуру и возвращения на UI
    public long[] startSort(long size) {
        //Создаём массив на 3 элемента - простая сортировка, естественная и метод поглощения
        long[] massOfResults = new long[3];
        try {
            createFiles(size);
            massOfResults[0] = SimpleSort(size);
            copy();
            massOfResults[1] = NaturalSort();
            copy();
            massOfResults[2] = AbsorbSort(size);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return massOfResults;
    }
    //Метод для чтения числа для метода поглощения. Принимает позицию числа в файле
    private int getNumberAt(long p) {
        try {
            //если позиция меньше нуля или превышает число чисел в файле, то возвращаем огромное число (pos*5L) - костыль, помогающий представлять числа как ячейки массива. В числе 4 символа цифр и пробел = 5 символов. L - приведение числа к long
            if (p < 0 || randomAccessFile.length() - 1 < p * 5L) {
                return Integer.MAX_VALUE;
            }
            //Перемещаем курсор к позиции
            randomAccessFile.seek(p * 5L);
            //Создаём продвинутый объект для хранения строк
            StringBuilder buffer = new StringBuilder();
            //переменная для очередного символа
            int ch;
            //Читаем символ и записываем в переменную, после чего сразу проверяем его на то пробел ли это
            while ((ch = randomAccessFile.read()) != 32 ) {
                //Если не пробел, то добавляем символ к строке
                buffer.append((char) ch);
            }
            //Если пробел, то приводим к числу и возвращаем
            return Integer.parseInt(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Если случилась какая-то жесть с файлом во время выполнения, то ломаем алгоритм и возвращаем -1
        return -1;
    }
    //Метод для записи числа в файл для метода поглощения. Принимает позицию и само число
    private void setNumberAt(long p, int num) {
        try {
            //если позиция меньше нуля или превышает число чисел в файле, то прерываем запись. В целом при полностью правильном алгоритме никогда не сработает, но если что-то пойдёт не так в алгоритме, то если это убрать, то программа сломается с концами
            if (p < 0 || randomAccessFile.length() - 1 < p * 5L) {
                return;
            }
            //Перемещаемся на позицию
            randomAccessFile.seek(p * 5L);
            //Записываем, сохраняя 4 значный формат числа
            randomAccessFile.writeBytes(String.format("%04d", num) + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Генерируем файл с данными массива в формате, где у числа всегда фиксированная длинна в 4 символа: 0001, 0010, 0100, 1000
    private void createFiles(long size) {
        try (FileWriter writer = new FileWriter("array.txt")) {
            //Объект для создания рандомных чисел
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                writer.write(String.format("%04d", random.nextInt(10000)) + " ");
            }
            writer.flush();
            //Клонируем файл
            copy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Метод для клонирования файлов
    private void copy() throws IOException {
        //Объект для записи
        try (PrintWriter writer = new PrintWriter(new FileWriter("a.txt"))) {
            //Объект для чтения
            StreamTokenizer reader = new StreamTokenizer(new FileReader("array.txt"));
            //Читаем токен
            int token = reader.nextToken();
            //Пока не достигнут конец файла крутимся в цикле
            while (token != StreamTokenizer.TT_EOF) {
                //пишем число, сохраняя 4 значный формат
                writer.write(String.format("%04d", (int) reader.nval) + " ");
                //Читаем новый токен
                token = reader.nextToken();
            }
            //говорим классу для записи напечатать в файл текст
            writer.flush();
        } catch (IOException e) {
        }

    }
    //Метод для разбиения чисел на 2 файла в методе простого слияния
    private void toTwoFiles() throws IOException {
        //Создаём классы для записи
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter("b.txt"));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter("c.txt"));
        //Создаём класс для чтения
        StreamTokenizer reader = new StreamTokenizer(new FileReader("a.txt"));
        //Переменная для определения файла для записи
        boolean direction = true;
        //Читаем первый токен
        int elem = reader.nextToken();
        //Читаем пока не закончатся числа
        while (elem != StreamTokenizer.TT_EOF) {
            //В зависимости от направления пишем в файл число
            if (direction) {
                writerToFirstFile.write((int) reader.nval + " ");
                //Меняем направление
                direction = false;
            } else {
                writerToSecondFile.write((int) reader.nval + " ");
                //Меняем направление
                direction = true;
            }
            //Читаем новый токен
            elem = reader.nextToken();
        }
        //Освобождаем ресурсы и говорим классам для записи напечатать в файлы текст
        writerToFirstFile.flush();
        writerToSecondFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.close();
    }
    //Метод для объединения 2 файлов в методе простого слияния
    private void toOneFile(long count, String firstFileToRead, String secondFileToRead) throws IOException {
        //Создаём класс для записи
        PrintWriter writer = new PrintWriter(new FileWriter("a.txt"));
        //Создаём классы для чтения
        StreamTokenizer readerFirst = new StreamTokenizer(new FileReader(firstFileToRead));
        StreamTokenizer readerSecond = new StreamTokenizer(new FileReader(secondFileToRead));
        //Читаем первые числа из 2 файлов
        int first = readerFirst.nextToken();
        int second = readerSecond.nextToken();
        //Крутимся в цикле, пока не достигнем конца в обоих файлах
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            //Выставляем длину для цепочек
            long localFirst = count;
            long localSecond = count;
            //Крутимся в цикле, пока не закончится место в цепочке или пока не достигнем конца файла
            while (localFirst != 0 && localSecond != 0 && first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF) {
                //Сравниваем 2 числа из 2 файлов
                if (readerFirst.nval < readerSecond.nval) {
                    //Если первое меньше, то записываем его в файл a
                    writer.write((int) readerFirst.nval + " ");
                    //Уменьшаем доступное место в цепочке для первого файла
                    localFirst--;
                    //Читаем новый токен из первого файла
                    first = readerFirst.nextToken();
                } else {
                    //Если второе меньше, то пишем его в файл а
                    writer.write((int) readerSecond.nval + " ");
                    //Уменьшаем доступное место в цепочке для второго файла
                    localSecond--;
                    //Читаем новый токен из второго файла
                    second = readerSecond.nextToken();
                }
            }
            //Либо закончилась цепочка для одного из файлов, либо данные в файле
            //Дописываем данные из первого файла, если второй либо закончился, либо достигнут лимит чисел в цепочке из второго
            while (localFirst != 0 && first != StreamTokenizer.TT_EOF) {
                writer.write((int) readerFirst.nval + " ");
                localFirst--;
                first = readerFirst.nextToken();
            }
            //Либо дописываем из второго
            while (localSecond != 0 && second != StreamTokenizer.TT_EOF) {
                writer.write((int) readerSecond.nval + " ");
                localSecond--;
                second = readerSecond.nextToken();
            }
        }
        //говорим классу для записи напечатать в файл текст
        writer.flush();
    }
    //Перемещаем данные в файлах в методе простого слияния. Принимает имена файлов для чтения и записи, а также длину цепочки
    private void switchFiles(long count, String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        //Создаём классы для записи
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(secondToWrite));
        //Создаём классы для чтения
        StreamTokenizer readerFirst = new StreamTokenizer(new FileReader(firstToRead));
        StreamTokenizer readerSecond = new StreamTokenizer(new FileReader(secondToRead));
        //Переменная для определения файлов для записи
        boolean direction = true;
        //Читаем токены у первых чисел 2 файлов
        int first = readerFirst.nextToken();
        int second = readerSecond.nextToken();
        //Пока хотя в одном файле есть что читать, то продолжаем крутиться в цикле
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            //Выставляем длину для цепочек
            long localFirst = count;
            long localSecond = count;
            //Пока в обоих файлах есть что читать и не достигли конца цепочек, то продолжаем крутиться в цикле
            while (localFirst != 0 && localSecond != 0 && first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF) {
                //Сравниваем 2 числа из 2 файлов
                if (readerFirst.nval < readerSecond.nval) {
                    //Если первое меньше, то записываем его в файл в зависимости от направления
                    if (direction)
                        writerToFirstFile.write((int) readerFirst.nval + " ");
                    else writerToSecondFile.write((int) readerFirst.nval + " ");
                    //Уменьшаем доступное место в цепочке для первого файла
                    localFirst--;
                    //Читаем новый токен из первого файла
                    first = readerFirst.nextToken();
                } else {
                    //Если второе меньше, то пишем его в файл в зависимости от направления
                    if (direction)
                        writerToFirstFile.write((int) readerSecond.nval + " ");
                    else writerToSecondFile.write((int) readerSecond.nval + " ");
                    //Уменьшаем доступное место в цепочке для второго файла
                    localSecond--;
                    //Читаем новый токен из второго файла
                    second = readerSecond.nextToken();
                }
            }
            //Дописываем данные из первого файла, если второй либо закончился, либо достигнут лимит чисел в цепочке из второго
            while (localFirst != 0 && first != StreamTokenizer.TT_EOF) {
                if (direction)
                    writerToFirstFile.write((int) readerFirst.nval + " ");
                else writerToSecondFile.write((int) readerFirst.nval + " ");
                localFirst--;
                first = readerFirst.nextToken();
            }
            //Либо дописываем из второго
            while (localSecond != 0 && second != StreamTokenizer.TT_EOF) {
                if (direction)
                    writerToFirstFile.write((int) readerSecond.nval + " ");
                else writerToSecondFile.write((int) readerSecond.nval + " ");
                localSecond--;
                second = readerSecond.nextToken();
            }
            //Меняем направление
            if (direction) {
                direction = false;
            } else direction = true;
        }
        //Освобождаем ресурсы и печатаем в файл
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }

    private void toTwoFilesNatural() throws IOException {
        //Создаём классы для записи
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter("b.txt"));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter("c.txt"));
        //Создаём класс для чтения
        StreamTokenizer reader = new StreamTokenizer(new FileReader("a.txt"));
        //Читаем токен
        int num = reader.nextToken();
        //Переменная для определения файлов для записи
        boolean direction = true;
        //Сохраняем предыдущее число. Ставим -1 чтобы первое число точно ушло в первый файл и не создало лишнего завершения цепочки.
        int last = -1;
        //Крутимся в цикле пока есть что читать
        while (num != StreamTokenizer.TT_EOF) {
            //Сравнниваем текущее число с предыдущим
            if (reader.nval < last) {
                //Если текущее больше предыдущего, то цепочка отсортированна (1, 3, где 1 - предыдущее, 3 - текущее)
                //Если нет, то цепочка не отсортирована и мы попадаем в этот if
                //Выбираем из направления в какой файл писать
                if (direction) {
                    //Меняем направление т.к. цепочка обрывается
                    direction = false;
                    //Ставим символ конца цепочки в файл, который мы писали
                    writerToFirstFile.write("-2 ");
                    //Записываем в другой файл число новой цепочки
                    writerToSecondFile.write((int) reader.nval + " ");
                } else {
                    //Меняем направление т.к. цепочка обрывается
                    direction = true;
                    //Ставим символ конца цепочки в файл, который мы писали
                    writerToSecondFile.write("-2 ");
                    //Записываем в другой файл число новой цепочки
                    writerToFirstFile.write((int) reader.nval + " ");
                }
            } else {
                //Сюда заходим если цепочка продолжается
                //В зависимости от направления пишем в файл прочитанное число
                if (direction) {
                    writerToFirstFile.write((int) reader.nval + " ");
                } else {
                    writerToSecondFile.write((int) reader.nval + " ");
                }
            }
            //Устанавливаем предыдущее число
            last = (int) reader.nval;
            //Читаем новый токен
            num = reader.nextToken();
        }
        //Меняем направление
        if (direction) {
            writerToFirstFile.write("-2 ");
        } else {
            writerToSecondFile.write("-2 ");
        }
        //Освобождаем ресурсы и печатаем в файл
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }
}
