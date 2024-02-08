package br.com.livelo.orderflight.utils;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StringPrefixedSequenceIdGeneratorTest {
    @Mock
    private StringPrefixedSequenceIdGenerator stringPrefixedSequenceIdGenerator;

    @Test
    void shouldGeneratePrefix() {
        var expected = "lf1";
        var sessionMock = mock(SharedSessionContractImplementor.class);
        var objectMock = mock(Object.class);
        when(stringPrefixedSequenceIdGenerator.generate(any(), any())).thenReturn(expected);
        var response = this.stringPrefixedSequenceIdGenerator.generate(sessionMock, objectMock);

        assertEquals(expected, response);
    }

//    @Test
//    void shouldDoConfiguration() {
//        Type type = mock(Type.class);
//        Properties params = mock(Properties.class);
//        ServiceRegistry serviceRegistry = mock(ServiceRegistry.class);
//        doNothing().when(this.stringPrefixedSequenceIdGenerator).configure(any(eq(Type.class)), any(eq(Properties.class)), any(eq(ServiceRegistry.class)));
//        doNothing().when(this.sequenceStyleGenerator).configure(any(eq(Type.class)), any(eq(Properties.class)), any(eq(ServiceRegistry.class)));
//
//        this.stringPrefixedSequenceIdGenerator.configure(type, params, serviceRegistry);
//        // Verificar se o método configure da superclasse foi chamado
//        verify(stringPrefixedSequenceIdGenerator, times(1)).configure(type, params, serviceRegistry);
//
//        // Adicione mais verificações conforme necessário
//    }
}
