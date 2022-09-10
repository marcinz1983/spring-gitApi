package com.gitapi.springgitapi.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RepositoryRequestDTO {

    private String name;
    private OwnerRequestDTO owner;
    private Boolean fork;
    private String branchesUrl;

}