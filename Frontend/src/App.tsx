
import { QueryClient, QueryClientProvider } from 'react-query';
import './App.css';
import AuthProvider from './providers/AuthProvider';
import { Route } from './providers/Route';


function App() {

  const queryClient = new QueryClient();

  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Route />
      </AuthProvider>
    </QueryClientProvider>
  )
}

export default App
