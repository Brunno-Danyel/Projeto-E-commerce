package io.github.brunnodanyel.projetovendas.validation;

import io.github.brunnodanyel.projetovendas.exception.SenhaInvalidaException;
import io.github.brunnodanyel.projetovendas.validation.anotacao.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

    }

    private boolean containsSequentialNumbers(String senha) {
        String sequentialNumbers = "01234567890";
        int sequentialLength = 3;

        for (int i = 0; i <= 10 - sequentialLength; i++) {
            String sequentialSubset = sequentialNumbers.substring(i, i + sequentialLength);
            if (senha.contains(sequentialSubset)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isValid(String senha, ConstraintValidatorContext constraintValidatorContext) {
        if (senha.length() < 6) {
            throw new SenhaInvalidaException("A senha deve ter pelo menos 6 caracteres");
        }

        // Verifica se contém caracteres especiais
        Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher specialCharMatcher = specialCharPattern.matcher(senha);
        boolean containsSpecialChar = specialCharMatcher.find();
        if (!containsSpecialChar) {
            throw new SenhaInvalidaException("A senha deve conter caracteres especiais");
        }

        // Verifica se contém letras maiúsculas
        Pattern uppercasePattern = Pattern.compile("[A-Z]");
        Matcher uppercaseMatcher = uppercasePattern.matcher(senha);
        boolean containsUppercase = uppercaseMatcher.find();
        if (!containsUppercase) {
            throw new SenhaInvalidaException("A senha deve conter letras maiúsculas");
        }

        // Verifica se contém números
        Pattern digitPattern = Pattern.compile("[0-9]");
        Matcher digitMatcher = digitPattern.matcher(senha);
        boolean containsDigit = digitMatcher.find();
        if (!containsDigit) {
            throw new SenhaInvalidaException("A senha deve conter números");
        }

        // Verifica se contém números sequenciais
        if (containsSequentialNumbers(senha)) {
            throw new SenhaInvalidaException("A senha não pode conter números consecutivos");
        }

        return true;
    }
}
