package com.electric3.server.resources.utils;

import com.electric3.dataatoms.User;

public class Mock {
    public static User getUserIvan() {
        User result = User.deserialize("{\n" +
                "    \"email\": \"ivan@handyassist.com\",\n" +
                "    \"email_verified\": false,\n" +
                "    \"user_id\": \"auth0|56c886e1ad96ed90125bf95a\",\n" +
                "    \"picture\": \"https://s.gravatar.com/avatar/32d4917d14c2d3d5d9724b2ef86e988b?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fiv.png\",\n" +
                "    \"nickname\": null,\n" +
                "    \"identities\": [\n" +
                "        {\n" +
                "            \"connection\": \"Username-Password-Authentication\",\n" +
                "            \"user_id\": \"56c886e1ad96ed90125bf95a\",\n" +
                "            \"provider\": \"auth0\",\n" +
                "            \"isSocial\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"updated_at\": \"2016-02-20T15:31:45.743Z\",\n" +
                "    \"created_at\": \"2016-02-20T15:31:45.743Z\",\n" +
                "    \"blocked_for\": []\n" +
                "}", User.class);

        return result;
    }
}
