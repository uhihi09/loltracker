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
public class MetadataDto {

    private String dataVersion;
    private String matchId;
    private List<String> participants;  // PUUID list
}