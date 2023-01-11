package com.dctcomposables.biobuilder.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.sql.Time

class ProfileModel: ViewModel() {

    private val _profilePicId: MutableLiveData<Int> = MutableLiveData()
    val profilePicId: LiveData<Int> = _profilePicId

    private val _name: MutableLiveData<String> = MutableLiveData()
    val name: LiveData<String> = _name

    private val _dob: MutableLiveData<Time> = MutableLiveData()
    val dob: LiveData<Time> = _dob

    private val _about: MutableLiveData<String> = MutableLiveData()
    val about: LiveData<String> = _about

    private val _email: MutableLiveData<String> = MutableLiveData()
    val email: LiveData<String> = _email

    private val _phone: MutableLiveData<String> = MutableLiveData()
    val phone: LiveData<String> = _phone

    fun updateName(name: String) {
        _name.value = name
    }

    fun updateDob(time: Time) {
        _dob.value = time
    }

    fun updateAbout(about: String) {
        _about.value = about
    }

    fun updateEmail(email: String) {
        _email.value = email
    }

    fun updatePhone(phone: String) {
        _phone.value = phone
    }

    fun updatePicId(id: Int) {
        _profilePicId.value = id
    }

    fun updateProfile(name: String, about: String, email: String, phone: String) {
        updateName(name)
        updateAbout(about)
        updateEmail(email)
        updatePhone(phone)
    }
}