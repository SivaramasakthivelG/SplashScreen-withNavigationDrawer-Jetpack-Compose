package com.example.composepractice

sealed class Screens(val screens: String){
    data object Home: Screens("home")
    data object Profile: Screens("profile")
}
