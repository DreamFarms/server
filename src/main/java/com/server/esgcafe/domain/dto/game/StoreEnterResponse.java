package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StoreEnterResponse {

    private long gold;
    private long cash;
    private List<BreadInfo> breads;

    @Getter
    @AllArgsConstructor
    public static class BreadInfo {
        private String name;
        private int count;
    }
}