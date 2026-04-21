package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@AllArgsConstructor
public class SellBreadResponse {

    private long currentGold;
    private List<SoldSlotInfo> soldSlots;

    @Getter
    @AllArgsConstructor
    public static class SoldSlotInfo {
        private Integer tableNo;
        private Integer slotNo;
        private String name;
        private Integer remainCount;
    }

}
