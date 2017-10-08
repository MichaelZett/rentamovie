package hh.fernuni.rentamovie.common.domain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdRepository {
	private static final Logger LOG = LoggerFactory.getLogger(IdRepository.class);
	private static final AtomicLong COUNT;
	private static final Path path = Paths.get("./id.db");

	static {
		long currentId = 0;
		try {
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				currentId = Long.parseLong(lines.get(0).trim());
			} else {
				Files.createFile(path);
				Files.write(path, Collections.singletonList(String.valueOf(currentId)), StandardCharsets.UTF_8,
				        StandardOpenOption.WRITE);
			}
		} catch (IOException e) {
			LOG.error("Error working with file in id.DB.");
		}
		COUNT = new AtomicLong(currentId);
	}

	public synchronized static long getNextId() {
		long newId = COUNT.incrementAndGet();
		try {
			Files.write(path, Collections.singletonList(String.valueOf(newId)), StandardCharsets.UTF_8,
			        StandardOpenOption.WRITE);
		} catch (IOException e) {
			LOG.error("Error writing id.db.");
		}
		return newId;
	}
}
