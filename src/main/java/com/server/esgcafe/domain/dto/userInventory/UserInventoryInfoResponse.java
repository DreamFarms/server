package com.server.esgcafe.domain.dto.userInventory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInventoryInfoResponse {

    private List<BreadInfo> breadInfoList;

}