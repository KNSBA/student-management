package com.example.studentmanager

import java.io.Serializable

data class Student(
    var name: String,
    var mssv: String,
    var email: String,
    var phone: String
) : Serializable
