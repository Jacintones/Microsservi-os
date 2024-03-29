import { useHistory } from "react-router-dom"

export const useNavigateTo = (path) => {
  const history = useHistory()

  const navigateTo = () => {
    history.push(path)
  }

  return navigateTo
}
