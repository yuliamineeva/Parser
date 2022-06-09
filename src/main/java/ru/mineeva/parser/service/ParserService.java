package ru.mineeva.parser.service;


import org.springframework.stereotype.Service;
import ru.mineeva.parser.model.Section;
import ru.mineeva.parser.model.StructuredText;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParserService {

    final String REGEX_LINEBREAK = "(\r\n)|(\n)|(\r)";
    static final String SECTION_START = "#";

    /**
     * Проверка на null одного аргумента
     *
     * @param obj         проверяемый объект
     * @param description описание
     * @return boolean
     */
    public boolean isNull(Object obj, String description) {
        if (obj == null) {
            System.out.println(description);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Чтение текста из файла
     *
     * @param fileLocation откуда читаем
     * @return строка
     */
    public String readText(String fileLocation) {
        String readFromFile = "";
        if (!isNull(fileLocation, "Пустая ссылка на файл")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation), "UTF-8"))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) { //Строка считается завершенной одним из следующих символов: перевод строки ('\n'), возврат каретки ('\r'), возврат каретки, за которым сразу следует перевод строки, или достижение конца файла
                    sb.append(line).append(System.lineSeparator());
                }
                readFromFile = sb.toString();
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
        return readFromFile;
    }

    /**
     * Разбивка текста на строки
     *
     * @param input строка
     * @return массив строк
     */
    public String[] parseToLines(String input) {
        String[] lines;
        if (isNull(input, "нет ссылки на текст")) {
            lines = new String[0];
        } else {
            Pattern pattern = Pattern.compile(System.lineSeparator());
            lines = pattern.split(input);
        }
        return lines;
    }

    /**
     * Разбивка текста на разделы
     *
     * @param inputLines массив строк
     * @return обработанный текст
     */
    public StructuredText parseToSection(String[] inputLines) {
        Map<Long, Section> sectionHashMap = new HashMap<>();
        Long sectionNumber = 0L;
        String totalCountLevel = "";
        Section section = new Section();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < inputLines.length; i++) {
            String inputLine = inputLines[i];

            if (inputLine.startsWith(SECTION_START)) {
                sb = new StringBuilder();
                Pattern p = Pattern.compile("#+");
                Matcher matcher = p.matcher(inputLine);
                if (matcher.find()) {
                    int nestingLevel = matcher.end();
                    int countLevel = section.getCountLevel();

                    if (nestingLevel == 1) {
                        sectionNumber++;
                        totalCountLevel = String.valueOf(sectionNumber);
                    }

                    if (section.getSectionNumber().equals(sectionNumber)){
                        if (section.getNestingLevel() == nestingLevel) {
                            countLevel += 1;
                            String t = section.getTotalCountLevel();
                            totalCountLevel = t.substring(0, t.length() - 1) + countLevel;
                        } else if (section.getNestingLevel() > nestingLevel) {
                            String t = section.getTotalCountLevel();
                            countLevel = Integer.parseInt(t.substring(nestingLevel - 1, nestingLevel)) + 1;
                            totalCountLevel = t.substring(0, nestingLevel - 1) + countLevel;
                        } else {
                            countLevel = 0;
                            totalCountLevel = section.getTotalCountLevel() + countLevel;
                        }
                    }

                    String title = replaceSymbol(inputLine, nestingLevel);
                    section = new Section(sectionNumber, nestingLevel, (long) i, totalCountLevel, countLevel, title);
                }
            }
            String line = replaceSymbol(inputLine, section.getNestingLevel());
            String content = sb.append(line).append(System.lineSeparator()).toString();
            section.setContent(content);
            sectionHashMap.put(section.getFirstLineNumber(), section);
        }
        addSubsections(sectionHashMap);
        return new StructuredText(sectionHashMap);
    }

    /**
     * Добавить содержание подразделов в разделы
     *
     * @param sectionHashMap контейнер секций
     */
    private void addSubsections(Map<Long, Section> sectionHashMap) {
        List<Section> sectionList = new ArrayList<>(sectionHashMap.values());
        sectionList.sort(Comparator.comparingLong(Section::getFirstLineNumber));
        if (sectionList.size() < 1) {
            return;
        }
        for (int i = 0; i < sectionList.size() - 1; i++) {
            for (int j = 1; j < sectionList.size(); j++) {
                Section currentSection = sectionList.get(i);
                Section nextSection = sectionList.get(j);

                if (currentSection.getSectionNumber().equals(nextSection.getSectionNumber())
                        && currentSection.getNestingLevel() < nextSection.getNestingLevel()) {
                    String countSubsection = currentSection.getTotalCountLevel();
                    String nextCountSubsection = nextSection.getTotalCountLevel().substring(0, countSubsection.length());

                    if (countSubsection.equals(nextCountSubsection)) {
                        String totalContent = currentSection.getContent() + nextSection.getContent();
                        currentSection.setContent(totalContent);
                        sectionHashMap.put(currentSection.getFirstLineNumber(), currentSection);
                    }
                }
            }
        }
    }

    /**
     * Замена специальных символов начала раздела
     *
     * @param line  строка для изменения
     * @param count количество символов
     * @return измененная строка
     */
    private String replaceSymbol(String line, int count) {
        if (line.contains(SECTION_START) && line.indexOf(SECTION_START) < count) {
            return line.substring(count);
        }
        return line;
    }

    /**
     * Разбор текста из файла (поэтапный процесс)
     *
     * @param fileLocation откуда читаем
     * @return обработанный текст
     */
    public StructuredText parcingText(String fileLocation) {
        String readFromFile = readText(fileLocation);
        String[] lines = parseToLines(readFromFile);
        return parseToSection(lines);
    }
}
