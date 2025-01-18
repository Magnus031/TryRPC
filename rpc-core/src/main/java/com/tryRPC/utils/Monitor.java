package com.tryRPC.utils;

import cn.hutool.core.io.watch.WatchMonitor;
import cn.hutool.core.io.watch.Watcher;
import com.tryRPC.Holder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.WatchEvent;

@Slf4j
public class Monitor {
    /**
     * This is a monitor class for the RPC framework.
     */
    private final File file;

    /**
     * This construtor function is used to record for the input fileName.
     * @param file
     */
    public Monitor(File file) {
        this.file = file;
    }

    public Monitor(){
        /**
         * 1. Get the configuration file name.
         * 2. Get the file object from the load dictory;
         */
        String fileName = "rpc-consumer/src/main/resources/application.yaml";
        //System.out.println("Working Directory:" + System.getProperty("user.dir"));
        file = new File(fileName);
        System.out.println(file);
    }

    public void startWatching(){
        System.out.println("Now we are starting to watch the file");
        if(file==null||!file.exists()){
            throw new IllegalArgumentException("File not found");
        }
        WatchMonitor monitor = WatchMonitor.create(file, WatchMonitor.ENTRY_MODIFY);
        monitor.setWatcher(new Watcher(){
            // Create
            @Override
            public void onCreate(WatchEvent<?> event, Path currentPath) {
                reload();
                System.out.println("File created: " + event.context() + " in " + currentPath);
            }
            // Delete
            @Override
            public void onDelete(WatchEvent<?> event, Path currentPath) {
                reload();
                System.out.println("File deleted: " + event.context() + " in " + currentPath);
            }

            // Overflow
            @Override
            public void onOverflow(WatchEvent<?> event, Path currentPath) {
                reload();
                System.out.println("File modified: " + event.context() + " in " + currentPath);
            }

            // Modify
            @Override
            public void onModify(WatchEvent<?> event, Path currentPath) {
                reload();
                System.out.println("File modified: " + event.context() + " in " + currentPath);
            }

        });
        // start for the thread of the monitor.
        monitor.start();
    }

    private void reload(){
        try{
            String fileName = file.getAbsolutePath();
            System.out.println("The fileName is : "+fileName);
            RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, fileName);
            System.out.println(rpcConfig);
            Holder.init(rpcConfig);
            System.out.println("[Info] : Config reloaded successfully.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
