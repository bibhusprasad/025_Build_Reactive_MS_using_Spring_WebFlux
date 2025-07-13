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
        immutableFlux.map(String::toUpperCase).log();
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
                .flatMap(s -> Flux.fromArray(s.split("")))
                .log(); //ALEX -> Flux(A,L,E,X)
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
                .concatMap(this::splitStringFlux_withDelay)
                .log();
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
                .filter(s -> s.length() > 3).log();

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split("")))
                .log(); //ALEX -> Flux(A,L,E,X)
    }

    public Flux<String> namesFlux_defaultIfEmpty() {
        Function<Flux<String>, Flux<String>> transformMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > 7).log();

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split("")))  //ALEX -> Flux(A,L,E,X)
                .defaultIfEmpty("default")
                .log();
    }

    public Flux<String> namesFlux_switchIfEmpty() {
        Function<Flux<String>, Flux<String>> transformMap = name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > 7);

        var defaultFlux = Flux.just("default");

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(transformMap)
                .flatMap(s -> Flux.fromArray(s.split("")))  //ALEX -> Flux(A,L,E,X)
                .switchIfEmpty(defaultFlux)
                .log();
    }

    public Flux<String> explore_concat(){
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.concat(abcFlux, defFlux).log();
    }

    public Flux<String> explore_concatWith(){
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.concatWith(defFlux).log();
    }

    public Flux<String> explore_concatWith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.concatWith(bMono).log();
    }

    public Flux<String> explore_merge(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.merge(abcFlux, defFlux).log();
    }

    public Flux<String> explore_mergeWith(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return abcFlux.mergeWith(defFlux).log();
    }

    public Flux<String> explore_mergeWith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.mergeWith(bMono).log();
    }

    public Flux<String> explore_mergeSequential(){
        var abcFlux = Flux.just("A", "B", "C")
                .delayElements(Duration.ofMillis(100));
        var defFlux = Flux.just("D", "E", "F")
                .delayElements(Duration.ofMillis(125));
        return Flux.mergeSequential(abcFlux, defFlux).log();
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();
    }

    public Flux<String> explore_zip_1() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        var _123Flux = Flux.just("1", "2", "3");
        var _456Flux = Flux.just("4", "5", "6");

        return Flux.zip(abcFlux, defFlux, _123Flux, _456Flux)
                .map(t4 -> t4.getT1() + t4.getT2() + t4.getT3() + t4.getT4())
                .log();
    }

    public Flux<String> explore_zipWith() {
        var abcFlux = Flux.just("A", "B", "C");
        var defFlux = Flux.just("D", "E", "F");
        return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
    }

    public Mono<String> explore_zipWith_mono(){
        var aMono = Mono.just("A");
        var bMono = Mono.just("B");
        return aMono.zipWith(bMono)
                .map(t2 -> t2.getT1() + t2.getT2())
                .log();
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

        service.explore_concat().subscribe(name -> {
            System.out.println("Concat Flux is : " + name);
        });

        service.explore_concatWith().subscribe(name -> {
            System.out.println("ConcatWith Flux is : " + name);
        });

        service.explore_concatWith_mono().subscribe(name -> {
            System.out.println("Concat With Mono is : " + name);
        });

        service.explore_merge().subscribe(name -> {
            System.out.println("Merge Flux is : " + name);
        });

        service.explore_mergeWith().subscribe(name -> {
            System.out.println("Merge With Flux is : " + name);
        });

        service.explore_mergeWith_mono().subscribe(name -> {
            System.out.println("Merge With Mono is : " + name);
        });

        service.explore_mergeSequential().subscribe(name -> {
            System.out.println("Merge Sequential Flux is : " + name);
        });

        service.explore_zip().subscribe(name -> {
            System.out.println("Zip Flux is : " + name);
        });

        service.explore_zip_1().subscribe(name -> {
            System.out.println("Tupple Zip Flux is : " + name);
        });

        service.explore_zipWith().subscribe(name -> {
            System.out.println("Zip With Flux is : " + name);
        });

        service.explore_zipWith_mono().subscribe(name -> {
            System.out.println("Zip With Mono is : " + name);
        });

    }
}
