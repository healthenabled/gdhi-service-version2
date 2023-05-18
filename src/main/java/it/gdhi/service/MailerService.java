package it.gdhi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import it.gdhi.model.Country;
import it.gdhi.utils.MailAddresses;
import it.gdhi.utils.Mailer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@Slf4j
public class MailerService {

    @Autowired
    private Mailer mailer;

    @Autowired
    private MailAddresses mailAddresses;

    @Value("${frontEndURL}")
    private String frontEndURL;

    static final String SUBJECT = "GDHM has a new response from %s";
    static final String HEALTH_INDICATOR_PATH = "%s/admin/health_indicator_questionnaire/%s/review";
    static final String BODY = "Hello %s,\n\n" +
            "The data for %s has been submitted on %s. Please review the data.\n\n" +
            "Regards \n" +
            "GDHM System";

    @Async
    public void send(Country country, String feeder, String feederRole, String contactEmail) {
        mailAddresses.getAddressMap().entrySet().forEach((entry) -> {
            String email = entry.getValue();
            String name = entry.getKey();
            String message = constructBody(country, name);
            mailer.send(email, constructSubject(country), message);
            log.info("Mail sent successfully to " + name + " " + email);
        });
    }


    private String constructBody(Country country, String name) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        return format(BODY, name, country.getName(), formattedDateTime);
    }

    private String constructSubject(Country country) {
        return format(SUBJECT, country.getName());
    }
}