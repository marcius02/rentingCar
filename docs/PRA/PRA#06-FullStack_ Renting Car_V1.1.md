# PRA#06-FullStack: Renting Car

## CIFO La Violeta - FullStack IFCD0210-25 MF03

> This guide outlines how to create a full-stack Renting Car Application using Spring Boot for the backend and React (via Hilla TSX) for the frontend.

Refrence labs:

- [Renting Car project](https://github.com/AlbertProfe/rentingCar): Sprint Boot Vaadin Hilla
- [mathsWeb](https://github.com/AlbertProfe/mathsWeb): React theming
- [easyLearning](https://github.com/AlbertProfe/easyLearning): Sprint Boot Vaadin Hilla Java Auth
- [authReactCognito](https://github.com/AlbertProfe/authReactCognito): React TS Cognito AWS auth
- [authReactCognito](https://github.com/AlbertProfe/authReactCognito): React JS Cognito AWS auth

## Project Overview

- Create a `Spring Boot` and `React` (Hilla) project from scratch or <mark>gh repo clone AlbertProfe/rentingCar</mark> at [commit 9e88199](https://github.com/AlbertProfe/rentingCar/tree/9e8819912e23b90cdc3c66d26dc14fb5bc3dce99/rentingCar)
- Do <mark>NOT</mark> Implement a `RESTful API`, it is not necessary.
- Use `DynamoDB` with two single tables for data storage:
  - Users/Bookings
  - Delegations/Cars
- Feature CRUD operations with `React` frontend
- Implement state management with `hooks` and `useContext`
- Use `Material UI` for styling with sidebar and navigation or similar
- Based on the [Renting Car project](https://github.com/AlbertProfe/rentingCar)

## Project Summary

Users:

> The Renting Car Application is a full-stack solution for managing car rentals. It allows <mark>users</mark> to browse available cars, make bookings with a calendar-based interface, and manage their profiles. 

Admins:

> <mark>Admins</mark> can access a dashboard to monitor bookings and manage the fleet. The application uses Spring Boot with Hilla to provide a seamless integration between the Java backend and React frontend. Data is stored in DynamoDB using two single-table designs for efficient scalability. 

## Technology Stack

### Backend

- [Spring Boot](https://spring.io/projects/spring-boot) - Java-based framework
- [Hilla](https://hilla.dev/) - Framework for integrating Spring Boot with React
- [Spring Data DynamoDB](https://github.com/derjust/spring-data-dynamodb) - Data persistence for DynamoDB
- [Lombok](https://projectlombok.org/) - Reduce boilerplate code (optional)
- [DynamoDB](https://aws.amazon.com/dynamodb/) - NoSQL database with two single-table designs
- **Bash/Python** and [AWS CLI](https://aws.amazon.com/cli/) to populate the DynamoDB.
- JUnit for testing
- Cognito AWS for auth.

### Frontend

- [React](https://reactjs.org/) - JavaScript library for building user interfaces (via Hilla)
- Hilla React, typing variables with **Typescript**
- [Axios](https://axios-http.com/) - HTTP client for API requests (optional to **populate**)
- [Material UI](https://mui.com/) - React component library
- [React Router DOM](https://reactrouter.com/) - Routing for React application
- Jest for testing

## Data Model

- [rentingCar_nosql - Hojas de cálculo de Google](https://docs.google.com/spreadsheets/d/1jCKONiQ8_KUPSXzlYQ5GwEaLhJo2aV9jCOtgarWg3Tc/edit?usp=sharing)

**Table 1: Inventory and Availability**  

- **Purpose**: Stores delegations, cars, and car availability.  
- **Partition Key**: `delegationId` (e.g., `DELEG#001`) or `carId` (e.g., `car#2023#006`).  
- **Sort Key**: `operation` (e.g., `profile`, `calendar`).  
- **Entities/Beans**:  
  - **Delegation**: `DELEG#<ID>` + `profile` (e.g., address, city, availableCarQty).  
  - **Car**: `car#<year>#<ID>` (e.g., color, make, model, price).  
  - **Calendar**: `car#<year>#<ID>` + `<calendarType>#calendar` (e.g., date availability flags).  
- **Access Patterns**: Query delegations by ID, cars by ID/model, availability by date.  
- **Notes**: Single-table design, scalable with potential GSIs on `city` or `model`.

**Table 2: Users and Bookings**  

- **Purpose**: Manages user profiles and bookings.  
- **Partition Key**: `userId` (e.g., `USER#001`).  
- **Sort Key**: `operation` (e.g., `profile`, `booking#<year>#<ID>`).  
- **Entities/Beans**:  
  - **User**: `USER#<ID>` + `profile` (e.g., email, fullName).  
  - **Booking**: `USER#<ID>` + `booking#<year>#<ID>` (e.g., car, delegation, dates, status).  
- **Access Patterns**: Query user profiles by ID, bookings by user/date/status.  
- **Notes**: Embeds car/delegation data in bookings for fast reads, scalable with potential GSIs on `email` or `statusBooking`.

**Core Design**:  

- Both tables use single-table DynamoDB with hierarchical keys for flexibility.  
- Table 1 organizes inventory; Table 2 handles user transactions.  
- Scalable, but could improve with GSIs, normalized calendars, and lighter booking data.
- TTL: Automatically delete expired items from a table, define TTL for a item/field <mark>epoch seconds</mark> for future date you want to expire.

## Tasks

### Mandatory Tasks for Students

1. **Design and Implement Rent a Car Feature**: Create a feature allowing users to rent a car using a<mark> calendar interface</mark> to select dates (for a particular(s) delegation) and view available cars. Ensure integration with the backend to check availability and confirm bookings.
   1. From version 1.0, commit <mark>9e88199</mark>
      1. v1.1.1: `listCars`, original commit
      2. v1.1.1: data model for avalaible cars
      3. v1.1.2: admin create availabilty car calendar by car:
         1.  `createAvailabiltyCalenderByCar`
      4. v1.1.3: user select dates/delegation and query available cars by dates/delegation
      5. v1.2: make a booking for a car/dates/delegation
      6. v1.3: update avaliable cars
2. **Create five different data models for representing available cars**. For each model, briefly explain its advantages and disadvantages. Then, choose the best option for your needs and justify your choice in a short paragraph. Keep your response around 50 words.
3. **List of Bookings and Admin Dashboard**: Implement a booking list view for users and a dashboard for admins to monitor bookings, view statistics, and manage the car fleet.
4. **User Configuration Domain**: Develop a user profile management system, allowing users to update personal information, view booking history, and manage preferences.

### Voluntary Tasks (Choose One)

1. **CSS Theme Injection**: Implement dynamic theme injection for the frontend using techniques outlined in [mathsWeb](https://github.com/AlbertProfe/mathsWeb).
2. **Java-Based Authentication**: Add JWT-based authentication using Spring Security, as demonstrated in [easyLearning](https://github.com/AlbertProfe/easyLearning).
3. **AWS Cognito Authentication**: Integrate AWS Cognito for user authentication and authorization [authReactCognito](https://github.com/AlbertProfe/authReactCognito): React TS Cognito AWS auth.
4. **Map with Tracking or Position of Cars and Delegations**: Utilize the Leaflet library to display interactive maps showing car locations and rental agency branches, with optional real-time tracking.
5. **Deploy with Docker, AWS ECS, and GitHub Actions**. To deploy with Docker, AWS ECS, and GitHub Actions:
   1. first containerize your application by creating a Dockerfile. Build and tag your Docker image, then push it to Amazon ECR (Elastic Container Registry). 
   2. Next, create an ECS cluster and define a task specifying how your container should run (CPU, memory, networking). Deploy your application to ECS, using either EC2 or Fargate. 
   3. Automate this workflow with GitHub Actions: set up a workflow to build your Docker image, push to ECR, and deploy to ECS on every push to your main branch. 
   4. Use official AWS GitHub Actions to streamline authentication and deploymen

### Extra tasks (just for champions)

**1. Testing Implementation**  

Backend: Use JUnit with Spring Boot's `@SpringBootTest` for integration testing, validating repository interactions and controller responses. Frontend: Configure Jest with React Testing Library for component testing, using `render` and `screen` utilities to verify UI behavior

**2. AWS S3 File Storage**  

Implement signed URLs for secure file uploads/downloads directly from React. Apply S3 best practices:

- Use **Intelligent-Tiering** for cost optimization

- Enable **Lifecycle Policies** for automatic archival

- Set bucket policies with least-privilege access

**3. Stripe Payment Integration**  

Utilize React Stripe.js with Elements provider:

```jsx
<Elements stripe={stripePromise} options={{appearance}}> 
  <PaymentElement />
</Elements>
```

Handle payment confirmation server-side using Stripe webhooks and `clientSecret` validation.

**4. Architecture Documentation**  

Create <mark>Quarto</mark> documentation with:

- Mermaid.js diagrams for system architecture

- Interactive component demos using React Live

- Automated API reference generation via Javadoc/TypeDoc

### Super-extra tasks (just for super-champions)

**Real-time Updates with DynamoDB**  

Configure TTL attribute with streams:

```json
{ 
  "Filters": [{
    "Pattern": { 
      "userIdentity": {
        "type": ["Service"],
        "principalId": ["dynamodb.amazonaws.com"] 
      }
    }
  }]
}
```

Use Lambda to process expiration events and push updates via WebSocket API.

### Backend Tasks

1. **Set Up the Spring Boot Project**: Create a new Spring Boot project with Hilla using Spring Initializr. Include dependencies like Spring Web, Spring Data DynamoDB, Lombok, and Hilla.
2. **Configure DynamoDB**: Set up DynamoDB with two single-table designs (e.g., for `Bookings` and `Users/Cars`). Configure connection details in `application.properties`.
3. **Create Bean Item Classes**: Define `Car`, `Booking`, and `User` bean classes with annotations for DynamoDB and Lombok (optional).
4. **Implement Repository Interfaces**: Create repository interfaces for each entity (`CarRepository`, `BookingRepository`, `UserRepository`) extending Spring Data DynamoDB repositories.
5. **Set Up Service & Endpoints Layer**: Implement service classes (`CarService`, `BookingService`, `UserService`) to handle business logic and interact with repositories.

### Frontend Tasks

1. **Set Up the Hilla/React Project**: Initialize a Hilla project to generate a React frontend integrated with Spring Boot. Install dependencies like Material UI, Axios, and React Router DOM.
2. **Create Project Structure**: Organize the project into folders like `components`, `pages`, `context`, `hooks`, and `services` for maintainability.
3. **Set Up API Context**: Implement an `ApiContext` to manage API requests, loading states, and errors across the application.
4. **Create Custom Hooks**: Develop custom hooks like `useCars`, `useBookings`, and `useUsers` to fetch and manage data from the backend.
5. **Implement Basic Layout**: Create layout components like `Header`, `Sidebar`, and `Footer` using Material UI. Set up routing using React Router DOM.

## Backend Skeleton Implementation

<mark>Just for consultation; these are NOT usable classes, components, code</mark>

### Java Beans Classes

#### Car Bean

```java
package com.renting.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@DynamoDBTable(tableName = "Cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String brand;

    @DynamoDBAttribute
    private String model;

    @DynamoDBAttribute
    private String licensePlate;

    @DynamoDBAttribute
    private String status; // e.g., AVAILABLE, RENTED

    @DynamoDBAttribute
    private List<String> bookingIds;
}
```

#### Booking Bean

```java
package com.renting.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@DynamoDBTable(tableName = "Bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String userId;

    @DynamoDBAttribute
    private String carId;

    @DynamoDBAttribute
    private LocalDateTime startDate;

    @DynamoDBAttribute
    private LocalDateTime endDate;

    @DynamoDBAttribute
    private String status; // e.g., PENDING, CONFIRMED, CANCELLED
}
```

#### User Bean

```java
package com.renting.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String email;

    @DynamoDBAttribute
    private String fullName;

    @DynamoDBAttribute
    private String role; // e.g., USER, ADMIN
}
```

### Repository Interfaces

```java
package com.renting.repository;

import com.renting.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends CrudRepository<Car, String> {

    List<Car> findByStatus(String status);

    Car findByLicensePlate(String licensePlate);
}
```

```java
package com.renting.repository;

import com.renting.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, String> {

    List<Booking> findByUserId(String userId);

    List<Booking> findByCarId(String carId);

    List<Booking> findByStartDateBetween(LocalDateTime start, LocalDateTime end);
}
```

### Service Implementation

```java
package com.renting.service;

import com.renting.model.Car;
import com.renting.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> findAll() {
        return (List<Car>) carRepository.findAll();
    }

    public Optional<Car> findById(String id) {
        return carRepository.findById(id);
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public void deleteById(String id) {
        carRepository.deleteById(id);
    }

    public List<Car> findByStatus(String status) {
        return carRepository.findByStatus(status);
    }

    public Car findByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }
}
```

### Controllers

```java
package com.renting.controller;

import com.renting.model.Car;
import com.renting.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable String id) {
        return carService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.save(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable String id, @RequestBody Car car) {
        return carService.findById(id)
                .map(existingCar -> {
                    car.setId(id);
                    return ResponseEntity.ok(carService.save(car));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable String id) {
        return carService.findById(id)
                .map(car -> {
                    carService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
```

## Frontend Skeleton Implementation

### Project Structure

```
frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── cars/
│   │   │   ├── CarList.js
│   │   │   ├── CarForm.js
│   │   │   └── CarDetails.js
│   │   ├── bookings/
│   │   │   ├── BookingList.js
│   │   │   ├── BookingForm.js
│   │   │   └── BookingDetails.js
│   │   ├── users/
│   │   │   ├── UserProfile.js
│   │   │   └── UserForm.js
│   │   ├── layout/
│   │   │   ├── Sidebar.js
│   │   │   ├── Header.js
│   │   │   └── Footer.js
│   ├── context/
│   │   └── ApiContext.js
│   ├── hooks/
│   │   ├── useApi.js
│   │   ├── useCars.js
│   │   ├── useBookings.js
│   │   └── useUsers.js
│   ├── pages/
│   │   ├── Home.js
│   │   ├── Cars.js
│   │   ├── Bookings.js
│   │   ├── Dashboard.js
│   │   └── Profile.js
│   ├── services/
│   │   └── api.js
│   ├── App.js
│   ├── index.js
│   └── routes.js
└── package.json
```

### API Context Setup

```jsx
// src/context/ApiContext.js
import React, { createContext, useState } from 'react';
import axios from 'axios';

export const ApiContext = createContext();

const API_BASE_URL = 'http://localhost:8080/api';

export const ApiProvider = ({ children }) => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  const get = async (endpoint, params = {}) => {
    setLoading(true);
    try {
      const response = await apiClient.get(endpoint, { params });
      setLoading(false);
      return response.data;
    } catch (err) {
      setError(err.response?.data || 'An error occurred');
      setLoading(false);
      throw err;
    }
  };

  const post = async (endpoint, data = {}) => {
    setLoading(true);
    try {
      const response = await apiClient.post(endpoint, data);
      setLoading(false);
      return response.data;
    } catch (err) {
      setError(err.response?.data || 'An error occurred');
      setLoading(false);
      throw err;
    }
  };

  const put = async (endpoint, data = {}) => {
    setLoading(true);
    try {
      const response = await apiClient.put(endpoint, data);
      setLoading(false);
      return response.data;
    } catch (err) {
      setError(err.response?.data || 'An error occurred');
      setLoading(false);
      throw err;
    }
  };

  const remove = async (endpoint) => {
    setLoading(true);
    try {
      const response = await apiClient.delete(endpoint);
      setLoading(false);
      return response.data;
    } catch (err) {
      setError(err.response?.data || 'An error occurred');
      setLoading(false);
      throw err;
    }
  };

  return (
    <ApiContext.Provider
      value={{
        loading,
        error,
        get,
        post,
        put,
        remove,
        setError
      }}
    >
      {children}
    </ApiContext.Provider>
  );
};
```

### Custom Hooks

#### useApi Hook

```jsx
// src/hooks/useApi.js
import { useContext } from 'react';
import { ApiContext } from '../context/ApiContext';

export const useApi = () => {
  const context = useContext(ApiContext);

  if (!context) {
    throw new Error('useApi must be used within an ApiProvider');
  }

  return context;
};
```

#### useCars Hook

```jsx
// src/hooks/useCars.js
import { useState, useEffect, useCallback } from 'react';
import { useApi } from './useApi';

export const useCars = () => {
  const { get, post, put, remove, loading } = useApi();
  const [cars, setCars] = useState([]);

  const fetchCars = useCallback(async () => {
    try {
      const response = await get('/cars');
      setCars(response);
      return response;
    } catch (error) {
      console.error('Error fetching cars:', error);
      return null;
    }
  }, [get]);

  const getCarById = useCallback(async (id) => {
    try {
      return await get(`/cars/${id}`);
    } catch (error) {
      console.error(`Error fetching car with id ${id}:`, error);
      return null;
    }
  }, [get]);

  const createCar = useCallback(async (carData) => {
    try {
      const newCar = await post('/cars', carData);
      setCars(prevCars => [...prevCars, newCar]);
      return newCar;
    } catch (error) {
      console.error('Error creating car:', error);
      return null;
    }
  }, [post]);

  const updateCar = useCallback(async (id, carData) => {
    try {
      const updatedCar = await put(`/cars/${id}`, carData);
      setCars(prevCars => 
        prevCars.map(car => 
          car.id === id ? updatedCar : car
        )
      );
      return updatedCar;
    } catch (error) {
      console.error(`Error updating car with id ${id}:`, error);
      return null;
    }
  }, [put]);

  const deleteCar = useCallback(async (id) => {
    try {
      await remove(`/cars/${id}`);
      setCars(prevCars => 
        prevCars.filter(car => car.id !== id)
      );
      return true;
    } catch (error) {
      console.error(`Error deleting car with id ${id}:`, error);
      return false;
    }
  }, [remove]);

  useEffect(() => {
    fetchCars();
  }, [fetchCars]);

  return {
    cars,
    loading,
    fetchCars,
    getCarById,
    createCar,
    updateCar,
    deleteCar
  };
};
```

### Components Implementation

#### App Component with Routing

```jsx
// src/App.js
import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { CssBaseline, Box } from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { ApiProvider } from './context/ApiContext';
import Header from './components/layout/Header';
import Sidebar from './components/layout/Sidebar';
import Footer from './components/layout/Footer';
import Home from './pages/Home';
import Cars from './pages/Cars';
import CarDetails from './components/cars/CarDetails';
import Bookings from './pages/Bookings';
import BookingDetails from './components/bookings/BookingDetails';
import Dashboard from './pages/Dashboard';
import Profile from './pages/Profile';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <ApiProvider>
      <ThemeProvider theme={theme}>
        <Router>
          <CssBaseline />
          <Box sx={{ display: 'flex' }}>
            <Header />
            <Sidebar />
            <Box
              component="main"
              sx={{
                flexGrow: 1,
                p: 3,
                width: { sm: `calc(100% - 240px)` },
                ml: { sm: '240px' },
                mt: '64px',
              }}
            >
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/cars" element={<Cars />} />
                <Route path="/cars/:id" element={<CarDetails />} />
                <Route path="/bookings" element={<Bookings />} />
                <Route path="/bookings/:id" element={<BookingDetails />} />
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/profile" element={<Profile />} />
              </Routes>
              <Footer />
            </Box>
          </Box>
        </Router>
      </ThemeProvider>
    </ApiProvider>
  );
}

export default App;
```

#### Layout Components

```jsx
// src/components/layout/Sidebar.js
import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  Drawer,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
  Toolbar,
} from '@mui/material';
import {
  Home as HomeIcon,
  DirectionsCar as CarIcon,
  Event as BookingIcon,
  Dashboard as DashboardIcon,
  Person as ProfileIcon,
} from '@mui/icons-material';

const drawerWidth = 240;

const Sidebar = () => {
  const menuItems = [
    { text: 'Home', icon: <HomeIcon />, path: '/' },
    { text: 'Cars', icon: <CarIcon />, path: '/cars' },
    { text: 'Bookings', icon: <BookingIcon />, path: '/bookings' },
    { text: 'Dashboard', icon: <DashboardIcon />, path: '/dashboard' },
    { text: 'Profile', icon: <ProfileIcon />, path: '/profile' },
  ];

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        ['& .MuiDrawer-paper']: {
          width: drawerWidth,
          boxSizing: 'border-box',
        },
      }}
    >
      <Toolbar />
      <Divider />
      <List>
        {menuItems.map((item) => (
          <ListItem button key={item.text} component={RouterLink} to={item.path}>
            <ListItemIcon>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItem>
        ))}
      </List>
    </Drawer>
  );
};

export default Sidebar;
```

#### Car Components

```jsx
// src/components/cars/CarList.js
import React, { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  IconButton,
  TextField,
  Box,
} from '@mui/material';
import {
  Edit as EditIcon,
  Delete as DeleteIcon,
  Search as SearchIcon,
} from '@mui/icons-material';
import { useCars } from '../../hooks/useCars';

const CarList = () => {
  const {
    cars,
    loading,
    fetchCars,
    deleteCar,
  } = useCars();

  const [searchTerm, setSearchTerm] = useState('');
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(null);

  const handleSearch = async () => {
    if (searchTerm.trim()) {
      // Implement search logic if needed
      fetchCars();
    } else {
      fetchCars();
    }
  };

  const handleDelete = async (id) => {
    const success = await deleteCar(id);
    if (success) {
      setShowDeleteConfirm(null);
    }
  };

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      <Box sx={{ p: 2, display: 'flex', justifyContent: 'space-between' }}>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <TextField
            variant="outlined"
            size="small"
            placeholder="Search cars..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            sx={{ mr: 1 }}
          />
          <IconButton onClick={handleSearch}>
            <SearchIcon />
          </IconButton>
        </Box>
        <Button
          variant="contained"
          color="primary"
          component={RouterLink}
          to="/cars/new"
        >
          Add Car
        </Button>
      </Box>

      <TableContainer sx={{ maxHeight: 440 }}>
        <Table stickyHeader>
          <TableHead>
            <TableRow>
              <TableCell>Brand</TableCell>
              <TableCell>Model</TableCell>
              <TableCell>License Plate</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  Loading...
                </TableCell>
              </TableRow>
            ) : cars.length === 0 ? (
              <TableRow>
                <TableCell colSpan={5} align="center">
                  No cars found
                </TableCell>
              </TableRow>
            ) : (
              cars.map((car) => (
                <TableRow key={car.id}>
                  <TableCell>{car.brand}</TableCell>
                  <TableCell>{car.model}</TableCell>
                  <TableCell>{car.licensePlate}</TableCell>
                  <TableCell>{car.status}</TableCell>
                  <TableCell align="right">
                    <IconButton 
                      component={RouterLink}
                      to={`/cars/${car.id}`}
                      size="small"
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => setShowDeleteConfirm(car.id)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  );
};

export default CarList;
```

```jsx
// src/components/cars/CarForm.js
import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import {
  Paper,
  TextField,
  Button,
  Grid,
  Typography,
  Box,
  CircularProgress,
} from '@mui/material';
import { useCars } from '../../hooks/useCars';

const CarForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { getCarById, createCar, updateCar, loading } = useCars();

  const [formData, setFormData] = useState({
    brand: '',
    model: '',
    licensePlate: '',
    status: 'AVAILABLE',
  });

  const [errors, setErrors] = useState({});
  const isEditMode = Boolean(id);

  useEffect(() => {
    const fetchCar = async () => {
      if (isEditMode) {
        const car = await getCarById(id);
        if (car) {
          setFormData({
            brand: car.brand,
            model: car.model,
            licensePlate: car.licensePlate,
            status: car.status,
          });
        }
      }
    };

    fetchCar();
  }, [getCarById, id, isEditMode]);

  const validateForm = () => {
    const newErrors = {};

    if (!formData.brand.trim()) {
      newErrors.brand = 'Brand is required';
    }

    if (!formData.model.trim()) {
      newErrors.model = 'Model is required';
    }

    if (!formData.licensePlate.trim()) {
      newErrors.licensePlate = 'License Plate is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    let result;

    if (isEditMode) {
      result = await updateCar(id, formData);
    } else {
      result = await createCar(formData);
    }

    if (result) {
      navigate('/cars');
    }
  };

  return (
    <Paper sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        {isEditMode ? 'Edit Car' : 'Add New Car'}
      </Typography>

      <form onSubmit={handleSubmit}>
        <Grid container spacing={3}>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Brand"
              name="brand"
              value={formData.brand}
              onChange={handleChange}
              error={Boolean(errors.brand)}
              helperText={errors.brand}
            />
          </Grid>

          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Model"
              name="model"
              value={formData.model}
              onChange={handleChange}
              error={Boolean(errors.model)}
              helperText={errors.model}
            />
          </Grid>

          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="License Plate"
              name="licensePlate"
              value={formData.licensePlate}
              onChange={handleChange}
              error={Boolean(errors.licensePlate)}
              helperText={errors.licensePlate}
            />
          </Grid>

          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Status"
              name="status"
              value={formData.status}
              onChange={handleChange}
              select
              SelectProps={{ native: true }}
            >
              <option value="AVAILABLE">Available</option>
              <option value="RENTED">Rented</option>
              <option value="MAINTENANCE">Maintenance</option>
            </TextField>
          </Grid>

          <Grid item xs={12}>
            <Box sx={{ display: 'flex', justifyContent: 'flex-end', gap: 2 }}>
              <Button 
                variant="outlined" 
                onClick={() => navigate('/cars')}
              >
                Cancel
              </Button>
              <Button 
                type="submit" 
                variant="contained" 
                color="primary"
                disabled={loading}
              >
                {loading ? <CircularProgress size={24} /> : isEditMode ? 'Update' : 'Create'}
              </Button>
            </Box>
          </Grid>
        </Grid>
      </form>
    </Paper>
  );
};

export default CarForm;
```

### Pages Implementation

```jsx
// src/pages/Home.js
import React from 'react';
import { Typography, Paper, Grid, Card, CardContent, CardActions, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';
import { DirectionsCar, Event, Dashboard, Person } from '@mui/icons-material';

const Home = () => {
  return (
    <>
      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h4" gutterBottom>
          Car Rental System
        </Typography>
        <Typography variant="body1">
          Welcome to the Car Rental System. This application allows you to manage car rentals, bookings, and user profiles through an intuitive interface.
        </Typography>
      </Paper>

      <Grid container spacing={3}>
        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <DirectionsCar fontSize="large" color="primary" />
              <Typography variant="h5" component="div">
                Cars
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Browse and manage the car fleet available for rental.
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small" component={RouterLink} to="/cars">
                View Cars
              </Button>
            </CardActions>
          </Card>
        </Grid>

        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Event fontSize="large" color="primary" />
              <Typography variant="h5" component="div">
                Bookings
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Manage car rental bookings and view booking history.
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small" component={RouterLink} to="/bookings">
                View Bookings
              </Button>
            </CardActions>
          </Card>
        </Grid>

        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Dashboard fontSize="large" color="primary" />
              <Typography variant="h5" component="div">
                Dashboard
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Admin dashboard for monitoring bookings and fleet status.
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small" component={RouterLink} to="/dashboard">
                View Dashboard
              </Button>
            </CardActions>
          </Card>
        </Grid>

        <Grid item xs={12} md={3}>
          <Card>
            <CardContent>
              <Person fontSize="large" color="primary" />
              <Typography variant="h5" component="div">
                Profile
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Manage your personal information and preferences.
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small" component={RouterLink} to="/profile">
                View Profile
              </Button>
            </CardActions>
          </Card>
        </Grid>
      </Grid>
    </>
  );
};

export default Home;
```

```jsx
// src/pages/Cars.js
import React from 'react';
import { Typography, Box } from '@mui/material';
import CarList from '../components/cars/CarList';

const Cars = () => {
  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Cars
      </Typography>
      <CarList />
    </Box>
  );
};

export default Cars;
```

## Project Configuration

### Backend Configuration

#### application.properties for DynamoDB

```properties
# DynamoDB Configuration
cloud.aws.region.static=${AWS_REGION}
cloud.aws.credentials.access-key=${AWS_ACCESS_KEY}
cloud.aws.credentials.secret-key=${AWS_SECRET_KEY}
spring.data.dynamodb.entity2ddl.auto=create-only

# JPA Configuration (if needed for other integrations)
spring.jpa.hibernate.ddl-auto=none

# Server Configuration
server.port=8080
```

### Frontend Configuration

#### package.json

```json
{
  "name": "renting-car-frontend",
  "version": "0.1.0",
  "private": true,
  "dependencies": {
    "@emotion/react": "^11.11.0",
    "@emotion/styled": "^11.11.0",
    "@mui/icons-material": "^5.11.16",
    "@mui/material": "^5.13.0",
    "@testing-library/jest-dom": "^5.16.5",
    "@testing-library/react": "^13.4.0",
    "@testing-library/user-event": "^13.5.0",
    "axios": "^1.4.0",
    "react": "^18.2.0",
    "react-dom": "^18.2.0",
    "react-router-dom": "^6.11.1",
    "react-scripts": "5.0.1",
    "web-vitals": "^2.1.4"
  },
  "scripts": {
    "start": "react-scripts start",
    "build": "react-scripts build",
    "test": "react-scripts test",
    "eject": "react-scripts eject"
  },
  "eslintConfig": {
    "extends": [
      "react-app",
      "react-app/jest"
    ]
  },
  "browserslist": {
    "production": [
      ">0.2%",
      "not dead",
      "not op_mini all"
    ],
    "development": [
      "last 1 chrome version",
      "last 1 firefox version",
      "last 1 safari version"
    ]
  }
}
```

## Getting Started

### Setting Up the Backend

1. Create a new Spring Boot project with Hilla:
   
   - Use Spring Initializr (https://start.spring.io/)
   - Add dependencies: Spring Web, Spring Data DynamoDB, Lombok, Hilla

2. Configure DynamoDB connection in `application.properties`

3. Create entity classes, repositories, services, and controllers

4. Run the Spring Boot application:
   
   ```bash
   mvn spring-boot:run
   ```

### Setting Up the Frontend

1. Create a new Hilla project:
   
   ```bash
   npx @hilla/cli init renting-car-frontend
   ```

2. Install required dependencies:
   
   ```bash
   npm install @mui/material @emotion/react @emotion/styled @mui/icons-material react-router-dom axios
   ```

3. Create the component structure and implement the components

4. Run the React application:
   
   ```bash
   npm start
   ```

## Best Practices

1. **Backend**:
   
   - Use DTOs/Projections to separate API layer from domain model
   - Implement exception handling with @ControllerAdvice
   - Add validation for request bodies
   - Use Swagger/OpenAPI/Postman for API documentation
   - Implement pagination for large datasets
   - Use proper HTTP status codes
   - Use ResponseEntity, Optional, etc.

2. **Frontend**:
   
   - Keep components small and focused
   - Separate state management from presentation
   - Handle loading states and errors gracefully
   - Use env variables for API URLs
   - Implement proper form validation
   - Add confirmation for destructive actions

## Additional Features

1. **Authentication and Authorization**:
   
   - Implement JWT-based authentication or AWS Cognito
   - Add role-based access control
   - Secure endpoints with Spring Security

2. **Advanced Filtering**:
   
   - Implement complex search criteria
   - Add sorting and filtering options

3. **Reporting**:
   
   - Generate booking statistics
   - Export data to PDF or Excel

4. **Real-time Updates**:
   
   - Implement WebSocket for booking status updates
   - Show notifications for status changes

5. **Library Leaflet Map for Locations**:
   
   - Implement library to add maps for car locations
   - Car tracker with maps and routes

## Deployment

1. **Containerization with Docker**:
   
   - Create a `Dockerfile` for the Spring Boot backend and Hilla frontend.
   
   - Use Docker Compose to orchestrate the backend, frontend, and DynamoDB containers.
   
   - Example `Dockerfile` for Spring Boot:
     
     ```dockerfile
     FROM openjdk:17-jdk-alpine
     VOLUME /tmp
     COPY target/renting-car-backend-0.0.1-SNAPSHOT.jar app.jar
     ENTRYPOINT ["java","-jar","/app.jar"]
     ```
   
   - Example `docker-compose.yml`:
     
     ```yaml
     version: '3.8'
     services:
       backend:
         build: ./backend
         ports:
           - "8080:8080"
         environment:
           - AWS_ACCESS_KEY=${AWS_ACCESS_KEY}
           - AWS_SECRET_KEY=${AWS_SECRET_KEY}
           - AWS_REGION=${AWS_REGION}
       frontend:
         build: ./frontend
         ports:
           - "3000:3000"
     ```

2. **Cloud Deployment**:
   
   - Deploy the backend to a cloud platform like AWS, Google Cloud, or Heroku.
   
   - Use services like AWS Elastic Beanstalk or Google App Engine for Spring Boot.
   
   - Deploy the Hilla frontend to platforms like Vercel or Netlify.

3. **Load Balancing and Scaling**:
   
   - Use a load balancer (e.g., NGINX) to distribute traffic across multiple backend instances.
   
   - Implement horizontal scaling using Kubernetes (K8s) for container orchestration.

4. **Database Scaling**:
   
   - Use AWS DynamoDB for scalable NoSQL storage.
   
   - Optimize single-table designs for performance.

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Hilla Documentation](https://hilla.dev/docs/)
- [Spring Data DynamoDB Documentation](https://github.com/derjust/spring-data-dynamodb)
- [React Documentation](https://reactjs.org/docs/getting-started.html)
- [Material UI Documentation](https://mui.com/material-ui/getting-started/overview/)
- [React Router Documentation](https://reactrouter.com/en/main)
- [Axios Documentation](https://axios-http.com/docs/intro)
- [Lombok Documentation](https://projectlombok.org/features/)
- [DynamoDB Documentation](https://docs.aws.amazon.com/dynamodb/)
- [Renting Car Project](https://github.com/AlbertProfe/rentingCar)
- [Theme Injection Reference](https://github.com/AlbertProfe/mathsWeb)
- [Authentication Reference](https://github.com/AlbertProfe/easyLearning)