package com.uhihi.loltracker.domain.player.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerRequest {

    @NotBlank(message = "게임 이름은 필수입니다")
    @Size(min = 3, max = 16, message = "게임 이름은 3-16자여야 합니다")
    private String gameName;

    @NotBlank(message = "태그라인은 필수입니다")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,5}$", message = "태그라인은 3-5자의 영문/숫자만 가능합니다")
    private String tagLine;

    public String getRiotId() {
        return gameName + "#" + tagLine;
    }
}