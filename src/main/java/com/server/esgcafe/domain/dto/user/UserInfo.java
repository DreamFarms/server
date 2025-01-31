package com.server.esgcafe.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfo {

    private String id;
    private String email;
    private String name;
    private String picture;

    @JsonProperty("verified_email")
    private Boolean verifiedEmail;


}
