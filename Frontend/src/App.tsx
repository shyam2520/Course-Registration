
import { QueryClient, QueryClientProvider } from 'react-query'
import './App.css'
import Signin from './components/Signin'
import Signup from './components/Signup'


function App() {

  const queryClient = new QueryClient();

  return (
    <QueryClientProvider client={queryClient}>
      <Signin />
      <Signup />
    </QueryClientProvider>
  )
}

export default App
