package com.linecounter.service;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws IOException {
		CounterAggregate counterAggregate = new CounterAggregate(new CodeLinesCounterRootEntity(new FileCodeLinesCounter()));
		System.out.println("Please enter path to file or directory: ");
		Scanner scanner = new Scanner(System.in);
		counterAggregate.aggregateCountLinesResults(scanner.next());
	}
}
