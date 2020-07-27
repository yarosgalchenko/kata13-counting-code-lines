package com.linecounter.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.apache.commons.lang3.StringUtils.substringBetween;

public class FileCodeLinesCounter implements CodeLinesCounter {
	private final String LINE_COMMENT = "//";
	private final String START_COMMENT_BLOCK = "/*";
	private final String END_COMMENT_BLOCK = "*/";

	private final Predicate<String> lessThanCommentLine = trimmedLine -> trimmedLine.length() < LINE_COMMENT.length();
	private final Predicate<String> startsWithLineComment = trimmedLine -> trimmedLine.startsWith(LINE_COMMENT);

	@Override
	public int countCodeLines(File file) {
		final AtomicBoolean insideCommentsBlock = new AtomicBoolean();
		final AtomicInteger codeLines = new AtomicInteger();

		try (Stream<String> stream = Files.lines(file.toPath())) {

			stream.forEach(line -> {
				line = cleanTextBetweenStringBrackets(deleteWhitespace(line));

				if (isCommentsBlockStarts(line)) {
					insideCommentsBlock.getAndSet(true);
				}

				if (!insideCommentsBlock.get()) {
					if (!line.isEmpty() && isNotCommented(line).test(line)) {
						codeLines.incrementAndGet();
					}

				} else if (isCommentsBlockEnds(line)) {
					insideCommentsBlock.getAndSet(false);
				}

			});

		} catch (IOException e) {
			System.err.println("Error " + e.getMessage() + " during processing file: " + file.getName());
		}

		return codeLines.get();
	}

	private Predicate<String> isNotCommented(String trimmedLine) {
		return isCommentedBySimpleComment(trimmedLine).negate().and(isCommentedByBlockComment(trimmedLine).negate());
	}

	private Predicate<String> isCommentedBySimpleComment(String trimmedLine) {
		return lessThanCommentLine.negate().and(startsWithLineComment);
	}

	private Predicate<String> isCommentedByBlockComment(String trimmedLine) {
		if (trimmedLine.length() < START_COMMENT_BLOCK.length() || !trimmedLine.startsWith(START_COMMENT_BLOCK)) {
			return x -> false;
		}

		if (trimmedLine.contains(END_COMMENT_BLOCK)) {
			return x -> true;
		}

		trimmedLine = trimmedLine.substring(trimmedLine.indexOf(END_COMMENT_BLOCK) + END_COMMENT_BLOCK.length());

		if (trimmedLine.isEmpty()) {
			return x -> true;
		}

		return isCommentedByBlockComment(trimmedLine);
	}

	private boolean isCommentsBlockStarts(String trimmedLine) {
		return isFirstMarkerAfterSecondOne(trimmedLine, START_COMMENT_BLOCK, END_COMMENT_BLOCK);
	}

	private boolean isCommentsBlockEnds(String trimmedLine) {
		return isFirstMarkerAfterSecondOne(trimmedLine, END_COMMENT_BLOCK, START_COMMENT_BLOCK);
	}

	private boolean isFirstMarkerAfterSecondOne(String line, String firstMarker, String secondMarker) {
		return line.lastIndexOf(firstMarker) > line.lastIndexOf(secondMarker);
	}

	private String cleanTextBetweenStringBrackets(String line) {
		String between = substringBetween(line, "\"", "\"");
		if (between != null) {
			line = line.replace(between, "");
		}

		return line;
	}
}

