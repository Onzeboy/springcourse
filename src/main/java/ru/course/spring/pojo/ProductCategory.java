package ru.course.spring.pojo;

public enum ProductCategory {
    BAKERY("Выпечка"),
    VEGETABLES("Овощи"),
    CEREALS("Крупы"),
    PREPAREDFOODS("Готовая еда"),
    FRUITS("Фрукты");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}