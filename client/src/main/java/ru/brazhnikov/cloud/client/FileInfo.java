package ru.brazhnikov.cloud.client;

public class FileInfo {
    private String name;
    private long size;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileInfo(String name, long size) {
        this.name = name;
        this.size = size;
    }
}
