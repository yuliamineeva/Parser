package ru.mineeva.parser.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для хранения раздела текста.
 */
public class Section {
    private Long sectionNumber = 0L;
    private int nestingLevel;
    private Long firstLineNumber;
    private int countLevel;
    private String totalCountLevel = "";
    private String title = "Без раздела";
    private String content;

    public Section() {
        this.firstLineNumber = 0L;
    }

    public Section(Long sectionNumber, int nestingLevel, Long firstLineNumber,
                   String totalCountLevel, int countLevel, String title) {
        this.sectionNumber = sectionNumber;
        this.nestingLevel = nestingLevel;
        this.firstLineNumber = firstLineNumber;
        this.totalCountLevel = totalCountLevel;
        this.countLevel = countLevel;
        this.title = title;
    }

    public Long getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(Long sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public void setNestingLevel(int nestingLevel) {
        this.nestingLevel = nestingLevel;
    }

    public Long getFirstLineNumber() {
        return firstLineNumber;
    }

    public void setFirstLineNumber(Long firstLineNumber) {
        this.firstLineNumber = firstLineNumber;
    }

    public int getCountLevel() {
        return countLevel;
    }

    public String getTotalCountLevel() {
        return totalCountLevel;
    }

    public void setTotalCountLevel(String totalCountLevel) {
        this.totalCountLevel = totalCountLevel;
    }

    public void setCountLevel(int countLevel) {
        this.countLevel = countLevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Section)) return false;

        Section section = (Section) o;

        if (!Objects.equals(firstLineNumber, section.firstLineNumber)) return false;
        return sectionNumber.equals(section.sectionNumber);
    }

    @Override
    public int hashCode() {
        int result = sectionNumber.hashCode();
        result = 31 * result + firstLineNumber.hashCode();
        return result;
    }

}
