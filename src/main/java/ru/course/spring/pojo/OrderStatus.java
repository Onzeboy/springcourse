package ru.course.spring.pojo;

public enum OrderStatus {
    CREATED, SHIPPED, CANCELED, DELIVERED;

    @Override
    public String toString() {
        switch (this) {
            case CREATED:
                return "Создан";
            case SHIPPED:
                return "Отправлен";
            case CANCELED:
                return "Отменен";
            case DELIVERED:
                return "Доставлен";
            default:
                return super.toString();
        }
    }
}
