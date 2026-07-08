package hh.fernuni.rentamovie.common.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractIdCarrierTest {

    @Test
    void shouldUseIdForEquality() {
        TestIdCarrier first = new TestIdCarrier(7L);
        TestIdCarrier second = new TestIdCarrier(7L);

        assertThat(first).isEqualTo(second);
        assertThat(first).hasSameHashCodeAs(second);
    }

    @Test
    void shouldDetectDifferentIds() {
        TestIdCarrier first = new TestIdCarrier(7L);
        TestIdCarrier second = new TestIdCarrier(8L);

        assertThat(first).isNotEqualTo(second);
    }

    private static class TestIdCarrier extends AbstractIdCarrier {
        TestIdCarrier(Long id) {
            super(id);
        }
    }
}
