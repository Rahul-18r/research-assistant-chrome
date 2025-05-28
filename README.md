# 🧠 AI-Powered Research Assistant

A full-stack application that enhances productivity by summarizing, explaining, rephrasing, and analyzing content using Google Gemini's generative AI APIs. Comes with an integrated Chrome Extension for quick, in-browser access.

---

## 🚀 Features

* ✅ Text summarization, explanation, and rephrasing
* ✅ Multiple intelligent operations (define, extract, highlight, citation, etc.)
* ✅ Clean Spring Boot backend with WebClient
* ✅ Gemini API integration for AI responses
* ✅ UTF-8 response formatting
* ✅ Chrome Extension with side panel UI

---

## 🛠️ Technologies Used

* **Java 17**
* **Spring Boot 3**
* **Maven**
* **WebClient** for HTTP requests
* **Chrome Extension (HTML/CSS/JavaScript)**
* **Google Gemini API (generative AI)**

---

## 📁 Project Structure

```
research-assistant/
├── research-assistant-chrome-extension/
│   ├── background.js
│   ├── image.png
│   ├── manifest.json
│   ├── sidepanel.css
│   ├── sidepanel.html
│   └── sidepanel.js
│
└── src/main/java/com/research/assistant/
    ├── controller/
    │   └── ResearchController.java
    ├── service/
    │   └── ResearchService.java
    ├── dto/
    │   └── ResearchRequest.java
    ├── model/
    │   └── GeminiResponse.java
    └── ResearchAssistantApplication.java
```

---

## 🌐 API Details

### 🔸 Endpoint

`POST /api/research/process`

### 🔸 Request Body (JSON)

```json
{
  "content": "version of present gemini model",
  "operation": "summarize"
}
```

### 🔸 Supported Operations

* `summarize`
* `suggest`
* `explain`
* `define`
* `highlight`
* `rephrase`
* `extract`
* `related`
* `analyze`
* `citation`

### 🔸 Response

* Plain text (`text/plain; charset=UTF-8`)
* UTF-8 formatted AI-generated response from Gemini API

---

## 🧠 Prompt Construction Logic

Each operation maps to a tailored prompt. Example for `summarize`:

```
Provide a clear and concise summary of the following text in a few sentences:

<user content>
```

Other operations follow similar structured prompts to elicit optimal AI responses.

---

## 🔌 Chrome Extension

### 🔹 manifest.json Highlights

```json
{
  "name": "Research assistant",
  "permissions": ["activeTab", "storage", "sidePanel", "scripting"],
  "side_panel": { "default_path": "sidepanel.html" },
  "host_permissions": ["https://localhost:8080/*"]
}
```

### 🔹 Sidepanel Interaction

* Frontend UI for input
* Sends request to Spring Boot backend
* Displays response from Gemini

> *Perfect for instant AI help while browsing content on the web!*

---

## ⚙️ Gemini API Setup

1. Go to [Google AI Studio](https://makersuite.google.com/app) and sign in.
2. Click on your profile icon → **API keys**.
3. Click **Create API key**.
4. Copy and paste it into your `application.properties`:

```properties
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=
gemini.api.key=YOUR_API_KEY_HERE
```

---

## 🧪 Testing

Use tools like Postman or the Chrome extension to test the endpoint:

### 🔸 Example cURL

```bash
curl -X POST http://localhost:8080/api/research/process \
     -H "Content-Type: application/json" \
     -d '{ "content": "Explain AI", "operation": "explain" }'
```

---

## 🏁 Run the Application

```bash
./mvnw spring-boot:run
```

Or use your IDE (IntelliJ/Eclipse).

Make sure Chrome Extension has permission to hit `localhost:8080`.

---

## 🛡️ License

This project is open-source and free to use under MIT License.
