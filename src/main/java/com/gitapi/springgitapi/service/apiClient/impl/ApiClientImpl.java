package com.gitapi.springgitapi.service.apiClient.impl;


import com.gitapi.springgitapi.DTO.BranchRequestDTO;
import com.gitapi.springgitapi.DTO.BranchResponseDTO;
import com.gitapi.springgitapi.DTO.RepoResponseDto;
import com.gitapi.springgitapi.DTO.RepositoryRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ApiClientImpl {

    private final RestTemplate restTemplate;
    static final String HOST_GIT_HUB_FOR_ALL_REPO = "https://api.github.com/users/";
    static final String HOST_GIT_HUB_FOR_ALL_BRANCHES = "https://api.github.com/repos/";

    @Autowired
    public ApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public ResponseEntity<List<RepoResponseDto>> getAllRepositoriesByUser(String user) {

        ResponseEntity<RepositoryRequestDTO[]> response = restTemplate.getForEntity(HOST_GIT_HUB_FOR_ALL_REPO + user + "/repos", RepositoryRequestDTO[].class);
        Map<RepositoryRequestDTO, BranchRequestDTO[]> result = Arrays.stream(response.getBody())
                .filter(x -> x.getFork().equals(false))
                .collect(Collectors.toMap(x -> x,
                        x -> restTemplate.getForEntity(HOST_GIT_HUB_FOR_ALL_BRANCHES
                                + x.getOwner().getLogin() + "/" + x.getName()
                                + "/branches", BranchRequestDTO[].class).getBody()));

        List<RepoResponseDto> repoList = new ArrayList<>();
        for (Map.Entry<RepositoryRequestDTO, BranchRequestDTO[]> pair : result.entrySet()) {
            List<BranchResponseDTO> branchResponseDTOS = new ArrayList<>();
            for (BranchRequestDTO br : pair.getValue()) {
                branchResponseDTOS.add(new BranchResponseDTO(br.getName(), br.getCommit().getSha()));
            }
            repoList.add(new RepoResponseDto(pair.getKey().getName(), pair.getKey().getOwner().getLogin(), branchResponseDTOS));
        }
        return new ResponseEntity<>(repoList, HttpStatus.OK);
    }

}
