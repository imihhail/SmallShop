export const RegisterUser = async (userData) => {
    try {
      const response = await fetch("http://localhost:8080/registerUser", {
        method: "POST",
        mode: "cors",
        cache: "no-cache",
        referrerPolicy: "no-referrer",
        redirect: "follow",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(userData),
        credentials: "include",
      });
      const resp = await response.json();
      console.log("response: ", resp);
    } catch (error) {
      console.log("Registration error");
      console.log(error);
      return { server: "fail", error: "Error 500, Internal server error!" };
    }
  };
  

  export const LoginUser = async (LoginData) => {
    try {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(LoginData),
            credentials: "include",
        });

        if (!response.ok) {
            const error = await response.json();
            console.error("Login failed:", error);
            return error;
        }
        const token = await response.json();
        console.log("Token received: ", token);
        localStorage.setItem('token', token.token)
        return { token };
    } catch (error) {
        console.error("Login error", error);
        return { error: "An unexpected error occurred." };
    }
};

export const TokenCheck = async () => {
  const token = localStorage.getItem('token');
  
  try {
      const response = await fetch("http://localhost:8080/authenticate", {
          method: "POST",
          headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
          },
          mode: "cors",
          credentials: "include",
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
    }
      const responseData = await response.json(); 
      console.log("Token received: ", responseData);
      return { responseData }

  } catch (error) {
      console.error("Login error", error);
      return { error: "An unexpected error occurred." };
  }
};



export const SecurityCheck = async () => {
  const token = localStorage.getItem('token');
  
  try {
      const response = await fetch("http://localhost:8080/security", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
        },
          credentials: "include",
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
    }
      const responseData = await response.json(); 
      console.log("Token received: ", responseData);
      return { token }

  } catch (error) {
      console.error("Login error", error);
      return { error: "An unexpected error occurred." };
  }
};

export const InsertProduct = async (newProduct) => {
  const token = localStorage.getItem('token');
  
  try {
      const response = await fetch("http://localhost:8080/insertProduct", {
          method: "POST",
          headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
          },
          mode: "cors",
          credentials: "include",
          body: JSON.stringify(newProduct),
          credentials: "include",
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
    }
      const responseData = await response.json(); 
      console.log("Token received: ", responseData);
      return { responseData }

  } catch (error) {
      console.error("Login error", error);
      return { error: "An unexpected error occurred." };
  }
};

export const DeleteProduct = async (id) => {
  const token = localStorage.getItem('token');
  
  try {
      const response = await fetch("http://localhost:8080/deleteProduct", {
          method: "POST",
          headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
          },
          mode: "cors",
          credentials: "include",
          body: JSON.stringify(id),
          credentials: "include",
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
    }
      const responseData = await response.json(); 
      console.log("Token received: ", responseData);
      return { responseData }

  } catch (error) {
      console.error("Login error", error);
      return { error: "An unexpected error occurred." };
  }
};

export const UpdateOwner = async (id) => {
  const token = localStorage.getItem('token');
  
  try {
      const response = await fetch("http://localhost:8080/updateOwner", {
          method: "PUT",
          headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
          },
          mode: "cors",
          credentials: "include",
          body: JSON.stringify(id),
          credentials: "include",
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
    }
      const responseData = await response.json(); 
      console.log("Token received: ", responseData);
      return { responseData }

  } catch (error) {
      console.error("Login error", error);
      return { error: "An unexpected error occurred." };
  }
};