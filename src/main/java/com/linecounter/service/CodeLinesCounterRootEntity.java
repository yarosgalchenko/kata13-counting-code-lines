package com.linecounter.service;

import model.FilePair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Collections.synchronizedList;

public class CodeLinesCounterRootEntity {

	private final CodeLinesCounter codeLinesCounter;

	public CodeLinesCounterRootEntity(CodeLinesCounter codeLinesCounter) {
		this.codeLinesCounter = codeLinesCounter;
	}

	public Map<String, List<FilePair>> count(String root) throws IOException {
		Map<String, List<FilePair>> filesPerDirectory = new ConcurrentHashMap<>();

		Stream<Path> paths = Files.walk(Paths.get(root));
		paths.parallel().filter(Files::isRegularFile).forEach(file -> {
				if (filesPerDirectory.containsKey(file.getParent().toString())) {
					filesPerDirectory.get(file.getParent().toString())
							.add(new FilePair(file.getFileName().toString(), codeLinesCounter.countCodeLines(file.toFile())));
				} else {
					filesPerDirectory.put(file.getParent().toString(),
							synchronizedList(new ArrayList<>(Collections.singleton(new FilePair(file.getFileName().toString(),
									codeLinesCounter.countCodeLines(file.toFile())
							))))
					);
				}
		});


		return filesPerDirectory;
	}
}