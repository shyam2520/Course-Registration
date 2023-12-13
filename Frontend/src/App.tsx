
import { QueryClient, QueryClientProvider } from 'react-query';
import './App.css';
import { ThemeProvider } from './providers/ThemeProvider';
import { useToken } from './store/AuthStore';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Signin from './components/Signin';
import ProtectedRoute from './components/ProtectedRoute';
import Dashboard from './components/Dashboard';
import Signup from './components/Signup';
import Courseregister from './components/CourseRegister';

function App() {

  const queryClient = new QueryClient();
  const token = useToken();

  return (
    <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
      <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Routes>
              <Route path={'/signup'} element={<Signup />}/>
              <Route path={'/signin'} element={<Signin />}/>
                <Route element={<ProtectedRoute token={token}/>}>
                  <Route path={'/'} element={<Courseregister />}/>
                  <Route path={'/:user'} element={<Dashboard />}/>
                </Route>
            </Routes>
          </BrowserRouter>
      </QueryClientProvider>
    </ThemeProvider>
  )
}

export default App
