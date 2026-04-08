package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationDataType;
import com.ohpenl.midoffice.configurationtracker.domain.ConfigurationType;
import com.ohpenl.midoffice.configurationtracker.problem.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ConfigurationChangeValidatorTest {

    @Spy
    @SuppressWarnings("unused")
    private List<DataValidator> validators = List.of(
            new StringDataValidator(),
            new NumberDataValidator(),
            new BooleanDataValidator()
    );

    @InjectMocks
    private ConfigurationChangeValidator configurationChangeValidator;

    @Test
    void validateDataTypes_shouldPass_whenValidString() {
        var type = new ConfigurationType(ConfigurationDataType.STRING, "name", 1L, 0L);

        assertThatCode(() -> configurationChangeValidator.validateDataTypes("hello", type))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDataTypes_shouldThrow_whenInvalidNumber() {
        var type = new ConfigurationType(ConfigurationDataType.NUMBER, "count", 1L, 0L);

        assertThatThrownBy(() -> configurationChangeValidator.validateDataTypes("abc", type))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void validateDataTypes_shouldPass_whenValidNumber() {
        var type = new ConfigurationType(ConfigurationDataType.NUMBER, "count", 1L, 0L);

        assertThatCode(() -> configurationChangeValidator.validateDataTypes("42.5", type))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDataTypes_shouldPass_whenValidBoolean() {
        var type = new ConfigurationType(ConfigurationDataType.BOOLEAN, "flag", 1L, 0L);

        assertThatCode(() -> configurationChangeValidator.validateDataTypes("true", type))
                .doesNotThrowAnyException();
    }

    @Test
    void validateDataTypes_shouldThrow_whenInvalidBoolean() {
        var type = new ConfigurationType(ConfigurationDataType.BOOLEAN, "flag", 1L, 0L);

        assertThatThrownBy(() -> configurationChangeValidator.validateDataTypes("yes", type))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void validateDataTypes_shouldSkip_whenValueIsNull() {
        var type = new ConfigurationType(ConfigurationDataType.STRING, "name", 1L, 0L);

        assertThatCode(() -> configurationChangeValidator.validateDataTypes(null, type))
                .doesNotThrowAnyException();
    }
}
