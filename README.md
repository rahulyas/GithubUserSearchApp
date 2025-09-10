# 📱 Github User Search App

An Android application that allows users to search for GitHub profiles and explore their repositories.  
Built with **Clean Architecture**, **MVVM**, **Kotlin Coroutines**, and **Jetpack components**.

---

## ✨ Features

- 🔍 Search GitHub users by username
- 👤 View detailed user profile information:
  - Avatar
  - Bio
  - Followers / Following count
- 📂 Explore repositories of a user with:
  - Repository name
  - Description
  - Star & Fork count
- ⚠️ Error handling for invalid users and network issues
- 🚀 Modern Android development practices:
  - **UseCases** for business logic
  - **ViewModel + StateFlow**
  - **Dependency Injection (Hilt)**
  - **Jetpack Compose UI** (if used, else mention XML)
  - **Paging 3** for repositories list (if applicable)

---

## 🖼️ Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/c115362f-2d29-4c80-bd03-2fef7b22d7d6" width="250"/>
  <img src="https://github.com/user-attachments/assets/261bd2ea-1237-4484-a925-e560abe35eb6" width="250"/>
  <img src="https://github.com/user-attachments/assets/82b8e74c-936a-49df-83b8-bdc03cc527d5" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/1e7bbd68-d34f-4df1-9d4e-29c77ea1defd" width="250"/>
  <img src="https://github.com/user-attachments/assets/42add94e-c7c2-48db-80f5-be717637517a" width="250"/>
  <img src="https://github.com/user-attachments/assets/13f9a325-d98a-4f16-b49e-925ebb645c61" width="250"/>
  <img src="https://github.com/user-attachments/assets/c76d950d-1242-4e36-97f5-1d8a64c6e582" width="250"/>
</p>

---

## 🏗️ Tech Stack

- **Kotlin**
- **Coroutines + Flow**
- **Clean Architecture** (UseCases, Repository pattern)
- **MVVM**
- **Jetpack Libraries**:
  - ViewModel
  - StateFlow
  - Paging 3
- **Hilt** for Dependency Injection
- **Retrofit + OkHttp** for Networking
- **Glide / Coil** for Image loading

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- Min SDK: 24+
- GitHub Personal Access Token (if API requires authentication)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/rahulyas/GithubUserSearchApp.git
