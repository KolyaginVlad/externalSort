package ru.externalsort.externalsort;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class Functions {
    final String b = "b.txt";
    final String c = "c.txt";
    final String d = "d.txt";
    final String e = "e.txt";
    RandomAccessFile randomAccessFile;
    private int n = 10;

    private long SimpleSort(long size) {
        long time = System.currentTimeMillis();
        System.out.println("simple");
        try {
            toTwoFiles();
            boolean direction = true;
            for (long i = 1; i < size / 2; i *= 2) {
                if (direction) {
                    switchFiles(i, b, c, d, e);
                    direction = false;
                } else {
                    switchFiles(i, d, e, b, c);
                    direction = true;
                }
            }
            if (direction)
                toOneFile(size, b, c);
            else toOneFile(size, d, e);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return System.currentTimeMillis() - time;
    }

    private long NaturalSort() {
        long time = System.currentTimeMillis();
        System.out.println("natural");
        try {
            toTwoFilesNatural();
            boolean direction = true;
            boolean flag = true;
            while (flag) {
                if (direction) {
                    flag = switchFilesNatural(b, c, d, e);
                    direction = false;
                } else {
                    flag = switchFilesNatural(d, e, b, c);
                    direction = true;
                }
            }
            if (direction)
                toOneFileNatural(b, c);
            else toOneFileNatural(d, e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis() - time;
    }

    private boolean switchFilesNatural(String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        PrintWriter writerToFirst = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecond = new PrintWriter(new FileWriter(secondToWrite));
        StreamTokenizer readerFromFirst = new StreamTokenizer(new FileReader(firstToRead));
        StreamTokenizer readerFromSecond = new StreamTokenizer(new FileReader(secondToRead));
        boolean direction = true;
        int counter = 0;
        int first = readerFromFirst.nextToken();
        int second = readerFromSecond.nextToken();
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            while (first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF && (int) readerFromFirst.nval != -2 && (int) readerFromSecond.nval != -2) {
                if ((int) readerFromFirst.nval < (int) readerFromSecond.nval) {
                    if (direction) {
                        writerToFirst.write((int) readerFromFirst.nval + " ");
                    } else {
                        writerToSecond.write((int) readerFromFirst.nval + " ");
                    }
                    first = readerFromFirst.nextToken();
                } else {
                    if (direction) {
                        writerToFirst.write((int) readerFromSecond.nval + " ");
                    } else {
                        writerToSecond.write((int) readerFromSecond.nval + " ");
                    }
                    second = readerFromSecond.nextToken();
                }
            }
            while (first != StreamTokenizer.TT_EOF && (int) readerFromFirst.nval != -2) {
                if (direction)
                    writerToFirst.write((int) readerFromFirst.nval + " ");
                else writerToSecond.write((int) readerFromFirst.nval + " ");
                first = readerFromFirst.nextToken();
            }
            while (second != StreamTokenizer.TT_EOF && (int) readerFromSecond.nval != -2) {
                if (direction)
                    writerToFirst.write((int) readerFromSecond.nval + " ");
                else writerToSecond.write((int) readerFromSecond.nval + " ");
                second = readerFromSecond.nextToken();
            }
            if (direction) {
                writerToFirst.write("-2 ");
            } else {
                writerToSecond.write("-2 ");
            }
            first = readerFromFirst.nextToken();
            second = readerFromSecond.nextToken();
            if (direction) {
                direction = false;
            } else direction = true;
            counter++;
        }
        writerToFirst.flush();
        writerToSecond.flush();
        writerToFirst.close();
        writerToSecond.close();
        return counter > 1;
    }

    private void toOneFileNatural(String firstFile, String secondFile) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("a.txt"));
        StreamTokenizer readerFromFirstFile = new StreamTokenizer(new FileReader(firstFile));
        StreamTokenizer readerFromSecondFile = new StreamTokenizer(new FileReader(secondFile));
        int first = readerFromFirstFile.nextToken();
        int second = readerFromSecondFile.nextToken();
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            while (first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF && (int) readerFromFirstFile.nval != -2 && (int) readerFromSecondFile.nval != -2) {
                if (readerFromFirstFile.nval < readerFromSecondFile.nval) {
                    writer.write((int) readerFromFirstFile.nval + " ");
                    first = readerFromFirstFile.nextToken();
                } else {
                    writer.write((int) readerFromSecondFile.nval + " ");
                    second = readerFromSecondFile.nextToken();
                }
            }
            while (first != StreamTokenizer.TT_EOF && (int) readerFromFirstFile.nval != -2) {
                writer.write((int) readerFromFirstFile.nval + " ");
                first = readerFromFirstFile.nextToken();
            }
            while (second != StreamTokenizer.TT_EOF && (int) readerFromSecondFile.nval != -2) {
                writer.write((int) readerFromSecondFile.nval + " ");
                second = readerFromSecondFile.nextToken();
            }
            first = readerFromFirstFile.nextToken();
            second = readerFromSecondFile.nextToken();
        }
        writer.flush();
    }

    private long AbsorbSort(long size) {
        try {
            long time = System.currentTimeMillis();
            randomAccessFile = new RandomAccessFile(new File("a.txt"), "rw");
            int[] buffer = new int[n];
            for (int i = 0; i < n; i++) {
                buffer[i] = getNumberAt(size - 1 - i);
            }
            Arrays.sort(buffer);
            if (n > size) {
                for (int i = 0; i < size; i++) {
                    setNumberAt(i, buffer[i]);
                }
                randomAccessFile.close();
                return System.currentTimeMillis() - time;
            }
            for (int i = 0; i < n; i++) {
                setNumberAt(size - n + i, buffer[i]);
            }
            int iterations = 1;
            while (iterations <= size / n - 1) {
                for (int i = 0; i < n; i++) {
                    buffer[i] = getNumberAt(size - 1 - (long) n * iterations - i);
                }
                Arrays.sort(buffer);
                int kBuffer = 0;
                int kFiles = 0;
                for (int i = 0; i < n + n * iterations; i++) {
                    if (buffer[kBuffer] <= getNumberAt(size - (long) n * iterations + kFiles)) {
                        setNumberAt(size - (long) n * (iterations + 1) + i, buffer[kBuffer]);
                        kBuffer++;
                        if (kBuffer == n) break;
                    } else {
                        setNumberAt(size - (long) n * (iterations + 1) + i, getNumberAt(size - (long) n * iterations + kFiles));
                        kFiles++;
                    }
                }
                iterations++;
            }
            int mod = (int) (size % n);

            for (int i = 0; i < mod; i++) {
                buffer[i] = getNumberAt(i);
            }
            for (int i = mod; i < n; i++) {
                buffer[i] = Integer.MAX_VALUE;
            }
            Arrays.sort(buffer);
            int number = 0;
            int file = 0;
            for (int i = 0; i < size - 1; i++) {
                if (buffer[number] <= getNumberAt(mod + file)) {
                    setNumberAt(i, buffer[number]);
                    number++;
                    if (number == mod) break;
                } else {
                    setNumberAt(i, getNumberAt(mod + file));
                    file++;
                }
            }
            randomAccessFile.close();
            return System.currentTimeMillis() - time;
        } catch (IOException e) {
            return 0;
        }
    }

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

    private int getNumberAt(long p) {
        try {
            if (p < 0 || randomAccessFile.length() - 1 < p * 5L) {
                return Integer.MAX_VALUE;
            }
            randomAccessFile.seek(p * 5L);
            StringBuilder buffer = new StringBuilder();
            int ch;
            while ((ch = randomAccessFile.read()) != 32 && randomAccessFile.getFilePointer() != randomAccessFile.length()) {
                buffer.append((char) ch);
            }
            return Integer.parseInt(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void setNumberAt(long p, int num) {
        try {
            if (p < 0 || randomAccessFile.length() - 1 < p * 5L) {
                return;
            }
            randomAccessFile.seek(p * 5L);
            randomAccessFile.writeBytes(String.format("%04d", num) + " ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFiles(long size) {
        try (FileWriter writer = new FileWriter("array.txt")) {
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                writer.write(String.format("%04d", random.nextInt(10000)) + " ");
            }
            writer.flush();
            copy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copy() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("a.txt"))) {
            StreamTokenizer reader = new StreamTokenizer(new FileReader("array.txt"));
            int token = reader.nextToken();
            while (token != StreamTokenizer.TT_EOF) {
                writer.write(String.format("%04d", (int) reader.nval) + " ");
                token = reader.nextToken();
            }
            writer.flush();
        } catch (IOException e) {
        }

    }

    private void toTwoFiles() throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter("b.txt"));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter("c.txt"));
        StreamTokenizer reader = new StreamTokenizer(new FileReader("a.txt"));
        boolean direction = true;
        int elem = reader.nextToken();
        while (elem != StreamTokenizer.TT_EOF) {
            if (direction) {
                writerToFirstFile.write((int) reader.nval + " ");
                direction = false;
            } else {
                writerToSecondFile.write((int) reader.nval + " ");
                direction = true;
            }
            elem = reader.nextToken();
        }
        writerToFirstFile.flush();
        writerToSecondFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.close();
    }

    private void toOneFile(long count, String firstFileToRead, String secondFileToRead) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("a.txt"));
        StreamTokenizer readerFirst = new StreamTokenizer(new FileReader(firstFileToRead));
        StreamTokenizer readerSecond = new StreamTokenizer(new FileReader(secondFileToRead));
        int first = readerFirst.nextToken();
        int second = readerSecond.nextToken();
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            long localFirst = count;
            long localSecond = count;
            while (localFirst != 0 && localSecond != 0 && first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF) {
                if (readerFirst.nval < readerSecond.nval) {
                    writer.write((int) readerFirst.nval + " ");
                    localFirst--;
                    first = readerFirst.nextToken();
                } else {
                    writer.write((int) readerSecond.nval + " ");
                    localSecond--;
                    second = readerSecond.nextToken();
                }
            }
            while (localFirst != 0 && first != StreamTokenizer.TT_EOF) {
                writer.write((int) readerFirst.nval + " ");
                localFirst--;
                first = readerFirst.nextToken();
            }
            while (localSecond != 0 && second != StreamTokenizer.TT_EOF) {
                writer.write((int) readerSecond.nval + " ");
                localSecond--;
                second = readerSecond.nextToken();
            }
        }
        writer.flush();
    }

    private void switchFiles(long count, String firstToRead, String secondToRead, String firstToWrite, String secondToWrite) throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter(firstToWrite));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter(secondToWrite));
        StreamTokenizer readerFirst = new StreamTokenizer(new FileReader(firstToRead));
        StreamTokenizer readerSecond = new StreamTokenizer(new FileReader(secondToRead));
        boolean direction = true;
        int first = readerFirst.nextToken();
        int second = readerSecond.nextToken();
        while (first != StreamTokenizer.TT_EOF || second != StreamTokenizer.TT_EOF) {
            long localFirst = count;
            long localSecond = count;
            while (localFirst != 0 && localSecond != 0 && first != StreamTokenizer.TT_EOF && second != StreamTokenizer.TT_EOF) {
                if (readerFirst.nval < readerSecond.nval) {
                    if (direction)
                        writerToFirstFile.write((int) readerFirst.nval + " ");
                    else writerToSecondFile.write((int) readerFirst.nval + " ");
                    localFirst--;
                    first = readerFirst.nextToken();
                } else {
                    if (direction)
                        writerToFirstFile.write((int) readerSecond.nval + " ");
                    else writerToSecondFile.write((int) readerSecond.nval + " ");
                    localSecond--;
                    second = readerSecond.nextToken();
                }
            }
            while (localFirst != 0 && first != StreamTokenizer.TT_EOF) {
                if (direction)
                    writerToFirstFile.write((int) readerFirst.nval + " ");
                else writerToSecondFile.write((int) readerFirst.nval + " ");
                localFirst--;
                first = readerFirst.nextToken();
            }
            while (localSecond != 0 && second != StreamTokenizer.TT_EOF) {
                if (direction)
                    writerToFirstFile.write((int) readerSecond.nval + " ");
                else writerToSecondFile.write((int) readerSecond.nval + " ");
                localSecond--;
                second = readerSecond.nextToken();
            }
            if (direction) {
                direction = false;
            } else direction = true;
        }
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }

    private void toTwoFilesNatural() throws IOException {
        PrintWriter writerToFirstFile = new PrintWriter(new FileWriter("b.txt"));
        PrintWriter writerToSecondFile = new PrintWriter(new FileWriter("c.txt"));
        StreamTokenizer reader = new StreamTokenizer(new FileReader("a.txt"));
        int num = reader.nextToken();
        boolean direction = true;
        int last = -1;
        while (num != StreamTokenizer.TT_EOF) {
            if (reader.nval < last) {
                if (direction) {
                    direction = false;
                    writerToFirstFile.write("-2 ");
                    writerToSecondFile.write((int) reader.nval + " ");
                } else {
                    direction = true;
                    writerToSecondFile.write("-2 ");
                    writerToFirstFile.write((int) reader.nval + " ");
                }
            } else {
                if (direction) {
                    writerToFirstFile.write((int) reader.nval + " ");
                } else {
                    writerToSecondFile.write((int) reader.nval + " ");
                }
            }
            last = (int) reader.nval;
            num = reader.nextToken();
        }
        if (direction) {
            writerToFirstFile.write("-2 ");
        } else {
            writerToSecondFile.write("-2 ");
        }
        writerToFirstFile.flush();
        writerToFirstFile.close();
        writerToSecondFile.flush();
        writerToSecondFile.close();
    }
}
