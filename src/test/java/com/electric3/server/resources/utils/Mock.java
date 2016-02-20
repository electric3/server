package com.electric3.server.resources.utils;

import com.electric3.dataatoms.Client;
import com.electric3.dataatoms.User;

public enum Mock {
    ME;

    private Client client;

    Mock() {
        client = new Client();
        client.setOwner(getUserDirector());
        client.setEmail("ivan@handyassist.com");
        client.setDescription("description");
        client.setPhone("+79817727449");
    }


    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public static User getUserDirector() {
        User result = User.deserialize(" {\n" +
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
                "    \"updated_at\": \"2016-02-20T16:31:25.661Z\",\n" +
                "    \"created_at\": \"2016-02-20T15:31:45.743Z\",\n" +
                "    \"user_metadata\": {\n" +
                "        \"photoUrl\": \"https://pbs.twimg.com/profile_images/2606822886/v3v58lx1a6n91uzjh5ep.jpeg\",\n" +
                "        \"skypeName\": \"hej.ivan\",\n" +
                "        \"phone\": \"+79817727449\",\n" +
                "        \"name\": \"Ivan Alyakskin\",\n" +
                "        \"clientId\": \"56c88e55088684da2efa4b06\",\n" +
                "        \"role\": \"0\"\n" +
                "    },\n" +
                "    \"blocked_for\": []\n" +
                "}", User.class);

        return result;
    }

    public static User getUserManager() {
        User result = User.deserialize(" {\n" +
                "    \"email\": \"stas@handyassist.com\",\n" +
                "    \"email_verified\": false,\n" +
                "    \"user_id\": \"auth0|56c88f00ad96ed90125bf9f4\",\n" +
                "    \"picture\": \"https://s.gravatar.com/avatar/44ec7a2bed98a42ce52030de604c9253?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fst.png\",\n" +
                "    \"nickname\": null,\n" +
                "    \"identities\": [\n" +
                "        {\n" +
                "            \"connection\": \"Username-Password-Authentication\",\n" +
                "            \"user_id\": \"56c88f00ad96ed90125bf9f4\",\n" +
                "            \"provider\": \"auth0\",\n" +
                "            \"isSocial\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"updated_at\": \"2016-02-20T16:06:24.698Z\",\n" +
                "    \"created_at\": \"2016-02-20T16:06:24.698Z\",\n" +
                "    \"user_metadata\": {\n" +
                "        \"photoUrl\": \"https://pbs.twimg.com/profile_images/1143717167/x_eb3b3f88.jpg\",\n" +
                "        \"skypeName\": \"casper1149\",\n" +
                "        \"phone\": \"+79219262241\",\n" +
                "        \"name\": \"Stas Smirnov\",\n" +
                "        \"clientId\": \"56c88e55088684da2efa4b06\",\n" +
                "        \"role\": \"1\"\n" +
                "    },\n" +
                "    \"blocked_for\": []\n" +
                "}", User.class);

        return result;
    }

    public static User getUserUser() {
        User result = User.deserialize(" {\n" +
                "    \"email\": \"tanya@handyassit.com\",\n" +
                "    \"email_verified\": false,\n" +
                "    \"user_id\": \"auth0|56c88f1aad96ed90125bf9f6\",\n" +
                "    \"picture\": \"https://s.gravatar.com/avatar/8290cdea6f40bd06dc74e34b165c60cc?s=480&r=pg&d=https%3A%2F%2Fcdn.auth0.com%2Favatars%2Fta.png\",\n" +
                "    \"nickname\": null,\n" +
                "    \"identities\": [\n" +
                "        {\n" +
                "            \"connection\": \"Username-Password-Authentication\",\n" +
                "            \"user_id\": \"56c88f1aad96ed90125bf9f6\",\n" +
                "            \"provider\": \"auth0\",\n" +
                "            \"isSocial\": false\n" +
                "        }\n" +
                "    ],\n" +
                "    \"updated_at\": \"2016-02-20T16:06:50.151Z\",\n" +
                "    \"created_at\": \"2016-02-20T16:06:50.151Z\",\n" +
                "    \"user_metadata\": {\n" +
                "        \"photoUrl\": \"https://pbs.twimg.com/profile_images/2060238222/myspace.png\",\n" +
                "        \"skypeName\": \"zzzlayaspb\",\n" +
                "        \"phone\": \"+79211234567\",\n" +
                "        \"name\": \"Tatiana Trubitcyna\",\n" +
                "        \"clientId\": \"56c88e55088684da2efa4b06\",\n" +
                "        \"role\": \"2\"\n" +
                "    },\n" +
                "    \"blocked_for\": []\n" +
                "}", User.class);

        return result;
    }
}
