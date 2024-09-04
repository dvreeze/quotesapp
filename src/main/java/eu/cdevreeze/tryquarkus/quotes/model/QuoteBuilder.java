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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * Builder of a quote record. It uses Jakarta Bean Validation.
 *
 * @author Chris de Vreeze
 */
@Getter
@Setter
public class QuoteBuilder {

    @NotBlank
    private String quoteText;

    @NotBlank
    private String attributedTo;

    @NotEmpty
    private Set<@NotBlank String> subjects;

    public QuoteBuilder() {
    }

    public QuoteBuilder(String quoteText, String attributedTo, Set<String> subjects) {
        this.quoteText = quoteText;
        this.attributedTo = attributedTo;
        this.subjects = new HashSet<>(subjects);
    }

    public Quote build() {
        return new Quote(quoteText, attributedTo, ImmutableSet.copyOf(subjects));
    }
}
