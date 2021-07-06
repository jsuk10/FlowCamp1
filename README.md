# FlowCamp1

## Week 1 assignment

Development sprint : 1 week( 21.06.30 ~ 21.07.06 )


### Library Dependency

App development using Android Studio 4.2.1


### Team

Kim HyeonJi, [Lee JeongSeok](https://www.notion.so/JEONGSEOK-LEE-eca35bb9a8be48feb92029bf2f8a3298)


  

# Project Description

Receive the fragment adapter from the main activity and change the fragment  

Fragment Base Tab3
tab1|tab2|tab3|
---|---|---|
Contacts|Gallery|Music|

Through contentResolver, it accesses database and external storage of the mobile phone.
and Reads data, and List up files.


Icon|
---|
![icon](./img/icon_madcamp.png)|
[APK file link](https://drive.google.com/file/d/1C3vrMhj9cooshJgfzQY92SEJI-FF-Wfr/view?usp=sharing)|


---
# Detail
## Main

1. Add "FlowCamp" image to titlebar and displayed
2. Create 3 Fragment tabs through the Fragment adapter and add a function to change the tab when clicking each button or swiping window left or right
3. When you slide up, a refresh effect occurs and the window is refreshed
     Get data again
<p align="center"> 
   <img src="./img/total.gif"> 
</p> 

## Tab Common Features

1. Sort item list
2. Access phone data directly (Permission Check()

## Tab1 : Contacts - ListView

1. It reads the contacts stored in the database inside the mobile phone
   - Display the phone number, name, and image. 
   - The image was marked in a circle.
2. When each contact is clicked, the phone's calling function is turned on immediately to make a call possible.
3. By adding a floating action button, a dialog is output when clicked
   - Add a name and number to a contact when inputing a name and number in text in the dialog
<p align="center"> <img src="./img/tab1.gif"> </p> 

---

## Tab2 Gallery - GridView

1. Display the image stored in external storage inside the phone in grid view
   - In this case, it is written as (n x n) according to the size of the mobile phone.
2. When click each image, the image is enlarged
   - Click outside the enlarged image to close the dialog
<p align="center"> <img src="./img/tab2.gif"> </p> 

---

## Tab3 Musics - ListView

1. It reads the audio stored in the phone's external storage
     - Display the title, artist, duration, and album art.
2. When a music list item is pressed, a music is played.
3. Add current music info bar at the bottom.
     - When you slide seekbar thumb, then the music's current position is changed.
     - Read and mark the playing time and length of each music
4. Create previous music, pause, next music play button
     - Click the pause button, replace the image to play button

5. When you select music, the music album, name, and artist are displayed at the bottom.
     - If the title is long, it will slowly move to the left.
6. Play in the background
     - Lock your phone or Switch apps, and you'll see the music.
<p align="center"> <img src="./img/tab3.gif"> </p> 
---

# Design References in YouTube

![inapp](./img/ref.png)
[YouTube](https://www.youtube.com)