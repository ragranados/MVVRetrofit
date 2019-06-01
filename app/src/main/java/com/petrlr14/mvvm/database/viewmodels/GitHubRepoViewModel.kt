package com.petrlr14.mvvm.database.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.petrlr14.mvvm.database.RoomDB
import com.petrlr14.mvvm.database.entities.GithubRepo
import com.petrlr14.mvvm.database.repositories.GitHubRepoRepository
import kotlinx.coroutines.launch

class GitHubRepoViewModel(private val app: Application) : AndroidViewModel(app) {


    fun retrieveRepo(user: String) = viewModelScope.launch {
        /**
         * Se borra la tabla de repos
         */
        this@GitHubRepoViewModel.nuke()

        /**
         * obtiene el objeto, respuesta de la consulta a la API
         */

        val response = repository.retrieveReposAsync(user).await()

        /**
         *evalua y decide depediendo del estado de la respuetsa obtenida
         */

        if(response.isSuccessful) with(response){
            //toda la lista en la base
            this.body()?.forEach{
                this@GitHubRepoViewModel.insert(it)
            }
        }else{
            with(response){
                println(this.code())
                when(this.code()){
                  404->{ Toast.makeText(app, "Usuario no encontrado", Toast.LENGTH_LONG).show() }
                }
            }
        }



    }

    private val repository: GitHubRepoRepository

    init {
        val repoDao=RoomDB.getInstance(app).repoDao()
        repository= GitHubRepoRepository(repoDao)
    }

    private suspend fun insert(repo:GithubRepo)=repository.insert(repo)

    fun getAll():LiveData<List<GithubRepo>>{
        return repository.getAll()
    }

    private suspend fun nuke()= repository.nuke()

}