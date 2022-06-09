package ru.mineeva.parser.model;

import java.util.*;

/**
 * Класс для хранения разобранного текста и структуры.
 */
public class StructuredText {

    private Map<Long, Section> sectionHashMap;
    private List<Section> sectionList;

    public StructuredText(Map<Long, Section> sectionHashMap) {
        this.sectionHashMap = sectionHashMap;
    }


    public Map<Long, Section> getSectionHashMap() {
        return sectionHashMap;
    }

    public void setSectionHashMap(Map<Long, Section> sectionHashMap) {
        this.sectionHashMap = sectionHashMap;
    }

    public List<Section> getSectionList() {
        sectionList = new ArrayList<>(sectionHashMap.values());
        sectionList.sort(Comparator.comparingLong(Section::getFirstLineNumber));
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

}
