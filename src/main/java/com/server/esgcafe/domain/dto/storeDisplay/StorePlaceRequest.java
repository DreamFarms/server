package com.server.esgcafe.domain.dto.storeDisplay;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StorePlaceRequest {

    private String nickname;
    private Integer tableNo;
    private Integer slotNo;
    private String name;
    private Integer quantity;

}
