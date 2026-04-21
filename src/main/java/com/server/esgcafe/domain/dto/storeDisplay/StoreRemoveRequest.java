package com.server.esgcafe.domain.dto.storeDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRemoveRequest {

    private String nickname;
    private Integer tableNo;
    private Integer slotNo;
    private Integer quantity;

}
