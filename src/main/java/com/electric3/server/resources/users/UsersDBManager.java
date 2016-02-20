package com.electric3.server.resources.users;

import com.electric3.dataatoms.Holder;
import com.electric3.dataatoms.User;
import com.electric3.server.database.NoSqlBase;
import com.electric3.server.utils.http.HttpSender;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDBManager extends NoSqlBase {

    private static final String AUTH0_CLIENT_ID = "hoGYsgYO81VhIJVJSDNvb0CnJk6AfyP7";
    private static final String AUTH0_CONNECTION = "Username-Password-Authentication";
    private static final String AUTH0_USERS_ENDPOINT = "https://project-rack.eu.auth0.com/api/v2/users";
    private static final String AUTH0_AUTH_HEADER = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ4MmozSnZMb3Q2NVpScHNxQUFBajZIMXR0NEhYUE9rbyIsInNjb3BlcyI6eyJ1c2VycyI6eyJhY3Rpb25zIjpbInJlYWQiLCJjcmVhdGUiLCJ1cGRhdGUiXX19LCJpYXQiOjE0NTU5OTU3NzksImp0aSI6ImE5YzU4OThjMmQxMDhiN2JlNDQyYTU5YTQ2MmQxYTIyIn0.BrkBtWyIOL_pb-oMDC5Fu34z_rIBS943IGcQULPFv6Q";

    private static final UsersDBManager usersDBManager = new UsersDBManager();

    public static UsersDBManager getInstance() {
        return usersDBManager;
    }

    private static Logger log = Logger.getLogger(UsersDBManager.class.getName());

    private UsersDBManager() {
    }

    public String getUsers() throws Exception {
        log.info("get all users");
        Holder<User> holder = new Holder<>();

        HttpSender httpSender = HttpSender.getInstance();
        String responseString = httpSender.sendGet(AUTH0_USERS_ENDPOINT, AUTH0_AUTH_HEADER);

        ArrayList<User> users = new Gson().fromJson(responseString, new TypeToken<List<User>>() {
        }.getType());

        holder.setItems(users);

        return holder.serialize();
    }

    public String getClientUsers(String clientId) throws Exception {
        log.info("get all users for: " + clientId);
        Holder<User> holder = new Holder<>();

        HttpSender httpSender = HttpSender.getInstance();
        String url = AUTH0_USERS_ENDPOINT + "?q=" +
                URLEncoder.encode("user_metadata.clientId:", "UTF-8") + clientId + "&search_engine=v2";
        String responseString = httpSender.sendGet(url, AUTH0_AUTH_HEADER);

        ArrayList<User> users = new Gson().fromJson(responseString, new TypeToken<List<User>>() {
        }.getType());

        holder.setItems(users);

        return holder.serialize();
    }

    private void validateEmailUniqueness(String email, HttpSender httpSender) throws Exception {
        String url = AUTH0_USERS_ENDPOINT + "?q=" +
                URLEncoder.encode(String.format("email:\"%s\"", email), "UTF-8") + "&search_engine=v2";
        String responseString = httpSender.sendGet(url, AUTH0_AUTH_HEADER);

        ArrayList<User> users = new Gson().fromJson(responseString, new TypeToken<List<User>>() {
        }.getType());

        if (users.size() > 0) {
            throw new Exception("User already exist");
        }
    }

    public void createClientUser(String clientId, User user) throws Exception {
        log.info("create new user");

        HttpSender httpSender = HttpSender.getInstance();
        validateEmailUniqueness(user.getEmail(), httpSender);

        user.setConnection(AUTH0_CONNECTION);
        user.getUser_metadata().setClientId(clientId);

        httpSender.sendPost(AUTH0_USERS_ENDPOINT, AUTH0_AUTH_HEADER, user.serialize());
    }

    public void createUser(User user) throws Exception {
        log.info("create new user");

        HttpSender httpSender = HttpSender.getInstance();
        validateEmailUniqueness(user.getEmail(), httpSender);

        user.setConnection(AUTH0_CONNECTION);

        httpSender.sendPost(AUTH0_USERS_ENDPOINT, AUTH0_AUTH_HEADER, user.serialize());
    }

    public void setUserClientId(User user, String clientId) throws Exception {
        User editUser = new User();
        editUser.setConnection(AUTH0_CONNECTION);
        editUser.setClient_id(AUTH0_CLIENT_ID);
        editUser.setEmail(user.getEmail());
        user.getUser_metadata().setClientId(clientId);
        editUser.setUser_metadata(user.getUser_metadata());

        HttpSender httpSender = HttpSender.getInstance();
        String url = AUTH0_USERS_ENDPOINT + '/' + URLEncoder.encode(user.getUser_id(), "UTF-8");

        httpSender.sendPatch(url, AUTH0_AUTH_HEADER, editUser.serialize());
    }
}
