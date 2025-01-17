package org.apache.coyote.http11.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class UriTest {

    @DisplayName("Uri로부터 경로를 파싱한다")
    @ParameterizedTest
    @CsvSource(value = {"/api/login?account=gugu, /api/login"})
    void parsePath(final String uri, final String expected) throws URISyntaxException {
        final Uri actual = Uri.parse(new URI(uri));

        assertThat(actual.getPath()).isEqualTo(expected);
    }
}
