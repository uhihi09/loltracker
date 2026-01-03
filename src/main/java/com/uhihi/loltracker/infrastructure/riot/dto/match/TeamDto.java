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
public class TeamDto {

    private int teamId;
    private boolean win;

    // Objectives
    private ObjectivesDto objectives;

    // Bans
    private List<BanDto> bans;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ObjectivesDto {
        private ObjectiveDto baron;
        private ObjectiveDto champion;
        private ObjectiveDto dragon;
        private ObjectiveDto inhibitor;
        private ObjectiveDto riftHerald;
        private ObjectiveDto tower;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ObjectiveDto {
            private boolean first;
            private int kills;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BanDto {
        private int championId;
        private int pickTurn;
    }
}