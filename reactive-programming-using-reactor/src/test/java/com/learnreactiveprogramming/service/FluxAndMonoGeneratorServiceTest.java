package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void test_namesFlux_Map() {
        var namesFluxMap = service.namesFlux_map();
        StepVerifier.create(namesFluxMap)
                .expectNext("ALEX", "BEN", "CHLOE")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_Immutability() {
        var immutableFlux = service.namesFlux_immutability();
        StepVerifier.create(immutableFlux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_filter() {
        var filterFlux = service.namesFlux_filter();
        StepVerifier.create(filterFlux)
                .expectNext("ALEX", "CHLOE")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_flatmap() {
        var flatMapFlux = service.namesFlux_flatmap();
        StepVerifier.create(flatMapFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_flatmap_async() {
        var flatMapAsyncFlux = service.namesFlux_flatmap_async();
        StepVerifier.create(flatMapAsyncFlux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void test_namesFlux_concatmap() {
        var contactMapAsyncFlux = service.namesFlux_flatmap_async();
        StepVerifier.create(contactMapAsyncFlux)
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void test_namesMono_map() {
        var mapMono = service.namesMono_map();
        StepVerifier.create(mapMono)
                .expectNext("ALEX")
                .verifyComplete();
    }

    @Test
    void test_namesMono_flatmap() {
        var mapMono = service.namesMono_flatmap();
        StepVerifier.create(mapMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }

    @Test
    void test_namesFlux_transform() {
        var transformMap = service.namesFlux_transform();
        StepVerifier.create(transformMap)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_default() {
        var defaultFlux = service.namesFlux_defaultIfEmpty();
        StepVerifier.create(defaultFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void test_namesFlux_switchIfEmpty() {
        var defaultFlux = service.namesFlux_switchIfEmpty();
        StepVerifier.create(defaultFlux)
                .expectNext("default")
                .verifyComplete();
    }
}