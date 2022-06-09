package ru.mineeva.parser.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mineeva.parser.model.Section;
import ru.mineeva.parser.model.StructuredText;
import ru.mineeva.parser.service.FileService;

import java.util.List;

@Controller
public class FileController {

    private final FileService fileService;
    private  StructuredText structuredText;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    /**
     * Страница для загрузки
     *
     * @return форма для загрузки
     */
    @GetMapping("/")
    public String index() {
        return "upload";
    }

    /**
     * Страница загрузки структуры файла
     *
     * @param file Файл
     * @param model Модель для структуры
     * @return Оглавление
     */
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             Model model) {
        StructuredText structuredText = fileService.uploadFile(file);
        this.structuredText=structuredText;
        List<Section> sectionList = structuredText.getSectionList();
        model.addAttribute("filename", file.getOriginalFilename());
        model.addAttribute("sectionList", sectionList);

        return "resultStructure";
    }

    /**
     * Страница вывода текста по разделам
     *
     * @param firstLineNumber номер строки
     * @param model Модель для текста
     * @return Текст раздела
     */
    @GetMapping("/text/{id}")
    public String getResultText(@PathVariable("id") Long firstLineNumber, Model model) {
        Section section = structuredText.getSectionHashMap().get(firstLineNumber);
        model.addAttribute("section", section);
        return "resultText";
    }
}
