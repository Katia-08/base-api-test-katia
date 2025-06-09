package com.nttdata.testing.stepDefinitions;

import com.nttdata.testing.questions.ResponseCode;
import com.nttdata.testing.tasks.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.CoreMatchers.equalTo;

public class BookingStepDefinition {

    public static Logger LOGGER = LoggerFactory.getLogger(BookingStepDefinition.class);

    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    @Given("el {actor} establece el endpoint de booking")
    public void elActorEstableceElEndpointDeBooking(Actor actor) {
        actor.whoCan(CallAnApi.at("https://restful-booker.herokuapp.com"));
    }

    @When("el {actor} realiza una solicitud GET")
    public void elActorRealizaUnaSolicitudGET(Actor actor) {
        actor.attemptsTo(GetAllBookings.fromEndpoint("/booking"));
    }

    @Then("el codigo de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int responseCode) {
        theActorInTheSpotlight().should(seeThat("El código de respuesta", ResponseCode.getStatus(), equalTo(responseCode)));
    }

    @When("^el actor crea un booking con el (.*) (.*) (.*) (.*) (.*) (.*) (.*)$")
    public void elActorCreaUnBookingConEl(String firstname, String lastname, String totalprice, String depositpaid, String checkin, String checkout, String additionalneeds) {
        theActorInTheSpotlight().attemptsTo(PostBooking.fromPage(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds));
    }

    @When("^el actor actualiza un booking con los datos (.*) (.*) (.*)$")
    public void elActorActualizaUnBookingConLosDatos(String firstname, String lastname, String totalprice) {
        theActorInTheSpotlight().attemptsTo(PostToken.generateToken());
        theActorInTheSpotlight().attemptsTo(PatchBooking.fromPage(firstname, lastname, totalprice));
    }

    // New step for PUT
    @When("^el actor actualiza un booking usando PUT con los datos (.*) (.*) (.*)$")
    public void elActorActualizaUnBookingUsandoPUT(String firstname, String lastname, String totalprice) {
        theActorInTheSpotlight().attemptsTo(PostToken.generateToken());
        // Limpiar comillas y validar que totalprice sea un número
        String cleanedTotalPrice = totalprice.replaceAll("[\"']", "").trim();
        try {
            Integer.parseInt(cleanedTotalPrice);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("totalprice must be a valid number, received: " + totalprice);
        }
        theActorInTheSpotlight().attemptsTo(PutBooking.updateBooking(theActorInTheSpotlight().recall("bookingId"), firstname, lastname, cleanedTotalPrice));
    }

    // New step for DELETE
    @When("^el actor elimina un booking usando DELETE$")
    public void elActorEliminaUnBookingUsandoDELETE() {
        theActorInTheSpotlight().attemptsTo(PostToken.generateToken());
        theActorInTheSpotlight().attemptsTo(DeleteBooking.deleteBooking(theActorInTheSpotlight().recall("bookingId")));
    }

}
