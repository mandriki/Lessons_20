package taskPull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Random random=new Random(300);
    public static void main(String[] args) {
        /**1. Написать программу, бесконечно считывающую пользовательские числа из консоли.
         Эти числа представляют собой количество секунд.
         При каждом вводе числа, должна создаваться задача, которая засыпает на введённое число секунд и затем выводит "Я спал N секунд".
         Однако нужно сделать так, чтобы все задачи выполнялись в одном и том же потоке в порядке ввода.
         Т.е. в программе есть 2 потока: главный и поток для выполнения всех задач по очереди.
         При вводе -1 программа должна завершать свою работу.*/
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)))) {
            while (true) {
                int number = scanner.nextInt();
                if (number == -1) {
                    break;
                }
                executorService.execute(new CustomRunnable(number));
            }
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**2*. Задан массив случайных целых чисел (от 1 до 300) случайной длины (1 млн элементов).
         Найти максимальный элемент в массиве двумя способами: в одном потоке и используя 10 потоков.
         Сравнить затраченное в обоих случаях время.*/
        int a=random.nextInt(1000000);
        int []arr=new int[a];
        new Thread(new SortArr(arr));
        for (int i = 0; i <arr.length ; i++) {
            arr[i]= random.nextInt(300);
        }
        Thread qwe1=new Thread(new SortArr(arr));
        qwe1.start();
        ExecutorService executorService1 = Executors.newFixedThreadPool(10);
        for (int i = 0; i <10 ; i++) {
            executorService1.execute(new SortArr(arr));
        }
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(1L,TimeUnit.HOURS );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class CustomRunnable implements Runnable{
        private int second;
        public CustomRunnable(int second) {
            this.second = second;
        }
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName());
                Thread.sleep(second * 1000);
                System.out.println("Я спал " + second + " секунд");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class SortArr implements Runnable {
        private static int[] arr;
        public SortArr(int[] arr) {
            SortArr.arr = arr;
        }
        public void run() {
                synchronized (arr) {
                long timeIn = System.nanoTime();
                System.out.println(Thread.currentThread() + "поток зашел");
                int max = arr[0];
                    for (int value : arr) {
                        if (max < value)
                            max = value;
                    }
                System.out.println(max);
                long timeout = System.nanoTime() - timeIn;
                System.out.println(Thread.currentThread() + " поток вышел время -" + timeout);
            }
        }
    }

}


/**1. Написать программу, бесконечно считывающую пользовательские числа из консоли.
        Эти числа представляют собой количество секунд.
        При каждом вводе числа, должна создаваться задача, которая засыпает на введённое число секунд и затем выводит "Я спал N секунд".
        Однако нужно сделать так, чтобы все задачи выполнялись в одном и том же потоке в порядке ввода.
        Т.е. в программе есть 2 потока: главный и поток для выполнения всех задач по очереди.
        При вводе -1 программа должна завершать свою работу.


        2*. Задан массив случайных целых чисел (от 1 до 300) случайной длины (1 млн элементов).
        Найти максимальный элемент в массиве двумя способами: в одном потоке и используя 10 потоков.
        Сравнить затраченное в обоих случаях время.*/
