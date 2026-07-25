package com.server.esgcafe.domain.dto.userInventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserInventorySaveRequest {

    private String nickname;
    private List<UserInventoryInfo> rewards;

}
