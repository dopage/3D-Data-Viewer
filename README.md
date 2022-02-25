
<p align = right> <img src =https://forthebadge.com/images/badges/made-with-java.svg></p>

Java Progamming @ ET3  
Polytech Paris-Saclay | 2020-2021

<h1 align = "center">3D-DATA-VIEWER</h1>
<p align = center> I love Delphinidae 🐬 </p>

***
## About

The goal of this project is to allow the visualization of data from an external API in a 3D representation on the surface of the earth. The API used is a public database called OBIS which contains the list of all the reports of marine species for several years. We have created a graphical interface to search, select, compare, filter and learn about these species.

## Technologies & Dependencies

- Java (JRE11) : Programming language used.
- JavaFX (v16) : A framework to create a graphical interface for desktop applications.
- ControlsFX : ControlsFX is a library of UI controls and useful API.
- ObjModelImporterJFX.jar : Enable JavaFX applications to access 3D models and scenes provided in files based on widely supported 3D formats.
- JSON-20210307.jar : To read JSON file.

## Features & Explanations

<p align = center>
 The graphical interface is composed of 2 big parts: on one side a world-map and on the other side everything that can interact with it.</br></br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/presentation.PNG alt="présentation"/>
</p>

<p align = center>
 The first feature is to search for records of a species by entering its name. 
 It is possible to restrict this search by adding a period to the search.
 Finally by modifying the geoHash, the precision of the search will be different.</br></br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/date.PNG alt="date"/></br></br>
 Auto-completion is available to facilitate the user's search.</br></br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/autocompletion.png alt="autocomplétion"/></br></br>
 The result of this search is displayed on the world map with a caption.</br></br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/rechercher.PNG alt="recherche"/></br></br>
</p>

<p align = center>
 The second feature is the ability to interact directly with the world map.</br>
 By using `ALT + CLICK` on it, the program will display all the species that have already been reported in the click area.</br>
 The click is more or less accurate depending on the geoHash value.</br>
 By interacting with the list it is possible to get more information about a species.</br>
 Finally, by double clicking on a species in the list, a search is initiated.</br></br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/alt_click.PNG alt="altclick"/> </br>
</p>

<p align = center>
 The last feature is the Histogram mode. It allows you to see the result of a search in a different way.</br>
 Other buttons are available to control the world map and an animation of the data is possible if the search is bounded by dates.</br> </br>
 <img src = https://raw.githubusercontent.com/dopage/3D-Data-Viewer/main/screen/histo_slider.PNG alt="histo"/> </br>
</p>


## Authors
| Name             | GitHub      |
| ---------------- | ----------- |
| Rayane Hammadou  | [@dopage]   |
| Esteban Neraudau | [@s-posito] |


[@dopage]: https://github.com/dopage
[@s-posito]: https://github.com/s-posito
