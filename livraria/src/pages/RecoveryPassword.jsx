import { useState } from 'react';
import  axios  from 'axios';
import "./Css/RecoveryPassword.css"

const RecoveryPassword = () => {
    const [user, setUser] = useState("")
    const [email, setEmail] = useState("")
    const [confirmEmail, setConfirmEmail] = useState("")
    const [code, setCode] = useState("")
    const [secretCode, setSecretCode] = useState("")
    const [putEmail, setPutEmail] = useState(true)
    const [putCode, setPutCode] = useState(false)
    const [changePassword, setChangePassword] = useState(false)
    const [codeError, setCodeError] = useState(false);
    const [newPassword, setNewPassword] = useState("")
    const [confirmNewPassword, setConfirmNewPassword] = useState("")

    const handleCode = async (e) => {
        e.preventDefault(); // Evita o recarregamento da página
    
        if (email === confirmEmail) {
            console.log("oi")
            const response = await axios.get(`http://localhost:8765/auth-service/api/usuarios/getUser/${email}`)
            setUser(response.data)
    
            try {
                console.log(user)
                const codeResponse = await axios.get(`http://localhost:8765/auth-service/api/usuarios/code/${user.email}`)
                setSecretCode(codeResponse.data)
                setPutCode(true)
                setPutEmail(false)
                alert("Um código foi enviado para o seu e-mail")
            } catch (error) {
                console.error("Erro ao obter código secreto:", error)
                // Lógica de tratamento de erro
            }
        }
    }
    

    const handleConfirmCode = () => {
        if (code == secretCode) {
            setChangePassword(true)
            setPutCode(false)
        }else{
            setCodeError(true) // Código incorreto, definir codeError como true
        }
    }

    const handleChangePassword = async (e) => {
        e.preventDefault()
        if (newPassword == confirmNewPassword) {
            try {
                const response = await axios.put(`http://localhost:8765/auth-service/api/usuarios/atualizar/${user.id}`, {
                    name: user.name,
                    email: user.email,
                    password: newPassword,
                    image: user.image,
                    role: user.role,
                    availables: user.availables,
                    addresses: user.addresses,
                    orders: user.orders,
                    sales: user.sales
                }, {
                    headers: {
                        "Content-Type": "application/json"
                    }
                })

                alert("Senha alterada com sucesso")
            } catch (error) {
                console.error("Erro ao atualizar senha:", error)
                // Lógica de tratamento de erro
            }
        }
    }

    return (
        <div>
            {putEmail && <form action="" className='form-email-recovery'>
                <h1>Alterar Senha</h1>
                <input type="text" onChange={(e) => setEmail(e.target.value)} placeholder="Digite seu E-mail" />
                <input type="text" onChange={(e) => setConfirmEmail(e.target.value)} placeholder="Confirmar -mail"/>
                <button onClick={handleCode}>Enviar</button>
            </form>}
            {putCode && <>
                <form className='form-email-recovery'>
                    <h1>Alterar Senha</h1>
                    <input type="text" onChange={(e) => setCode(e.target.value)} 
                    placeholder='Digite o código'
                    className={codeError ? "input-error" : ""}
                    />
                    <button onClick={handleConfirmCode}>Enviar </button>
                </form>
            </>}
            {changePassword && <form className='form-email-recovery'>
                <h1>Alterar Senha</h1>
                <input type="password" onChange={(e) => setNewPassword(e.target.value)} placeholder='Nova senha'/>
                <input type="password" onChange={(e) => setConfirmNewPassword(e.target.value)} placeholder='Confirmar senha'/>
                <button onClick={handleChangePassword}>Enviar</button>
            </form>}
        </div>
    )
}

export default RecoveryPassword
