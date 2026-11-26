package com.server.esgcafe.domain.dto.customization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationUpdateRequest {

    private String nickname;
    private List<CustomizationChangeRequest> changes;
}
