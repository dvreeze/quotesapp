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
import jakarta.json.bind.annotation.JsonbCreator;

import java.util.List;

/**
 * Immutable quote record. This record can indirectly be converted to JSON out of the box (using JSON-B).
 *
 * @author Chris de Vreeze
 */
public record Quote(String quoteText, String attributedTo, ImmutableSet<String> subjects) {

    public Quote {
    }

    public JsonbQuote toJsonbQuote() {
        return new JsonbQuote(quoteText(), attributedTo(), subjects().stream().sorted().toList());
    }

    public static Quote fromJsonbQuote(JsonbQuote jsonbQuote) {
        return new Quote(jsonbQuote.quoteText(), jsonbQuote.attributedTo(), ImmutableSet.copyOf(jsonbQuote.subjects()));
    }

    public record JsonbQuote(String quoteText, String attributedTo, List<String> subjects) {

        @JsonbCreator
        public JsonbQuote {
        }
    }
}
