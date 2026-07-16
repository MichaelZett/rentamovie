package de.zettsystems.rentamovie.common.domain;

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

    @Test
    void shouldDetectDifferentTypes() {
        TestIdCarrier first = new TestIdCarrier(7L);
        OtherIdCarrier second = new OtherIdCarrier(7L);

        assertThat(first).isNotEqualTo(second);
    }

    private static class TestIdCarrier extends AbstractIdCarrier {
        TestIdCarrier(Long id) {
            super(id);
        }
    }

    private static class OtherIdCarrier extends AbstractIdCarrier {
        OtherIdCarrier(Long id) {
            super(id);
        }
    }
}
