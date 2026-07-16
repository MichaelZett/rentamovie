package de.zettsystems.rentamovie.customer.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled("Z9 persists to disk; integration test would need a temp working directory")
class CustomerRepositoryImplTest {

	@Test
    void shouldReadAndSave() {
        // Disabled: requires file-system isolation. See class-level @Disabled note.
	}
}
