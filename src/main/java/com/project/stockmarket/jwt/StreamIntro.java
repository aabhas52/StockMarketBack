package com.project.stockmarket.jwt;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StreamIntro {

	public static void main(String[] args) {

		List<String> collect = (List<String>) Stream.of("a", "b", "c").filter(element -> element.contains("b")).collect(Collectors.toList());
		Optional<String> anyElement = collect.stream().findAny();
		Optional<String> firstElement = collect.stream().findFirst();
		System.out.println(anyElement + "and " + firstElement);
	}
}
