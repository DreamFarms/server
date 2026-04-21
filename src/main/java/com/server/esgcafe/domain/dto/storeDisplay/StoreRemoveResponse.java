package com.server.esgcafe.domain.dto.storeDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRemoveResponse {

    private Integer tableNo;
    private Integer slotNo;
    private String name;
    private Integer slotCount;
    private Integer inventoryCount;

}
