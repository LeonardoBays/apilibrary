# Book Reservation System

## Overview
A book reservation system where users can reserve books, and each book can only have one active reservation at a time.

## Features
- ✅ Users can reserve available books
- ✅ Books can only have one active reservation at a time
- ✅ Users cannot reserve the same book twice simultaneously
- ✅ Reservations have a 7-day expiration period
- ✅ Reservations can be cancelled
- ✅ Reservations can be marked as completed
- ✅ View all user reservations (active and cancelled)
- ✅ Check active reservation for a specific book

## API Endpoints

### 1. Reserve a Book
**POST** `/api/reservations`
- **Authentication**: Required (JWT Bearer Token)
- **Description**: Create a new book reservation for the authenticated user

Request:
```json
{
  "bookId": 1
}
```

Response (201 Created):
```json
{
  "id": 1,
  "userId": 2,
  "userName": "John Doe",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "status": "ACTIVE",
  "reservedAt": "2026-05-05 21:55:42",
  "expiresAt": "2026-05-12 21:55:42",
  "createdAt": "2026-05-05 21:55:42",
  "updatedAt": "2026-05-05 21:55:42"
}
```

### 2. Get Reservation by ID
**GET** `/api/reservations/{reservationId}`
- **Authentication**: Required
- **Description**: Retrieve a specific reservation by its ID

Response (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "userName": "John Doe",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "status": "ACTIVE",
  "reservedAt": "2026-05-05 21:55:42",
  "expiresAt": "2026-05-12 21:55:42",
  "createdAt": "2026-05-05 21:55:42",
  "updatedAt": "2026-05-05 21:55:42"
}
```

### 3. Cancel Reservation
**PUT** `/api/reservations/{reservationId}/cancel`
- **Authentication**: Required
- **Description**: Cancel an active reservation (only the reservation owner can cancel)

Response (200 OK): Updated reservation with status "CANCELLED"

### 4. Complete Reservation
**PUT** `/api/reservations/{reservationId}/complete`
- **Authentication**: Required
- **Description**: Mark a reservation as completed

Response (200 OK): Updated reservation with status "COMPLETED"

### 5. Get All User Reservations
**GET** `/api/reservations/user/all`
- **Authentication**: Required
- **Description**: Get all reservations (active, cancelled, completed) for the authenticated user

Response (200 OK):
```json
{
  "userId": 2,
  "userName": "John Doe",
  "reservations": [
    {
      "id": 1,
      "userId": 2,
      "userName": "John Doe",
      "bookId": 1,
      "bookTitle": "The Great Gatsby",
      "bookAuthor": "F. Scott Fitzgerald",
      "status": "ACTIVE",
      "reservedAt": "2026-05-05 21:55:42",
      "expiresAt": "2026-05-12 21:55:42",
      "createdAt": "2026-05-05 21:55:42",
      "updatedAt": "2026-05-05 21:55:42"
    }
  ]
}
```

### 6. Get Active User Reservations
**GET** `/api/reservations/user/active`
- **Authentication**: Required
- **Description**: Get only active reservations for the authenticated user

Response (200 OK): Same format as above, but only active reservations

### 7. Get Book Reservation
**GET** `/api/reservations/book/{bookId}`
- **Authentication**: Required
- **Description**: Get the active reservation for a specific book (if any)

Response (200 OK):
```json
{
  "id": 1,
  "userId": 2,
  "userName": "John Doe",
  "bookId": 1,
  "bookTitle": "The Great Gatsby",
  "bookAuthor": "F. Scott Fitzgerald",
  "status": "ACTIVE",
  "reservedAt": "2026-05-05 21:55:42",
  "expiresAt": "2026-05-12 21:55:42",
  "createdAt": "2026-05-05 21:55:42",
  "updatedAt": "2026-05-05 21:55:42"
}
```

Error (404 Not Found): If book has no active reservation

## Data Model

### Reservation Entity
- `id`: Long (Primary Key)
- `userId`: Long (Foreign Key to User)
- `bookId`: Long (Foreign Key to Book)
- `status`: ReservationStatus (ACTIVE, CANCELLED, COMPLETED)
- `reservedAt`: LocalDateTime (when the reservation was created)
- `expiresAt`: LocalDateTime (7 days from reservedAt)
- `createdAt`: LocalDateTime
- `updatedAt`: LocalDateTime

## Business Rules

1. **One reservation per book**: Only one active reservation can exist for a book at any time
2. **No duplicate reservations**: A user cannot have two active reservations for the same book
3. **Expiration**: Reservations expire after 7 days
4. **Status management**:
   - ACTIVE: Reservation is current and valid
   - CANCELLED: User cancelled the reservation
   - COMPLETED: Reservation was completed (user picked up/used the book)

## Error Handling

### Common Errors

1. **ReservationAlreadyExistsException** (400)
   - User already has an active reservation for this book

2. **BookAlreadyReservedException** (400)
   - The book is already reserved by another user

3. **ReservationNotFoundException** (404)
   - Reservation ID doesn't exist

4. **BookNotFoundException** (404)
   - Book doesn't exist

5. **UserNotFoundException** (404)
   - User doesn't exist

6. **InvalidReservationStatusException** (400)
   - Operation invalid for current reservation status

## Example Workflow

1. **User registers and logs in**
   ```
   POST /api/users/register
   POST /api/users/login → Get JWT token
   ```

2. **User browses available books**
   ```
   GET /api/books → View all books
   ```

3. **User checks if a book is reserved**
   ```
   GET /api/reservations/book/1
   → 404 if not reserved (available)
   → 200 with reservation details if reserved
   ```

4. **User reserves a book**
   ```
   POST /api/reservations
   {
     "bookId": 1
   }
   ```

5. **User views their reservations**
   ```
   GET /api/reservations/user/active
   ```

6. **User cancels a reservation (if needed)**
   ```
   PUT /api/reservations/123/cancel
   ```

7. **User completes a reservation**
   ```
   PUT /api/reservations/123/complete
   ```

## Database Schema

```sql
CREATE TABLE reservations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    reserved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id),
    UNIQUE(book_id, status) -- Ensures only one active reservation per book
);

CREATE INDEX idx_reservations_user_id ON reservations(user_id);
CREATE INDEX idx_reservations_book_id ON reservations(book_id);
CREATE INDEX idx_reservations_status ON reservations(status);
```

