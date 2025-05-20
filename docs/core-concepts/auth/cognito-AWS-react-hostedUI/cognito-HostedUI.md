# AWS Cognito Hosted UI with Authentication Flows

> AWS Cognito Hosted UI provides a pre-built, customizable user interface for handling authentication processes like sign-in, sign-up, and sign-out, integrated with Amazon Cognito User Pools. 
> 
> It **simplifies the implementation of secure authentication** in web and mobile applications by leveraging OpenID Connect (OIDC) and OAuth 2.0 standards. 

Below, we’ll explore the authentication flows supported by Cognito, explain the provided React code, and describe the purpose of the `npm install` command for integrating Cognito Hosted UI with a React application.

## Authentication Flows in AWS Cognito

<mark>Amazon Cognito supports various authentication flows</mark> to cater to different application requirements. 

The choice of flows depends on your app type and security needs. Below is an overview of the flows listed in the query, with `ALLOW_REFRESH_TOKEN_AUTH` always enabled:

1. **Choice-based Sign-in (`ALLOW_USER_AUTH`)**
   
   - **Description**: The user pool responds to sign-in requests with a list of available authentication methods. Users can choose from options like one-time passwords (OTP), biometric devices, security keys, or password-based sign-in with multi-factor authentication (MFA).
   - **Use Case**: Ideal for applications that want to offer users flexibility in how they authenticate, enhancing user experience with multiple secure options.
   - **Hosted UI Support**: Fully supported, allowing users to interact with a customizable UI to select their preferred method.

2. **Sign-in with Username and Password (`ALLOW_USER_PASSWORD_AUTH`)**
   
   - **Description**: Users sign in by directly providing a username and password to the Cognito user pool.
   - **Use Case**: Suitable for simple, password-based authentication without additional complexity, though it may be less secure without MFA.
   - **Hosted UI Support**: Supported, as the Hosted UI can prompt for username and password.

3. **Sign-in with Secure Remote Password (SRP) (`ALLOW_USER_SRP_AUTH`)**
   
   - **Description**: Users sign in with a username and password, but the application uses Secure Remote Password (SRP) protocol to pass a password hash and verifier instead of the raw password, enhancing security.
   - **Use Case**: Preferred for client-side or server-side applications requiring secure password handling without transmitting plaintext credentials.
   - **Hosted UI Support**: Supported, as the Hosted UI can handle SRP-based authentication transparently.

4. **Sign-in with Server-side Administrative Credentials (`ALLOW_ADMIN_USER_PASSWORD_AUTH`)**
   
   - **Description**: Allows server-side authentication using username and password, typically for administrative operations.
   - **Use Case**: Used in server-side applications for backend processes, not for end-user sign-in.
   - **Hosted UI Support**: Not supported, as it’s designed for server-side operations rather than user-facing UI.

5. **Custom Authentication Flows with Lambda Triggers (`ALLOW_CUSTOM_AUTH`)**
   
   - **Description**: Enables custom sign-in processes where users may provide a username and password and respond to custom challenges defined in AWS Lambda functions.
   - **Use Case**: Useful for implementing non-standard authentication methods, such as custom challenge-response mechanisms.
   - **Hosted UI Support**: Supported, allowing custom flows to be integrated into the Hosted UI with Lambda triggers.

6. **Refresh Token Authentication (`ALLOW_REFRESH_TOKEN_AUTH`)**
   
   - **Description**: Allows applications to use a longer-lived refresh token to obtain new access and ID tokens without requiring users to re-authenticate.
   - **Use Case**: Essential for maintaining user sessions in long-running applications, improving user experience by reducing sign-in prompts.
   - **Hosted UI Support**: Automatically enabled and supported, as refresh tokens are part of the OAuth 2.0 flow.

## React Code with Cognito Hosted UI

The provided React code demonstrates how to integrate AWS Cognito Hosted UI with a React application using the `react-oidc-context` library, which simplifies OIDC-based authentication. Below is a detailed explanation of the code.

### `index.js`

This file sets up the React application and wraps it with an `AuthProvider` to enable OIDC authentication with Cognito.

```javascript
import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { AuthProvider } from "react-oidc-context";

const cognitoAuthConfig = {
  authority: "https://cognito-idp.eu-central-1.amazonaws.com/eu-central-1_rXV",
  client_id: "7kkicfmsl9b0inc8",
  redirect_uri: "http://localhost:5173/",
  response_type: "code",
  scope: "email openid phone",
};

const root = ReactDOM.createRoot(document.getElementById("root"));

root.render(
  <React.StrictMode>
    <AuthProvider {...cognitoAuthConfig}>
      <App />
    </AuthProvider>
  </React.StrictMode>
);
```

- **Purpose**: Configures the application to use Cognito’s Hosted UI for authentication.
- **Key Components**:
  - **`AuthProvider`**: A component from `react-oidc-context` that manages the OIDC authentication state and provides authentication-related functions to child components.
  - **`cognitoAuthConfig`**:
    - `authority`: The Cognito User Pool’s endpoint, specific to the `eu-central-1` region and user pool ID `eu-central-1_jcmrXV`.
    - `client_id`: The ID of the app client configured in the Cognito User Pool.
    - `redirect_uri`: The URL (`http://localhost:5173/`) where Cognito redirects the user after authentication. This must match the callback URL configured in the Cognito app client.
    - `response_type: "code"`: Specifies the OAuth 2.0 authorization code flow, which is secure for web applications as it exchanges an authorization code for tokens server-side or via the client.
    - `scope`: Requests access to the user’s `email`, `openid`, and `phone` claims in the ID token.
  - **Rendering**: The `App` component is wrapped in `AuthProvider`, enabling it to access authentication state and methods.

