# School-Attendance-ERP
School ERP system for managing attendance, marks, notices, fees, and schedules using Java and MySQL.
# AttendanceERP_Upgraded

![Java](https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-0095FF?logo=apache-netbeans&logoColor=white)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen)

A complete **Attendance and Academic Management System** for students and faculty, built in **Java (NetBeans)** with a **MySQL database backend**. Designed for **efficient attendance tracking, marks management, fees, and schedule management**.

---

## Features

### Student Module
- Registration and login  
- View **personal attendance** (subject-wise & total)  
- View **marks** (subject-wise & total)  
- Access **notices** from faculty  
- View **class schedule**

### Faculty Module
- Mark student **attendance**  
- Enter **student marks**  
- Post **notices**  
- Manage **class schedules**  

### General
- **Role-based login**: Students and Faculty  
- **Searchable tables** for quick data access  
- **Visual reports** for attendance and marks  
- Fully functional **GUI with attractive layouts**  

---

## Project Structure



---

## Installation
1. Install **Java JDK** (version 8 or above)  
2. Install **MySQL** and create a database `Attendance_ERP`  
3. Run the SQL script `Attendance_ERP.sql` to create all tables:  
   - `users`  
   - `attendance`  
   - `marks`  
   - `fees`  
   - `schedule`  
   - `notices`  
4. Open `Attendance_ERP.jar` from the `dist` folder to launch the application  

---

## Usage
1. Launch the application → Login screen appears  
2. Select **Student** or **Faculty**  
3. Navigate the dashboard using buttons for attendance, marks, notices, fees, and schedule  
4. Faculty can perform CRUD operations depending on the module  
5. Visual charts display attendance and marks statistics  

---

## Database Schema
**Users Table**
| Column        | Type         | Notes                     |
|---------------|--------------|---------------------------|
| id            | INT PK       | Auto-increment            |
| name          | VARCHAR      |                           |
| email         | VARCHAR      | Student/Faculty format    |
| password      | VARCHAR      |                           |
| role          | ENUM         | 'student', 'faculty'      |

**Attendance Table**: student_id, subject_id, date, status  
**Marks Table**: student_id, subject_id, marks_obtained, total_marks  
**Fees Table**: student_id, amount, paid_status  
**Schedule Table**: class_name, subject, day, start_time, end_time  
**Notices Table**: notice_id, title, description, posted_by, date_posted  

---

## Screenshots
*(Add screenshots of login, dashboards, attendance, marks, notices, and charts here)*  

---

## Technologies Used
- **Java (NetBeans IDE)**  
- **Swing GUI**  
- **MySQL** (Database)  
- **JDBC** (Database connection)  
- **JFreeChart** or any chart library for attendance/marks visualization  

---

## Future Improvements
- Add **email notifications** for notices and attendance alerts  
- Integrate **online payment gateway** for fees  
- Mobile-friendly web version  
- Role-based access with **admin panel**  

---

## Author
**Heet Lakhani**  
GitHub: [https://github.com/YourUsername](https://github.com/YourUsername)
