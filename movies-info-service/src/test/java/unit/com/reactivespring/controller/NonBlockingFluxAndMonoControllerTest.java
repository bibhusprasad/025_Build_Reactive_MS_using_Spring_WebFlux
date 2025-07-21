package com.reactivespring.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

@WebFluxTest(controllers = NonBlockingFluxAndMonoController.class)
@AutoConfigureWebTestClient
class NonBlockingFluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void flux_approach_1() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(5);
    }

    @Test
    void flux_approach_2() {
        var flux = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(flux)
                .expectNextCount(5);
    }

    @Test
    void flux_approach_3() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Integer.class)
                .consumeWith(
                        item -> {
                            var result = item.getResponseBody();
                            assert(Objects.requireNonNull(result).size() == 5);
                        }
                );
    }

    @Test
    void helloWorldMono() {
        webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith( item -> {
                    var result = item.getResponseBody();
                    assert(Objects.requireNonNull(result).contains("Hello World Mono"));
                });
    }

    @Test
    void stream() {
        var stream = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(stream)
                .expectNext(0L,1L,2L,3L)
                .thenCancel()
                .verify();
    }
}