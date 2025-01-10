package de.conet.isd.skima.skimabackend.domain.entities._common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests {@link PersistableEnumConverter}
 */
class PersistableEnumConverterTest {

    private static enum TestEnum implements PersistableEnum<String> {
        CONST_1("C1"), CONST_2("C2"), CONST_3("C3");

        private static class TestEnumConverter extends PersistableEnumConverter<TestEnum, String> {
            @Override
            protected Class<TestEnum> getEnumType() {
                return TestEnum.class;
            }
        }

        private final String enumId;

        TestEnum(String enumId) {
            this.enumId = enumId;
        }

        @Override
        public String getDbId() {
            return enumId;
        }
    }

    @Test
    @DisplayName("convertToEntityAttribute should find an existing enum constant for a given database-enumId")
    void convertToEntityAttribute() {
        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter();
        TestEnum result = underTest.convertToEntityAttribute("C2");
        assertEquals(TestEnum.CONST_2, result, "Result for id 'C2' should be TestEnum.CONST_2");
    }

    @Test
    @DisplayName("convertToEntityAttribute should not throw excpetions on null input")
    void convertToEntityAttributeNullsafe() {
        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter();
        assertNull(underTest.convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("convertToEntityAttribute should throw an exception when an enum constant is not found," +
                 " when in strict mode.")
    void convertToEntityStrictModeAttributeFails() {

        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            underTest.convertToEntityAttribute("Not Existing ID");
        });
    }

    @Test
    @DisplayName("convertToEntityAttribute should return null when an enum constant is not found," +
                 " when not in strict mode.")
    void convertToEntityNonStrictReturnsNull() {
        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter() {
            @Override
            protected boolean strict() {
                return false;
            }
        };
        assertNull(underTest.convertToEntityAttribute("Not Existing ID"));
    }

    @Test
    @DisplayName("convertToDatabaseColumn should return null when an enum constant is not found.")
    void convertToDatabaseColumn() {
        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter();
        Assertions.assertEquals(TestEnum.CONST_3.getDbId(), underTest.convertToDatabaseColumn(TestEnum.CONST_3));
    }

    @Test
    @DisplayName("convertToDatabaseColumn should not throw Exceptions with null as input.")
    void convertToDatabaseColumnNullsafe() {
        PersistableEnumConverter<TestEnum, String> underTest = new TestEnum.TestEnumConverter();
        Assertions.assertNull(underTest.convertToDatabaseColumn(null));
    }
}
