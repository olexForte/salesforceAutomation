package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.salesforce.common.RegistrationComponent;
import configuration.ProjectConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class UserPull {
    private static final Logger logger = LoggerFactory.getLogger(UserPull.class);
    static List<User> users = new ArrayList<>();
    static int timeoutForUser = 120; // minutes
    static int timeoutForFindUser = 10; // minutes
    private static UserPull instance = null;


    public static UserPull getInstance() {
        if (instance == null)
            instance = new UserPull();

        return instance;
    }

    static public synchronized User getUser(String... permission) throws Exception {
        int time = 0;
        do {
            for (User user : users) {
                if (user.getExpiredAt().isAfter(LocalDateTime.now())) {
                    user.setStatus(User.Status.AVAILABLE);
                    logger.info("User "+user.getUsername()+", timeout was expired, status set as available");
                }


                if (user.getPermission().equals(permission)) {
                    if (user.getStatus().equals(User.Status.AVAILABLE)) {
                        freezeUser(user);
                        return user;
                    }
                } else
                    throw new Exception("Don`t exist user with permission " + permission);

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        } while (time++ < timeoutForFindUser);
        return null;

    }

    static public synchronized User getAnyUser(String permission) throws Exception {

        for (User user : users) {
            if (user.getPermission().equals(permission)) {
                return user;
            } else
                throw new Exception("Don`t exist user with permission " + permission);
        }
        return null;
    }


    static public void releaseUser(User user) {
        user.setStatus(User.Status.AVAILABLE);
    }

    static public void freezeUser(User user) {
        ProjectConfiguration.setLocalThreadConfigProperty(user.username, LocalDateTime.now().toString());
        user.setStatus(User.Status.RESERVED);
        user.setExpiredAt(LocalDateTime.now().plusMinutes(timeoutForUser));
        logger.info("User "+user.getUsername()+" was froze to "+ user.getExpiredAt());
    }


    public static void toObjects(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
        users = Arrays.asList(gson.fromJson(json, User[].class).clone());
    }
}