### `App.js`

This file defines the main application component, handling user sign-in, sign-out, and displaying authentication details.

```javascript
import { useAuth } from "react-oidc-context";

function App() {
  const auth = useAuth();

  const signOutRedirect = () => {
    const clientId = "7kkios0nc8";
    const logoutUri = "<logout uri>";
    const cognitoDomain = "https://eu-central-1jcmrxv.auth.eu-central-1.amazoncognito.com";
    window.location.href = `${cognitoDomain}/logout?client_id=${clientId}&logout_uri=${encodeURIComponent(logoutUri)}`;
  };

  if (auth.isLoading) {
    return <div>Loading...</div>;
  }

  if (auth.error) {
    return <div>Encountering error... {auth.error.message}</div>;
  }

  if (auth.isAuthenticated) {
    return (
      <div>
        <pre> Hello: {auth.user?.profile.email} </pre>
        <pre> ID Token: {auth.user?.id_token} </pre>
        <pre> Access Token: {auth.user?.access_token} </pre>
        <pre> Refresh Token: {auth.user?.refresh_token} </pre>
        <button onClick={() => auth.removeUser()}>Sign out</button>
      </div>
    );
  }

  return (
    <div>
      <button onClick={() => auth.signinRedirect()}>Sign in</button>
      <button onClick={() => signOutRedirect()}>Sign out</button>
    </div>
  );
}

export default App;
```

- **Purpose**: Manages the user interface for authentication, displaying user details when authenticated and providing sign-in/sign-out functionality.
- **Key Components**:
  - **`useAuth` Hook**: From `react-oidc-context`, it provides access to authentication state (`isAuthenticated`, `isLoading`, `error`, `user`) and methods (`signinRedirect`, `removeUser`).
  - **Conditional Rendering**:
    - If `auth.isLoading` is true, displays a "Loading..." message while the authentication state is being determined.
    - If `auth.error` exists, displays the error message.
    - If `auth.isAuthenticated` is true, displays the user’s email, ID token, access token, and refresh token, along with a "Sign out" button.
    - If not authenticated, displays "Sign in" and "Sign out" buttons.
  - **`signinRedirect`**: Initiates the OIDC authorization code flow, redirecting the user to the Cognito Hosted UI for sign-in.
  - **`signOutRedirect`**: Redirects the user to the Cognito Hosted UI’s logout endpoint, passing the `client_id` and `logout_uri` (which must be configured in the Cognito app client). Note: The `<logout uri>` placeholder must be replaced with the actual logout redirect URL.
  - **`removeUser`**: Clears the local authentication state, used as an alternative sign-out method.

## oidc-client-ts & react-oidc-context

The `npm install` command installs two key dependencies required for integrating Cognito Hosted UI with a React application:

1. **`oidc-client-ts`**:
   
   - **Purpose**: A TypeScript-based library for handling OIDC and OAuth 2.0 authentication flows in client-side applications. It manages tasks like redirecting to the authorization server (Cognito Hosted UI), handling token exchange, and refreshing tokens.
   - **Role in the Code**: Provides the underlying logic for `react-oidc-context` to interact with Cognito’s OIDC endpoints, handling the authorization code flow and token management.
   - **Why Needed**: Ensures secure communication with Cognito, supporting flows like `ALLOW_USER_SRP_AUTH` and `ALLOW_REFRESH_TOKEN_AUTH`.

2. **`react-oidc-context`**:
   
   - **Purpose**: A React-specific wrapper around `oidc-client-ts` that provides a context API and hooks (like `useAuth`) to simplify OIDC authentication in React applications.
   - **Role in the Code**: Enables the `AuthProvider` component and `useAuth` hook, making it easy to manage authentication state and trigger sign-in/sign-out actions.
   - **Why Needed**: Simplifies integration with React by providing a declarative way to handle authentication, reducing boilerplate code.
- ** `--save` Flag**: Ensures these dependencies are added to the `dependencies` section of the project’s `package.json`, making them available for production builds.

## How the Code Integrates with Cognito Hosted UI

1. **Configuration**:
   
   - The `cognitoAuthConfig` in `index.js` specifies the Cognito User Pool’s OIDC endpoint (`authority`), app client ID, redirect URI, and scopes. These must match the configuration in the AWS Cognito console.
   - The `response_type: "code"` indicates the use of the OAuth 2.0 authorization code flow, which is secure and supported by Cognito Hosted UI.

2. **Sign-in Flow**:
   
   - Clicking the "Sign in" button triggers `auth.signinRedirect()`, which redirects the user to the Cognito Hosted UI.
   - The user authenticates (e.g., via `ALLOW_USER_AUTH` or `ALLOW_USER_SRP_AUTH`), and Cognito redirects back to the `redirect_uri` with an authorization code.
   - The `react-oidc-context` library exchanges the code for tokens (ID, access, and refresh tokens) and stores them in the `auth.user` object.

3. **Authenticated State**:
   
   - When `auth.isAuthenticated` is true, the app displays the user’s email and tokens, demonstrating successful authentication.
   - The refresh token (`ALLOW_REFRESH_TOKEN_AUTH`) can be used to renew tokens without user interaction.

4. **Sign-out Flow**:
   
   - The `signOutRedirect` function redirects to the Cognito logout endpoint, clearing the session on the Cognito side.
   - The `auth.removeUser()` method clears the local authentication state, ensuring the app reflects the signed-out state.
