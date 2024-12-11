package com.tumtech.schoolApp.config;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.tumtech.schoolApp.dto.UserCSVRepresentation;
import com.tumtech.schoolApp.enums.Gender;
import com.tumtech.schoolApp.enums.Role;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component

@Slf4j
public class AutoSaveUser implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
Logger logger = LoggerFactory.getLogger(AutoSaveUser.class);
    @Value("${resourceUrl}")
    private String resourceUrl;

    public AutoSaveUser(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        registerSuperAdmin(resourceUrl);

    }
    public void registerSuperAdmin(String file) throws IOException {
        Set<Users> usersSet = parseCsv (file);
        Set<Users> newSet = new HashSet<>();
        for(Users users: usersSet){
            if( userRepository.existsByEmail(users.getEmail()) || userRepository.count()>1){
                logger.info("Admin {} exists",users.getEmail());
                continue;

            }
            newSet.add(users);

        }
        List<Users> usersList= userRepository.saveAll(newSet);
        System.out.println(usersList.size());
    }

    private Set<Users> parseCsv(String file) throws IOException {
        try(Reader reader = new BufferedReader(new FileReader(file))){
            HeaderColumnNameMappingStrategy<UserCSVRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(UserCSVRepresentation.class);
            CsvToBean<UserCSVRepresentation> csvToBean= new CsvToBeanBuilder<UserCSVRepresentation>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse()
                    .stream()
                    .map(csvLine-> {
                        Users admin = new Users();
                        admin.setFirstName(csvLine.getFname());
                        admin.setGender(Gender.valueOf(csvLine.getGender().toUpperCase()));
                        admin.setLastName(csvLine.getLname());
                        admin.setEmail(csvLine.getEmail());
                        admin.setPassword(passwordEncoder.encode(csvLine.getPassword()));
                        admin.setRole(Role.ADMIN);
                        admin.setPhone(csvLine.getPhone());
                        return admin;
                    } )
                    .collect(Collectors.toSet());
        }
    }
}
