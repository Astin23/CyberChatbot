# CyberChatbot
A Java-based AI chatbot with cybersecurity features.

## Features
- GUI using Swing
- Database connectivity with SQLite
- Cybersecurity: Password hashing, message encryption, input validation
- Unit and integration testing with JUnit
- Build with Maven

## Project Structure
CyberChatbot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── yourname/
│   │   │   │   │   ├── chatbot/
│   │   │   │   │   │   ├── model/       (User, Message classes)
│   │   │   │   │   │   ├── service/      (Chatbot logic, Security)
│   │   │   │   │   │   ├── db/          (Database handling)
│   │   │   │   │   │   ├── ui/          (Swing GUI)
│   │   │   │   │   │   └── Main.java    (Entry point)
│   │   └── resources/
│   └── test/
│       └── java/ (Tests)
├── pom.xml
└── .gitignore

## Setup Instructions
1. **Clone the Repository**:
   git clone https://github.com/Astin23/CyberChatbot.git cd CyberChatbot
2. **Install Dependencies**:
- Ensure Maven is installed.
- Run:
   mvn clean install

3. **Run the Application**:
- Run `Main.java`:
  mvn exec:java -Dexec.mainClass="com.baayu.chatbot.Main"

- Or run directly in IntelliJ by right-clicking `Main.java` → Run.

## Usage
1. Start the application.
2. Enter a username and password to register.
3. Log in with the same credentials.
4. Send messages:
- Type `encrypt` to toggle encryption.
- Type `history` to see your messages.
- Type `logout` to log out.
- Type `exit` to quit.
5. Messages are stored in `chatbot.db` (SQLite database).

## Dependencies
- SQLite JDBC Driver (`org.xerial:sqlite-jdbc:3.49.1.0`)
- jBCrypt (`org.mindrot:jbcrypt:0.4`)

## UML Diagram
See `uml_diagram.png` for the class diagram of the project.

## License
MIT License
