package com.server.esgcafe.domain.enum_class;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KneadingGrade {
    A(3), B(2), C(1), F(0);

    private final int doughCount;
}
