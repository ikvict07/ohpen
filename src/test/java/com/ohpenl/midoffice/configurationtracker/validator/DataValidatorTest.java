package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class DataValidatorTest {

    @Nested
    class StringDataValidatorTest {
        private final StringDataValidator validator = new StringDataValidator();

        @Test
        void dataType_shouldReturnString() {
            assertThat(validator.dataType()).isEqualTo(ConfigurationDataType.STRING);
        }

        @Test
        void isValid_shouldReturnTrue_forNonEmptyString() {
            assertThat(validator.isValid("hello")).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        void isValid_shouldReturnFalse_forBlankString(String input) {
            assertThat(validator.isValid(input)).isFalse();
        }
    }

    @Nested
    class NumberDataValidatorTest {
        private final NumberDataValidator validator = new NumberDataValidator();

        @Test
        void dataType_shouldReturnNumber() {
            assertThat(validator.dataType()).isEqualTo(ConfigurationDataType.NUMBER);
        }

        @ParameterizedTest
        @ValueSource(strings = {"42", "-1", "3.14", "-0.5"})
        void isValid_shouldReturnTrue_forValidNumbers(String input) {
            assertThat(validator.isValid(input)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"abc", "", "12a", "1.2.3"})
        void isValid_shouldReturnFalse_forInvalidNumbers(String input) {
            assertThat(validator.isValid(input)).isFalse();
        }
    }

    @Nested
    class BooleanDataValidatorTest {
        private final BooleanDataValidator validator = new BooleanDataValidator();

        @Test
        void dataType_shouldReturnBoolean() {
            assertThat(validator.dataType()).isEqualTo(ConfigurationDataType.BOOLEAN);
        }

        @ParameterizedTest
        @ValueSource(strings = {"true", "false", "TRUE", "False"})
        void isValid_shouldReturnTrue_forValidBooleans(String input) {
            assertThat(validator.isValid(input)).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"yes", "no", "1", "0", ""})
        void isValid_shouldReturnFalse_forInvalidBooleans(String input) {
            assertThat(validator.isValid(input)).isFalse();
        }
    }
}
