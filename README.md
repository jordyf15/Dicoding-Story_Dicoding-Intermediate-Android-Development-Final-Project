# Dicoding Story

## Final Project for Dicoding's Intermediate Android Development Course
Dicoding Story is an android application where users can view and post stories. Users can also view the locations of those stories in Google Map. This project was made in order to pass the Dicoding's Intermediate Android Development Course.

## Application Detail
The application consist of 6 activities. They are Register Activity, Login Activity, Main Activity, Detail Activity, Map Activity and Add Story Activity.

### Register Activity
The activity / page where the user can register their account. In here the user must provide their full name, email, and password.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/27/58/58/2758583c3f65d08bd05985947fa195a5.jpg" alt="Dicoding Story's Register Activity">
</p>

### Login Activity
The activity / page where the user can login into their account, by entering their email and password.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/de/4c/de/de4cded1c881f00516e1ff29f3ba3f2b.jpg" alt="Dicoding Story's Login Activity">
</p>

### Main Activity
The activity / page where the user can view other user's stories. The user must be logged in before they can access this activity. In this activity the user can also view the detail of the stories by clicking the story.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/19/ac/b0/19acb0c7b12833ee375e0cbe099955b7.jpg" alt="Dicoding Story's Main Activity">
</p>

### Detail Activity
The activity / page where user can view more information of the story. This activity can be accessed by clicking any of the story in the Main Activity.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/05/ec/01/05ec0105f199b065aab41e03f74e4c0e.jpg" alt="Dicoding Story's Detail Activity">
</p>

### Map Activity
The activity / page where user can view the locations where the stories are posted in google map. This activity can be accessed by clicking the map icon in the action bar / top part of your screen in the Main Activity or Detail Activity.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/d4/3e/06/d43e063f5f9e84249355754a17382848.jpg" alt="Dicoding Story's Map Activity">
</p>

### Add Story Activity
The activity / page where user can post their own story. The user must provide a description and a picture for the story. As for the picture, the user can pick from their gallery or take a picture with their camera. This activity can be accessed by clicking the add icon in the action bar or top part of your screen in the Main Activity or Detail Activity.
<p align="center">
<img width="300px" src="https://i.pinimg.com/originals/0e/f1/67/0ef167a41267369cf9abdb1b46cc8e7e.jpg" alt="Dicoding Story's Add Story Activity No Picture">
<img width="300px" src="https://i.pinimg.com/originals/9d/8f/7e/9d8f7ec229154915b140d68511961df5.jpg" alt="Dicoding Story's Add Story Activity Picture Selected">
</p>

## Installation
Clone this repository and import it into Android Studio

Note: You will need to provide your own Google Map API Key in local.properties in the project level directory. In order to build and run this application properly.  
Example:
```
MAPS_API_KEY=loremipsumloremipsumloremipsumloremipsum
```