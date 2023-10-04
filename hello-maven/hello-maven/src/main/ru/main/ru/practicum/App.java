package ru.yandex.practicum;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        System.out.print("Введите строку => ");
        final String input = scanner.nextLine();
        final String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(
                input.getBytes()
        );
        System.out.println(encoded);
    }
}