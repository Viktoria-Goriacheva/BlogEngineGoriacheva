package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "photo",
    "email",
    "moderation",
    "moderationCount",
    "settings"
})
public class UserResponse {

  public int id;
  public String name;
  public String photo;
  public String email;
  public boolean moderation;
  public int moderationCount;
  public boolean settings;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isModeration() {
    return moderation;
  }

  public void setModeration(boolean moderation) {
    this.moderation = moderation;
  }

  public int getModerationCount() {
    return moderationCount;
  }

  public void setModerationCount(int moderationCount) {
    this.moderationCount = moderationCount;
  }

  public boolean isSettings() {
    return settings;
  }

  public void setSettings(boolean settings) {
    this.settings = settings;
  }
}
