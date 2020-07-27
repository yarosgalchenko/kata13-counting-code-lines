package com.linecounter.service;

import model.FilePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CounterAggregate {

	public final CodeLinesCounterRootEntity codeLinesCounterRootEntity;

	public CounterAggregate(CodeLinesCounterRootEntity codeLinesCounterRootEntity) {
		this.codeLinesCounterRootEntity = codeLinesCounterRootEntity;
	}

	public void aggregateCountLinesResults(String root) throws IOException {
		Map<String, List<FilePair>> filesPerDirectory = codeLinesCounterRootEntity.count(root);

		Map<String, Integer> totalPerDirectory = new HashMap<>();
		filesPerDirectory.forEach((a, b) -> totalPerDirectory.put(a,
				filesPerDirectory.get(a).stream().map(FilePair::getLinesOfCode).reduce(0, Integer::sum)
		));
		System.out.println("Root : " + totalPerDirectory.values().stream().reduce(0, Integer::sum) + " " + filesPerDirectory.get(root)
				.stream()
				.map(Object::toString)
				.collect(Collectors.joining(" : ")));
		filesPerDirectory.forEach((k, v) -> {
			if (!k.equals(root)) {
				System.out.println(
						k + " : " + totalPerDirectory.get(k) + " " + v.stream().map(Object::toString).collect(Collectors.joining(" : ")));
			}
		});

	}

}
