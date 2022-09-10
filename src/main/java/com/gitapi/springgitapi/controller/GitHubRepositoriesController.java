package com.gitapi.springgitapi.controller;

import com.gitapi.springgitapi.DTO.RepoResponseDto;
import com.gitapi.springgitapi.service.apiClient.impl.ApiClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/repository")
public class GitHubRepositoriesController {

    private  final ApiClientImpl apiClient;

    @Autowired
    public GitHubRepositoriesController(ApiClientImpl apiClient) {
        this.apiClient = apiClient;
    }

    @GetMapping("/getRepoByUser")
    public ResponseEntity<List<RepoResponseDto>> getAll(@RequestParam(required = true) String user){
        return apiClient.getAllRepositoriesByUser(user);
    }

}
