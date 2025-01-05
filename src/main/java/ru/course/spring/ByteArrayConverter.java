package ru.course.spring;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ByteArrayConverter implements AttributeConverter<byte[], byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(byte[] attribute) {
        return attribute;
    }

    @Override
    public byte[] convertToEntityAttribute(byte[] dbData) {
        return dbData;
    }
}