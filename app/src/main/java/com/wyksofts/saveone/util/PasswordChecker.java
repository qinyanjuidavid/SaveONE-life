package com.wyksofts.saveone.util;

import com.wyksofts.saveone.util.Constants.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChecker {

    public boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = Constants.PASSWORD_PATTERN;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
