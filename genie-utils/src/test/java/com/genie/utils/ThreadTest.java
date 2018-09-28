package com.genie.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadTest {


    @Test
    public void testListReadThread() throws InterruptedException {
        List<String> list = new ArrayList<>();
        ThreadGroup group = new ThreadGroup("List-Thread-Group");
        for(int j = 1; j <= 10; j++) {
            System.out.println("New Loop " + j);
            for (int i = 1; i <= 1000; i++) {
                list.add("String" + i);
                if (i % 100 == 0) {
                    System.out.println("Start thread " + i);
                    Thread thread = new Thread(group, new ListReadThread(list));
                    thread.start();
                    list = new ArrayList<>();
                }
            }
            while (group.activeCount() > 0) {
                Thread.yield();
            }
        }
    }

    @Test
    public void testListReadInFixThread() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        List threadList = new ArrayList();
        long threadSize = 3;
        long yieldNum = 0;
        ThreadGroup group = new ThreadGroup("List-Thread-Group");
        for(int j = 1; j <= 5; j++) {

            System.out.println("New Loop " + j);
            for (int i = 1; i <= 1000; i++) {
                list.add("String" + i);
                if (i % 100 == 0) {
//                    while (group.activeCount() >= threadSize) {
//                        Thread.yield();
//                        yieldNum++;
//                    }
                    while(threadList.size() >= threadSize){
                        clearThread(threadList);
                        Thread.yield();
//                        Thread.sleep(20);
                    }
                    System.out.println("Start thread " + i);
                    Thread thread = new Thread(group, new ListReadThread(list));
                    threadList.add(thread);
                    thread.start();
                    list = new ArrayList<>();
                }
            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("yieldNum : " + yieldNum);
        System.out.println("Finish in " + (endTime - startTime) + " ms.");
    }

    public void clearThread(List threadList) {
        Iterator<Thread> it = threadList.iterator();
        while (it.hasNext()) {
            Thread thread = it.next();
            if (!thread.isAlive()) {
                it.remove();
            }
        }
    }

    @Test
    public void testExecutorService() throws InterruptedException {
        ThreadGroup group = new ThreadGroup("List-Thread-Group");
        List<String> list = new ArrayList<>();
        for(int t = 1; t <= 10; t++) {
            System.out.println("New Loop " + t);
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
            for (int i = 1; i < 1000; i++) {
                String content = "String";
                for (int j = 0; j < 10000; j++) {
                    content.concat("String");
                }
                list.add(content.concat(String.valueOf(i)));
                if (i % 100 == 0) {
                    System.out.println("Start thread " + i);
                    Thread thread = new Thread(group, new ListReadThread(list));
                    fixedThreadPool.execute(thread);
                    list = new ArrayList<>();
                }
            }
            fixedThreadPool.shutdown();
            while (!fixedThreadPool.isTerminated()){
                Thread.yield();
            }
//            while (group.activeCount() > 0) {
//                Thread.yield();
//            }

        }
        Thread.sleep(10000);
        Runtime.getRuntime().totalMemory();
        Runtime.getRuntime().gc();

        Thread.sleep(10000);
    }

    @Test
    public void testExecutorServiceSubmit() throws InterruptedException {
        ThreadGroup group = new ThreadGroup("List-Thread-Group");
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        List<String> list = new ArrayList<>();
        for(int t = 1; t <= 10; t++) {
            System.out.println("New Loop " + t);
            for (int i = 1; i < 1000; i++) {
                String content = "String";
                list.add(content.concat(String.valueOf(i)));
                if (i % 100 == 0) {
                    System.out.println("Start thread " + i);
                    Thread thread = new Thread(group, new ListReadThread(list));
                    fixedThreadPool.submit(thread);
                    list = new ArrayList<>();
                }
            }
            while (group.activeCount() > 0) {
                Thread.yield();
            }
        }
        System.out.println("==================");
        Thread.sleep(10000);
    }

    class ListReadThread extends Thread {

        List list;

        public ListReadThread(List list) {
            this.list = list;
        }


        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                Thread.sleep(1000);
                long endTime = System.currentTimeMillis();

                System.out.println("deal list [" + list.hashCode() + "]size : " + list.size() + " in " + (endTime - startTime) + " ms");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
