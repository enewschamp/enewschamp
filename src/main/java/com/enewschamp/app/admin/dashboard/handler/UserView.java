package com.enewschamp.app.admin.dashboard.handler;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface UserView {
   @JsonProperty("id")
   String getUserId();
   String getName();
   String getSurname();
   String getOtherNames();
   String getTitle();
   String getIsActive();
}
