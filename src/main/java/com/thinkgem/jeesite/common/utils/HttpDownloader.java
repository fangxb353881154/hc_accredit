package com.thinkgem.jeesite.common.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Calendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/9/23.
 */

public class HttpDownloader implements Callable<String> {
    URLConnection connection;
    FileChannel outputChann;
    public static volatile int count = 0;

    public static void main(String[] args) throws Exception {

        ExecutorService poll = Executors.newFixedThreadPool(2);

        for (int i = 0; i < 2000; i++) {
            Calendar now = Calendar.getInstance();
            String fileName = "d:/data/" + now.get(Calendar.YEAR) + "年"
                    + (now.get(Calendar.MONTH) + 1) + "月"
                    + now.get(Calendar.DAY_OF_MONTH) + "日--" + i + ".txt";
            poll.submit(new HttpDownloader(
                    "http://m.fang.com/bbs/changchun/1710763523/182859674.htm",
                    (new FileOutputStream(fileName)).getChannel()));
        }

        poll.shutdown();

        long start = System.currentTimeMillis();
        while (!poll.isTerminated()) {
            Thread.sleep(1000);
            System.out.println("已运行"
                    + ((System.currentTimeMillis() - start) / 1000) + "秒，"
                    + HttpDownloader.count + "个任务还在运行");
        }
    }

    public HttpDownloader(String url, FileChannel fileChannel) throws Exception {
        synchronized (HttpDownloader.class) {
            count++;
        }
        connection = (new URL(url)).openConnection();
        this.outputChann = fileChannel;
    }

    @Override
    public String call() throws Exception {
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        ReadableByteChannel rChannel = Channels.newChannel(inputStream);
        outputChann.transferFrom(rChannel, 0, Integer.MAX_VALUE);
        // System.out.println(Thread.currentThread().getName() + " completed!");
        inputStream.close();
        outputChann.close();
        synchronized (HttpDownloader.class) {
            count--;
        }
        return null;
    }
}
