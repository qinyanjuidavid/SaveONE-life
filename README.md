# SaveOne-Life

## Running the project
### Installation by Downloading the APK
- To download the apk click the link [SaveOne-Life]()
- Install the apk on your android mobile phone
### Installation by Cloning to Android Studio
- Download Android Studio
- Clown the repo using the command ``` git clone https://github.com/wykeenjenga/SaveONE-life.git```
- Run the android app on Android Studio


## Impact
SaveOne-Life is an android mobile application that helps solve three United Nations  Sustainable Development Goals(SDG). These goals are;
> #### No-Poverty
> According to the United Nations, one out of five children live in extreme poverty, and the negative effects of poverty and deprivation in the early years have ramifications that can last a lifetime. With SaveOne-Life, donors can be able to explore and donate to orphanages that are facing extreme poverty. Some of the items that can be donated to reduce poverty include; clothings, better housing, beddings, etc.

> #### Quality Education
> To protect the well-being of children and ensure they have access to continued learning, the SaveOne-Life android application enables donors to search for orphanages and donate learning materials such as books, pens, and many more.

> #### Zero-Hunger
> Zero hunger is one of the United Nations SDGS. To enhance and maintain zero hunger among the young generation, SaveONE life provides a platform whereby donors can donate food stuffs to the affected orphanages.

## Scalability of the project.
> SaveONE Life project is scalable,
> If we were to continue, we were to implement addition feature regarding the health of the childrens, whereby Orphan keeper will have to add and Indicate the number of childrens who are not in good health, this also will lead to additional integration with good willing health Sectors. Health Sector will be able to login to the app and locate those poor childrens that are in need.

> Most orphanages in Kenya are founded and run by individuals, not the government. and most wealthiest families have a tendance of adopting 1 or 2 childrens from the orphanage and help them either by educating them and many more. But some families they do adopt the childrens and mistreate them. In future we are planning to add more features whereby the app will be tracking the progress and the number of childrens whom are adopted, including the location and lifestyle of the childrens. This feature will help 
age Masters to track the well being of their childrens.

> M-Pesa is a virtual banking system that provides transaction services through a SIM card. Once the SIM has been inserted into the card slot of the mobile device, users can make payments and transfer money to vendors and family members with SMS messages. In future we are planning to integrate mpesa APIs with our app, so that it can be easy for donors to donate with mpesa with a single touch from the app.

## App Testing.
> We tested SaveONE-life App with two childrens home as shown on the screenshoots below. Orpan keeper were very excited because they will be able to express their needs via SaveONE life, for the real experience you can download SaveONE life from [Set Up](#SaveOne-Life)
> <img src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" data-canonical-src="https://user-images.githubusercontent.com/46722362/158591591-e0b1c6b8-d784-4a36-bebb-6a393d21424a.png" width="200" height="400" />


# TECHNOLOGY
SaveOne-Life is an android mobile application developed using Android Studio, Programming language used is JAVA.

## Google technology
> ### Firebase 
> #### Landing Page
> <img src="https://user-images.githubusercontent.com/46722362/158593532-1b32df9c-acb9-42c5-8312-c106a144b34a.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158593532-1b32df9c-acb9-42c5-8312-c106a144b34a.png"
       width="240" height="450" />

- On landing page we have 4 buttons which include Donate Now, Register an Orphanage, Register as a donor, and Login button.

> ### Donor Authentication 
-  Donor - To authenticate donor we used firebase auth dependecy (firebase-auth). 
```
implementation 'com.google.firebase:firebase-auth:21.0.1'

```
- Donor can opt to Donate without an account, but this has limitations for example he/she(Donor) will not be able to participate on [SaveONE -life forum](#SaveOne-Life).
- If donor wish to participate on SaveONE life forum, he/she must have an account.

> ### Orphange / Children's Home Authentication 
- To authenticate an orphanage, Orphan Keeper is supposed to register the orphange by clicking Register button, he will pass through 4 different fragments sections as shown below.
- ### 1. Basic Information 
- On this page Orphanage keeper is supposed to fill in name, email and password of the orphange as shown
- <img src="https://user-images.githubusercontent.com/46722362/158599594-b612623f-3de1-447e-b65a-1d48805fcc24.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158599594-b612623f-3de1-447e-b65a-1d48805fcc24.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158608189-5a1560c6-0f69-4b70-9d99-5fb20b8a9731.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158608189-5a1560c6-0f69-4b70-9d99-5fb20b8a9731.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158609052-5ab9ab48-04a3-4dc2-baa0-e05c88a33f39.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158609052-5ab9ab48-04a3-4dc2-baa0-e05c88a33f39.png"
       width="220" height="450" />
       
- ### 2. Additional Information
-  On this page Orphanage keeper is supposed to fill in Orphanage Group Photo, Phone number, Till number, Bank account number, Bank Name and country where the orphange is located.
- On this page we used firebase storage dependecy and storage reference class so as to enable user to upload group photo to firebase storage database.
- On this fragment we will create a sharable link of the image uploaded.
```
implementation 'com.google.firebase:firebase-storage:20.0.0'
```


- ### 3. Other Information
- On this page Orphanage keeper is supposed to fill in Brief description about the orphanage, number of children in the orphanage, what they need most (include food, water, clothings, money for school fees) and the location address of the orphanage. 

       
- ### 4. Orphanage location
- Orphanage location fragment shows a google map showing the current location of the user.
- Orphan keeper is supposed to drag and drop the pin to the actual position of the orphanage.
- We used google map API, the following dependecies were used  
- We created Google Map Api from google clouds
 ```
 build.grandle(:app)
 
 dependencies{
 implementation 'com.google.android.gms:play-services-maps:18.0.2'
 implementation 'com.google.android.gms:play-services-location:19.0.1'
 }
 
 dependencies{
 classpath 'com.google.gms:google-services:4.3.10'
 classpath 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1'
 
 }
 
 //on the manifest file  we added a meta data with a string of API key value 
 <meta-data
      android:name="com.google.android.geo.API_KEY"
      android:value="@string/map" />
      
 //This legacy name (com.google.android.geo.API_KEY)  allows authentication to the Android Maps API
 ```
 

 
<img src="https://user-images.githubusercontent.com/46722362/158611030-d17d2023-3971-4d5c-9331-f1b152c09775.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158611030-d17d2023-3971-4d5c-9331-f1b152c09775.png"
       width="220" height="450" />
       <img src="https://user-images.githubusercontent.com/46722362/158611060-44edd280-e8ad-45b8-8aa5-6116edbcb019.png"
      data-canonical-src="https://user-images.githubusercontent.com/46722362/158611060-44edd280-e8ad-45b8-8aa5-6116edbcb019.png"
       width="220" height="450" />





## Licences

## Thanks <span style="color: #fb8100;">&hearts;</span>
Thanks to Google Developer Students Club for contributing towards the growth of [@DSCTTU](https://twitter.com/DscTtu?t=nLFp2oGleW6Tpu3XpzbugQ&s=09).
