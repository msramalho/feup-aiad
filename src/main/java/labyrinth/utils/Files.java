package labyrinth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Files {

    private static String getFileExtension(String path) {
        int lastIndexOf = path.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return path.substring(lastIndexOf + 1).toLowerCase();
    }

    private static File getFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("must not be a directory: " + path);
        }

        return file;
    }

    public static void assertPathHasExtension(String path, String... extensions) {
        boolean hasExtension = Arrays.asList(extensions)
                .contains(getFileExtension(path));
        if (!hasExtension) {
            throw new IllegalArgumentException("Path: " + path + " must have one of extensions: " +
                    String.join(", ", extensions));
        }
    }

    public static <T> void serializeDataAsCsv(String filePath, List data, Class<T> dataClazz) throws IOException {
        assertPathHasExtension(filePath, "csv");

        File file = getFile(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(dataClazz)
                .withHeader();

        mapper.writer(schema)
                .writeValue(file, data);
    }

    public static <T> T deserializeYamlOrJsonObject(String path, Class<T> clazz) throws IOException {
        File file = getFile(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("file doesn't exist: " + path);
        }

        String extension = getFileExtension(path);
        ObjectMapper yamlMapper;
        assertPathHasExtension(path, "json", "yaml", "yml");
        if (extension.equals("json")) {
            yamlMapper = new ObjectMapper();
        } else {
            yamlMapper = new ObjectMapper(new YAMLFactory());
        }

        return yamlMapper.readValue(file, clazz);
    }
}
