package com.nttdata.testing.tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.interactions.Put;

import java.util.HashMap;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PutBooking implements Task {

    private final String bookingId;
    private final String firstname, lastname, totalprice;
    private final String token;

    public PutBooking(String bookingId, String firstname, String lastname, String totalprice) {
        this.bookingId = bookingId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.token = OnStage.theActorInTheSpotlight().recall("token");
    }

    public static Performable updateBooking(String bookingId, String firstname, String lastname, String totalprice) {
        return instrumented(PutBooking.class, bookingId, firstname, lastname, totalprice);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        // Limpiar y validar totalprice
        String cleanedTotalPrice = totalprice.replaceAll("[\"']", "").trim();
        int price;
        try {
            price = Integer.parseInt(cleanedTotalPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("totalprice must be a valid number, received: " + totalprice);
        }

        // Construir el cuerpo JSON usando Jackson
        Map<String, Object> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2022-05-01");
        bookingDates.put("checkout", "2022-05-15");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("firstname", firstname);
        requestBody.put("lastname", lastname);
        requestBody.put("totalprice", price);
        requestBody.put("depositpaid", true);
        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Breakfast");

        ObjectMapper mapper = new ObjectMapper();
        String jsonBody;
        try {
            jsonBody = mapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize JSON body", e);
        }

        actor.attemptsTo(
                Put.to("/booking/" + bookingId)
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .header("Cookie", "token=" + token)
                                .body(jsonBody)
                                .log().all())
        );
    }
}