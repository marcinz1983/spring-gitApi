package com.gitapi.springgitapi.service.apiClient.impl;


import com.gitapi.springgitapi.DTO.BranchRequestDTO;
import com.gitapi.springgitapi.DTO.BranchResponseDTO;
import com.gitapi.springgitapi.DTO.RepoResponseDto;
import com.gitapi.springgitapi.DTO.RepositoryRequestDTO;
import com.gitapi.springgitapi.service.apiClient.ApiClient;
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
public class ApiClientImpl implements ApiClient {

    private final RestTemplate restTemplate;
    static final String HOST_GIT_HUB_FOR_ALL_REPO = "https://api.github.com/users/{user}/repos";
    static final String HOST_GIT_HUB_FOR_ALL_BRANCHES = "https://api.github.com/repos/{login}/{name}/branches";

    @Autowired
    public ApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

    }

    public List<RepoResponseDto> getAllRepositoriesByUser(String user) {
        RepositoryRequestDTO[] responseFromGitApi = getForGITApiResponse(HOST_GIT_HUB_FOR_ALL_REPO, RepositoryRequestDTO[].class, user);
        Map<RepositoryRequestDTO, BranchRequestDTO[]> result = getMapRepo(responseFromGitApi);
        return createRepoResponse(result);
    }


    private List<RepoResponseDto> createRepoResponse(Map<RepositoryRequestDTO, BranchRequestDTO[]> result) {
        List<RepoResponseDto> repoList = new ArrayList<>();
        for (Map.Entry<RepositoryRequestDTO, BranchRequestDTO[]> pair : result.entrySet()) {
            List<BranchResponseDTO> branchResponseDTOS = new ArrayList<>();
            for (BranchRequestDTO br : pair.getValue()) {
                branchResponseDTOS.add(new BranchResponseDTO(br.getName(), br.getCommit().getSha()));
            }
            repoList.add(new RepoResponseDto(pair.getKey().getName(), pair.getKey().getOwner().getLogin(), branchResponseDTOS));
        }
        return repoList;
    }

    private Map<RepositoryRequestDTO, BranchRequestDTO[]> getMapRepo(RepositoryRequestDTO[] responseFromGitApi) {
        return Arrays.stream(responseFromGitApi)
                .filter(x -> x.getFork().equals(false))
                .collect(Collectors.toMap(x -> x,
                        x -> getForGITApiResponse(HOST_GIT_HUB_FOR_ALL_BRANCHES, BranchRequestDTO[].class, x.getOwner().getLogin(), x.getName())));
    }

    private <T> T getForGITApiResponse(String url, Class<T> responseType, Object... objects) {
        return restTemplate.getForObject(url, responseType, objects);
    }


}
