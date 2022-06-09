package ru.mineeva.parser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mineeva.parser.exception.FileStorageException;
import ru.mineeva.parser.model.StructuredText;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileService {

    private final ParserService parserService;
    private final Path uploadPath = Path.of(System.getProperty("java.io.tmpdir"));

    @Autowired
    public FileService(ParserService parserService) {
        this.parserService = parserService;
    }

    /**
     * Загрузка файла на сервер
     *
     * @param file загружаемый файл
     * @return обработанный текст
     */
    public StructuredText uploadFile(MultipartFile file) {
        Path copyLocation = Paths
                .get(uploadPath + File.separator
                        + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        try {
            if (file.isEmpty()) {
                throw new FileStorageException("Вы пытаетесь загрузить пустой файл");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, copyLocation, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            System.out.println(e.toString());
            throw new FileStorageException("Не удалось сохранить файл " + file.getOriginalFilename()
                    + ". Пожалуйста, попробуйте еще раз!");
        }
        return parserService.parcingText(String.valueOf(copyLocation));
    }

}
