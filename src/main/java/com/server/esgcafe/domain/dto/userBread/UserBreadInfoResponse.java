package com.server.esgcafe.domain.dto.userBread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBreadInfoResponse {

    private List<BreadInfo> breadInfoList;

}