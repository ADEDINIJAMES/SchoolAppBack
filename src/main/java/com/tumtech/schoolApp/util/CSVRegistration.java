package com.tumtech.schoolApp.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.tumtech.schoolApp.dto.SubjectCSVRepresentation;
import com.tumtech.schoolApp.enums.Category;
import com.tumtech.schoolApp.model.Subject;
import com.tumtech.schoolApp.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component

@Slf4j
public class CSVRegistration {

    private final SubjectRepository subjectRepository;
    Logger logger = LoggerFactory.getLogger(CSVRegistration.class);
@Autowired
    public CSVRegistration(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Integer uploadSubjects(MultipartFile file) throws IOException {
        Set<Subject> subjectSetSet = parseCsv(file);
        Set<Subject> newSubjects = new HashSet<>();

        for (Subject subject : subjectSetSet) {
            if (subjectRepository.existsByNameIgnoreCase(subject.getName())) {
                logger.info("Subject with name {} already exists, skipping...", subject.getName());
                continue;
            }
            logger.info("Adding new subject: {}", subject.getName());
            newSubjects.add(subject);
        }

        List<Subject> savedSubject = subjectRepository.saveAll(newSubjects);
        logger.info("Registered {} new SUbject.", savedSubject.size());
        return savedSubject.size();
    }

    private Set<Subject> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<SubjectCSVRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(SubjectCSVRepresentation.class);

            CsvToBean<SubjectCSVRepresentation> csvToBean = new CsvToBeanBuilder<SubjectCSVRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            logger.info("CSV file parsed successfully.");

            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> {
                        try {
                            Subject subject = new Subject();
                            subject.setName(csvLine.getName().toUpperCase());
                            subject.setCategory(Category.valueOf(csvLine.getCategory().toUpperCase()));
                            subject.setCompulsory(Boolean.parseBoolean(csvLine.getIsCompulsory()));

                            return subject;
                        } catch (IllegalArgumentException e) {
                            logger.warn(e.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // Exclude null values caused by invalid roles
                    .collect(Collectors.toSet());
        }
    }
}
