package com.hiarc.Hiting.domain.admin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolvedTagDTO {

    private String key;
    private int solved;

    @JsonProperty("solved")
    public void setSolved(int solved) {
        this.solved = solved;
    }

    @JsonProperty("tag")
    private void unpackTag(Map<String, Object> tag) {
        this.key = (String) tag.get("key");
    }

}
