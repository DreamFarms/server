package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreEnterResponse {

    private long gold;
    private long cash;
    private List<InventoryBreadInfo> inventoryBreads;
    private List<DisplayBreadInfo> displayBreads;

    @Getter
    @AllArgsConstructor
    public static class InventoryBreadInfo {
        private String name;
        private int count;
    }

    @Getter
    @AllArgsConstructor
    public static class DisplayBreadInfo {
        private Integer tableNo;
        private Integer slotNo;
        private String name;
        private int count;
    }
}