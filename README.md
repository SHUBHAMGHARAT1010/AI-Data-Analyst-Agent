# 🤖 AI Data Analyst Agent

An AI-powered data analysis backend designed to process fintech transaction reports, profile datasets, assess data quality, identify trends and anomalies, and eventually generate intelligent business insights using AI/LLM models.

The system is being built with a production-oriented architecture using **Java, Spring Boot, PostgreSQL, Flyway, Apache POI, Spring Data JPA, and scheduled background processing**.

---

## 🚀 Project Overview

The AI Data Analyst Agent allows users to upload transaction datasets, such as Excel (`.xlsx`) reports.

The system processes the uploaded dataset asynchronously through a background processing pipeline.

The current processing flow is:

```text
Excel / XLSX Report
        │
        ▼
   Dataset Upload
        │
        ▼
   Dataset Creation
        │
        ▼
 Processing Job Creation
        │
        ▼
 Scheduled Worker
        │
        ▼
    File Reader
        │
        ▼
 Dataset Profiling
        │
        ▼
  Column Profiling
        │
        ▼
 Data Quality Assessment
        │
        ▼
    Analytics Engine
        │
        ▼
 AI / LLM Analysis
        │
        ▼
 Intelligent Insights
```

---

# 🎯 Problem Statement

Fintech and payment systems generate large transaction reports containing thousands or millions of records.

Manually analyzing these reports to identify:

* Transaction declines
* Success/failure rates
* Store-level issues
* Terminal-level issues
* Processor performance
* Card/tender behavior
* Response-code patterns
* Transaction trends
* Geographic patterns
* Data-quality problems

can be time-consuming and error-prone.

The goal of this project is to automate this analysis and provide meaningful insights from raw transaction datasets.

---

# ✨ Key Features

## Current Features

* XLSX dataset upload
* Dataset creation and management
* Dataset processing jobs
* Scheduled background processing
* Excel file parsing using Apache POI
* Dataset profiling
* Column-level profiling
* Data type detection
* Null/blank value analysis
* Basic data-quality assessment
* Processing job lifecycle management
* PostgreSQL persistence
* Database versioning using Flyway
* Spring Data JPA integration

## Planned Features

* Job retry mechanism
* FAILED / COMPLETED job lifecycle
* Advanced data-quality rules
* Analytics engine
* Transaction success/failure analysis
* Store-level analytics
* Terminal-level analytics
* Processor analysis
* Tender/card analysis
* Response-code analysis
* Trend detection
* Anomaly detection
* Persistent analytical results
* AI/LLM-powered insight generation
* Natural-language analysis requests
* Visualization
* Production-grade retry and error recovery
* Security and access control

---

# 🏗️ Architecture

The application follows a modular Spring Boot architecture.

```text
com.aidataagent.ai_data_analyst
│
├── analysis
│   ├── dto
│   ├── entity
│   ├── model
│   ├── repository
│   └── service
│
├── dataset
│   ├── controller
│   ├── dto
│   ├── entity
│   ├── model
│   ├── repository
│   ├── service
│   │
│   └── processing
│       ├── controller
│       ├── dto
│       ├── entity
│       ├── mapper
│       ├── model
│       ├── profiler
│       ├── reader
│       ├── repository
│       ├── service
│       └── worker
│
└── shared
    ├── exception
    └── util
```

---

# 🔄 Dataset Processing

When a dataset is uploaded, the application creates a dataset record and processing job.

The scheduled worker periodically looks for pending jobs and processes them.

```text
Dataset
   │
   └── Processing Job
           │
           ├── PENDING
           │
           ├── PROCESSING
           │
           ├── COMPLETED
           │
           └── FAILED
```

The processing pipeline is designed to support retries and failure recovery.

---

# 📊 Dataset Profiling

Dataset profiling provides high-level information about the uploaded dataset.

Examples include:

* Number of rows
* Number of columns
* Column names
* Data types
* Null counts
* Blank counts
* Unique values
* Duplicate information
* Basic statistical information

---

# 🔎 Column Profiling

Each column is analyzed independently.

Example:

```text
Column: transaction_amount

Data Type: DECIMAL
Total Values: 100000
Null Values: 120
Unique Values: 18450
Minimum: 1.00
Maximum: 25000.00
Average: 427.53
```

This information will later be used by the analytics and AI layers.

---

# 💳 Fintech Analytics

The project is specifically designed with payment/transaction datasets in mind.

Potential analytical dimensions include:

```text
Store
Terminal
Card Type
Tender Type
Entry Mode
Processor
Card BIN
State
Country
Bank AID
Transaction Type
Sub Transaction Type
Response Code
```

The analytics engine will identify patterns such as:

