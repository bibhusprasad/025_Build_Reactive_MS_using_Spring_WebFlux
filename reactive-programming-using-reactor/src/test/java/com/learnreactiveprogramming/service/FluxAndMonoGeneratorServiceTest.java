package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    public void test_namesFlux_1() {
        var namesFlux = service.namesFlux();
        StepVerifier.create(namesFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    public void test_namesFlux_2() {
        var namesFlux = service.namesFlux();
        StepVerifier.create(namesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void test_namesFlux_3() {
        var namesFlux = service.namesFlux();
        StepVerifier.create(namesFlux)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void test_namesMono_1() {
        var namesMono = service.namesMono();
        StepVerifier.create(namesMono).expectNext("alex").verifyComplete();
    }
}