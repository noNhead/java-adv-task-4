package org.example.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);
    public static final Pattern PATTERN_NUMBER = Pattern
            .compile("\\+[0-9]\\([0-9]{3}\\)\\s[0-9]{3}\\s[0-9]{2}\\s[0-9]{2}", Pattern.UNICODE_CHARACTER_CLASS);
    public static final Pattern PATTERN_DELETE_CHARACTER_IN_NUMBER = Pattern
            .compile("[()\\s+]", Pattern.UNICODE_CHARACTER_CLASS);
    public List<String> textAnalyzer(String string) {
        Matcher matcher = PATTERN_NUMBER.matcher(string);
        List<String> numbers = new ArrayList<>();
        while (matcher.find()){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = matcher.start(); i < matcher.end(); i++) {
                stringBuilder.append(string.charAt(i));
            }
            numbers.add(stringBuilder.toString().replaceAll(PATTERN_DELETE_CHARACTER_IN_NUMBER.pattern(), ""));
        }
        return numbers;
    }

    public String fileReaderToString(String path) throws IOException {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8);
    }

    public void fileWriter(String text, String path) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
        DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream)){
            dataOutputStream.writeUTF(text);
        } catch (IOException e) {
            LOGGER.warn(String.valueOf(e));
        }
    }

    public void lifecycle(){
        String text = "";
        try {
            text = fileReaderToString(getPath());
        } catch (IOException e) {
            LOGGER.warn(String.valueOf(e));
        }
        List<String> numbers = textAnalyzer(text);
        String numbersStr = listNumbersToString(numbers);
        fileWriter(numbersStr, getPath());
    }

    private static String listNumbersToString(List<String> numbers){
        StringBuilder stringBuilder = new StringBuilder();
        for (String v :
                numbers) {
            stringBuilder.append(v).append(",\n");
        }
        return stringBuilder.toString();
    }

    private static String getPath(){
        String rootPath = Objects.requireNonNull(Thread
                .currentThread()
                .getContextClassLoader()
                .getResource(""))
                .getPath();

        String propertyPath = rootPath + "config.properties";
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(propertyPath)){
            properties.load(fileInputStream);
            return properties.getProperty("filePathRewrite");
        } catch (IOException e) {
            LOGGER.warn(String.valueOf(e));
            return null;
        }
    }
}