```text
Store A
 └── Decline Rate: 18.4%

Terminal T102
 └── Decline Rate: 27.2%

Processor X
 └── Timeout Rate increased by 14%

Card BIN 123456
 └── Failure rate significantly higher than baseline
```

---

# 🗄️ Database

The application uses **PostgreSQL** for persistent storage.

Database schema evolution is managed using **Flyway**.

Current migrations:

```text
V1__initial_schema.sql

V2__create_dataset_and_analysis_request_tables.sql

V3__create_dataset_processing_job_table.sql

V4__update_dataset_processing_job_table.sql

V5__create_dataset_profiles.sql

V6__create_detaset_column_profiles.sql
```

---

# 🛠️ Technology Stack

| Technology      | Purpose                         |
| --------------- | ------------------------------- |
| Java            | Backend programming language    |
| Spring Boot     | Application framework           |
| Spring Data JPA | Database access                 |
| Hibernate       | ORM                             |
| PostgreSQL      | Relational database             |
| Flyway          | Database migrations             |
| Apache POI      | Excel/XLSX processing           |
| Maven           | Build and dependency management |
| Lombok          | Boilerplate reduction           |
| REST API        | Client communication            |

---

# 📁 Project Structure

```text
ai-data-analyst-agent/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/aidataagent/ai_data_analyst/
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/
│   │           └── migration/
│   │
│   └── test/
│
├── .gitignore
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

# ⚙️ Getting Started

## Prerequisites

Make sure you have:

* Java 17+ installed
* Maven
* PostgreSQL
* Git

Check Java:

```bash
java -version
```

Check Maven:

```bash
mvn -version
```

---

# 🗄️ Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE ai_data_analyst;
```

Create/configure a database user with the required permissions.

Database credentials should be provided through environment variables rather than committed to Git.

Example:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

---

# ▶️ Running the Application

Clone the repository:

```bash
git clone https://github.com/<your-username>/ai-data-analyst-agent.git
```

Navigate into the project:

```bash
cd ai-data-analyst-agent
```

Run using Maven:

```bash
./mvnw spring-boot:run
```

On Windows:

```cmd
mvnw.cmd spring-boot:run
```

The application will start on the configured Spring Boot port.

---

# 🧪 Running Tests

Run:

```bash
./mvnw test
```

Windows:

```cmd
mvnw.cmd test
```

---

# 🔐 Security

Sensitive configuration should never be committed to Git.

Examples:

```text
Database passwords
API keys
Private SSH keys
LLM API keys
Production credentials
```

Use environment variables or an external secret-management solution for sensitive configuration.

---

# 🗺️ Roadmap

The project is being developed incrementally.

### Phase 1 — Data Ingestion

* [x] XLSX file reading
* [x] Dataset creation
* [x] Processing job creation
* [x] Scheduled worker

### Phase 2 — Dataset Understanding

* [x] Dataset profiling
* [x] Column profiling
* [ ] Data quality assessment

### Phase 3 — Job Management

* [ ] COMPLETED lifecycle
* [ ] FAILED lifecycle
* [ ] Retry mechanism
* [ ] Failure recovery

### Phase 4 — Analytics

* [ ] Transaction analytics
* [ ] Success/failure analysis
* [ ] Processor analysis
* [ ] Store analysis
* [ ] Terminal analysis
* [ ] Tender/card analysis
* [ ] Response-code analysis
* [ ] Trend detection
* [ ] Anomaly detection

### Phase 5 — AI Analyst

* [ ] Analysis request workflow
* [ ] LLM integration
* [ ] AI-generated insights
* [ ] Natural-language questions
* [ ] Automated conclusions

### Phase 6 — Visualization

* [ ] Charts
* [ ] Dashboards
* [ ] Trend visualization
* [ ] Analytical reports

### Phase 7 — Production Readiness

* [ ] Authentication & authorization
* [ ] Observability
* [ ] Structured logging
* [ ] Retry/error recovery
* [ ] Performance optimization
* [ ] Security hardening
* [ ] Containerization
* [ ] CI/CD

---

# 🧠 Future AI Analyst Example

The eventual goal is for a user to upload a transaction report and ask:

```text
Why did transaction declines increase this week?
```

The system will analyze the dataset and produce an explanation such as:

```text
Transaction declines increased by 12.8%.

The largest contributor was Store 102,
where decline rates increased from 4.2% to 17.6%.

The increase was primarily associated with:

1. Response Code 05
2. Processor X
3. Card Entry Mode: Manual
4. Terminals T102 and T103

The increase began on August 12 and peaked on August 15.
```

The objective is to transform raw transaction data into **actionable business intelligence**.

---

# 👨‍💻 Development Status

This project is actively under development.

Current focus:

**Job Lifecycle Management → Data Quality Assessment → Analytics Engine**

---

# 📜 License

This project is currently under development.

License information will be added in a future release.
