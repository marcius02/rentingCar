# Authentication Workflow for React Application with AWS Cognito

This document outlines the workflow for user authentication in a React application using AWS Cognito, covering sign-up, sign-in, account confirmation, forgot password, and home page access.

## 1. Application Entry (App.jsx)

- **Component**: `App`
- **Purpose**: Serves as the main entry point, managing routing and authentication checks.
- **Workflow**:
  - The `BrowserRouter` and `Routes` from `react-router-dom` define the application's navigation structure.
  - The `isAuthenticated` function checks for an `accessToken` in `sessionStorage`.
  - Routes:
    - `/`: Redirects to `/home` if authenticated, else to `/login`.
    - `/login`: Renders `LoginPage`.
    - `/confirm`: Renders `ConfirmUserPage`.
    - `/home`: Renders `HomePage` if authenticated, else redirects to `/login`.
    - `/forgot`: Renders `ForgotPassword`.

## 2. Sign-In / Sign-Up (LoginPage.jsx)

- **Component**: `LoginPage`
- **Purpose**: Handles user sign-in and sign-up processes.
- **Workflow**:
  - **State**: Manages `email`, `password`, `confirmPassword`, and `isSignUp` (toggles between sign-in and sign-up modes).
  - **Sign-In**:
    - User enters `email` and `password`.
    - On form submission, `handleSignIn` calls `signIn` from `authService.ts`.
    - `signIn` sends an `InitiateAuthCommand` to AWS Cognito with `USER_PASSWORD_AUTH` flow.
    - On success, stores `idToken`, `accessToken`, and `refreshToken` in `sessionStorage`.
    - Redirects to `/home` using `window.location.href`.
    - On failure, displays an error alert.
  - **Sign-Up**:
    - User enters `email`, `password`, and `confirmPassword`.
    - If passwords match, `handleSignUp` calls `signUp` from `authService.ts`.
    - `signUp` sends a `SignUpCommand` to AWS Cognito with user details.
    - On success, navigates to `/confirm`, passing the `email` via `state`.
    - On failure, displays an error alert.
  - **Additional Features**:
    - Toggle between sign-in and sign-up modes using `setIsSignUp`.
    - Link to `/forgot` for password recovery.

## 3. Account Confirmation (ConfirmUserPage.jsx)

- **Component**: `ConfirmUserPage`
- **Purpose**: Confirms a new user's account using a confirmation code.
- **Workflow**:
  - **State**: Manages `email` (pre-filled from `location.state`) and `confirmationCode`.
  - User enters or verifies `email` and enters the `confirmationCode` received via email.
  - On form submission, `handleSubmit` calls `confirmSignUp` from `authService.ts`.
  - `confirmSignUp` sends a `ConfirmSignUpCommand` to AWS Cognito.
  - On success, displays a success alert and navigates to `/login`.
  - On failure, displays an error alert.

## 4. Forgot Password (ForgotPassword.jsx)

- **Component**: `ForgotPassword`
- **Purpose**: Allows users to reset their password.
- **Workflow**:
  - **State**: Manages `email`, `code`, `newPassword`, and `step` (1 for sending code, 2 for resetting password).
  - **Step 1: Send Code**:
    - User enters `email`.
    - On form submission, `handleSendCode` calls `forgotPassword` from `authService.ts`.
    - `forgotPassword` sends a `ForgotPasswordCommand` to AWS Cognito.
    - On success, advances to `step` 2.
    - On failure, displays an error alert.
  - **Step 2: Reset Password**:
    - User enters `code` (received via email) and `newPassword`.
    - On form submission, `handleConfirmPassword` calls `confirmForgotPassword` from `authService.ts`.
    - `confirmForgotPassword` sends a `ConfirmForgotPasswordCommand` to AWS Cognito.
    - On success, displays a success alert and navigates to `/login`.
    - On failure, displays an error alert.

## 5. Home Page (HomePage.jsx)

- **Component**: `HomePage`
- **Purpose**: Displays user information and provides logout functionality.
- **Workflow**:
  - Checks authentication via `App`'s route guard (`isAuthenticated`).
  - Parses `idToken` and `accessToken` from `sessionStorage` using `parseJwt`.
  - Logs token details to the console for debugging (not for production use).
  - Displays a simple "Hello World" message and token information prompt.
  - **Logout**:
    - User clicks the "Logout" button, triggering `handleLogout`.
    - Clears `sessionStorage` and navigates to `/login`.

## 6. Authentication Service (authService.ts)

- **Purpose**: Provides functions to interact with AWS Cognito.
- **Functions**:
  - `signIn`: Authenticates user credentials and stores tokens.
  - `signUp`: Registers a new user.
  - `confirmSignUp`: Confirms a user's account with a code.
  - `forgotPassword`: Initiates password reset by sending a code.
  - `confirmForgotPassword`: Resets the password using the code.
- **Configuration**:
  - Uses `CognitoIdentityProviderClient` with region from `config.json`.
  - Requires `clientId` from `config.json` for all commands.

## Notes

- **Security**: Tokens are stored in `sessionStorage`, which is cleared on logout. For production, consider more secure storage or refresh token strategies.
- **Error Handling**: Alerts are used for user feedback. In production, consider a more robust UI feedback mechanism.
- **Routing**: `react-router-dom` ensures protected routes (`/home`) require authentication.
- **Dependencies**: Relies on `@aws-sdk/client-cognito-identity-provider` for Cognito interactions.

This workflow provides a clear path for user authentication and navigation within the application, leveraging AWS Cognito for secure user management.