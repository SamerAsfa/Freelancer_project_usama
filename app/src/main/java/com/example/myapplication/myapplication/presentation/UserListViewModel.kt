package com.example.myapplication.myapplication.presentation

import android.net.Uri
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.myapplication.data.UserDataProvider
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userDataProvider: UserDataProvider
) : ViewModel() {

    val userData: LiveData<List<UserListingViewModel>>
        get() = _userData

    private val _userData = MutableLiveData<List<UserListingViewModel>>(emptyList())


    val showProgressBarUserInfo: LiveData<Boolean>
        get() = _showProgressBarUserInfo

    private val _showProgressBarUserInfo = MutableLiveData<Boolean>(false)

    init {
        loadUserData()
    }


    private fun loadUserData() {
        _showProgressBarUserInfo.postValue(true)
        viewModelScope.launch {
            val userList = mutableListOf<UserListingViewModel>()
            userDataProvider.getUserData().addOnSuccessListener { it ->
                it.documents.forEach {
                    val user = UserListingViewModel(
                        name = it.data?.getValue("name").toString(),
                        id = it.data?.getValue("id").toString(),
                        profilePicUrl = it.data?.getValue("profile_pic_url").toString()
                    );
                    userList.add(user)
                }
                _showProgressBarUserInfo.postValue(false)
                _userData.postValue(userList)
            }
        }
    }

    fun uploadingImage(filePath: Uri,addOnSuccessListener: (UploadTask.TaskSnapshot) -> Unit,addOnFailureListener: (Exception) -> Unit,addOnCompleteListener: (Uri) -> Unit)   {
        viewModelScope.launch {
            val storageReference = userDataProvider.uploadUserImageData()
            storageReference.putFile(filePath).addOnSuccessListener {
                  addOnSuccessListener(it)
              }.addOnFailureListener {
                  addOnFailureListener(it)
              }.addOnCompleteListener{
                storageReference.downloadUrl.addOnSuccessListener{
                      addOnCompleteListener(it)
                  }

              }
        }
    }


    fun searchByNameData(query: String) {
        _showProgressBarUserInfo.postValue(true)
        viewModelScope.launch {
            val userList = mutableListOf<UserListingViewModel>()
            userDataProvider.getUserData().addOnSuccessListener { it ->
                it.documents.forEach {
                    val user = UserListingViewModel(
                        name = it.data?.getValue("name").toString(),
                        id = it.data?.getValue("id").toString(),
                        profilePicUrl = it.data?.getValue("profile_pic_url").toString()
                    );
                    if (query.isEmpty()) {
                        userList.add(user)
                    } else if (user.name.contains(query)) {
                        userList.add(user)
                    }
                }
                _showProgressBarUserInfo.postValue(false)
                _userData.postValue(userList)
            }
        }
    }


    fun filterImagesData(hasImage: Boolean) {
        _showProgressBarUserInfo.postValue(true)
        viewModelScope.launch {
            val userList = mutableListOf<UserListingViewModel>()
            userDataProvider.getUserData().addOnSuccessListener { it ->
                it.documents.forEach {
                    val user = UserListingViewModel(
                        name = it.data?.getValue("name").toString(),
                        id = it.data?.getValue("id").toString(),
                        profilePicUrl = it.data?.getValue("profile_pic_url").toString()
                    );
                    if (hasImage) {
                        if (!TextUtils.isEmpty(user.profilePicUrl.trim()) && !user.profilePicUrl.contentEquals(
                                "null"
                            )
                        ) {
                            userList.add(user)
                        }
                    } else {
                        if (TextUtils.isEmpty(user.profilePicUrl.trim()) || user.profilePicUrl.contentEquals(
                                "null"
                            )
                        ) {
                            userList.add(user)
                        }
                    }

                }
                _showProgressBarUserInfo.postValue(false)
                _userData.postValue(userList)
            }
        }
    }


    fun addUserData(name: String, url: String, deviceId: String) {
        _showProgressBarUserInfo.postValue(true)
        viewModelScope.launch {
            userDataProvider.addUserData(name, url, deviceId).addOnSuccessListener {
                loadUserData()
            }
        }
    }


    companion object {
        const val LISTING_ITEM = 1
    }
}