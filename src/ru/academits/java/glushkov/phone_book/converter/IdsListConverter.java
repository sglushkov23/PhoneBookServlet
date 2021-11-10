package ru.academits.java.glushkov.phone_book.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;

public class IdsListConverter {
    private final Gson gson = new GsonBuilder().create();

    public HashSet<Integer> convertFromJson(String idsToDeleteJson) {
        Type type = new TypeToken<HashSet<Integer>>() {
        }.getType();

        return gson.fromJson(idsToDeleteJson, type);
    }
}