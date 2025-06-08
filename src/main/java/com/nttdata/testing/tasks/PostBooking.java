package com.nttdata.testing.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostBooking implements Task {

    private final String firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds;

    public PostBooking(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.checkin = checkin;
        this.checkout = checkout;
        this.additionalneeds = additionalneeds;
    }

    public static Performable fromPage(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) {
        return instrumented(PostBooking.class, firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.to("/booking")
                .with(requestSpecification -> requestSpecification
                        .contentType(ContentType.JSON)
                        .header("Accept", "application/json")
                        .body("{\"firstname\":" + firstname + ","
                                + "\"lastname\":" + lastname + ","
                                + "\"totalprice\":" + totalprice + ","
                                + "\"depositpaid\":" + depositpaid + ","
                                + "\"bookingdates\":" + "{"
                                + "\"checkin\":" + checkin + ","
                                + "\"checkout\":" + checkout + "},"
                                + "\"additionalneeds\":" + additionalneeds + "}")
                        .log().all()));


        SerenityRest.lastResponse().body().prettyPrint();
        if (SerenityRest.lastResponse().statusCode() == 200) {
            OnStage.theActorInTheSpotlight().remember("bookingId", SerenityRest.lastResponse().path("bookingid").toString());
            String valorDelBookingId = actor.recall("bookingId");
                    //SerenityRest.lastResponse().path("bookingid").toString();
            System.out.println("BOOK ID: " + valorDelBookingId);
        }


    }

}
