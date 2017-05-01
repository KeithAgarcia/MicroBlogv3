package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;

public class Main {

    static HashMap<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        Spark.init();
        Spark.get( //creating a basic default get route
                "/",
                ((request, response) -> {
                    HashMap m = new HashMap<>(); // creating a hashmap (m) for user.
                    Session session = request.session(); // starting a session
                    String name = session.attribute("userName"); //userName comes from /create-user post

                    User user = users.get(name); // string user is used for the name that's from the users hashmap.
                    if (user == null) {
                        return new ModelAndView(m, "index.html"); // go to  index when no username.
                    } else {
                        m.put("name", user.name); // putting user.name in "Name" which is used in {{name}} on messages. html.
                        return new ModelAndView(user, "messages.html");
                    }
                }),
                new MustacheTemplateEngine()
        );
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String name = request.queryParams("loginName"); //comes from the name = "loginName" and turns it into name
                    User user = users.get(name); //calling a user from the name in the users arraylist.
                    if (user == null) {
                        user = new User(name); //creating a new user
                        users.put(name, user); // putting that user into a users hashmap and using user as the name for the hashmap.
                    }

                    Session session = request.session();
                    session.attribute("userName", name); //putting name into "userName" before sending it back to th "/" route

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/create-message",
                ((request, response) -> {
                    Session session = request.session();
                    String name = session.attribute("userName"); //using userName from session and changing
                    User user = users.get(name);
//                    if (user == null) {  NOT USED
//                        throw new Exception("User is not logged in"); NOT USED
//                    } NOT USED
                    String userMessage = request.queryParams("userMessage"); // comes from userMessage on messages.html

                    Message message = new Message(userMessage);
                    user.messages.add(message); // in the user class adding message to the messages arraylist in use class.

                    response.redirect("/");
                    return "";
                })
        );
        Spark.post(
                "/logout",
                ((request, response) -> {
                    Session session = request.session();
                    session.invalidate(); //terminates session
                    response.redirect("/");
                    return "";
                })
        );
    }
}
