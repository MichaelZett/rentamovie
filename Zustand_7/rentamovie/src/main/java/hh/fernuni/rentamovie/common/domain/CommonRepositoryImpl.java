package hh.fernuni.rentamovie.common.domain;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CommonRepositoryImpl<T extends AbstractIdCarrier> implements CommonRepository<T> {
	private static final Logger LOG = LoggerFactory.getLogger(CommonRepositoryImpl.class);
	private final Map<Long, T> repo = new ConcurrentHashMap<>();
	private final Path path;
	protected static final String DELIMITER = ",";

	protected CommonRepositoryImpl(String filename) {
		path = Paths.get(filename);
		init();
	}

	protected void init() {
		try {
			if (Files.exists(path)) {
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				for (String domainClassAsText : lines) {
					T domainClass = fromText(domainClassAsText.split(DELIMITER));
					repo.put(domainClass.getId(), domainClass);
				}
			} else {
				Files.createFile(path);
			}
		} catch (IOException e) {
			LOG.error("Error working with file in {}.", path.toString());
		}
	}

	@Override
	public void save(T domainClass) {
		T old = repo.put(domainClass.getId(), domainClass);

		if (old == null) {
			try {
				Files.write(path, Collections.singletonList(toText(domainClass)), StandardCharsets.UTF_8,
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				LOG.error("Error writing to db.");
			}
		} else {
			List<String> allEntities = repo.values().stream().map(this::toText).collect(Collectors.toList());
			try {
				Files.write(path, allEntities, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
						StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				LOG.error("Error writing to db.");
			}
		}
	}

	@Override
	public T read(Long id) {
		return repo.get(id);
	}

	@Override
	public Collection<T> readAll() {
		return repo.values();
	}

	protected abstract T fromText(String[] split);

	protected abstract String toText(T domainClass);

}
