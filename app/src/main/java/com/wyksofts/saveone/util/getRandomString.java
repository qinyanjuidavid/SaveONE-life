package com.wyksofts.saveone.util;

import static com.wyksofts.saveone.util.Constants.Constants.ALLOWED_CHARACTERS;

import java.util.Random;

public class getRandomString {

    public static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }
}
