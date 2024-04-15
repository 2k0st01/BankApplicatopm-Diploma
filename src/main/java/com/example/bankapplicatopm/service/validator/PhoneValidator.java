package com.example.bankapplicatopm.service.validator;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class PhoneValidator implements Predicate<String> {

    private static final String PHONE_PATTERN = "^(50|66|95|99|63|93|67|96|97|98|68|39|91|92|94|89)[0-9]{7}$";

    private static final Pattern pattern = Pattern.compile(PHONE_PATTERN);

    @Override
    public boolean test(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        return pattern.matcher(phoneNumber).matches();
    }
}
