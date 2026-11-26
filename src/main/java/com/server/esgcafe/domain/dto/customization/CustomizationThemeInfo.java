package com.server.esgcafe.domain.dto.customization;

import com.server.esgcafe.domain.enum_class.FurnitureType;
import com.server.esgcafe.domain.enum_class.ThemeCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomizationThemeInfo {

    private FurnitureType themePartType;
    private ThemeCode themeCode;
}
