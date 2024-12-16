import React, { useState } from 'react'
import './style.css';
import Login from '../login/login';
import { axiosConf, backofficeConf } from '../axiosConf';

interface IProduct {
    name: string;
}

function MyButton() {

    const [name, setName] = useState('');
    const [allNames, setAllName] = useState<IProduct[]>([]);

    const onNameChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setName(event.target.value);
    };

    const onClickSave = () => {
        axiosConf.post("/products", {"name":name})
    };

    const onClickGetAll = () => {
        console.log(process.env.REACT_APP_BASE_URL)
        axiosConf.get('/products')
        .then((response) => {
            setAllName(response.data)
            console.log(response.data)
        })
    };

    const onClickGetAllNoAuth = () => {
        console.log(process.env.REACT_APP_BASE_URL)
        backofficeConf.get('/products')
        .then((response) => {
            setAllName(response.data)
            console.log(response.data)
        })
    };

    return (
        <div style={{
                display: "flex", 
                flexDirection: "column", 
                flexWrap: "wrap", 
                gap: "2rem",
                padding: "30rem"
            }}>
            <Login/>
            <input id="myText" type="text" onChange={(e) => onNameChange(e)}></input>
            <button onClick={() => onClickSave()}>send save from input</button>
            <button onClick={() => onClickGetAll()}>get all</button>
            <button onClick={() => onClickGetAllNoAuth()}>no auth get all</button>
            <div>
                <ul>
                    {allNames.map((i) => (
                        <li key={i.name}>{i.name}</li>
                    ))}
                </ul>
            </div>
        </div>
    )
}

export default MyButton