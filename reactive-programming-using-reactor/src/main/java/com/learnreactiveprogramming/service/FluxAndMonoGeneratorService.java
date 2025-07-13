package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
    }

    public Mono<String> namesMono() {
        return Mono.just("alex").log();
    }

    public Flux<String> namesFlux_map() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFlux_immutability() {
        var immutableFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));
        immutableFlux.map(String::toUpperCase);
        return immutableFlux;
    }

    public Flux<String> namesFlux_filter() {
        //filter the string whose name is grater than 3
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > 3)
                .log();
    }

    public Flux<String> namesFlux_flatmap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > 3)
                .flatMap(s -> Flux.fromArray(s.split(""))); //ALEX -> Flux(A,L,E,X)
    }

    public Flux<String> namesFlux_flatmap_async() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > 3)
                .flatMap(this::splitStringFlux_withDelay)
                .log();
    }
    public Flux<String> namesFlux_concatmap() {
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > 3)
                .concatMap(this::splitStringFlux_withDelay);
    }

    public Flux<String> splitStringFlux_withDelay(String name) {
        var charArray = name.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(charArray).delayElements(Duration.ofMillis(delay));
    }

    public Mono<String> namesMono_map() {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .log();
    }

    public Mono<List<String>> namesMono_flatmap() {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .flatMap(this::splitStringMono)
                .log();
    }

    public Mono<List<String>> splitStringMono(String name) {
        var charArray = name.split("");
        var charList = List.of(charArray);
        return Mono.just(charList);
    }

    public Flux<String> namesFlux_transform() {
        Function<Flux<String>, Flux<String>> transformMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > 3);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split(""))); //ALEX -> Flux(A,L,E,X)
    }

    public Flux<String> namesFlux_defaultIfEmpty() {
        Function<Flux<String>, Flux<String>> transformMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > 7);

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split("")))  //ALEX -> Flux(A,L,E,X)
                .defaultIfEmpty("default");
    }

    public Flux<String> namesFlux_switchIfEmpty() {
        Function<Flux<String>, Flux<String>> transformMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > 7);

        var defaultFlux = Flux.just("default");

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split("")))  //ALEX -> Flux(A,L,E,X)
                .switchIfEmpty(defaultFlux);
    }

    public static void main(String[] args) {
        FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

        service.namesFlux().subscribe(name -> {
            System.out.println("Flux Name is : " + name);
        });

        service.namesMono().subscribe(name -> {
            System.out.println("Mono Name is : " + name);
        });

        service.namesFlux_map().subscribe(name -> {
            System.out.println("Upper Case Flux Name is : " + name);
        });

        service.namesFlux_immutability().subscribe(name -> {
            System.out.println("Immutable Flux Name is : " + name);
        });

        service.namesFlux_filter().subscribe(name -> {
            System.out.println("Filter Flux Name is : " + name);
        });

        service.namesFlux_flatmap().subscribe(name -> {
            System.out.println("Flatmap Flux Name is : " + name);
        });

        service.namesFlux_flatmap_async().subscribe(name -> {
            System.out.println("Async Flatmap Flux Name is : " + name);
        });

        service.namesFlux_concatmap().subscribe(name -> {
            System.out.println("ConcatMap Flux Name is : " + name);
        });

        service.namesMono_map().subscribe(name -> {
            System.out.println("Map mono Name is : " + name);
        });

        service.namesMono_flatmap().subscribe(name -> {
            System.out.println("Flat Map mono Name is : " + name);
        });

        service.namesFlux_transform().subscribe(name -> {
            System.out.println("Transform Flux Name is : " + name);
        });

        service.namesFlux_defaultIfEmpty().subscribe(name -> {
            System.out.println("Default if Empty Flux Name is : " + name);
        });

        service.namesFlux_switchIfEmpty().subscribe(name -> {
            System.out.println("Switch if Empty Flux Name is : " + name);
        });

    }
}
