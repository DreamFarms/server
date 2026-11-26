package com.server.esgcafe.domain.dto.kneading;

import com.server.esgcafe.domain.dto.userInventory.UserInventoryResponse;
import com.server.esgcafe.domain.enum_class.KneadingGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class KneadingResultResponse {
    private Long attemptNo;
    private String foodName;
    private KneadingGrade grade;
    private int grantedDoughCount;
    private List<UserInventoryResponse> inventory;
}
