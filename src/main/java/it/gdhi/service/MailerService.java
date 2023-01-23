package it.gdhi.service;

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

    static final String SUBJECT = "GDHI has a new response from %s";
    static final String HEALTH_INDICATOR_PATH = "%s/admin/health_indicator_questionnaire/%s/review";
    static final String BODY = "Hello %s  ,\n" +
            "%s, %s has provided response for %s, in the GDHI website.\n" +
            "Contact: %s \n" +
            "For more details visit: %s \n" +
            "Regards \n" +
            "GDHI Team ";

    @Async
    public void send(Country country, String feeder, String feederRole, String contactEmail) {
        mailAddresses.getAddressMap().entrySet().forEach((entry) -> {
            String email = entry.getKey();
            String name = entry.getValue();
            String message = constructBody(country, name, feeder, feederRole, contactEmail);
            mailer.send(email, constructSubject(country), message);
            log.info("Mail sent successfully to " + name + " " + email);
        });
    }


    private String constructBody(Country country, String name, String feeder, String feederRole, String contactMail) {
        return format(BODY, name, feeder, feederRole, country.getName(),
                contactMail, constructHealthIndicatorPath(country));
    }

    private String constructSubject(Country country) {
        return format(SUBJECT, country.getName());
    }

    private String constructHealthIndicatorPath(Country country) {
        return format(HEALTH_INDICATOR_PATH, frontEndURL, country.getUniqueId());
    }
}