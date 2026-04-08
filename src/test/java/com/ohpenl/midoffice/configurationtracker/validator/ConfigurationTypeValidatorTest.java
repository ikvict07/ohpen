package com.ohpenl.midoffice.configurationtracker.validator;

import com.ohpenl.midoffice.configurationtracker.problem.exception.AlreadyExistsException;
import com.ohpenl.midoffice.configurationtracker.repository.ConfigurationTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConfigurationTypeValidatorTest {

    @Mock
    private ConfigurationTypeRepository configurationTypeRepository;

    @InjectMocks
    private ConfigurationTypeValidator validator;

    @Test
    void validateUniqueName_shouldPass_whenNameNotExists() {
        given(configurationTypeRepository.existsByName("new-type")).willReturn(false);

        assertThatCode(() -> validator.validateUniqueName("new-type"))
                .doesNotThrowAnyException();
    }

    @Test
    void validateUniqueName_shouldThrow_whenNameExists() {
        given(configurationTypeRepository.existsByName("existing")).willReturn(true);

        assertThatThrownBy(() -> validator.validateUniqueName("existing"))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }
}
