
<p align = right> <img src =https://forthebadge.com/images/badges/made-with-java.svg></p>

Programmation Java @ Et3  
Polytech Paris-Saclay | 2020-21

<h1 align = "center">3D-DATA-VIEWER</h1>
<p align = center> I love Delphinidae 🐬 </p>

***
## About
Interface Homme-Machine permettant de visualiser des données via l'API OBIS</br>
Projet IHM (Interface Homme-Machine) réalisé dans le cadre du cursus scolaire à Polytech Paris-Saclay</br>
Java FX16 - JRE11

## Dependencies

 1. ControlsFX : ControlsFX is a library of UI controls and useful API for JavaFX 8.0 and beyond
 2. Apache Commons Packages : The JavaFX 3D model importers enable JavaFX applications to access 3D models and scenes provided in files based on widely supported 3D formats.
 3. JSON-20210307 : To read JSON file

## Quelques Explications

<p align = center>
 L'interface graphique est composée de 2 grandes parties : une map-monde et tout ce qui peut intéragir avec celle-ci </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/presentation.PNG alt="présentation"/>
</p>

<p align = center>
 La première fonctionalité est de pouvoir rechercher les signalements d'une espèce en entrant son nom. </br> 
 Il est possible de borner cette recherche en ajoutant une période à la recherche </br>
 Enfin en modifiant le geoHash, la précision de la recherche va être différente </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/date.PNG alt="date"/> </br>
 </br> L'autocomplétion est disponible afin de faciliter les recherches de l'utilisateur </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/autocompletion.png alt="autocomplétion"/> </br> </br>
 Le résultat de cette recherche est affiché sur la map-monde de manière légendé. </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/rechercher.PNG alt="recherche"/> </br> </br> </br>
</p>

<p align = center>
 La seconde fonctionnalité est le fait de pouvoir intéragir directement avec la map-monde. </br>
 En faisant ALT + CLIC sur celle-ci, le logiciel affichera toutes les espèces disponible dans la zone du clic. </br>
 Le clic est plus ou moins selon la valeur du geoHash. </br>
 En intéragissant avec la liste il est possible d'obtenir plus d'informations sur une espèce. </br>
 Enfin en double cliquant sur une espèce de la liste, une recherche est lancée </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/alt_click.PNG alt="altclick"/> </br>
</p>

<p align = center>
 La dernière fonctionalité est le mode Histogramme. Il permet de voir le resultat d'une recherche d'une manière différente. </br>
 D'autres boutons sont à disposition pour contrôler la map-monde et une animation des données est possible si la recherche est bornée </br> </br>
 <img src = https://github.com/dopage/3D-Data-Viewer/blob/main/screen/histo_slider.PNG alt="histo"/> </br>
</p>


## Credits ©
|    Name        |GitHub
|----------------|-----------|
|Rayane Hammadou |@dopage    |       
|Neraudau Esteban|@s-posito	 |


   
