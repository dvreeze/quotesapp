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

package eu.cdevreeze.tryquarkus.quotes.web;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonPointer;
import jakarta.json.JsonValue;
import jakarta.json.spi.JsonProvider;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Quote web resource test.
 *
 * @author Chris de Vreeze
 */
@QuarkusTest
class QuoteResourceTest {

    private final JsonProvider jsonProvider = JsonProvider.provider();

    @Test
    void testQuotesEndpoint() {
        Response response =
                given()
                        .when().get("/quotes")
                        .then()
                        .extract().response();

        assertEquals(200, response.statusCode());
        assertTrue(response.contentType().startsWith("application/json"));

        JsonBuilderFactory jbf = jsonProvider.createBuilderFactory(Map.of());

        JsonValue jsonPayload =
                jsonProvider.createReader(new StringReader(response.getBody().asPrettyString())).readValue();
        assertTrue(jsonPayload.asJsonArray().contains(
                jbf.createObjectBuilder()
                        .add("quoteText", "Real patriotism is a willingness to challenge the government when it's wrong.")
                        .add("attributedTo", "Ron Paul")
                        .add("subjects", jbf.createArrayBuilder(Set.of("patriotism", "liberty").stream().sorted().toList()))
                        .build()
        ));
        assertTrue(jsonPayload.asJsonArray().contains(
                jbf.createObjectBuilder()
                        .add("quoteText", "War is never economically beneficial except for those in position to profit from war expenditures.")
                        .add("attributedTo", "Ron Paul")
                        .add("subjects", jbf.createArrayBuilder(Set.of("war", "profit").stream().sorted().toList()))
                        .build()
        ));
        assertTrue(jsonPayload.asJsonArray().contains(
                jbf.createObjectBuilder()
                        .add("quoteText", "If you want to find the secrets of the universe, think in terms of energy, frequency and vibration.")
                        .add("attributedTo", "Nikola Tesla")
                        .add("subjects", jbf.createArrayBuilder(Set.of("hidden knowledge").stream().sorted().toList()))
                        .build()
        ));
    }

    @Test
    void testQuotesByAuthorEndpoint() {
        Response response =
                given()
                        .when().get("/quotes/attributedTo/Ron Paul")
                        .then()
                        .extract().response();

        assertEquals(200, response.statusCode());
        assertTrue(response.contentType().startsWith("application/json"));

        JsonBuilderFactory jbf = jsonProvider.createBuilderFactory(Map.of());

        JsonValue jsonPayload =
                jsonProvider.createReader(new StringReader(response.getBody().asPrettyString())).readValue();
        assertTrue(jsonPayload.asJsonArray().contains(
                jbf.createObjectBuilder()
                        .add("quoteText", "Real patriotism is a willingness to challenge the government when it's wrong.")
                        .add("attributedTo", "Ron Paul")
                        .add("subjects", jbf.createArrayBuilder(Set.of("patriotism", "liberty").stream().sorted().toList()))
                        .build()
        ));
        assertTrue(jsonPayload.asJsonArray().contains(
                jbf.createObjectBuilder()
                        .add("quoteText", "War is never economically beneficial except for those in position to profit from war expenditures.")
                        .add("attributedTo", "Ron Paul")
                        .add("subjects", jbf.createArrayBuilder(Set.of("war", "profit").stream().sorted().toList()))
                        .build()
        ));

        JsonPointer jsonPointer = jsonProvider.createPointer("/attributedTo");
        assertTrue(jsonPayload.asJsonArray().stream()
                .map(JsonValue::asJsonObject)
                .allMatch(v -> jsonPointer.getValue(v).equals(jsonProvider.createValue("Ron Paul"))));
    }

    @Test
    void testQuotesBySubjectEndpoint() {
        Response response =
                given()
                        .when().get("/quotes/subject/peace")
                        .then()
                        .extract().response();

        assertEquals(200, response.statusCode());
        assertTrue(response.contentType().startsWith("application/json"));

        JsonValue jsonPayload =
                jsonProvider.createReader(new StringReader(response.getBody().asPrettyString())).readValue();

        JsonPointer subjectsJsonPointer = jsonProvider.createPointer("/subjects");
        JsonPointer attributedToJsonPointer = jsonProvider.createPointer("/attributedTo");

        assertTrue(jsonPayload.asJsonArray().stream()
                .map(JsonValue::asJsonObject)
                .allMatch(v -> subjectsJsonPointer.getValue(v).asJsonArray().contains(jsonProvider.createValue("peace"))));

        assertTrue(jsonPayload.asJsonArray().stream()
                .map(JsonValue::asJsonObject)
                .allMatch(v -> attributedToJsonPointer.getValue(v).equals(jsonProvider.createValue("Ron Paul"))));
    }
}
