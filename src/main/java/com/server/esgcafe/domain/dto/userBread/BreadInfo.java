package com.server.esgcafe.domain.dto.userBread;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BreadInfo {

    private String name;
    private int count;

}