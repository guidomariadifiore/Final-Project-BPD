## Project Specification: Public Billposting Service

### 1. System Description and Process Flow
The project requires the realization of a system that allows users to book spaces for a public billposting service[cite: 1]. The process flow must follow these sequential steps:

1. The user specifies via an API the cities (one or more) where they want to affix posters, the poster format (e.g., 60x80, 200x100, etc.), and the maximum price that can be paid for each of the selected cities[cite: 1].
2. After receiving the message from the exposed API, the process retrieves the user details (personal data) and the list of available zones (along with their prices) from two web services[cite: 1].
3. For each chosen city, zones are selected so that their total price is lower than the specified maximum price[cite: 1]. The selection is performed by taking the most expensive zones first until the maximum price is reached[cite: 1].
4. A posting service is invoked, sending the list of selected zones and the user details[cite: 1]. This service will return a request ID[cite: 1].
5. The request ID and the list of the selected zones are returned to the user[cite: 1].
6. The user then sends a message containing the request ID and a final decision: "confirm" or "cancel"[cite: 1].

**If the decision is "confirm":**
* The confirmation is sent to the posting service, which will return the billing information including the total amount due[cite: 1].
* The order information is printed into a file[cite: 1].
* The billing information is returned to the user[cite: 1].

**If the decision is "cancel":**
* The order cancelation is sent to the posting service[cite: 1].
* A cancelation notice is printed in the standard output[cite: 1].
* An empty bill is returned to the user[cite: 1].

---

### 2. Architectural and Technological Requirements
* **Process Engine:** Students are required to model a BPMN process that realizes the described scenario[cite: 1]. This BPMN process must be executed through the Camunda platform embedded into a Spring Boot project[cite: 1].
* **API Integration:** The project must expose two API endpoints (REST or SOAP)[cite: 1]. These API endpoints must interact with the process to start and resume its execution through messages[cite: 1]. 
* **Correlation:** The messages exchanged within the process must be correlated[cite: 1].
* **External Communication:** Communication with external services must be implemented using connectors[cite: 1].
* **Additional Computations:** Extra tasks may be added to perform extra computation, such as the zone selection logic[cite: 1].

---

### 3. APIs to Expose
The project requires the exposure of two services, which can be realized as either REST or SOAP services[cite: 1]. If REST is chosen, the HTTP methods (GET, POST, etc.) must be chosen wisely based on the action performed[cite: 1].

**Endpoint 1: Request availability**[cite: 1]
* **Input data:** Username, List of cities, Maximum price for each of the listed cities, Poster format[cite: 1].
* **Output data:** List of selected zones, Total price, Request ID[cite: 1].

**Endpoint 2: Send decision**[cite: 1]
* **Input data:** Request ID, Decision ("confirm" or "cancel")[cite: 1].
* **Output data:** Billing information[cite: 1].

---

### 4. External Services Details

* **Get User Data:** A REST service endpoint accessible at `[GET] http://localhost:9080/user/<USERNAME>`[cite: 1]. Available mock usernames are `mariorossi`, `sarabianchi`, and `johndoe`[cite: 1].
* **Get Zone List:** A REST service endpoint accessible at `[GET] http://localhost:9090/zones/<FORMAT>`[cite: 1]. This service is mocked and returns the same list for any specified format[cite: 1]. The available cities are L'Aquila, Rome, and Milan[cite: 1].
* **Check Availability:** A SOAP service available at the WSDL location `http://localhost:8888/postingservice/postingservice.wsdl`[cite: 1]. Submitting an `availabilityRequest` returns an `availabilityResponse` containing the `requestId` and an `available` boolean flag[cite: 1].
* **Confirm Request:** Utilizes the same SOAP WSDL[cite: 1]. Submitting a `confirmationRequest` with the request ID returns a `confirmationResponse` containing a `bill` with the account holder, invoice number, and amount due[cite: 1].
* **Cancel Request:** Utilizes the same SOAP WSDL[cite: 1]. Submitting a `cancelRequest` with the request ID yields an empty response if the request is acceptable[cite: 1].

---

### 5. File Writing (Write on file)
The system must handle writing the data of confirmed requests[cite: 1]. There are two accepted implementations:
* **Basic Implementation:** Add a new line into a file named "posting_requests.txt", creating the file if it does not already exist[cite: 1]. The line format must exactly be: `username, request ID, invoice number, amount due;`[cite: 1].
* **Preferred Implementation:** Create a separate file for each individual confirmed request[cite: 1]. This file should contain all the user information, request information, zone lists, invoice number, and amount due[cite: 1].

---

### 6. Advanced Requirements 
*Note: These tasks are mandatory unless the student passed the midterm examination, in which case they are considered optional.*[cite: 1]

* **Error Handling and Robustness:** When realizing the API interface, developers must account for potentially wrong or inadmissible inputs, or any other process-related faults[cite: 1]. The system must avoid returning unmanaged error messages (e.g., 500 internal server error) to the user[cite: 1]. The process must also be protected from getting "lost" due to wrong inputs[cite: 1]. HTTP status codes should be utilized to "prettify" and add semantics to possible error messages[cite: 1]. The final application must be robust to errors and faults[cite: 1].
* **Multiple Selection Strategies:** Students must develop multiple zones selection strategies and allow the user to choose which one to use[cite: 1].
* **Use of Templates:** The use of templates is preferred if they are needed for complex connector inputs or for generating files (if choosing the single-file-per-request implementation)[cite: 1].
* **Extensibility:** Students may extend the system with additional features if they find them useful[cite: 1].

---

### 7. Environment Setup and Services Execution
* The external services required for composition are provided as `.jar` files (`user-service.jar`, `zones-service.jar`, `posting-service.jar`)[cite: 1].
* These must be run from the terminal using the `java -jar` command (e.g., `java -jar user-service.jar`)[cite: 1].
* **Java 8** is strictly required to run both the external services and the Camunda engine if utilizing Javascript tasks[cite: 1].