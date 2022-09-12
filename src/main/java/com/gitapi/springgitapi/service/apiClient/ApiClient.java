package com.gitapi.springgitapi.service.apiClient;

import com.gitapi.springgitapi.DTO.RepoResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ApiClient {

    List<RepoResponseDto> getAllRepositoriesByUser(String user);

}
