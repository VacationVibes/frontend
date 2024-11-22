package com.github.sviatoslavslysh.vacationvibes.model;

public class Place {
    private static long counter = 0L;
    private final long id;
    private final String name;
    private final String city;
    private final String url;

    public Place(String name, String city, String url) {
        this.id = counter++;
        this.name = name;
        this.city = city;
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getUrl() {
        return url;
    }
}
