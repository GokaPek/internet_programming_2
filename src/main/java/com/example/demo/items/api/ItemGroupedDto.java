package com.example.demo.items.api;

public class ItemGroupedDto {
    private Long id;
    private String name;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int ApplicationCount;

    public ItemGroupedDto() {
    }

    public ItemGroupedDto(Long id, int ApplicationCount) {
        this.id = id;
        this.ApplicationCount = ApplicationCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getApplicationCount() {
        return ApplicationCount;
    }

    public void setApplicationCount(int ApplicationCount) {
        this.ApplicationCount = ApplicationCount;
    }
}
