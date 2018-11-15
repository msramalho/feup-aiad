package labyrinth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import labyrinth.cli.ConfigurationFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Serialization {

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

    public static<T> void serializeDataAsCsv(String filePath, List data, Class<T> dataClazz) {
        if (!getFileExtension(filePath).equals("csv")) {
            throw new IllegalArgumentException("not a csv file: " + filePath);
        }

        File file = getFile(filePath);

        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(dataClazz)
                .withHeader();

        try {
            mapper.writer(schema)
                    .writeValue(file, data);
        } catch (IOException e) {
           throw new RuntimeException("Failed to save csv to path: " + filePath, e);
        }
    }

    public static<T> T deserializeYamlOrJsonObject(String path, Class<T> clazz) {
        File file = getFile(path);
        if (!file.exists()) {
            throw new IllegalArgumentException("file doesn't exist: " + path);
        }

        String extension = getFileExtension(path);
        ObjectMapper yamlMapper;
        switch (extension) {
            case "json":
                yamlMapper = new ObjectMapper();
                break;
            case "yaml":
            case "yml":
                yamlMapper = new ObjectMapper(new YAMLFactory());
                break;
            default:
                throw new IllegalArgumentException("invalid path: " + path);
        }

        try {
            return yamlMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
