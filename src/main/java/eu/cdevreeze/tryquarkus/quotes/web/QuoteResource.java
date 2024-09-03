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

import com.google.common.collect.ImmutableList;
import eu.cdevreeze.tryquarkus.quotes.model.Quote;
import eu.cdevreeze.tryquarkus.quotes.service.QuoteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Quote web resource.
 *
 * @author Chris de Vreeze
 */
@Path("/quotes")
public class QuoteResource {

    private final QuoteService quoteService;

    @Inject
    public QuoteResource(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllQuotes() {
        ImmutableList<Quote> quotes = quoteService.findAllQuotes();
        List<Quote.JsonbQuote> jsonbQuotes = quotes.stream().map(Quote::toJsonbQuote).toList();
        return Response.ok(jsonbQuotes).build();
    }

    @GET
    @Path("/attributedTo/{attributedTo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findQuotesByAuthor(@PathParam("attributedTo") String author) {
        ImmutableList<Quote> quotes = quoteService.findQuotesByAuthor(author);
        List<Quote.JsonbQuote> jsonbQuotes = quotes.stream().map(Quote::toJsonbQuote).toList();
        return Response.ok(jsonbQuotes).build();
    }

    @GET
    @Path("/subject/{subject}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findQuotesBySubject(@PathParam("subject") String subject) {
        ImmutableList<Quote> quotes = quoteService.findQuotesBySubject(subject);
        List<Quote.JsonbQuote> jsonbQuotes = quotes.stream().map(Quote::toJsonbQuote).toList();
        return Response.ok(jsonbQuotes).build();
    }
}
