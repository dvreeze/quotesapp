/*
 * Copyright 2024-2024 Chris de Vreeze
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.cdevreeze.tryquarkus.quotes.model;

import com.google.common.collect.ImmutableSet;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Quote test, testing JSON-B serialization.
 *
 * @author Chris de Vreeze
 */
class QuoteTest {

    @Test
    void testJsonbSerialization() throws Exception {
        var quote = new Quote(
                """
                        Legitimate use of violence can only be that which is required in self-defense.""",
                "Ron Paul",
                ImmutableSet.of("defense")
        );

        try (Jsonb jsonb = JsonbBuilder.create()) {
            String quoteJsonString = jsonb.toJson(quote.toJsonbQuote());

            assertTrue(quoteJsonString.contains("Ron Paul"));
        }
    }

    @Test
    void testJsonbRoundtripping() throws Exception {
        var quote = new Quote(
                """
                        Legitimate use of violence can only be that which is required in self-defense.""",
                "Ron Paul",
                ImmutableSet.of("defense")
        );

        try (Jsonb jsonb = JsonbBuilder.create()) {
            String quoteJsonString = jsonb.toJson(quote.toJsonbQuote());

            assertTrue(quoteJsonString.contains("Ron Paul"));

            Quote quote2 = Quote.fromJsonbQuote(jsonb.fromJson(quoteJsonString, Quote.JsonbQuote.class));

            assertEquals(quote, quote2);
        }
    }
}
