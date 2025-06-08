package com.nttdata.testing.tasks;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class PostToken implements Task {

    private String username = "admin";
    private String password = "password123";

    public static Performable generateToken() {
        return instrumented(PostToken.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(Post.to("/auth").with(requestSpecification -> requestSpecification.contentType(ContentType.JSON).
                body("{\"username\":\"" + username + "\","
                        + "\"password\":\"" + password + "\"}")
                .log().all()));

        SerenityRest.lastResponse().body().prettyPrint();
        if(SerenityRest.lastResponse().statusCode() == 200){
            OnStage.theActorInTheSpotlight().remember("token", SerenityRest.lastResponse().path("token").toString());
        }
    }


}
