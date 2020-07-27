package com.linecounter.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileCodeLinesCounterTest {

	private CodeLinesCounter codeLinesCounter;

	@BeforeEach
	public void init() {
		this.codeLinesCounter = new FileCodeLinesCounter();
	}

	@Test
	void testGetCodeLinesMain() throws IOException {
		assertEquals(5, codeLinesCounter.countCodeLines(Paths.get("src","test","resources", "Main.java").toFile()));
	}

	@Test
	void testGetCodeLinesDave() throws IOException {
		assertEquals(3, codeLinesCounter.countCodeLines(Paths.get("src","test","resources", "Dave.java").toFile()));
	}

	@Test
	void testGetCodeLinesEmptyFile() throws IOException {
		assertEquals(0, codeLinesCounter.countCodeLines(Paths.get("src","test","resources", "EmptyFile.java").toFile()));
	}

}
