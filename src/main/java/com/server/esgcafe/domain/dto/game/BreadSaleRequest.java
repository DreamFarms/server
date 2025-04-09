package com.server.esgcafe.domain.dto.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreadSaleRequest {

    private String nickname;
    private List<BreadSaleItem> breadList;
}