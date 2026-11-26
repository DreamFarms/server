package com.server.esgcafe.domain.dto.userInventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.esgcafe.domain.enum_class.ItemType;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ItemType itemType;

    public UserInventoryResponse(int code, String name, int count) {
        this(code, name, count, null);
    }
}
