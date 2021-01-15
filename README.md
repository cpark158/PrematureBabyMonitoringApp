# PrematureBabyMonitoringApp
Link to GitHub repo(s):
https://github.com/cpark158/PrematureBabyMonitoringApp.git
https://github.com/pbtomova/bhmDB2020 

Project Overview
We have designed a working app to monitor premature babies. The app’s user interface flow is described below:

Welcome Page
When the app starts, the patient list is retrieved from the remote server. On the welcome page, users can choose to add a new patient or view the current list of patients. In a future version, the welcome page can be used as a login for clinicians or engineers. This will add a layer of security to the database so that information will not be viewed or edited by unauthorised people. 

View Patient Page
Patient list can be viewed using the drop-down list on the upper right-hand corner of the screen. This list shows only the patient’s hospital ID for confidentiality purposes (requested by client). The user can then select a patient from the list to view more details or choose to add a new patient.

Add Patient Page
A new patient can be added by selecting the option from the drop-down list or by clicking the ‘Add Patient’ button on the screen. The user will be redirected to the ‘Add Patient’ page, which asks the user to input required patient details (Name, Hospital ID, Date of Birth and Gender). Hospital ID must be a number/integer, date of birth must be in the format yyyy-mm-dd and gender must be either ‘male’ or ‘female’. If data is input in an invalid form, an alert will appear, asking the input to check the details. The Hospital ID is also checked for duplicates in the database, as it should be unique for each patient. Once the correct details are entered, the save button can be pressed and a new Patient is added to the local and remote databases.

Patient Page
After adding a patient, the user will be redirected to the new patient’s page. The patient page consists of 4 tabs: Basic Information, Health, Edit Details and Remove patient. 
a.	Basic Information
The basic information tab displays the details of the patient. After adding patient, Name, Hospital ID, Gender and Date of birth are filled according to the data input. Additional details such as weight, parents’ information, etc. are added or edited through the Edit Details tab. 

b.	Health
The patient’s health parameters are displayed here. The filename can be input to download the patient’s data. A text file containing data recorded from the monitoring device is retrieved from the remote database. Graphs of glucose concentration against time and lactate concentration against time can be plotted from this data. (To test, enter the filename “ “). Comments can be added next to these graphs for information about treatment or to explain why the graph has a certain pattern.
 
c.	Edit details
Similar to entering the details when a new patient is added, the user can input weight, time of birth, etc. Into separate textboxes. As the information on this page is optional, the user can save additional details individually. Data on the Basic Information page will be updated when the save button next to corresponding data field is clicked. These updates are not currently sent to the remote database but the feature can be added in the future.  

d.	Remove patient
Patient can be removed by clicking the remove patient tab if there is a mistake in entering the patient hospID. An alert appears confirming the removal of the patient, which can be cancelled in the case it was accidentally clicked.
