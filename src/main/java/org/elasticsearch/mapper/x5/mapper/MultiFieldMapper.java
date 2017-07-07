package org.elasticsearch.mapper.x5.mapper;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.mapper.x5.annotations.fieldtype.MultiField;
import org.elasticsearch.mapper.x5.annotations.fieldtype.MultiNestedField;

import java.io.IOException;
import java.lang.reflect.Field;

public class MultiFieldMapper {

    public static boolean isValidMultiFieldType(Field field) {
        Class<?> fieldClass = field.getType();
        return String.class.isAssignableFrom(fieldClass) && field.isAnnotationPresent(MultiField.class);
    }

    public static void mapDataType(XContentBuilder mappingBuilder, Field field) throws IOException {
        if (!isValidMultiFieldType(field)) {
            throw new IllegalArgumentException(
                    String.format("field type[%s] is invalid type of string.", field.getType()));
        }
        MultiField multiField = field.getDeclaredAnnotation(MultiField.class);
        StringFieldMapper.mapDataType(mappingBuilder, multiField.mainField());

        mappingBuilder.startObject("fields");

        for (MultiNestedField otherField : multiField.fields()) {
            mappingBuilder.startObject(otherField.name());
            StringFieldMapper.mapDataType(mappingBuilder, otherField.field());
            mappingBuilder.endObject();
        }

        mappingBuilder.endObject();
    }
}
