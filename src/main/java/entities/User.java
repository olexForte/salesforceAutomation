package entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

public class User {
    public String id;
    public String password;
    public String username;
    public Status status=Status.AVAILABLE;
    public Permission permission;

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime expiredAt=null;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public enum Status{
        AVAILABLE("available"),
        RESERVED("reserved");

        public String getStatus() {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }

        String status;

        Status(String status) {
            this.status = status;
        }
    }

    public enum Permission{
        ADMIN("admin"),
        ORDINARY("ordinary"),
        ACCOUNT_MANAGER("account_manager");


        public String getStatus() {
            return permission;
        }

        public void setStatus(String permission) {
            this.permission = permission;
        }

        String permission;

        Permission(String permission) {
            this.permission = permission;
        }
    }

}
