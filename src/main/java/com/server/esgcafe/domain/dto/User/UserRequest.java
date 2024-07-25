package com.server.esgcafe.domain.dto.User;

import com.server.esgcafe.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 5, message = "닉네임은 5글자 이하여야 합니다.")
    private String nickname;

    public User toEntity() {
        return User.builder()
                .nickName(getNickname())
                .build();
    }

}
