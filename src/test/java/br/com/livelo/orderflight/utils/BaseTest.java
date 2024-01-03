package br.com.livelo.orderflight.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class BaseTest {

    public static String readFile(final String filePath) {
        try {
            final File input = new ClassPathResource(filePath).getFile();
            return Files.readString(Paths.get(input.getPath())).replaceAll("\\\\", "");
        } catch (IOException e) {
            log.error("Could not read file.", e);
        }
        return null;
    }
}