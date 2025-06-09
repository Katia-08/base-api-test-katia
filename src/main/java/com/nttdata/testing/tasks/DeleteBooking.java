package com.nttdata.testing.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteBooking implements Task {

        private final String bookingId;
    private final String token;

    public DeleteBooking(String bookingId) {
        this.bookingId = bookingId;
        this.token = OnStage.theActorInTheSpotlight().recall("token");


    }

    public static Performable deleteBooking(String bookingId) {
        return instrumented(DeleteBooking.class, bookingId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Delete.from("/booking/" + bookingId)
                        .with(requestSpecification -> requestSpecification
                                .contentType(ContentType.JSON)
                                .header("Accept", "application/json")
                                .header("Cookie", "token=" + token)
                                .log().all())
        );
    }
}