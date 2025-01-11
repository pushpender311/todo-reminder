# To-Do Reminder App

## Overview
The To-Do Reminder App is an Android application that allows users to create, view, edit, and delete reminders with a variety of features such as local storage, API integration, notifications, and accessibility support through Text-to-Speech (TTS). This project demonstrates the use of Android Jetpack components, AlarmManager for scheduling notifications, and TextToSpeech for accessibility.

---

## Features

### 1. Reminder Management
- Create, view, edit, and delete reminders with details such as title, description, date-time, and recurrence options (e.g., every 15 minutes, hourly, daily).
- Displaying reminders fetched from an API alongside locally created reminders, with differentiation between the two using a textview indicating **API** and **LOCAL**.
- Trigger notifications for reminders at the scheduled time using **AlarmManager**.
- Tap on reminders to hear their title and description using **Text-to-Speech (TTS)**.

### 2. Local Storage
- Locally created reminders are stored in a Room database to ensure offline functionality.

### 3. API Integration
- Fetch reminders from an external API endpoint: `https://jsonplaceholder.typicode.com/todos`.
- API reminders include an additional `reminderTime` field for scheduling notifications using alarm manager.

### 4. Notifications
- Notifications for reminders are scheduled using **AlarmManager**.
- Notifications include the reminder title and description.

### 5. Accessibility
- Integrates **TextToSpeech (TTS)** to announce reminder details when tapped.


### 6. UI/UX
- Built with **ConstraintLayout** for a modern, responsive design.
- Includes a DialogFragment for creating reminders with:
    - Title and description inputs.
    - Date and time pickers.
    - Recurrence options (e.g., every  minutes, hourly, daily).
    - Save button to save reminders to the database and schedule notifications.

---

## Project Structure

### **1. Activities**
- **MainActivity**: Displays a list of reminders, integrates RecyclerView, and handles TTS announcements.
- **ReminderDetailActivity**: Allows viewing, editing, and deleting reminder details.

### **2. Fragments**
- **AddNewReminderDialogFragment**: A dialog fragment to create new reminders with inputs for title, description, date-time, and recurrence.

### **3. ViewModel and Repository**
- **ReminderViewModel**: Manages reminders using LiveData and interacts with the repository.
- **ReminderRepository**: Handles data operations from the Room database and API.

### **4. Database**
- **ReminderDatabase**: A Room database to store reminders locally.
- **ReminderDao**: Data Access Object for querying reminders.

### **5. Notifications**
- **ReminderReceiver**: Handles reminder notifications triggered by AlarmManager.

---

## Key Libraries and Tools
- **Room Database**: For local storage.
- **Retrofit**: For API integration.
- **AlarmManager**: For scheduling notifications.
- **TextToSpeech API**: For accessibility support.
- **LiveData & ViewModel**: For reactive UI updates.
- **ConstraintLayout**: For responsive UI design.

---

## How to Run the Project

### Prerequisites
- Android Studio installed on your system.
- A physical or virtual device running Android 6.0 (API 23) or higher.

### Steps
1. Clone the repository.
2. Open the project in Android Studio.
3. Sync the Gradle files.
4. Build and run the app on an emulator or physical device.

---

## API Details
- Endpoint: `https://jsonplaceholder.typicode.com/todos`
- Sample Response:
```json
[
  {
    "userId": 1,
    "id": 1,
    "title": "delectus aut autem",
    "completed": false,
    "reminderTime": "2025-01-10T10:30:00"
  }
]
```

---


## Future Enhancements
- Fetching the results in pagination
- Implement support for recurring reminders with more customization options.
- Add themes for better visual customization.

---

## Author
Pushpender (Android Developer)