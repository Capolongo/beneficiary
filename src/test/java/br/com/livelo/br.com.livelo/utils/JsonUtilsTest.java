package br.com.livelo.br.com.livelo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JsonUtilsTest {

    @Test
    void whenConvertObjectWorks() {
        var product = JsonUtils.convert(new Product("product name", "description"));
        assertThat(product).isEqualTo("{\"name\":\"product name\",\"description\":\"description\"}");
    }

    @Test
    @SneakyThrows
    void whenConvertObjectFail() {
        var product = mock(Product.class);
        when(product.getName()).thenThrow(new NullPointerException());
        assertThatThrownBy(() -> JsonUtils.convert(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenConvertParamIsNull() {
        assertThatThrownBy(() -> JsonUtils.convert(null))
                .hasMessage("parameter obj cannot be null")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Getter
    @AllArgsConstructor
    private static class Product {
        private String name;
        private String description;
    }
}