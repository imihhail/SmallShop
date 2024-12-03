import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './homepage.css';
import { TokenCheck, InsertProduct, DeleteProduct, UpdateOwner } from '../connections/backendConnection';

function HomePage() {
  const navigate = useNavigate();
  const [productsData, setProductsData] = useState([]);
  const [showWindow, setShowWindow] = useState(false);
  const [newProduct, setNewProduct] = useState({id: '', name: '', price: '', owner: 'No owner'});
  const [role, setRole] = useState('USER');
  const [username, setUsername] = useState('');
  const [id, setId] = useState('');


  // TokenCheck
  useEffect(() => {
    const checkToken = async () => {
      try {
        const response = await TokenCheck();
        if (response) {
          setProductsData(response.responseData.productsData || []);
          setRole(response.responseData.role)
          setUsername(response.responseData.username)
          setId(response.responseData.productId)
        } else {
          navigate("/loginpage");
        }
      } catch (error) {
        navigate("/loginpage");
      }
    };

    checkToken();
  }, [navigate]);

  const openWindow = () => setShowWindow(true);
  const closeWindow = () => setShowWindow(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNewProduct((prev) => ({ ...prev, [name]: value }));
  };

  const newProductObj = async() => {
    const { error, responseData } = await InsertProduct(newProduct);

    if (error) {
      alert(error)
      navigate("/loginpage")
    } else {
      newProduct.id = responseData.productId
      setProductsData((prev) => [...prev, newProduct])
      setShowWindow(false)
    }
  }

  const deleteProduct = async(removedProduct) => {
    const error = await DeleteProduct(removedProduct.id)

    if (error) {
      alert(error.error)
      navigate("/loginpage")
    } else {
      setProductsData(productsData.filter(product => product !== removedProduct))
    }
  }

  const updateOwner = async(newOwner) => {
    const error = await UpdateOwner(newOwner.id)

    if (error) {
      alert(error.error)
      navigate("/loginpage")
    } else {
      setProductsData((prev) =>
      prev.map((product) =>
        product === newOwner ? { ...product, owner: username } : product
      ))
    }
  }
  
  const handleLogout = () => {
    localStorage.removeItem('token')
    navigate('/loginpage')
  };

  return (
    <div className="homepage">
      <div className="content">
        
        <div className="userbox">
          <p className="username">{username}</p>
          <button className="logout-button" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <h1>Products</h1>
        {productsData.length > 0 ? (
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Owner</th>
                <th>Price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {productsData.map((product) => (
                <tr key={product.id}>
                  <td>{product.name}</td>
                  <td>{product.owner}</td>
                  <td>{product.price}</td>
                  {role == "ADMIN" ? <td onClick={() => deleteProduct(product)} style={{ cursor: 'pointer' }}>
                    <u><b>DELETE</b></u>
                  </td> : <td onClick={() => updateOwner(product)} style={{ cursor: 'pointer' }}>
                    <u><b>BUY</b></u>
                  </td>}

                </tr>
              ))}
            </tbody>

          </table>

        ) : (
          <p>No products available.</p>
        )}

      {role === "ADMIN" && (
        <button className="insert-button" onClick={openWindow}>
          Insert product
        </button>
      )}

      </div>

      {showWindow && (
        <>
      <div className= "insertWindow">

        <div className="windowInputs">
              <input
                type="text"
                name="name"
                placeholder="Name"
                onChange={handleInputChange}
              />
              <input
                type="number"
                name="price"
                placeholder="Price"
                onChange={handleInputChange}
              />
        </div>

        <div className="windowButtons">
          <button onClick={newProductObj}>Insert</button>
          <button className='cancelButton' onClick={closeWindow}>Cancel</button>
        </div>

      </div>
      </>
      )}

    </div>
  );
}

export default HomePage;
