package com.territory;

import org.bson.Document;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;
import java.util.stream.Collectors;

public class TestUtil {

    public static Document createDocumentFromJson(String path) {
        InputStream inputStream = TestUtil.class.getClassLoader().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + path);
        }

        String json = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        return Document.parse(json);
    }

    public static void renameDocument(Document document, String oldName, String newName) {
        if (document.containsKey(oldName)) {
            Object value = document.get(oldName);
            document.remove(oldName);
            document.put(newName, value);
        }
    }

    //converts uuid to base64 binary so it can be querid for mongo db using compass
    public static void main(String[] args) {
        UUID uuid = UUID.fromString("11eed97f-e52a-6a4d-919a-15b5d3fedfa9");
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        String base64Uuid = Base64.getEncoder().encodeToString(bb.array());
        System.out.println(base64Uuid);
    }

}



