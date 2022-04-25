package com.kovatech.auth.core.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kovatech.auth.core.config.WsStarterProperties;
import com.kovatech.auth.core.exception.WsBadRequestException;
import com.kovatech.auth.core.service.WsStarterService;
import org.springframework.stereotype.Repository;

import java.security.SecureRandom;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Repository
public class WsStarterServiceImpl
        implements WsStarterService {
    private final WsStarterProperties starterProperties;
    private final ObjectMapper objectMapper;

    public WsStarterServiceImpl(WsStarterProperties config) {
        this.starterProperties = config;
        this.objectMapper = new ObjectMapper();
    }


    public String formatAndCompareMsisdn(String headerMsisdn, String bodyMsisdn, boolean validate, boolean hash) {
        headerMsisdn = this.starterProperties.getMsisdnPrefix() + getMsisdn(headerMsisdn);
        if (validate) {
            bodyMsisdn = this.starterProperties.getMsisdnPrefix() + getMsisdn(bodyMsisdn);
            if (headerMsisdn.equals(bodyMsisdn)) {
                return hash ? hashText(bodyMsisdn) : bodyMsisdn;
            }
            throw new WsBadRequestException("452");
        }

        return hash ? hashText(headerMsisdn) : headerMsisdn;
    }


    public String formatAndHashMsisdn(String msisdn, boolean hash) {
        return hash ? hashText(this.starterProperties.getMsisdnPrefix() + getMsisdn(msisdn)) : (
                this.starterProperties.getMsisdnPrefix() + getMsisdn(msisdn));
    }


    public String hashText(String text) {
        return text;
        //return SHA256Hashing.hashPlainText(text, this.starterProperties.getStaticIv());
    }


    public HashSet<String> getHashSet(String text, String del) {
        if (text == null || "".equals(text)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(text.split(del)));
    }


    public List<String> getList(String text, String del) {
        if (text == null || "".equals(text)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(text.split(del)));
    }


    public boolean contentValid(String regexp, String text) {
        Pattern pattern = Pattern.compile(regexp);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }


    public boolean parametersMatch(String param1, String param2) {
        if (param1.equals(param2)) return true;
        throw new WsBadRequestException("453");
    }


    public String serialize(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            return "";
        }
    }


    public String generateRandomText(String[] fromString, int groups, String del) {
        StringBuilder randomText = new StringBuilder();
        StringBuilder text = new StringBuilder();

        (new String[1])[0] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz*@!$";
        fromString = (fromString == null) ? new String[1] : fromString;
        for (int g = 0; g < groups; g++) {
            for (String txt : fromString) {
                String[] part = txt.split(",");
                text.append(getRandomText(Integer.valueOf((part.length > 1) ? Integer.parseInt(part[1]) : 8), part[0]));
            }
            randomText.append(shuffleText(text.toString()));
            if (text.length() > 0 && g < groups - 1) {
                randomText.append(del);
            }
        }
        return randomText.toString();
    }


    public String getRandomText(Integer length, String fromString) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length.intValue());
        if (length.intValue() > 0) {
            for (int i = 0; i < length.intValue(); i++) {
                sb.append(fromString.charAt(rnd.nextInt(fromString.length())));
            }
        }
        return sb.toString();
    }

    public String shuffleText(String text) {
        List<Character> characters = text.chars().mapToObj(c -> Character.valueOf((char) c)).collect(Collectors.toList());
        StringBuilder result = new StringBuilder();
        IntStream.range(0, text.length()).forEach(index -> {
            int randomPosition = (new Random()).nextInt(characters.size());
            result.append(characters.get(randomPosition));
            characters.remove(randomPosition);
        });
        return result.toString();
    }

    private String getMsisdn(String msisdn) {
        return msisdn.substring(msisdn.length() - this.starterProperties.getMsisdnLength());
    }
}