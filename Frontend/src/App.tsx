
import { QueryClient, QueryClientProvider } from 'react-query';
import './App.css';
import { ThemeProvider } from './providers/ThemeProvider';
import { getUser, useToken } from './store/AuthStore';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Signin from './components/Signin';
import ProtectedRoute from './components/ProtectedRoute';
import Dashboard from './components/Dashboard';
import Signup from './components/Signup';
import AdminDashboard from './components/AdminDashboard';
import { Role } from './lib/role';


function App() {
  const queryClient = new QueryClient();
  const token = useToken();
  
  let isUserAnAdmin = false;
  if (token) {
    const userDetails = getUser();
    isUserAnAdmin = userDetails.role === Role.ADMIN;
  }

  return (
    <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
      <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <Routes>
            <Route path={"/signup"} element={<Signup />} />
            <Route path={"/signin"} element={<Signin />} />
            <Route
              path={"/"}
              element={
                isUserAnAdmin ? 
                <ProtectedRoute token={token}>
                    <AdminDashboard />
                  </ProtectedRoute> :
                <ProtectedRoute token={token}>
                  <Dashboard />
                </ProtectedRoute>
              }
            />
          </Routes>
        </BrowserRouter>
      </QueryClientProvider>
    </ThemeProvider>
  );
}

export default App;
