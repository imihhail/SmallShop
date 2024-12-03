export const RegisterUser = async (userData) => {
    try {
      const response = await fetch("http://localhost:8080/registerUser", {
        method: "POST",
        mode: "cors",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(userData),
        credentials: "include",
      });
      const resp = await response.json()
      return resp

    } catch (error) {
      console.log(error);
      return { server: "fail", error: "Error 500, Internal server error!" };
    }
  };
  

  export const LoginUser = async (LoginData) => {
    try {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            mode: "cors",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(LoginData),
            credentials: "include",
        });

        if (!response.ok) {
            const error = await response.text()
            return error;
        }
        const token = await response.json();
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
        console.error(`HTTP error! Status: ${response.status}`);
        const error = await response.text(); 
        return error
      }
      const responseData = await response.json(); 
      return { responseData }

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
          body: JSON.stringify(newProduct),
          credentials: "include"
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`)
        const error = await response.text()
        return { error }
      }
      
      const responseData = await response.json(); 
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
          method: "DELETE",
          headers: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
          },
          mode: "cors",
          body: JSON.stringify(id),
          credentials: "include"
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`);
        const error = await response.text(); 
        return { error };
      }

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
          body: JSON.stringify(id)
      });

      if (!response.ok) {
        console.error(`HTTP error! status: ${response.status}`)
        const error = await response.text()
        return { error };
    }

  } catch (error) {
      console.error("Login error", error)
      return { error: "An unexpected error occurred." }
  }
};