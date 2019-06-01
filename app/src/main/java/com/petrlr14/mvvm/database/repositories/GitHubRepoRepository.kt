package com.petrlr14.mvvm.database.repositories

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.petrlr14.mvvm.database.daos.GitHubDAO
import com.petrlr14.mvvm.database.entities.GithubRepo
import com.petrlr14.mvvm.service.retrofit.GithubService
import kotlinx.coroutines.Deferred
import retrofit2.Response

class GitHubRepoRepository (private val repoDao:GitHubDAO){

    @WorkerThread
    suspend fun insert(repo:GithubRepo){
        repoDao.insert(repo)
    }

    fun getAll():LiveData<List<GithubRepo>>{
        return repoDao.getAllRepos()
    }

    @WorkerThread
    suspend fun nuke(){
        return repoDao.nukeTable()
    }

    fun retrieveReposAsync(user: String): Deferred<Response<List<GithubRepo>>> {
        return GithubService.getRetrofit().getRepos(user)
    }

}