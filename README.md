# *Goden Entertainment APS* 
## Project Manager Application

### Links
* Vores [Github Link][githublink]
* Link til vores applikation på Azure: https://project-manager-e2b8ceejfmgaddh7.norwayeast-01.azurewebsites.net/user/login

## Introduktion
 __Velkommen til dette repository som har til formål at udvikle et Scrum management system til vores kunde__: [Alpha Solutions][alphalink].

Vi er fire studerende, som går på EK, der har fået en deadline til dette eksamens projekt, der er til _17. December 2025_.

I dette repository har vi til opgave at udføre et program der har til formål at struktere et projekt baseret system.

## Program guide *-VIGTIG-*
__Admin-bruger:__ username: ADMIN , password: admin123

* __For at bruge systemets project-management funktioner skal *admin* først lave en project-manager og devs til teams.__
1. Log ind med ADMIN bruger
2. Opret/slette evt brugere efter behov.
3. Log ud efter brugere er oprettet og log ind som project manager brugeren du har lavet.
4. Done! Held og lykke med jeres projekter.

>Password er ikke påkrevet for at oprette devs. Det eneste der er påkrevet for at oprette en project-manager er username, password og userType(PROJECTMANAGER).
Email og worktime per day er ikke implimenteret i systemets nuværende funtionalitet og det er derfor anbefalet ikke at udfylde de felter.

## Database Set-up *-VIGTIGT-(Hvis Azure ikke er deployet)*
Database navn: project_manager
Vi benytter os af MySQL workbench og bruger det som vores database i vores projekt.

1. Opret et schema i MySQL ved navn project_manager.

2. Når programmet bliver kørt bliver tabellerne og evt data automatisk oprettet i databasen.

## Funktionalitet af programmet
* Programmet har til formål at gøre det lettere for en project manager at tidsestimere og strukturer hans projekter mm.
* Tidsestimering kan sættes på hvert Projekt, SubProject, Task og SubTask. 
Actual time værdien burde kun sættes i Subtasks siden at actual time værdien i Projects, SubProjects og Tasks bliver udregnet baseret på deres Subtasks.


## Opretterer af produktet aka OGs.
Dette projekt er lavet af følgende personer:
* [Marco][marcoLink]
* [Yacqub][yacLink]
* [Rune][runeLink]
* [August][augustLink]
> Link til [contributing fil][conLink]

[githublink]: https://github.com/Goden-Entertainment
[alphalink]: https://www.alpha-solutions.com/da
[runeLink]: https://github.com/runejy
[yacLink]: https://github.com/yacqubjad
[augustLink]: https://github.com/lornst
[marcoLink]: https://github.com/mstor332
[conLink]: https://github.com/Goden-Entertainment/ProjectManager/blob/main/CONTRIBUTING.md
