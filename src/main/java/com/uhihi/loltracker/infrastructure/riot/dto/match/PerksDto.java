package com.uhihi.loltracker.infrastructure.riot.dto.match;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PerksDto {

    private PerkStatsDto statPerks;
    private List<PerkStyleDto> styles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PerkStatsDto {
        private int defense;
        private int flex;
        private int offense;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PerkStyleDto {
        private String description;
        private List<PerkStyleSelectionDto> selections;
        private int style;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PerkStyleSelectionDto {
            private int perk;
            private int var1;
            private int var2;
            private int var3;
        }
    }
}