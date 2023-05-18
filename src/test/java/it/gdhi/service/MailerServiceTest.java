package it.gdhi.service;

import it.gdhi.model.Country;
import it.gdhi.utils.MailAddresses;
import it.gdhi.utils.Mailer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.String.format;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailerServiceTest {

    @InjectMocks
    MailerService mailerService;

    @Mock
    MailAddresses mailAddresses;

    @Mock
    Mailer mailer;

    @Test
    public void shouldVerifyEmailIsSent() {
        ReflectionTestUtils.setField(mailerService, "frontEndURL", "http://test");
        String email1 = "test1@test.com";
        String email2 = "test2@test.com";
        String name1 = "test1";
        String name2 = "test2";
        UUID uniqueId = UUID.randomUUID();
        Country country = new Country("Ind", "India", uniqueId, "IN");
        String feeder = "feeder";
        String feederRole = "feeder role";
        String contactEmail = "contact@test.com";

        Map<String, String> address = new HashMap<>();
        address.put(name1, email1);
        address.put(name2, email2);
        when(mailAddresses.getAddressMap()).thenReturn(address);
        mailerService.send(country, feeder, feederRole, contactEmail);

        verify(mailer).send(email2, format(MailerService.SUBJECT, country.getName()), constructBody(name2, country));
        verify(mailer).send(email1, format(MailerService.SUBJECT, country.getName()), constructBody(name1, country));
    }

    private String constructBody(String name, Country country) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);
        return format(MailerService.BODY, name, country.getName(), formattedDateTime);
    }

}