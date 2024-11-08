package com.server.esgcafe.domain.dto.userInventory;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInventoryResponse {

    private int code;
    private String name;
    private int count;
}
