/*
 * Copyright (C) 2017 Eric Afenyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.eric.bakingrecipes.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by eric on 06/12/2017
 * Source : https://github.com/ashwanikumar04/retroclient/blob/master/android/retro-client/src/main/java/in/ashwanik/retroclient/utils/Json.java
 * By Ashwani Kumar Under The MIT License (MIT)
 */

public class ParseJson  {

    /**
     * Serialize string.
     *
     * @param <T> Type of the object passed
     * @param obj Object to serialize
     * @return Serialized string
     */
    public static <T> String serialize(T obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);

    }

    /**
     * De-serialize a string
     *
     * @param <T>        Type of the object
     * @param jsonString Serialized string
     * @param tClass     Class of the type
     * @return De-serialized object
     * @throws ClassNotFoundException the class not found exception
     */
    public static <T> T deSerialize(String jsonString, Class<T> tClass) throws ClassNotFoundException {
        if (!isValid(jsonString)) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, tClass);
    }

    /**
     * De-serialize to list
     *
     * @param <T>  Type of the object
     * @param data Serialized string
     * @param type Type of the object
     * @return List of converted object
     */
    public static <T> List<T> deSerializeList(String data, Type type) {
        if (!isValid(data)) {
            return null;
        }
        return new Gson().fromJson(data,type);
    }

    /**
     * Check if a string is valid json.
     *
     * @param json Json String
     * @return Flag indicating if string is json
     */
    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }
}
